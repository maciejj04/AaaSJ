package com.maciejj.AaaSJ.domain;

public class AudioFileDetails {

    private int offset;
    private AudioFileMetadata metadata;

    public AudioFileDetails(int offset, AudioFileMetadata metadata) {
        this.offset = offset;
        this.metadata = metadata;
    }

    public AudioFileMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(AudioFileMetadata metadata) {
        this.metadata = metadata;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

}
