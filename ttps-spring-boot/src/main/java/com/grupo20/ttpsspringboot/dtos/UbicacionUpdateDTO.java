package com.grupo20.ttpsspringboot.dtos;

import com.grupo20.ttpsspringboot.domain.models.Ubicacion;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UbicacionUpdateDTO implements Serializable {

    @NotBlank(message = "La latitud es obligatoria.")
    private Double latitud;
    @NotBlank(message = "La longitud es obligatoria.")
    private Double longitud;


    @NotBlank(message = "La provincia es obligatoria.")
    private String provincia;
    @NotBlank(message = "El id Externo Provincia es obligatoria.")
    private String idExternoProvincia;

    @NotBlank(message = "El municipio es obligatoria.")
    private String municipio;
    @NotBlank(message = "El id Externo del Municipio es obligatoria.")
    private String idExternoMunicipio;

    @NotBlank(message = "El Departamento es obligatoria.")
    private String departamento;
    @NotBlank(message = "El Id Externo Departamento es obligatoria.")
    private String idExternoDepartamento;

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