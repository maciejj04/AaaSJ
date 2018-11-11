package com.maciejj.AaaSJ.services;

import com.maciejj.AaaSJ.commands.CalculateOffsetCommand;
import com.maciejj.AaaSJ.commands.RegisterTrackRQ;
import com.maciejj.AaaSJ.db.AudioFileInfoRepository;
import com.maciejj.AaaSJ.domain.ComputedAudioAttributes;
import com.maciejj.AaaSJ.domain.AudioFileDetails;
import com.maciejj.AaaSJ.domain.AudioFileMetadata;
import com.maciejj.AaaSJ.domain.BaseAudioAttributes;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class TrackRegistrationService {

    private OffsetService offsetService;
    private IAmplitudeSpectrumService amplitudeSpectrumService;
    private AudioFileInfoRepository repository;

    public TrackRegistrationService(
            OffsetService offsetService,
            @Qualifier("amplitureSpectrumService_v1") IAmplitudeSpectrumService amplitudeSpectrumService,
            AudioFileInfoRepository repository) {

        this.offsetService = offsetService;
        this.amplitudeSpectrumService = amplitudeSpectrumService;
        this.repository = repository;
    }

    public void registerTrack(RegisterTrackRQ registerTrackRQ) throws InterruptedException, ExecutionException {
        // TODO: async. - open audio file and process async
        ComputedAudioAttributes attributes = calculateAttributes(registerTrackRQ);
        AudioFileMetadata metadata = getMetadata();//.thenAcceptAsync((metadata) -> amplitudeSpectrumService.amplitudeSpectrum());
        BaseAudioAttributes baseAttributes = getBaseAudioAttributes();

        repository.save(new AudioFileDetails(registerTrackRQ.getName(), "mockCreator", baseAttributes, attributes));// mockCreator may be JWT or some other system-token value. - may be placed in InvocationContext
    }

    private AudioFileMetadata getMetadata(){
        // duration etc.
        return new AudioFileMetadata();
    }

    private BaseAudioAttributes getBaseAudioAttributes() {
        // Dirty code! Make use of InMemoryAudioFileFacade.
        return new BaseAudioAttributes(
            1,2,3
        );
    }

    private ComputedAudioAttributes calculateAttributes(RegisterTrackRQ track) throws ExecutionException, InterruptedException {
        // offset
        CompletableFuture<Integer> offsetFuture = offsetService.calculateOffset(new CalculateOffsetCommand(track.getName(), track.getLocalPath()));
        //TODO: add centroid.

        CompletableFuture.allOf(offsetFuture).get();
        // refactor to base attributes and extended(those which need some computations ones)?
        return new ComputedAudioAttributes(offsetFuture.get());

    }

}
