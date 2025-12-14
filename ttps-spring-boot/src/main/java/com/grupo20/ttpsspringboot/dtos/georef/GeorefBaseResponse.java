package com.grupo20.ttpsspringboot.dtos.georef;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeorefBaseResponse <T> {
    // Usamos JsonAlias para atrapar cualquiera de los 3 nombres posibles
    @JsonAlias({"provincias", "municipios", "departamentos"})
    private List<T> resultados; // Le cambié el nombre a 'resultados' para que sea genérico

    private int cantidad;
    private int total;
    private int inicio;
}
