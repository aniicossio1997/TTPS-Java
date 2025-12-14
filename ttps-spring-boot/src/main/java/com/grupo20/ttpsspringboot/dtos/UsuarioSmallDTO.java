package com.grupo20.ttpsspringboot.dtos;


import com.grupo20.ttpsspringboot.domain.enums.RolUsuarioEnum;
import com.grupo20.ttpsspringboot.domain.models.Usuario;
import lombok.Data;

@Data
public class UsuarioSmallDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private RolUsuarioEnum rol;
    private String telefono;
    private Integer puntos;

    public static UsuarioSmallDTO fromEntity(Usuario entity) {
        UsuarioSmallDTO dto = new UsuarioSmallDTO();

        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setApellido(entity.getApellido());
        dto.setEmail(entity.getEmail());
        dto.setRol(entity.getRol());
        dto.setTelefono(entity.getTelefono());
        dto.setPuntos(entity.getPuntos());
        return dto;
    }
}
