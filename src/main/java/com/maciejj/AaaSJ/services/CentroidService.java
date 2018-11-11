package com.maciejj.AaaSJ.services;


import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.maciejj.AaaSJ.commands.CentroidRQ;
import com.maciejj.AaaSJ.domain.AudioFormatValidator;
import com.maciejj.AaaSJ.domain.BytesArrayMapper;
import com.maciejj.AaaSJ.domain.Centroid;
import com.maciejj.AaaSJ.domain.facades.InMemoryAudioFileFacade;
import com.maciejj.AaaSJ.domain.facades.InMemoryAudioFileFacadeFactory;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import static com.google.common.util.concurrent.MoreExecutors.listeningDecorator;
import static com.maciejj.AaaSJ.domain.DomainUtils.generateByteBufer;
import static com.maciejj.AaaSJ.domain.DomainUtils.generateFrequencyBins;
import static com.maciejj.AaaSJ.domain.DomainUtils.getRealAndTakeHalf;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static javax.sound.sampled.AudioSystem.getAudioFileFormat;
import static javax.sound.sampled.AudioSystem.getAudioInputStream;

@Service
public class CentroidService {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private InMemoryAudioFileFacadeFactory audioFacadeFactory;

    public CentroidService(InMemoryAudioFileFacadeFactory audioFacadeFactory) {
        this.audioFacadeFactory = audioFacadeFactory;
    }

    public List<Centroid> centoids(CentroidRQ request) throws UnsupportedAudioFileException, IOException, ExecutionException, InterruptedException {
        validateRequest(request);
        InMemoryAudioFileFacade audiofacade = audioFacadeFactory.getOne(request.getFileName());

        double[] frequencyBins = generateFrequencyBins(audiofacade.getFrameRate(), 4096);

        ExecutorService executorService = newFixedThreadPool(2);
        ListeningExecutorService listeningExecutorService = listeningDecorator(executorService);

        List<ListenableFuture<Centroid>> executors = new LinkedList<>();
        FastFourierTransformer fftObject = new FastFourierTransformer(DftNormalization.STANDARD);//?

        logger.info("Computing centroids...");
        while (audiofacade.canRead()){// This is rather functional style. Consider extracting new methods and changing this to rather 'stream()/pipeline' code style.
            double[] mappedBuffer = audiofacade.read();

            executors.add(
                    listeningExecutorService.submit(() ->
                            calculateCentroid(
                                    getRealAndTakeHalf(Double.class, fftObject.transform(mappedBuffer, TransformType.FORWARD)),
                                    frequencyBins
                            )

                    )
            );
//            Plot plt = Plot.create();
//            plt.plot().add(Arrays.asList(getRealDouble(fftObject.transform(mapper.updateData(buffer).mapToDoubleArr(), TransformType.FORWARD))));
//            plt.show();
        }

        // TODO: currently processed audio file informations should be stored in cache.
        return Futures.allAsList(executors).get();
    }

    private void validateRequest(CentroidRQ request) {
        //TODO: validate window width to be power of 2.
    }


    private Centroid calculateCentroid(Double[] amplitudeSpectrum, double[] frequencyBins){
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

