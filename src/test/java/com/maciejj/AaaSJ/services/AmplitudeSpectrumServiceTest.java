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

    private AmplitudeSpectrumService service = new AmplitudeSpectrumService();

    @Test
    @Ignore("TODO")
    public void ampitude_spectrum_should_have_proper_length() throws Exception {
        int windowSize = 4096;
        service.amplitudeSpectrum(new AmplitudeSpectrumRQ("simpleWhistle.wav", windowSize, windowSize/2));
    }

    @Test
    @Ignore("TODO")
    public void should_always_return_same_values(){
        //TODO: invoke service 2/3 times and deeply check all values that are present in response table.
    }

    @Test
    @Ignore("TODO")
    public void both_service_versions_should_return_same_values() {
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