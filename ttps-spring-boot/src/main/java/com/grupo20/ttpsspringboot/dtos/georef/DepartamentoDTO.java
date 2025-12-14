package com.grupo20.ttpsspringboot.dtos.georef;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DepartamentoDTO {

    private String id;
    private String nombre;

    // Campos "aplanados"
    @JsonProperty("centroide_lat")
    private Double latitud;

    @JsonProperty("centroide_lon")
    private Double longitud;

    @JsonProperty("provincia_id")
    private String provinciaId;

    @JsonProperty("provincia_nombre")
    private String provinciaNombre;
}
