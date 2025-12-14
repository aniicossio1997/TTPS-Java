package com.grupo20.ttpsspringboot.controller;


import com.grupo20.ttpsspringboot.domain.models.Ubicacion;
import com.grupo20.ttpsspringboot.dtos.UbicacionDTO;
import com.grupo20.ttpsspringboot.dtos.UbicacionUpdateDTO;
import com.grupo20.ttpsspringboot.exceptions.NotFoundException;
import com.grupo20.ttpsspringboot.services.impl.UbicacionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Ubicaciones")
@RestController
@RequestMapping("/api/ubicaciones")
public class UbicacionController extends BaseController {

    @Autowired
    private UbicacionService ubicacionService;
    @Autowired
    private Ubicacion ubicacion;


    @GetMapping("/{id}")
    public ResponseEntity<UbicacionDTO> get(@PathVariable("id") Long id) {
        Ubicacion ubicacion = ubicacionService.getUbicacion(id);
        if (ubicacion == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(UbicacionDTO.fromEntity(ubicacion), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id,
                                    @RequestBody UbicacionUpdateDTO dto) {
        try {
            getUsuario();
            Ubicacion ubicacionActualizada = ubicacionService.updateUbicacion(id, dto);
            return new ResponseEntity<>(UbicacionDTO.fromEntity(ubicacionActualizada), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/buscar")
    public ResponseEntity<List<UbicacionDTO>> getFiltered(
            @RequestParam(required = false) String idExternoProvincia,
            @RequestParam(required = false) String provincia,
            @RequestParam(required = false) String idExternoMunicipio,
            @RequestParam(required = false) String municipio,
            @RequestParam(required = false) String idExternoDepartamento,
            @RequestParam(required = false) String departamento) {

        List<Ubicacion> ubicaciones = ubicacionService.buscarPorCriterio(idExternoProvincia, provincia, idExternoMunicipio, municipio, idExternoDepartamento, departamento);

        if (ubicaciones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(ubicaciones.stream().map(UbicacionDTO::fromEntity).toList());

    }
}