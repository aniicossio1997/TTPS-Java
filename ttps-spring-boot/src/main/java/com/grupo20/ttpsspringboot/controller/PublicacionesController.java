package com.grupo20.ttpsspringboot.controller;


import com.grupo20.ttpsspringboot.domain.models.Publicacion;

import com.grupo20.ttpsspringboot.dtos.PublicacionCreateDTO;
import com.grupo20.ttpsspringboot.dtos.PublicacionDTO;
import com.grupo20.ttpsspringboot.dtos.PublicacionFilterDTO;
import com.grupo20.ttpsspringboot.dtos.PublicacionUpdateDTO;
import com.grupo20.ttpsspringboot.dtos.bases.PaginateBaseDTO;
import com.grupo20.ttpsspringboot.services.impl.PublicacionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Publicaciones")
@RestController
@RequestMapping("/api/publicaciones")
public class PublicacionesController extends BaseController {

    @Autowired
    private PublicacionService service;

    @PostMapping()
    public ResponseEntity<PublicacionDTO> create(@Valid @RequestBody PublicacionCreateDTO dto) {
        try {
            Publicacion publicacion = service.create(getUsuario(), dto);
            return new ResponseEntity<>(PublicacionDTO.fromEntity(publicacion), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicacionDTO> get(@PathVariable("id") Long id) {
        try {
            Publicacion publicacion = service.get(id);
            return new ResponseEntity<>(PublicacionDTO.fromEntity(publicacion), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping()
    public ResponseEntity<PaginateBaseDTO<PublicacionDTO>> getFiltered(@ModelAttribute PublicacionFilterDTO filter) {
        PaginateBaseDTO<PublicacionDTO> publicaciones = service.getFiltered(filter);
        return ResponseEntity.ok(publicaciones);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublicacionDTO> update(@PathVariable("id") Long id, @Valid @RequestBody PublicacionUpdateDTO dto) {
        try {
            Publicacion publicacion = service.update(id, getUsuario(), dto);
            return new ResponseEntity<>(PublicacionDTO.fromEntity(publicacion), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PublicacionDTO> delete(@PathVariable("id") Long id) {
        try {
            service.delete(id, getUsuario());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}