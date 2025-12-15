package com.grupo20.ttpsspringboot.dtos.georef;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeorefUbicacionResponse {

    // El objeto raíz contiene "ubicacion"
    private UbicacionDetalleDTO ubicacion;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UbicacionDetalleDTO {

        // Provincia
        @JsonProperty("provincia_id")
        private String provinciaId;

        @JsonProperty("provincia_nombre")
        private String provinciaNombre;

        // Municipio (puede venir null si es zona rural)
        @JsonProperty("municipio_id")
        private String municipioId;

        @JsonProperty("municipio_nombre")
        private String municipioNombre;

        // Departamento
        @JsonProperty("departamento_id")
        private String departamentoId;

        @JsonProperty("departamento_nombre")
        private String departamentoNombre;

        // Coordenadas
        private Double lat;
        private Double lon;
    }
}