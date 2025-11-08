package com.grupo20.ttpsspringboot.controller;


import com.grupo20.ttpsspringboot.domain.models.Publicacion;

import com.grupo20.ttpsspringboot.dtos.PublicacionDTO;
import com.grupo20.ttpsspringboot.services.impl.PublicacionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/publicaciones")
public class PublicacionesController extends BaseController {

    @Autowired
    private PublicacionService publicacionService;

    @PostMapping()
    public ResponseEntity<PublicacionDTO> createPublicacion(@Valid @RequestBody PublicacionCreateDTO dto) {
        try {
            Publicacion publicacion = publicacionService.create(getUsuario(), dto);
            return new ResponseEntity<>(PublicacionDTO.fromEntity(publicacion), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}