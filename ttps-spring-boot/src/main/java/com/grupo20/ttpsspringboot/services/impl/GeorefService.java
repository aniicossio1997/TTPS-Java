package com.grupo20.ttpsspringboot.services.impl;

import com.grupo20.ttpsspringboot.dtos.georef.GeorefMunicipiosResponse;
import com.grupo20.ttpsspringboot.dtos.georef.GeorefProvinciasResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeorefService {

    @Autowired
    private RestTemplate restTemplate;

    // La URL PROVINCIAS no requiere parámetros
    private static final String URL_PROVINCIAS = "https://apis.datos.gob.ar/georef/api/provincias?orden=nombre&aplanar=true&campos=estandar&max=100&exacto=true";

    // La URL MUNICIPIOS requiere el ID de la provincia como parámetro
    private static final String URL_MUNICIPIOS = "https://apis.datos.gob.ar/georef/api/municipios?provincia={id}&orden=nombre&aplanar=true&campos=estandar&max=250&exacto=true";

    public GeorefProvinciasResponse obtenerProvincias() {
        try {
            // Spring convierte automáticamente el JSON a tus objetos Java
            return restTemplate.getForObject(URL_PROVINCIAS, GeorefProvinciasResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public GeorefMunicipiosResponse obtenerMunicipios(String idProvincia) {
        try {
            // El tercer parámetro reemplaza el "{id}" en la URL
            return restTemplate.getForObject(URL_MUNICIPIOS, GeorefMunicipiosResponse.class, idProvincia);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}