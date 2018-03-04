package com.maciejj.AaaSJ.domain;

import org.junit.Ignore;
import org.junit.Test;

import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class DomainUtilsTest {

    @Test
    public void should_check_power_of_2(){
        assertTrue(DomainUtils.isPowerOf2(2));
        assertTrue(DomainUtils.isPowerOf2(16_384));

        assertFalse(DomainUtils.isPowerOf2(3));
        assertFalse(DomainUtils.isPowerOf2(16_383));

        assertFalse(DomainUtils.isPowerOf2(-199));
        assertFalse(DomainUtils.isPowerOf2(-16_384));
    }

    @Test
    public void should_split_array_properly(){
        // given
        double[] testData = DoubleStream.iterate(1, e -> e + 1).limit(1000).toArray();

        // when
        double[][] splitted = DomainUtils.splitDataByNElements(testData, 100);
        double[][] splitted2 = DomainUtils.splitDataByNElements(testData, 99);

        //then
        assertEquals(splitted.length, 10);
        assertEquals(splitted2.length, 11);

    }

}