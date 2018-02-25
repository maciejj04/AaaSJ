package com.maciejj.AaaSJ.domain;

import org.junit.Test;

import javax.sound.sampled.UnsupportedAudioFileException;

import static org.junit.Assert.*;

public class BytesArrayMapperTest {

    @Test
    public void should_properly_map_to_double_array() throws UnsupportedAudioFileException {
        byte[] bytes = new byte[] {(byte)0x00, (byte)0x2F, (byte)0x01, (byte)0x10};
        BytesArrayMapper mapper = new BytesArrayMapper(bytes, 2);

        double[] doubles = mapper.mapToDoubleArr();
        assertArrayEquals(new double[]{47d, 272d}, doubles, 0);
    }
}