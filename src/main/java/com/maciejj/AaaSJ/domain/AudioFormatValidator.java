package com.maciejj.AaaSJ.domain;

import javax.sound.sampled.AudioFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

enum AudioFormats{
    WAVE("wave");

    private String type;

    AudioFormats(String type){
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}

public class AudioFormatValidator {

    private int allowedNrOfchanells = 1;
    private List<AudioFormats> allowedFormats = Collections.singletonList(AudioFormats.WAVE);

    public static void validateAudioFormat(AudioFormat formatInfo) throws Exception {
        if (formatInfo.getChannels() != 1){
            throw new Exception("Required nr of chanells = 1. Format change not yet supported.");// TODO: AWS lambda for format exchange.e.g mp3->wave
        }
    }
}
