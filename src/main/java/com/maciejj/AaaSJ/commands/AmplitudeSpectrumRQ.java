package com.maciejj.AaaSJ.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AmplitudeSpectrumRQ {
    private String fileName;
    private int windowSize;
    private int offset;
}
