package com.maciejj.AaaSJ.services;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.maciejj.AaaSJ.commands.AmplitudeSpectrumRQ;
import com.maciejj.AaaSJ.domain.AmplitudeSpectrum;
import com.maciejj.AaaSJ.domain.AudioFormatValidator;
import com.maciejj.AaaSJ.domain.BytesArrayMapper;
import com.maciejj.AaaSJ.infrastructure.PerformanceResolver;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static com.google.common.util.concurrent.MoreExecutors.listeningDecorator;
import static com.maciejj.AaaSJ.domain.DomainUtils.*;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static javax.sound.sampled.AudioSystem.getAudioInputStream;

@RestController
public class AmplitudeSpectrumService implements IAmplitudeSpectrumService {
    /*
        For now service will just use as many threads to process data as many processors are available on the machine.
        There is UI to define sufficient nr of threads it should use.
        NOTE: this may be tricky when it comes to PaaS like deployments when there might be many pods running on same physical/virtual machine.
     */

    private AudioFileFormat audioFileData;
    private AudioInputStream audioStream;
    private AudioFormat formatInfo;
    private int sampleSizeInBytes;
    private double[] frequencyBins;

    @Value("${audio-repository-path}")
    private String AUDIO_REPOSITORY_PATH;       // TODO: Enhance by specific user directory path.

    @RequestMapping(path = "/v1/amplitudeSpectrum")
    public List<AmplitudeSpectrum> amplitudeSpectrum(@RequestBody AmplitudeSpectrumRQ request) throws Exception {
        validateRequest(request);
        // TODO: check if file is present in user session.
        // TODO: load file from proper S3 bucket to proper directory. Interceptor, Spring cloud AWS?

        audioStream = getAudioInputStream(new File(AUDIO_REPOSITORY_PATH + request.getFileName()));
        formatInfo = audioStream.getFormat();
        sampleSizeInBytes = formatInfo.getSampleSizeInBits() / 8;

        AudioFormatValidator.validateAudioFormat(formatInfo);
        frequencyBins = generateFrequencyBins((int) this.formatInfo.getFrameRate(), 4096);

        byte[] buffer = generateByteBufer(4096, sampleSizeInBytes);//TODO: for now just use power of 2 request.getWindowSize()

        ExecutorService executorService = newFixedThreadPool(PerformanceResolver.threadCountForThreadPool());
        ListeningExecutorService listeningExecutorService = listeningDecorator(executorService);

        List<ListenableFuture<AmplitudeSpectrum>> executors = new LinkedList<>();
        FastFourierTransformer fftObject = new FastFourierTransformer(DftNormalization.STANDARD);//?

        BytesArrayMapper mapper = new BytesArrayMapper(sampleSizeInBytes);

        while (audioStream.read(buffer) != -1) {// This is rather functional style. Consider extracting new methods and changing this to rather 'stream()/pipeline' code style.
            double[] mappedBuffer = mapper.updateData(buffer).mapToDoubleArr();

            executors.add(
                    listeningExecutorService.submit(() ->
                            new AmplitudeSpectrum(Arrays.asList(getRealAndTakeHalf(Double.class, fftObject.transform(mappedBuffer, TransformType.FORWARD))))
                    )
            );
        }

        // TODO: currently processed audio file informations should be stored in session.
        List<AmplitudeSpectrum> centroids = Futures.allAsList(executors).get();
        audioStream.close();
        return centroids;
    }

    private void validateRequest(AmplitudeSpectrumRQ request) {
        if (!isPowerOf2(request.getWindowSize())) {
            throw new IllegalArgumentException("Not power of 2!");
        }
    }

}
