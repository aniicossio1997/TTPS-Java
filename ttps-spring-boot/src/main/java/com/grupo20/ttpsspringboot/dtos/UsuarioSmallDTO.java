package com.grupo20.ttpsspringboot.dtos;


import com.grupo20.ttpsspringboot.domain.enums.EstadoUsuarioEnum;
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
    private FotoLinkDTO foto;
    private EstadoUsuarioEnum estado;

    public static UsuarioSmallDTO fromEntity(Usuario entity) {
        UsuarioSmallDTO dto = new UsuarioSmallDTO();
        //UbicacionDTO  ubicacionDTO = UbicacionDTO.fromEntity(entity.getUbicacion());

        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setApellido(entity.getApellido());
        dto.setEmail(entity.getEmail());
        dto.setRol(entity.getRol());
        dto.setTelefono(entity.getTelefono());
        dto.setPuntos(entity.getPuntos());
        dto.setEstado(entity.getEstado());
        //dto.setUbicacion(ubicacionDTO);

        if (entity.getFotoPerfil() != null) {
            dto.setFoto(FotoLinkDTO.fromEntity(entity.getFotoPerfil()));
        }
        return dto;
    }
}
