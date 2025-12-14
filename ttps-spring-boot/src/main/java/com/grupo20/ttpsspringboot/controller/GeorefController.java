package com.grupo20.ttpsspringboot.controller;

import com.grupo20.ttpsspringboot.dtos.georef.GeorefMunicipiosResponse;
import com.grupo20.ttpsspringboot.dtos.georef.GeorefProvinciasResponse;
import com.grupo20.ttpsspringboot.services.impl.GeorefService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Georef Proxy", description = "Endpoints para consultar la API del gobierno")
@RestController
@RequestMapping("/api/georef")
public class GeorefController {

    @Autowired
    private GeorefService georefService;

    @Operation(summary = "Obtener listado de provincias (Proxy)")
    @GetMapping("/provincias")
    public ResponseEntity<GeorefProvinciasResponse> getProvincias() {

        GeorefProvinciasResponse response = georefService.obtenerProvincias();

        if (response == null) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener municipios de una provincia")
    @GetMapping("/municipios/{idProvincia}")
    public ResponseEntity<GeorefMunicipiosResponse> getMunicipios(@PathVariable String idProvincia) {

        GeorefMunicipiosResponse response = georefService.obtenerMunicipios(idProvincia);

        if (response == null) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok(response);
    }
}