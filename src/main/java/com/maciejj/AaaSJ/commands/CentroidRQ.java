package com.maciejj.AaaSJ.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CentroidRQ {
    String fileName;
    int windowSize;
}
