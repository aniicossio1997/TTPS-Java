package com.grupo20.ttpsspringboot.services.interfaces;

import com.grupo20.ttpsspringboot.dtos.bases.UbicacionBaseDTO;
import com.grupo20.ttpsspringboot.dtos.georef.GeorefDepartamentosResponse;
import com.grupo20.ttpsspringboot.dtos.georef.GeorefMunicipiosResponse;
import com.grupo20.ttpsspringboot.dtos.georef.GeorefProvinciasResponse;
import com.grupo20.ttpsspringboot.dtos.georef.GeorefUbicacionResponse;

public interface IGeorefService {
    GeorefProvinciasResponse obtenerProvincias();

    GeorefDepartamentosResponse obtenerDepartamentos(String idProvincia);

    GeorefMunicipiosResponse obtenerMunicipios(String idProvincia);

    GeorefUbicacionResponse obtenerUbicacion(String lat, String lon);

    <T extends UbicacionBaseDTO> T getUbicacionFormateada(String lat, String lon, Class<T> tipoClase);
}
