package com.grupo20.ttpsspringboot.dtos;

import com.grupo20.ttpsspringboot.domain.models.Ubicacion;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UbicacionDTO implements Serializable {

    private Long id;

    private Double latitud;

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
