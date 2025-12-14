package com.grupo20.ttpsspringboot.dtos;

import com.grupo20.ttpsspringboot.domain.models.Publicacion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicacionDTO implements Serializable {

    private Long id;
    private String nombre;
    private String descripcion;
    private Date fecha;
    private String color;
    private String especie;
    private String raza;
    private String tamanio;

    private UbicacionDTO ubicacion;
    private Long usuarioId;

    private UsuarioSmallDTO usuario;

    private EstadoPublicacionDTO estado;

    private List<String> fotos = new ArrayList<>();

    public static PublicacionDTO fromEntity(Publicacion entity) {
        PublicacionDTO dto = new PublicacionDTO();

        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setDescripcion(entity.getDescripcion());
        dto.setFecha(entity.getFecha());
        dto.setColor(entity.getColor());
        dto.setEspecie(entity.getEspecie());
        dto.setRaza(entity.getRaza());
        dto.setTamanio(entity.getTamanio());

        if (entity.getUbicacion() != null) {
            dto.setUbicacion(UbicacionDTO.fromEntity(entity.getUbicacion()));
        }
        if (entity.getUsuario() != null) {
            dto.setUsuarioId(entity.getUsuario().getId());
            dto.setUsuario(UsuarioSmallDTO.fromEntity(entity.getUsuario()));
        }

        if (entity.getEstado() != null) {
            dto.setEstado(EstadoPublicacionDTO.fromEntity(entity.getEstado()));
        }

        return dto;
    }
}
