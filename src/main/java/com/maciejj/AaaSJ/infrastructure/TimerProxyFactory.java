package com.maciejj.AaaSJ.infrastructure;

import java.lang.reflect.Proxy;

public class TimerProxyFactory {

    public static Object newInstance(Object object){
        return Proxy.newProxyInstance(object.getClass().getClassLoader(), object.getClass().getInterfaces() , new TimerProxy(object));
    }
}
