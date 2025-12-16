package com.grupo20.ttpsspringboot.dtos;

import com.grupo20.ttpsspringboot.domain.models.Ubicacion;
import com.grupo20.ttpsspringboot.dtos.bases.UbicacionBaseDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serializable;


public class UbicacionUpdateDTO extends UbicacionBaseDTO {

    public static UbicacionDTO fromEntity(Ubicacion entity) {
        UbicacionDTO dto = new UbicacionDTO();

        dto.setId(entity.getId());
        dto.setProvincia(entity.getProvincia());
        dto.setIdExternoProvincia(entity.getIdExternoProvincia());
        dto.setMunicipio(entity.getMunicipio());
        dto.setIdExternoMunicipio(entity.getIdExternoMunicipio());
        dto.setDepartamento(entity.getDepartamento());
        dto.setIdExternoDepartamento(entity.getIdExternoDepartamento());
        dto.setLatitud(entity.getLatitud());
        dto.setLongitud(entity.getLongitud());

        return dto;
    }
    
}