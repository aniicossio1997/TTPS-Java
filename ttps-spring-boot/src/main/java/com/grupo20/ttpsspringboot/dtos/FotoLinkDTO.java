package com.grupo20.ttpsspringboot.dtos;

import com.grupo20.ttpsspringboot.domain.models.Foto;
import lombok.Data;

@Data
public class FotoLinkDTO {
    private Long id;
    private String nombre;
    private String contentType;
    private String url;

    public static FotoLinkDTO fromEntity(Foto foto) {
        if (foto == null) return null;
        FotoLinkDTO dto = new FotoLinkDTO();
        dto.setId(foto.getId());
        dto.setNombre(foto.getNombre());
        dto.setContentType(foto.getContentType());
        dto.setUrl("/api/fotos/" + foto.getId() + "/raw"); // ✅ sirve para ambos
        return dto;
    }
}
