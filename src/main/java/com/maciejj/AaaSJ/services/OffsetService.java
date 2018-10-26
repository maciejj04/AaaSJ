package com.maciejj.AaaSJ.services;


import com.maciejj.AaaSJ.commands.AudioFileRQ;
import com.maciejj.AaaSJ.domain.AudioFileDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.sound.sampled.AudioInputStream;
import java.io.File;
import java.util.concurrent.*;

import static javax.sound.sampled.AudioSystem.getAudioInputStream;

@RestController
public class OffsetService {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());



    @RequestMapping(method = RequestMethod.POST, path = "/offset")
    public ResponseEntity setOffset(AudioFileRQ fileRQ){
        // Service should calculate other audio file metadata which are being preserved in DB.

        CompletableFuture.runAsync(() -> syncGetOffset(fileRQ));

        return ResponseEntity.ok("OK WILL DO");
    }

    private void syncGetOffset(AudioFileRQ fileRQ) {
        try{
            logger.info("Calculating offset...");

            AudioInputStream audioStream = getAudioInputStream(new File(fileRQ.getLocalPath() + fileRQ.getName()));
            // TODO: ...




            Thread.sleep(5000);
            logger.info("Updating DB...");
        } catch (Throwable throwable) {
            logger.info("Calculating offset...");
            // Add to task queue to be eventually consistent.
        }
    }

}
