package com.grupo20.ttpsspringboot.dtos;

import com.grupo20.ttpsspringboot.domain.enums.RolUsuarioEnum;
import com.grupo20.ttpsspringboot.domain.models.Usuario;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UsuarioDetallelDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private RolUsuarioEnum rol;
    private String telefono;
    private List<MedallaDTO> medallas = new ArrayList<>();

    private UbicacionDTO ubicacion;

    private FotoLinkDTO fotoLink;

    public static UsuarioDetallelDTO fromEntity(Usuario entity) {
        UsuarioDetallelDTO dto = new UsuarioDetallelDTO();
        UbicacionDTO  ubicacionDTO = UbicacionDTO.fromEntity(entity.getUbicacion());


        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setApellido(entity.getApellido());
        dto.setEmail(entity.getEmail());
        dto.setRol(entity.getRol());
        dto.setTelefono(entity.getTelefono());
        dto.setUbicacion(ubicacionDTO);

        dto.fotoLink = new FotoLinkDTO();
        dto.fotoLink = FotoLinkDTO.fromEntity(entity.getFotoPerfil());

        return dto;
    }
}
