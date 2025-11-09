package com.grupo20.ttpsspringboot.controller;

import com.grupo20.ttpsspringboot.domain.models.Avistamiento;
import com.grupo20.ttpsspringboot.domain.models.Publicacion;
import com.grupo20.ttpsspringboot.dtos.*;
import com.grupo20.ttpsspringboot.services.impl.AvistamientoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Avistamientos")
@RestController
@RequestMapping("/api/avistamientos")
public class AvistamientoController extends BaseController{

    @Autowired
    private AvistamientoService service;

    @PostMapping()
    public ResponseEntity<AvistamientoDTO> create(@Valid @RequestBody AvistamientoCreateDTO dto) {
        try {
            Avistamiento avistamiento = service.create(getUsuario(), dto);
            return new ResponseEntity<>(AvistamientoDTO.fromEntity(avistamiento), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping()
    public ResponseEntity<List<AvistamientoDTO>> getFiltered(@ModelAttribute AvistamientoFilterDTO filter) {
        List<Avistamiento> avistamientos = service.getFiltered(filter);
        return ResponseEntity.ok(avistamientos.stream().map(AvistamientoDTO::fromEntity).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvistamientoDTO> get(@PathVariable("id") Long id) {
        try {
            Avistamiento avistamiento = service.get(id);
            return new ResponseEntity<>(AvistamientoDTO.fromEntity(avistamiento), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
