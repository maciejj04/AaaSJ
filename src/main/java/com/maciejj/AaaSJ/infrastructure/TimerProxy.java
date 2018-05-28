package com.maciejj.AaaSJ.infrastructure;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TimerProxy extends HandlerInterceptorAdapter {

    private final String START_TIME = "start-time";

    Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(START_TIME, System.nanoTime());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long startTime = (long) request.getAttribute(START_TIME);
        logger.info("Request on {} took: {} nanoSec", request.getRequestURI(), System.nanoTime() - startTime);
    }
}
