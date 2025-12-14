package com.grupo20.ttpsspringboot.dtos.georef;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProvinciaExternoDTO {
    private String id;

    private String nombre;

    @JsonProperty("centroide_lat")
    private Double latitud;

    @JsonProperty("centroide_lon")
    private Double longitud;
}
