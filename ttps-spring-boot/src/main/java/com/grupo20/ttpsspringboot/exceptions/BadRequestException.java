package com.grupo20.ttpsspringboot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends APIException {
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "bad_request");
    }
}

