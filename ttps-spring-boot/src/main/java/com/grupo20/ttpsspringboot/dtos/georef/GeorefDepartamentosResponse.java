package com.grupo20.ttpsspringboot.dtos.georef;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeorefDepartamentosResponse {

    private List<DepartamentoDTO> departamentos; // Mapea la key "departamentos"
    private int cantidad;
    private int total;
    private int inicio;
}