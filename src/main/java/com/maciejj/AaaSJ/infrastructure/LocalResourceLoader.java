package com.maciejj.AaaSJ.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("local-dev")
@Component
public class LocalResourceLoader implements AudioResourceLoader {

    @Value("${audio-repository-path}")
    private String AUDIO_REPOSITORY_PATH;       // TODO: Enhance by specific user directory path.

    @Override
    public void get(String name) {

    }
}
