package com.maciejj.AaaSJ.infrastructure;

import lombok.Getter;
import org.springframework.stereotype.Component;


@Component
@Getter
public class PerformanceResolver {
// TODO: This is to be implemented.

    // Maybe it is just enough to get availableProcessors in every place needed usind Runtime?
    private static int processorsCount;
    //private int threads;

    PerformanceResolver() {
        processorsCount = Runtime.getRuntime().availableProcessors();
    }
    public static int threadCountForThreadPool() {
        return processorsCount;
    }
}
