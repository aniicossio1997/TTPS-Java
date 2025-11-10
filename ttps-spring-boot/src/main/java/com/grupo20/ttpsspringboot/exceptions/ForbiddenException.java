package com.grupo20.ttpsspringboot.exceptions;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends APIException {

    public ForbiddenException() {
        super("No permtido", HttpStatus.FORBIDDEN, "forbidden");
    }
}
