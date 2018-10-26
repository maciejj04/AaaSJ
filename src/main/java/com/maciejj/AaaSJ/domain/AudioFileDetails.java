package com.maciejj.AaaSJ.domain;

import java.util.Map;

public class AudioFileDetails {

    private int offset;

    private Map<String, String> metadata;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
