package com.maciejj.AaaSJ.services;

import com.maciejj.AaaSJ.commands.AmplitudeSpectrumRQ;
import org.junit.Test;

public class AmplitudeSpectrumServiceTest {

    AmplitudeSpectrumService service = new AmplitudeSpectrumService();
    int windowSize = 4096;

    @Test
    public void  ampitude_spectrum_should_have_proper_length() throws Exception {
        service.amplitudeSpectrum(new AmplitudeSpectrumRQ("simplaWhistle.wav", windowSize, windowSize/2));

    }

}