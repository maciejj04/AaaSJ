package com.maciejj.AaaSJ.services;

import com.maciejj.AaaSJ.commands.CalculateOffsetCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioInputStream;
import java.io.File;
import java.util.concurrent.*;

import static javax.sound.sampled.AudioSystem.getAudioInputStream;

public class OffsetService {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    public CompletableFuture calculateOffset(CalculateOffsetCommand offsetCmd){
        return CompletableFuture.runAsync(() -> syncGetOffset(offsetCmd));
    }

    private Integer syncGetOffset(CalculateOffsetCommand offsetCmd) {
        try{
            logger.info("Calculating offset...");

            AudioInputStream audioStream = getAudioInputStream(new File(offsetCmd.getLocalFsPath() + offsetCmd.getName()));
            // TODO: ...




            Thread.sleep(5000);
            logger.info("Updating DB...");
        } catch (Throwable throwable) {
            logger.warn("Calculating offset failed, adding task queue...");
            // throw
            // Add to task queue to be eventually consistent.
        }
        return 0;
    }

}
