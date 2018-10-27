package com.maciejj.AaaSJ.domain.facades;

import com.maciejj.AaaSJ.domain.AudioFormatValidator;
import com.maciejj.AaaSJ.domain.BytesArrayMapper;
import org.springframework.beans.factory.annotation.Value;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

import static com.maciejj.AaaSJ.domain.DomainUtils.generateByteBufer;
import static javax.sound.sampled.AudioSystem.getAudioInputStream;

public class InMemoryAudioFileFacade {

    private final AudioInputStream audioStream;
    private byte[] buffer;//TODO: for now just use power of 2 request.getWindowSize()
    private BytesArrayMapper mapper;
    private int sampleSizeInBytes;

    private AudioFileFormat audioFileData;
    private AudioFormat formatInfo;


    @Value("${audio-repository-path:/var/tmp/aaasj/}")
    private String audioRepositoryPath;       // TODO: Enhance by specific user directory path.


    public InMemoryAudioFileFacade(String name) throws IOException, UnsupportedAudioFileException {
        audioStream = getAudioInputStream(new File(audioRepositoryPath + name));
        formatInfo = audioStream.getFormat();
        AudioFormatValidator.validateAudioFormat(formatInfo);
        sampleSizeInBytes = formatInfo.getSampleSizeInBits() / 8;

        buffer = generateByteBufer(4096, sampleSizeInBytes);
        mapper = new BytesArrayMapper(sampleSizeInBytes);
    }

    public double[] read() throws IOException {
        audioStream.read(buffer);
        return mapper.updateData(buffer).mapToDoubleArr();
    }

    public boolean canRead() throws IOException {
        if (audioStream.available() > 0) {
            audioStream.close();
            return false;
        }
        return true;
    }
}
