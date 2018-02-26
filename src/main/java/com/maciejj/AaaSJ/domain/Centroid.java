package com.maciejj.AaaSJ.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Timer;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Centroid {
    //TODO: add time?
    private Float amplitude;
    private Float frequency;
}
