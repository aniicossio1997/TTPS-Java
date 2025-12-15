package com.grupo20.ttpsspringboot.services.impl;

import com.grupo20.ttpsspringboot.dtos.georef.GeorefDepartamentosResponse;
import com.grupo20.ttpsspringboot.dtos.georef.GeorefMunicipiosResponse;
import com.grupo20.ttpsspringboot.dtos.georef.GeorefProvinciasResponse;
import com.grupo20.ttpsspringboot.dtos.georef.GeorefUbicacionResponse;
import com.grupo20.ttpsspringboot.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GeorefService {

    @Autowired
    private RestTemplate restTemplate;

    // La URL PROVINCIAS no requiere parámetros
    private static final String URL_PROVINCIAS = "https://apis.datos.gob.ar/georef/api/provincias?orden=nombre&aplanar=true&campos=estandar&max=100&exacto=true";

    // La URL MUNICIPIOS requiere el ID de la provincia como parámetro
    private static final String URL_MUNICIPIOS = "https://apis.datos.gob.ar/georef/api/municipios?provincia={id}&orden=nombre&aplanar=true&campos=estandar&max=550&exacto=true";

    private static final String URL_DEPARTAMENTOS = "https://apis.datos.gob.ar/georef/api/departamentos?provincia={id}&aplanar=true&campos=estandar&max=550&exacto=true";

    private static final String URL_UBICACION = "https://apis.datos.gob.ar/georef/api/ubicacion";

    public GeorefProvinciasResponse obtenerProvincias() {
        try {
            // Spring convierte automáticamente el JSON a tus objetos Java
            return restTemplate.getForObject(URL_PROVINCIAS, GeorefProvinciasResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public GeorefDepartamentosResponse obtenerDepartamentos(String idProvincia) {
        try {
            // RestTemplate reemplaza {id} por la variable idProvincia
            return restTemplate.getForObject(URL_DEPARTAMENTOS, GeorefDepartamentosResponse.class, idProvincia);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public GeorefMunicipiosResponse obtenerMunicipios(String idProvincia) {
        try {

            return restTemplate.getForObject(URL_MUNICIPIOS, GeorefMunicipiosResponse.class, idProvincia);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public GeorefUbicacionResponse obtenerUbicacion(String lat, String lon) {
        Double latitud;
        Double longitud;


        try {
            if (lat == null || lon == null) {
                throw new BadRequestException("Latitud y Longitud son obligatorias.");
            }
            latitud = parsearCoordenada(lat);
            longitud = parsearCoordenada(lon);
        } catch (NumberFormatException e) {

            throw new BadRequestException("Error: Las coordenadas deben ser números válidos. (Ej: -34.56)");
        }

        // 2. Llamada a la API Externa
        try {
            String url = UriComponentsBuilder.fromHttpUrl(URL_UBICACION)
                    .queryParam("lat", latitud)
                    .queryParam("lon", longitud)
                    .queryParam("aplanar", true)
                    .queryParam("campos", "estandar")
                    .queryParam("formato", "json")
                    .toUriString();

            return restTemplate.getForObject(url, GeorefUbicacionResponse.class);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Double parsearCoordenada(String coord) {
        if (coord == null) return null;
        String limpia = coord.replace(",", ".").trim();
        return Double.valueOf(limpia);
    }

}