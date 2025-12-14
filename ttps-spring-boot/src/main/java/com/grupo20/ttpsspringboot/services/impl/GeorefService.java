package com.grupo20.ttpsspringboot.services.impl;

import com.grupo20.ttpsspringboot.dtos.georef.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference; // IMPORTANTE
import org.springframework.http.HttpMethod; // IMPORTANTE
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeorefService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String URL_PROVINCIAS = "https://apis.datos.gob.ar/georef/api/provincias?orden=nombre&aplanar=true&campos=estandar&max=100&exacto=true";
    private static final String URL_MUNICIPIOS = "https://apis.datos.gob.ar/georef/api/municipios?provincia={id}&orden=nombre&aplanar=true&campos=estandar&max=550&exacto=true";
    private static final String URL_DEPARTAMENTOS = "https://apis.datos.gob.ar/georef/api/departamentos?provincia={id}&aplanar=true&campos=estandar&max=550&exacto=true";

    public GeorefBaseResponse<ProvinciaDTO> obtenerProvincias() {
        try {
            // Usamos .exchange en lugar de .getForObject para manejar Genéricos <T>
            return restTemplate.exchange(
                    URL_PROVINCIAS,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<GeorefBaseResponse<ProvinciaDTO>>() {}
            ).getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public GeorefBaseResponse<DepartamentoDTO> obtenerDepartamentos(String idProvincia) {
        try {
            return restTemplate.exchange(
                    URL_DEPARTAMENTOS,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<GeorefBaseResponse<DepartamentoDTO>>() {},
                    idProvincia 
            ).getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public GeorefBaseResponse<MunicipioDTO> obtenerMunicipios(String idProvincia) {
        try {
            return restTemplate.exchange(
                    URL_MUNICIPIOS,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<GeorefBaseResponse<MunicipioDTO>>() {},
                    idProvincia 
            ).getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}