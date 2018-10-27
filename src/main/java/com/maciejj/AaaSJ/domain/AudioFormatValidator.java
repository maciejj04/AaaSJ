package com.maciejj.AaaSJ.domain;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.util.Collections;
import java.util.List;

enum AudioFormat {
    WAVE("wave");

    private String type;

    AudioFormat(String type){
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}

// May be extracted to AWS lambda in future.
public class AudioFormatValidator {

    private int allowedNrOfchanells = 1;
    private List<AudioFormat> allowedFormats = Collections.singletonList(AudioFormat.WAVE);

    public static void validateAudioFormat(javax.sound.sampled.AudioFormat formatInfo) throws UnsupportedAudioFileException {
        if (formatInfo.getChannels() != 1){
            throw new UnsupportedAudioFileException("Required nr of chanells = 1. Format change not yet supported.");// TODO: AWS lambda for format exchange.e.g mp3->wave
        }
    }
}
