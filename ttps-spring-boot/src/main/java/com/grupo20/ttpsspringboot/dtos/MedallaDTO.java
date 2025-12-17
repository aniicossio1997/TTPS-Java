package com.grupo20.ttpsspringboot.dtos;

import com.grupo20.ttpsspringboot.domain.enums.MedallaEnum;
import com.grupo20.ttpsspringboot.domain.models.Medalla;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MedallaDTO  {
    private  Long id;
    private MedallaEnum tipo;
    private Date fechaAsignacion = new Date();

    public static MedallaDTO fromEntity(Medalla medalla) {
        MedallaDTO dto = new MedallaDTO();
        dto.setId(medalla.getId());
        dto.setTipo(medalla.getTipo());
        dto.setFechaAsignacion(medalla.getFechaAsignacion());
        return dto;
    }
}