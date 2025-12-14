package com.grupo20.ttpsspringboot.dtos.bases;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponseDTO {
    private boolean ok;
    private int statusCode;
    private String message;
}
