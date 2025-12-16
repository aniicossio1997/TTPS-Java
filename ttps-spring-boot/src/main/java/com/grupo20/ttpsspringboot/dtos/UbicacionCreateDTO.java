package com.grupo20.ttpsspringboot.dtos;

import com.grupo20.ttpsspringboot.domain.models.Ubicacion;
import com.grupo20.ttpsspringboot.dtos.bases.UbicacionBaseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;


public class UbicacionCreateDTO extends UbicacionBaseDTO {


    public Ubicacion toEntity() {
        Ubicacion ubicacion = new Ubicacion();

        ubicacion.setProvincia(this.getProvincia());
        ubicacion.setIdExternoProvincia(this.getIdExternoProvincia());
        ubicacion.setMunicipio(this.getMunicipio());
        ubicacion.setIdExternoMunicipio(this.getIdExternoMunicipio());
        ubicacion.setDepartamento(this.getDepartamento());
        ubicacion.setIdExternoDepartamento(this.getIdExternoDepartamento());
        ubicacion.setLatitud(this.getLatitud());
        ubicacion.setLongitud(this.getLongitud());

        return ubicacion;
    }
}