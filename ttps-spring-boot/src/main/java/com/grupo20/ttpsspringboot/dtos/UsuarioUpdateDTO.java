package com.grupo20.ttpsspringboot.dtos;

import com.grupo20.ttpsspringboot.domain.models.Usuario;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Optional;

@Data
public class UsuarioUpdateDTO {

    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String nombre;

    @Size(max = 100, message = "El apellido no puede exceder los 100 caracteres")
    private String apellido;

    @NotNull(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    private String email;

    private String telefono;

    private UbicacionUpdateDTO ubicacion;

}