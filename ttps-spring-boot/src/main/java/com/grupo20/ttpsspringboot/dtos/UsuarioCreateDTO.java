package com.grupo20.ttpsspringboot.dtos;

import com.grupo20.ttpsspringboot.domain.enums.EstadoUsuarioEnum;
import com.grupo20.ttpsspringboot.domain.enums.RolUsuarioEnum;
import com.grupo20.ttpsspringboot.domain.models.Ubicacion; // Importa Ubicacion
import com.grupo20.ttpsspringboot.domain.models.Usuario;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UsuarioCreateDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @Email(message = "Debe ser un formato de email válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @NotNull(message = "El rol es obligatorio")
    private RolUsuarioEnum rol;

    @NotNull(message = "La ubicación es obligatoria")
    public UbicacionCreateDTO ubicacion;

    private String telefono;
    /**
     * Convierte el DTO a la entidad Usuario.
     * @param ubicacion La entidad Ubicacion, que debe ser buscada previamente por el servicio usando ubicacionId.
     * @return Una nueva instancia de la entidad Usuario.
     */
    public Usuario toEntity(Ubicacion ubicacion) {
        Usuario usuario = new Usuario();
        usuario.setNombre(this.nombre);
        usuario.setApellido(this.apellido);
        usuario.setEmail(this.email);
        usuario.setPassword(this.password);

        // Campos que se inicializan por defecto
        usuario.setRol(this.rol);
        usuario.setPuntos(0); // Inicializado en 0
        usuario.setEstado(EstadoUsuarioEnum.HABILITADO); // Valor por defecto
        usuario.setAyudadosEnZona(0);
        usuario.setUbicacion(ubicacion);
        usuario.setTelefono(this.telefono);

        return usuario;
    }

}