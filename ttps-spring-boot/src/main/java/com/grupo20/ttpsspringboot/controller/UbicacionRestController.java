package com.grupo20.ttpsspringboot.controller;


import com.grupo20.ttpsspringboot.domain.models.Ubicacion;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import  com.grupo20.ttpsspringboot.services.UbicacionService;

import java.util.List;

@Tag(name = "Ubicaciones")
@RestController
@RequestMapping("/api/ubicaciones")
public class UbicacionRestController extends BaseController {

    @Autowired
    private UbicacionService ubicacionService;

    @GetMapping
    public ResponseEntity<List<Ubicacion>> getAll() {
        List<Ubicacion> ubicaciones = ubicacionService.getAllUbicaciones();
        if (ubicaciones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        
        return new ResponseEntity<>(ubicaciones, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ubicacion> get(@PathVariable("id") Long id) {
        Ubicacion ubicacion = ubicacionService.getUbicacion(id);
        if (ubicacion == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ubicacion, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Ubicacion> create(@RequestBody Ubicacion ubicacion) {
        try {
            Ubicacion nuevaUbicacion = ubicacionService.crearUbicacion(ubicacion);
            return new ResponseEntity<>(nuevaUbicacion, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ubicacion> update(@PathVariable("id") Long id, @RequestBody Ubicacion ubicacion) {
        Ubicacion ubicacionExistente = ubicacionService.getUbicacion(id);
        if (ubicacionExistente == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            Ubicacion ubicacionActualizada = ubicacionService.actualizarUbicacion(ubicacion);
            return new ResponseEntity<>(ubicacionActualizada, HttpStatus.OK); 
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUbicacion(@PathVariable("id") Long id) { 
        Ubicacion ubicacion = ubicacionService.getUbicacion(id);
        if (ubicacion == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ubicacionService.eliminarUbicacion(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Ubicacion>> getFiltered(
            @RequestParam(required = false) String idExterno,
            @RequestParam(required = false) String provincia,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) String barrio) {

        List<Ubicacion> ubicaciones = ubicacionService.buscarPorCriterio(idExterno, provincia, ciudad, barrio);

        if (ubicaciones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(ubicaciones, HttpStatus.OK);
    }
}