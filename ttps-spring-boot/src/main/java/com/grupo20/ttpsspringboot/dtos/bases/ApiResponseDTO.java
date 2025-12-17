package com.grupo20.ttpsspringboot.dtos.bases;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ApiResponseDTO {
    private boolean ok;
    private Integer  statusCode;
    private String status;
    private String message;

    // solo mensaje
    public ApiResponseDTO(boolean ok, String message) {
        this.ok = ok;
        this.message = message;
    }

    // mensaje + status
    public ApiResponseDTO(boolean ok, HttpStatus status, String message) {
        this.ok = ok;
        this.statusCode = status.value();
        this.status = status.name().toLowerCase();
        this.message = message;
    }
}
