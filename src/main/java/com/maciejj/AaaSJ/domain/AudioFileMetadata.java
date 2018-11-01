package com.maciejj.AaaSJ.domain;

import java.util.Map;

public class AudioFileMetadata {

//    public enum AudioMetadataKey {
//        private String type;
//
//        AudioMetadataKey(String type){
//            this.type = type;
//        }
//
//        @Override
//        public String toString() {
//            return type;
//        }
//    }
//

    //TODO: figure out the best way to store metadata.
    private Map<String, String> metadata;

    public AudioFileMetadata withEntry(String key, String value){
        metadata.put(key, value);
        return null;
    }

}
