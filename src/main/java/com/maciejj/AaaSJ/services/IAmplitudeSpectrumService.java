package com.maciejj.AaaSJ.services;

import com.maciejj.AaaSJ.commands.AmplitudeSpectrumRQ;
import com.maciejj.AaaSJ.domain.AmplitudeSpectrum;

import java.util.List;

import static com.maciejj.AaaSJ.domain.DomainUtils.isPowerOf2;

public interface IAmplitudeSpectrumService {
    int defaultWindowSize = 4100;

    List<AmplitudeSpectrum> amplitudeSpectrum(AmplitudeSpectrumRQ request) throws Exception;

    // Checks if request contains required informations to perform service.
    default void validateRequest(AmplitudeSpectrumRQ request) {
        if (!isPowerOf2(request.getWindowSize())) {
            throw new IllegalArgumentException("Not power of 2!");
        }
    }
}
