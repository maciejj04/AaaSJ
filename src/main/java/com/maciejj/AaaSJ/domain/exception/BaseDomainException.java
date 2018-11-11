package com.maciejj.AaaSJ.domain.exception;

import org.springframework.http.ResponseEntity;

public abstract class BaseDomainException extends Exception {
    public abstract ResponseEntity mapToHttp();
}
