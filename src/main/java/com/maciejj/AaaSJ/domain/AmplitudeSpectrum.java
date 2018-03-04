package com.maciejj.AaaSJ.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AmplitudeSpectrum {
    List<Double> coefficients;

    public <T extends Number> AmplitudeSpectrum(T[] coefficients) {
        this.coefficients = (List<Double>)Arrays.asList(coefficients);
    }
}
