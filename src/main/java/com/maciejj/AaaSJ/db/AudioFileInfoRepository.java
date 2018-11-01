package com.maciejj.AaaSJ.db;

import com.maciejj.AaaSJ.domain.AudioFileDetails;
import org.springframework.data.repository.CrudRepository;

public interface AudioFileInfoRepository extends CrudRepository<AudioFileDetails, Integer> {
}
