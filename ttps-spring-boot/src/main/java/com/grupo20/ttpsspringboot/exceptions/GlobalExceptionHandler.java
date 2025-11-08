package com.grupo20.ttpsspringboot.exceptions;

import com.grupo20.ttpsspringboot.dtos.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorCampo = "Datos de entrada inválidos";
        String detalleMensaje = "La petición contiene uno o más campos inválidos.";

        ObjectError primerError = ex.getBindingResult().getAllErrors().stream()
                .findFirst()
                .orElse(null);

        if (primerError != null) {
            detalleMensaje = primerError.getDefaultMessage();

            if (primerError instanceof FieldError) {
                FieldError fieldError = (FieldError) primerError;
                errorCampo = "El campo '" + fieldError.getField() + "' contiene un error.";
            } else {
                errorCampo = "Error de validación a nivel de objeto.";
            }
        }

        ErrorDTO errorDTO = new ErrorDTO(
                HttpStatus.BAD_REQUEST.value(),
                detalleMensaje,
                errorCampo
        );

        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDTO> handleJsonParseException(HttpMessageNotReadableException ex) {

        ErrorDTO errorDTO = new ErrorDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Error de formato de la petición.",
                "json malformado"
        );

        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }
}
