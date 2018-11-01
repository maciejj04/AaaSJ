package com.maciejj.AaaSJ.domain;


import javax.persistence.Embeddable;

@Embeddable
public class BaseAudioAttributes {

    private int sampleSizeInBits;
    private int frameSize;
    private int frameRate;

    public BaseAudioAttributes(int sampleSizeInBits, int frameSize, int frameRate) {
        this.sampleSizeInBits = sampleSizeInBits;
        this.frameSize = frameSize;
        this.frameRate = frameRate;
    }

    public int getSampleSizeInBits() {
        return sampleSizeInBits;
    }

    public void setSampleSizeInBits(int sampleSizeInBits) {
        this.sampleSizeInBits = sampleSizeInBits;
    }

    public int getFrameSize() {
        return frameSize;
    }

    public void setFrameSize(int frameSize) {
        this.frameSize = frameSize;
    }

    public int getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }

}
