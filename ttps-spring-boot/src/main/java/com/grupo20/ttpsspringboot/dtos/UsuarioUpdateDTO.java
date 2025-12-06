package com.grupo20.ttpsspringboot.dtos;

import com.grupo20.ttpsspringboot.domain.models.Usuario;
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

    @Email(message = "Debe ser un formato de email válido")
    private String email;

    private UbicacionUpdateDTO ubicacion;

}