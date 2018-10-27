package com.maciejj.AaaSJ.db;

import com.maciejj.AaaSJ.domain.AudioFileDetails;
import org.springframework.data.repository.CrudRepository;

public abstract class AudioFileInfoRepository implements CrudRepository<AudioFileDetails, Integer> {
}
