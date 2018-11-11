package com.maciejj.AaaSJ.domain.facades;

import com.maciejj.AaaSJ.domain.AudioFileMetadata;
import com.maciejj.AaaSJ.domain.AudioFormatValidator;
import com.maciejj.AaaSJ.domain.BytesArrayMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

import static com.maciejj.AaaSJ.domain.DomainUtils.generateByteBufer;
import static javax.sound.sampled.AudioSystem.getAudioInputStream;

public class InMemoryAudioFileFacade {

    private String name;
    private Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private AudioInputStream audioStream;
    private byte[] buffer;//TODO: for now just use power of 2 request.getWindowSize()
    private BytesArrayMapper mapper;
    private int sampleSizeInBytes;

    private AudioFileFormat audioFileData;
    private AudioFormat formatInfo;

    private String audioRepositoryPath;       // TODO: Enhance by specific user directory path.

    public InMemoryAudioFileFacade(String audioRepositoryPath) {
        this.audioRepositoryPath = audioRepositoryPath;
    }

    public InMemoryAudioFileFacade runningAudioFile(String fileName) throws IOException, UnsupportedAudioFileException {
        this.name = fileName;
        audioStream = getAudioInputStream(new File(audioRepositoryPath + name));
        formatInfo = audioStream.getFormat();
        AudioFormatValidator.validateAudioFormat(formatInfo);
        sampleSizeInBytes = formatInfo.getSampleSizeInBits() / 8;

        buffer = generateByteBufer(4096, sampleSizeInBytes);
        mapper = new BytesArrayMapper(sampleSizeInBytes);
        return this;
    }

    public double[] read() {
        try {
            audioStream.read(buffer);
        } catch (IOException e) {

            logger.warn("Could not read " + name + " further. Use canRead()!");
            e.printStackTrace();
        }
        return mapper.updateData(buffer).mapToDoubleArr();
    }

    public boolean canRead() throws IOException {
        if (audioStream.available() > 0) {
            audioStream.close();
            return false;
        }
        return true;
    }

    public AudioInputStream getAudioStream() {
        return audioStream;
    }

    public AudioFileMetadata getMetadata() {
        return new AudioFileMetadata()
                    .withEntry("", "");
                    //...

    }
    public int getFrameRate() {
        return (int) formatInfo.getFrameRate(); // int?
    }

}
