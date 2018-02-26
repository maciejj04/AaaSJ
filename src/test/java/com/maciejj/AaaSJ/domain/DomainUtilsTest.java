package com.maciejj.AaaSJ.domain;

import org.junit.Test;

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

}