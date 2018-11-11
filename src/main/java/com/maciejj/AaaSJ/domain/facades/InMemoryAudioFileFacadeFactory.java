package com.maciejj.AaaSJ.domain.facades;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class InMemoryAudioFileFacadeFactory {

    private Logger logger = LoggerFactory.getLogger(InMemoryAudioFileFacadeFactory.class.getSimpleName());

    @Value("${audio-repository-path:/var/tmp/aaasj/}")
    private String audioRepositoryPath;

    public InMemoryAudioFileFacade getOne(String fileName) throws UnsupportedAudioFileException {
        // InvocationContext.getProperties
        // ^e.g. user properties from JWT.
        try {
            return new InMemoryAudioFileFacade(audioRepositoryPath).runningAudioFile(fileName);
        } catch (IOException e) {
            String domainErrorInfo = "Domain internal error." + e;
            logger.error(domainErrorInfo, e);
            throw new InternalError(domainErrorInfo, e);
        }
    }

}
