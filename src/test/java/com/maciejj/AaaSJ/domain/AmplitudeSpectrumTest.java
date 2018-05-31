package com.maciejj.AaaSJ.domain;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class AmplitudeSpectrumTest {

    @Test
    public void should_map_properly(){
        // given
        Integer[] integers = IntStream.iterate(0, e -> e + 1).boxed().limit(1000).toArray(Integer[]::new);

        try{
            // when
            AmplitudeSpectrum amplitudeSpectrumFromIntegers = new AmplitudeSpectrum(integers);

            // then
            assertEquals(amplitudeSpectrumFromIntegers.getCoefficients().size(), integers.length);
        } catch (ClassCastException e){
            Assertions.fail("Cast exception!");
        }
    }
}