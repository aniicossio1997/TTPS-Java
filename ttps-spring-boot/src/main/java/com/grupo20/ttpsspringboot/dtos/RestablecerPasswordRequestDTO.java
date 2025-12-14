package com.grupo20.ttpsspringboot.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;
import lombok.Data;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestablecerPasswordRequestDTO {
    @NotBlank(message = "La contraseña actual es obligatoria")
    private String passwordOld;

    @NotBlank(message = "La nueva contraseña es obligatoria")
    @Size(min = 6, message = "La nueva contraseña debe tener al menos 8 caracteres")
    private String nuevoPassword;

    @NotBlank(message = "La confirmación es obligatoria")
    private String confirmarPassword;

}
