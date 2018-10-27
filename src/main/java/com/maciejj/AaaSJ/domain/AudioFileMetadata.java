package com.maciejj.AaaSJ.domain;

import java.util.Map;

public class AudioFileMetadata {

    private Map<String, String> metadata;

    public AudioFileMetadata withValues(String key, String value){
        metadata.put(key, value);
        return null;
    }
}
