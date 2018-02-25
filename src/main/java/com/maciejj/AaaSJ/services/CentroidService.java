package com.maciejj.AaaSJ.services;


import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.maciejj.AaaSJ.commands.CentroidRQ;
import com.maciejj.AaaSJ.domain.AudioFormatValidator;
import com.maciejj.AaaSJ.domain.BytesArrayMapper;
import com.maciejj.AaaSJ.domain.Centroid;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.google.common.util.concurrent.MoreExecutors.listeningDecorator;
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

    @RequestMapping(path = "/spectralCentroids", method = RequestMethod.POST)
    public List<Centroid> centoids(@RequestBody CentroidRQ request) throws Exception {
        // TODO: check if file is present in user session.
        // TODO: load file from proper S3 bucket to proper directory. Interceptor?


//        BufferedInputStream bufferedInputStream = (BufferedInputStream) getClass().getResourceAsStream(AUDIO_REPOSITORY_PATH + request.getFileName());

        audioStream = getAudioInputStream(new File(AUDIO_REPOSITORY_PATH + request.getFileName()));
        formatInfo = audioStream.getFormat();
//        audioFileData = getAudioFileFormat(audioStream);
        sampleSizeInBytes = formatInfo.getSampleSizeInBits()/8;

        AudioFormatValidator.validateAudioFormat(formatInfo);
        frequencyBins = getFrequencyBins((int) this.formatInfo.getFrameRate(), 4096);

        byte[] buffer = generateBufer(4096);//TODO: for now just use power of 2 request.getWindowSize()

        ExecutorService executorService = newFixedThreadPool(2);
        ListeningExecutorService listeningExecutorService = listeningDecorator(executorService);

        List<ListenableFuture<Centroid>> executors = new LinkedList<>();
        FastFourierTransformer fftObject = new FastFourierTransformer(DftNormalization.STANDARD);//?

        BytesArrayMapper mapper = new BytesArrayMapper(sampleSizeInBytes);

        while (audioStream.read(buffer) != -1 ){// This is rather functional style. Consider extracting new methods and changing this to rather 'stream()/pipeline' code style.
            executors.add(
                    listeningExecutorService.submit(() ->
                            calculateCentroid(
                                    getRealAndTakeHalf(fftObject.transform(
                                                mapper.updateData(buffer).mapToDoubleArr(), TransformType.FORWARD
                                            )
                                    )
                            )

                    )
            );
//            Plot plt = Plot.create();
//            plt.plot().add(Arrays.asList(getRealDouble(fftObject.transform(mapper.updateData(buffer).mapToDoubleArr(), TransformType.FORWARD))));
//            plt.show();
        }
//        Plot.create().plot().add(Arrays.asList(getRealDouble(fftObject.transform(mapper.updateData(buffer).mapToDoubleArr(), TransformType.FORWARD))))
        // TODO: currently processed audio file informations should be stored in session.
        return Futures.allAsList(executors).get();
    }


    private Centroid calculateCentroid(double[] amplitudeSpectrum){
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

//        sum = 0
//        discriminator = 0
//        for i in range(0, len(normalizedAmpSpecrt)):
//        sum += normalizedAmpSpecrt[i] * freqs[i]
//        discriminator += normalizedAmpSpecrt[i]
//
//        return sum / discriminator
    }



    private byte[] generateBufer(int windowSize){ // TODO:
        return new byte[windowSize*(sampleSizeInBytes)];
    }
    private double[] getRealAndTakeHalf(Complex[] complexes){
        double[] real = new double[complexes.length/2];

        for(int i = 0; i < complexes.length/2   ; i++){
            real[i] = complexes[i + complexes.length/2].getReal();
        }
        return real;
    }
    private Double[] getRealDouble(Complex[] complexes){
        Double[] real = new Double[complexes.length];
        for(int i = 0; i < complexes.length; i++){
            real[i] = complexes[i].getReal();
        }
        return real;
    }

    private double[] getFrequencyBins(int samplingRate, int fftSize){
        int accuracy = samplingRate/fftSize;
        return IntStream.range(0,samplingRate/(2*accuracy))
                .map(v -> v * (samplingRate/fftSize))
                .mapToDouble(Double::new)
                .toArray();
    }

}

