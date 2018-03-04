package com.maciejj.AaaSJ.services;

import com.maciejj.AaaSJ.commands.AmplitudeSpectrumRQ;
import com.maciejj.AaaSJ.domain.AmplitudeSpectrum;

import java.util.List;

public interface IAmplitudeSpectrumService {
    List<AmplitudeSpectrum> amplitudeSpectrum(AmplitudeSpectrumRQ request) throws Exception;
}
