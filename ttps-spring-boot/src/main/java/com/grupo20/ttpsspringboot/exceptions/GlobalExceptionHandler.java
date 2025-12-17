package com.grupo20.ttpsspringboot.exceptions;

import com.grupo20.ttpsspringboot.dtos.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
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

    // Este es el método que capturará tu BadRequestException porque hereda de APIException
    @ExceptionHandler(APIException.class)
    public ResponseEntity<ErrorDTO> handleApiException(APIException ex) {
        ErrorDTO error = new ErrorDTO(
                ex.getStatus().value(),
                ex.getMessage(),
                ex.getErrorCode()
        );
        return ResponseEntity.status(ex.getStatus()).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDTO> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorDTO error = new ErrorDTO(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                "bad_request"
        );
        return ResponseEntity.badRequest().body(error);
    }

}