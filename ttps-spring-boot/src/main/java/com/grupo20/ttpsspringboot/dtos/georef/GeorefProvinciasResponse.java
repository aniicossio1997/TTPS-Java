package com.grupo20.ttpsspringboot.dtos.georef;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeorefProvinciasResponse {

    // Mapea la lista "provincias" del JSON
    private List<ProvinciaExternoDTO> provincias;

    private int cantidad;
    private int total;
    private int inicio;
}