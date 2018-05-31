package com.maciejj.AaaSJ.services;

import com.maciejj.AaaSJ.Utils.SpringDataProviderRunner;
import com.maciejj.AaaSJ.commands.AmplitudeSpectrumRQ;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.Resource;
import javax.sound.sampled.AudioInputStream;
import java.io.File;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static javax.sound.sampled.AudioSystem.getAudioInputStream;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringDataProviderRunner.class)
@TestPropertySource(locations="classpath:test.properties")
public class AmplitudeSpectrumServiceTest {

    @Resource(name = "amplitureSpectrumService_v2")
    private AmplitudeSpectrumService_v2 amplitudeSpectrumService_v2;

    @Resource(name = "amplitureSpectrumService_v1")
    private AmplitudeSpectrumService amplitudeSpectrumService_v1;

    @Value("${audio-repository-path}")
    String AUDIO_REPOSITORY_PATH;


    @Test
    @Ignore("This test is not passing. Have a look at service. It's probably trimming data")
    public void ampitude_spectrum_should_have_proper_length() throws Exception {
        // given
        int windowSize = 4096;
        String testAudioFile = "simpleWhistle.wav";
        AmplitudeSpectrumRQ amplitudeSpectrumRQ = new AmplitudeSpectrumRQ(testAudioFile, windowSize, windowSize / 2);
        long framesCount;
        try (AudioInputStream audioInputStream = getAudioInputStream(new File(AUDIO_REPOSITORY_PATH + testAudioFile))) {
            framesCount = audioInputStream.getFrameLength();
        }

        // when
        List<Double> resp1 = amplitudeSpectrumService_v1.amplitudeSpectrum(amplitudeSpectrumRQ).stream().flatMap((e) -> e.getCoefficients().stream()).collect(toList());

        // then
        assertThat(resp1.size()).isEqualTo(framesCount/2);

    }

    //TODO: Should be testes with various cases (to be done using @DataProvider)
    @Test
    public void v1_service_should_always_return_same_values() throws Exception {
        //TODO: invoke service 2/3 times and deeply check all values that are present in response table.
        // given
        int windowSize = 4096;
        AmplitudeSpectrumRQ amplitudeSpectrumRQ = new AmplitudeSpectrumRQ("simpleWhistle.wav", windowSize, windowSize / 2);

        // when
        List<Double> resp1 = amplitudeSpectrumService_v1.amplitudeSpectrum(amplitudeSpectrumRQ).stream().flatMap((e) -> e.getCoefficients().stream()).collect(toList());
        List<Double> resp2 = amplitudeSpectrumService_v1.amplitudeSpectrum(amplitudeSpectrumRQ).stream().flatMap((e) -> e.getCoefficients().stream()).collect(toList());
        List<Double> resp3 = amplitudeSpectrumService_v1.amplitudeSpectrum(amplitudeSpectrumRQ).stream().flatMap((e) -> e.getCoefficients().stream()).collect(toList());

        // then
        assertThat(resp1).containsExactly(resp2.toArray(new Double[resp2.size()]));
        assertThat(resp2).containsExactly(resp3.toArray(new Double[resp3.size()]));
    }

    @Test
    @Ignore("TODO")
    public void both_service_versions_should_return_same_values() {
    }

}