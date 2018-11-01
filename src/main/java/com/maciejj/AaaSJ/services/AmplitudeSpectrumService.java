package com.maciejj.AaaSJ.services;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.maciejj.AaaSJ.commands.AmplitudeSpectrumRQ;
import com.maciejj.AaaSJ.domain.AmplitudeSpectrum;
import com.maciejj.AaaSJ.domain.AudioFormatValidator;
import com.maciejj.AaaSJ.domain.BytesArrayMapper;
import com.maciejj.AaaSJ.domain.facades.InMemoryAudioFileFacade;
import com.maciejj.AaaSJ.infrastructure.PerformanceResolver;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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

@Service("amplitureSpectrumService_v1")
public class AmplitudeSpectrumService implements IAmplitudeSpectrumService {
    /*
        For now service will just use as many threads to process data as many processors are available on the machine.
        Need to establish sufficient thread count. Note Amdahls law!
     */

    Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    InMemoryAudioFileFacade audioFacade;

    public List<AmplitudeSpectrum> amplitudeSpectrum(AmplitudeSpectrumRQ request) throws Exception {
        validateRequest(request);
        audioFacade = new InMemoryAudioFileFacade(request.getFileName());
        logger.info("Amplitude spectrum computations...");

        //frequencyBins = generateFrequencyBins((int) this.formatInfo.getFrameRate(), 4096);

        ExecutorService executorService = newFixedThreadPool(PerformanceResolver.threadCountForThreadPool());
        ListeningExecutorService listeningExecutorService = listeningDecorator(executorService);

        List<ListenableFuture<AmplitudeSpectrum>> executors = new LinkedList<>();
        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);//?


        while (audioFacade.canRead()) {// This is not functional style. Consider extracting new methods and changing this to rather 'stream()/pipeline' code style.
            double[] buffer = audioFacade.read();

            executors.add(
                    listeningExecutorService.submit(() ->
                            new AmplitudeSpectrum(Arrays.asList(getRealAndTakeHalf(Double.class, fft.transform(buffer, TransformType.FORWARD))))
                    )
            );
        }

        // TODO: currently processed audio file informations should be stored in session(?).
        List<AmplitudeSpectrum> spectrums = Futures.allAsList(executors).get();

        logger.info("Finished amplitude spectrum computations...");
        return spectrums;
    }
}
