package com.maciejj.AaaSJ.domain;

import com.maciejj.AaaSJ.domain.exception.BaseDomainException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.sound.sampled.UnsupportedAudioFileException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.status;

@ControllerAdvice
public class DomainExceptionMapper {

    @ExceptionHandler(Throwable.class)
    public final ResponseEntity handleException(Exception e, WebRequest request){
        if (e instanceof BaseDomainException) {
            // I am gonna need it :D to be polished.
            return ((BaseDomainException) e).mapToHttp();
        } else {
            if (e instanceof UnsupportedAudioFileException) {
                return badRequest().build();
            } else {
                return status(INTERNAL_SERVER_ERROR).build();
            }
        }
    }

}
