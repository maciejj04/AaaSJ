package com.maciejj.AaaSJ.infrastructure;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public class TimerProxy implements InvocationHandler {

    private Object targetObject;
    Logger logger = Logger.getLogger("Timer");

    public TimerProxy(Object targetObject) {
        this.targetObject = targetObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long startTime = System.nanoTime();
        method.invoke(targetObject, args);
        System.out.println("Method "+method.getName()+" took: "+ (System.nanoTime()-startTime)+" nanoSec");

        return new Object();
    }
}
