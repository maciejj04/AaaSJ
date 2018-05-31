package com.maciejj.AaaSJ.endpoints;

import com.maciejj.AaaSJ.commands.AmplitudeSpectrumRQ;
import com.maciejj.AaaSJ.domain.AmplitudeSpectrum;
import com.maciejj.AaaSJ.services.IAmplitudeSpectrumService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AmplitudeSpectrumServiceRestEndpoints {

    private IAmplitudeSpectrumService amplitudeSpectrumService;
    private IAmplitudeSpectrumService amplitudeSpectrumService_v2;

    public AmplitudeSpectrumServiceRestEndpoints(
            @Qualifier("amplitureSpectrumService_v1") IAmplitudeSpectrumService amplitudeSpectrumService,
            @Qualifier("amplitureSpectrumService_v2") IAmplitudeSpectrumService amplitudeSpectrumService_v2)
    {
        this.amplitudeSpectrumService = amplitudeSpectrumService;
        this.amplitudeSpectrumService_v2 = amplitudeSpectrumService_v2;
    }

    @RequestMapping(path = "/v1/amplitudeSpectrum")
    public List<AmplitudeSpectrum> amplitudeSpectrum_v1(@RequestBody AmplitudeSpectrumRQ request) throws Exception {
        return amplitudeSpectrumService.amplitudeSpectrum(request);
    }

    @RequestMapping(path = "/v2/amplitudeSpectrum")
    public List<AmplitudeSpectrum> amplitudeSpectrum_v2(@RequestBody AmplitudeSpectrumRQ request) throws Exception {
        return amplitudeSpectrumService.amplitudeSpectrum(request);
    }

}
