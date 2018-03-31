package com.maciejj.AaaSJ.infrastructure;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.logging.Logger;

public class TimerProxy extends HandlerInterceptorAdapter {

    private final String START_TIME = "start-time";

    //TODO: add Logger

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(START_TIME, System.nanoTime());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long startTime = (long) request.getAttribute(START_TIME);
        System.out.println("Request on " + request.getRequestURI() +" took: "+ (System.nanoTime() - startTime)+" nanoSec");
    }
}
