package com.grupo20.ttpsspringboot.dtos;

import com.grupo20.ttpsspringboot.domain.models.Ubicacion;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UbicacionUpdateDTO implements Serializable {

    private String idExterno;

    private String provincia;

    private String ciudad;

    private String barrio;

    private Double latitud;

    private Double longitud;

    public Ubicacion toEntity() {
        Ubicacion ubicacion = new Ubicacion();

        ubicacion.setIdExterno(this.idExterno);
        ubicacion.setProvincia(this.provincia);
        ubicacion.setCiudad(this.ciudad);
        ubicacion.setBarrio(this.barrio);
        ubicacion.setLatitud(this.latitud);
        ubicacion.setLongitud(this.longitud);
        return ubicacion;
    }
}