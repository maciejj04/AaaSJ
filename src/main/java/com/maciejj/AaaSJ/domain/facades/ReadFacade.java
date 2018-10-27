package com.maciejj.AaaSJ.domain.facades;

import com.maciejj.AaaSJ.db.AudioFileInfoRepository;

public class ReadFacade {

    AudioFileInfoRepository repository;

    public ReadFacade(AudioFileInfoRepository repository) {
        this.repository = repository;
    }



}
