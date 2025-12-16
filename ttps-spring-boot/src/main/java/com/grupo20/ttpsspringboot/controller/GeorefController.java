package com.grupo20.ttpsspringboot.controller;

import com.grupo20.ttpsspringboot.dtos.georef.*;
import com.grupo20.ttpsspringboot.services.impl.GeorefService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "Obtener departamentos de una provincia")
    @GetMapping("/departamentos/{idProvincia}")
    public ResponseEntity<GeorefDepartamentosResponse>  getDepartamentos(@PathVariable String idProvincia) {

        // Validación básica
        if (idProvincia == null || idProvincia.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        GeorefDepartamentosResponse response = georefService.obtenerDepartamentos(idProvincia);

        if (response == null) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener municipios de una provincia")
    @GetMapping("/municipios/{idProvincia}")
    public ResponseEntity<GeorefMunicipiosResponse>  getMunicipios(@PathVariable String idProvincia) {

        GeorefMunicipiosResponse response = georefService.obtenerMunicipios(idProvincia);

        if (response == null) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener ubicación (Provincia/Muni/Depto) desde coordenadas")
    @GetMapping("/ubicacion")
    public ResponseEntity<GeorefUbicacionResponse> getUbicacion(
            @RequestParam(required = true) String latitud,
            @RequestParam(required = true) String longitud) {
        

        GeorefUbicacionResponse response = georefService.obtenerUbicacion(latitud, longitud);

        if (response == null) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok(response);
    }

}