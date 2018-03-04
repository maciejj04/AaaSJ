package com.maciejj.AaaSJ.services;

import com.maciejj.AaaSJ.commands.AmplitudeSpectrumRQ;
import com.maciejj.AaaSJ.domain.AmplitudeSpectrum;
import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class AmplitudeSpectrumServiceTest {

    AmplitudeSpectrumService service = new AmplitudeSpectrumService();
    int windowSize = 4096;

    @Test
    @Ignore("TODO")
    public void  ampitude_spectrum_should_have_proper_length() throws Exception {
        service.amplitudeSpectrum(new AmplitudeSpectrumRQ("simplaWhistle.wav", windowSize, windowSize/2));

    }

    @Test
    public void should_map_properly(){
        Double[] doubles = DoubleStream.iterate(0, e -> e + 1).boxed().limit(1000).toArray(Double[]::new);
        Integer[] integers = IntStream.iterate(0, e -> e + 1).boxed().limit(1000).toArray(Integer[]::new);;

        try{
            new AmplitudeSpectrum(doubles);
            AmplitudeSpectrum amplitudeSpectrumFromIntegers = new AmplitudeSpectrum(integers);
            assertEquals(amplitudeSpectrumFromIntegers.getCoefficients().size(), integers.length);
        } catch (ClassCastException e){
            Assertions.fail("Exception on cast!");
        }
    }

}