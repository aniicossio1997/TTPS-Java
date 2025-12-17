package com.grupo20.ttpsspringboot.exceptions;

import com.grupo20.ttpsspringboot.dtos.ErrorDTO;
import com.grupo20.ttpsspringboot.dtos.bases.ApiResponseDTO;
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

    private ResponseEntity<ApiResponseDTO> build(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(new ApiResponseDTO(false, status.value(), status.name(),message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getAllErrors().stream()
                .findFirst()
                .map(ObjectError::getDefaultMessage)
                .orElse("La petición contiene uno o más campos inválidos.");

        return build(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponseDTO> handleJsonParseException(HttpMessageNotReadableException ex) {
        return build(HttpStatus.BAD_REQUEST, "Error de formato de la petición.");
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<ApiResponseDTO> handleApiException(APIException ex) {
        // si tu APIException ya tiene status, lo respetamos:
        return build(ex.getStatus(), ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDTO> handleIllegalArgument(IllegalArgumentException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO> handleGeneric(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
    }
}
