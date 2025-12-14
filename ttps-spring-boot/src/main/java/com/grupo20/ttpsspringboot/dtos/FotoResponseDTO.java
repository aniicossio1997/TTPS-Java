package com.grupo20.ttpsspringboot.dtos;

import com.grupo20.ttpsspringboot.domain.models.Avistamiento;
import com.grupo20.ttpsspringboot.domain.models.Foto;
import com.grupo20.ttpsspringboot.domain.models.Publicacion;
import com.grupo20.ttpsspringboot.domain.models.Usuario;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Arrays;

@Data
public class FotoResponseDTO {

    private  Long id;
    private String nombre;

    private byte[] content;

    private String contentType;

    private String url; // endpoint raw


    public static FotoResponseDTO fromEntity(Foto entity) {
        FotoResponseDTO dto = new FotoResponseDTO();

        dto.setId(entity.getId());
        dto.setContentType(entity.getContentType());

        dto.setNombre(entity.getNombre());
        dto.setContent(entity.getContent());
        dto.setUrl("/api/fotos/" + entity.getId() + "/raw");


        return dto;
    }
}
