package com.maciejj.AaaSJ.domain;

import org.apache.commons.math3.complex.Complex;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.stream.IntStream;

public class DomainUtils {

    public static double[] generateFrequencyBins(int samplingRate, int fftSize){
        int accuracy = samplingRate/fftSize;
        return IntStream.range(0,samplingRate/(2*accuracy))
                .map(v -> v * (samplingRate/fftSize))
                .mapToDouble(Double::new)
                .toArray();
    }

    public static byte[] generateByteBufer(int windowSize, int sampleSizeInBytes){ // TODO:
        return new byte[windowSize*(sampleSizeInBytes)];
    }

    public static boolean isPowerOf2(int number){
        return (number & -number) == number;
    }

    public static <T extends Number> T[] getRealAndTakeHalf(Class<? extends Number> type, Complex[] complexes){
        T[] real = (T[]) Array.newInstance(type, complexes.length/2);

        for(int i = 0; i < complexes.length/2; i++){
            real[i] = (T) type.cast(complexes[i + complexes.length/2].getReal());
        }
        return real;
    }

    public static double[] getRealAndTakeHalf(Complex[] complexes){
        double[] real = new double[complexes.length/2];

        for(int i = 0; i < complexes.length/2; i++){
            real[i] = complexes[i + complexes.length/2].getReal();
        }

        return real;
    }

    private Double[] getReal(Complex[] complexes){
        Double[] real = new Double[complexes.length];
        for(int i = 0; i < complexes.length; i++){
            real[i] = complexes[i].getReal();
        }
        return real;
    }
    private <T> T[] takeHalf(T[] arr){
        return Arrays.copyOfRange(arr,0,arr.length/2);
    }

}