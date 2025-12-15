package com.grupo20.ttpsspringboot.dtos;

import com.grupo20.ttpsspringboot.domain.models.Ubicacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UbicacionCreateDTO implements Serializable {

    @NotBlank(message = "La latitud es obligatoria.")
    private Double latitud;
    @NotBlank(message = "La longitud es obligatoria.")
    private Double longitud;


    private String provincia;
    private String idExternoProvincia;

    private String municipio;
    private String idExternoMunicipio;

    private String departamento;
    private String idExternoDepartamento;

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