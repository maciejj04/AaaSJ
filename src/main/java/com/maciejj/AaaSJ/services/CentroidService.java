package com.maciejj.AaaSJ.services;


import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.maciejj.AaaSJ.commands.CentroidRQ;
import com.maciejj.AaaSJ.domain.AudioFormatValidator;
import com.maciejj.AaaSJ.domain.BytesArrayMapper;
import com.maciejj.AaaSJ.domain.Centroid;
import com.maciejj.AaaSJ.domain.DomainUtils;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.awt.geom.Path2D;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static com.google.common.util.concurrent.MoreExecutors.listeningDecorator;
import static com.maciejj.AaaSJ.domain.DomainUtils.generateByteBufer;
import static com.maciejj.AaaSJ.domain.DomainUtils.generateFrequencyBins;
import static com.maciejj.AaaSJ.domain.DomainUtils.getRealAndTakeHalf;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static javax.sound.sampled.AudioSystem.getAudioFileFormat;
import static javax.sound.sampled.AudioSystem.getAudioInputStream;

@RestController
public class CentroidService {

    private AudioFileFormat audioFileData;
    private AudioInputStream audioStream;
    private AudioFormat formatInfo;
    private int sampleSizeInBytes;
    private double[] frequencyBins;

    @Value("${audio-repository-path}")
    private String AUDIO_REPOSITORY_PATH;       // TODO: Enhance by specific user directory path.

    @RequestMapping(path = "/v1/spectralCentroids", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Centroid> centoids(@RequestBody CentroidRQ request) throws Exception {
        validateRequest(request);
        // TODO: check if file is present in user session.
        // TODO: load file from proper S3 bucket to proper directory. Interceptor, Spring cloud AWS?

        audioStream = getAudioInputStream(new File(AUDIO_REPOSITORY_PATH + request.getFileName()));
        formatInfo = audioStream.getFormat();
        sampleSizeInBytes = formatInfo.getSampleSizeInBits()/8;

        AudioFormatValidator.validateAudioFormat(formatInfo);
        frequencyBins = generateFrequencyBins((int) this.formatInfo.getFrameRate(), 4096);

        byte[] buffer = generateByteBufer(4096, sampleSizeInBytes);//TODO: for now just use power of 2 request.getWindowSize()

        ExecutorService executorService = newFixedThreadPool(2);
        ListeningExecutorService listeningExecutorService = listeningDecorator(executorService);

        List<ListenableFuture<Centroid>> executors = new LinkedList<>();
        FastFourierTransformer fftObject = new FastFourierTransformer(DftNormalization.STANDARD);//?

        BytesArrayMapper mapper = new BytesArrayMapper(sampleSizeInBytes);

        while (audioStream.read(buffer) != -1 ){// This is rather functional style. Consider extracting new methods and changing this to rather 'stream()/pipeline' code style.
            double[] mappedBuffer = mapper.updateData(buffer).mapToDoubleArr();

            executors.add(
                    listeningExecutorService.submit(() ->
                            calculateCentroid(
                                    getRealAndTakeHalf(Double.class, fftObject.transform(mappedBuffer, TransformType.FORWARD))
                            )

                    )
            );
//            Plot plt = Plot.create();
//            plt.plot().add(Arrays.asList(getRealDouble(fftObject.transform(mapper.updateData(buffer).mapToDoubleArr(), TransformType.FORWARD))));
//            plt.show();
        }

        // TODO: currently processed audio file informations should be stored in session.
        List<Centroid> centroids = Futures.allAsList(executors).get();
        audioStream.close();
        return centroids;
    }

    private void validateRequest(CentroidRQ request) {
        //TODO: validate window width to be power of 2.
    }


    private Centroid calculateCentroid(Double[] amplitudeSpectrum){
        /* Calculates spectral centroid as pointed in https://en.wikipedia.org/wiki/Spectral_centroid
        and
        https://dsp.stackexchange.com/questions/27499/finding-the-right-measure-to-compare-sound-signals-in-the-frequency-domain/27533#27533
        */

        double sum = 0d;
        double discriminator = 0;

        for (int i = 0; i < amplitudeSpectrum.length; i++){
            sum += amplitudeSpectrum[i] * frequencyBins[i];
            discriminator += amplitudeSpectrum[i];
        }
        return new Centroid(0f, (float) (sum/discriminator));
    }

}

