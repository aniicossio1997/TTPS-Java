package com.grupo20.ttpsspringboot.exceptions;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends APIException {

    public ForbiddenException() {
        super("No permtido", HttpStatus.FORBIDDEN, "forbidden");
    }

    public  ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN, "FORBIDDEN");
    }
}
