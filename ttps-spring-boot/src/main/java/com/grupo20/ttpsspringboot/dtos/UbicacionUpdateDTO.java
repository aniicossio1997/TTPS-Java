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


    private String provincia;
    private String idExternoProvincia;

    private String municipio;
    private String idExternoMunicipio;

    private String departamento;
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