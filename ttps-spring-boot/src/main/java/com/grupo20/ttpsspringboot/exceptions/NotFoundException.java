package com.grupo20.ttpsspringboot.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends APIException {

    public NotFoundException() {
        super("Recurso no encontrado", HttpStatus.NOT_FOUND, "not_found");
    }

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "not_found");
    }
}
