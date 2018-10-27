package com.maciejj.AaaSJ.services;

import com.maciejj.AaaSJ.commands.CalculateOffsetCommand;
import com.maciejj.AaaSJ.commands.RegisterTrackRQ;
import com.maciejj.AaaSJ.db.AudioFileInfoRepository;
import com.maciejj.AaaSJ.domain.AudioAttributes;
import com.maciejj.AaaSJ.domain.AudioFileDetails;
import com.maciejj.AaaSJ.domain.AudioFileMetadata;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class TrackRegistrationService {

    private OffsetService offsetService;
    private IAmplitudeSpectrumService amplitudeSpectrumService;
    private AudioFileInfoRepository repository;

    public TrackRegistrationService(OffsetService offsetService, IAmplitudeSpectrumService amplitudeSpectrumService) {
        this.offsetService = offsetService;
        this.amplitudeSpectrumService = amplitudeSpectrumService;
    }

    public void registerTrack(RegisterTrackRQ registerTrackRQ) throws InterruptedException, ExecutionException {
        // TODO: async. - open audio file and process async
        AudioAttributes audioAttributes = calculateAttributes(registerTrackRQ);
        AudioFileMetadata metadata = getMetadata();//.thenAcceptAsync((metadata) -> amplitudeSpectrumService.amplitudeSpectrum());


        // DB insert.
        repository.save(new AudioFileDetails(audioAttributes.getOffset(), metadata));
    }

    private AudioFileMetadata getMetadata(){
        // duration etc.
        return new AudioFileMetadata().withValues("duration", "1:23");// TODO: Map keys may be enum values.
    }

    private AudioAttributes calculateAttributes(RegisterTrackRQ track) throws ExecutionException, InterruptedException {
        // offset
        CompletableFuture offsetFuture = offsetService.calculateOffset(new CalculateOffsetCommand(track.getName(), track.getLocalPath()));


        CompletableFuture.allOf(offsetFuture).get();
        return new AudioAttributes((Integer) offsetFuture.get());

    }

}
