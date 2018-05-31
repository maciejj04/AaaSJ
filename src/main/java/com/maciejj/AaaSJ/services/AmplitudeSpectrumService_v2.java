package com.maciejj.AaaSJ.services;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.maciejj.AaaSJ.commands.AmplitudeSpectrumRQ;
import com.maciejj.AaaSJ.domain.AmplitudeSpectrum;
import com.maciejj.AaaSJ.domain.AudioFormatValidator;
import com.maciejj.AaaSJ.domain.BytesArrayMapper;
import com.maciejj.AaaSJ.infrastructure.AudioResourceLoader;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static com.google.common.util.concurrent.MoreExecutors.listeningDecorator;
import static com.maciejj.AaaSJ.domain.DomainUtils.*;
import static java.util.Arrays.stream;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static javax.sound.sampled.AudioSystem.getAudioInputStream;

@Service("amplitureSpectrumService_v2")
public class AmplitudeSpectrumService_v2 implements IAmplitudeSpectrumService {

    AudioResourceLoader audioResourceLoader;

    private AudioFileFormat audioFileData;
    private AudioInputStream audioStream;
    private AudioFormat formatInfo;
    private int sampleSizeInBytes;
    private double[] frequencyBins;

    @Value("${audio-repository-path:/var/tmp/aaasj/}")
    private String AUDIO_REPOSITORY_PATH;       // TODO: Enhance by specific user directory path.

    //TODO: this is not yet working properly (test it)!
    public List<AmplitudeSpectrum> amplitudeSpectrum(AmplitudeSpectrumRQ request) throws Exception {
        validateRequest(request);
        // TODO: check if file is present in user session.
        // TODO: load file from proper S3 bucket to proper directory. Interceptor, Spring cloud AWS?

        audioStream = getAudioInputStream(new File(AUDIO_REPOSITORY_PATH + request.getFileName()));
        formatInfo = audioStream.getFormat();
        sampleSizeInBytes = formatInfo.getSampleSizeInBits() / 8;

        AudioFormatValidator.validateAudioFormat(formatInfo);
        frequencyBins = generateFrequencyBins((int) this.formatInfo.getFrameRate(), 4096);

        byte[] fullBuffer = generateByteBufer((int) formatInfo.getSampleRate(), sampleSizeInBytes);

        ExecutorService executorService = newFixedThreadPool(2);
        ListeningExecutorService listeningExecutorService = listeningDecorator(executorService);

        List<ListenableFuture<AmplitudeSpectrum>> executors = new LinkedList<>();
        FastFourierTransformer fftObject = new FastFourierTransformer(DftNormalization.STANDARD);//?

        assert audioStream.read(fullBuffer) == -1;


        double[] fullAudioData = new BytesArrayMapper(fullBuffer, sampleSizeInBytes).mapToDoubleArr();
        double[][] splittedAudioData = splitDataByNElements(fullAudioData, request.getWindowSize());

        stream(splittedAudioData).forEach( row -> {
            executors.add(listeningExecutorService.submit( () ->
                    new AmplitudeSpectrum(getRealAndTakeHalf(Double.class, fftObject.transform(row, TransformType.FORWARD)))
            ));
        });

        // TODO: currently processed audio file informations should be stored in session.
        List<AmplitudeSpectrum> centroids = Futures.allAsList(executors).get();
        audioStream.close();
        return centroids;
    }

}
