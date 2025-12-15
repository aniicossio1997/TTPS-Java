package com.grupo20.ttpsspringboot.dtos;

import com.grupo20.ttpsspringboot.domain.models.Avistamiento;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class AvistamientoDTO {

    private Long id;
    private String descripcion;
    private boolean agradecimiento;
    private Date fecha;
    private UbicacionDTO ubicacion;
    private Long publicacionId;
    private UsuarioSmallDTO usuario;
    private List<FotoLinkDTO> fotos = new ArrayList<>();

    public static AvistamientoDTO fromEntity(Avistamiento entity) {
        AvistamientoDTO dto = new AvistamientoDTO();

        dto.setId(entity.getId());
        dto.setDescripcion(entity.getDescripcion());
        dto.setFecha(entity.getFecha());
        dto.setAgradecimiento(entity.isAgradecimiento());

        if (entity.getUbicacion() != null)
            dto.setUbicacion(UbicacionDTO.fromEntity(entity.getUbicacion()));

        if (entity.getPublicacion() != null)
            dto.setPublicacionId(entity.getPublicacion().getId());

        if (entity.getUsuario() != null)
            dto.setUsuario(UsuarioSmallDTO.fromEntity(entity.getUsuario()));

        if (!entity.getFotos().isEmpty()) {
            dto.setFotos(entity.getFotos().stream().map(FotoLinkDTO::fromEntity).toList());
        }
        return dto;
    }
}