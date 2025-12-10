package com.grupo20.ttpsspringboot.controller;

import com.grupo20.ttpsspringboot.dtos.PublicacionDTO;
import com.grupo20.ttpsspringboot.dtos.PublicacionFilterDTO;
import com.grupo20.ttpsspringboot.dtos.bases.PaginateBaseDTO;
import com.grupo20.ttpsspringboot.services.impl.PublicacionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Public")
@RestController
@RequestMapping("/api/public")
public class PublicController extends BaseController {

    @Autowired
    private PublicacionService service;

    @GetMapping("/publicaciones")
    public ResponseEntity<PaginateBaseDTO<PublicacionDTO>> getPublicaciones(@ModelAttribute PublicacionFilterDTO filter) {
        PaginateBaseDTO<PublicacionDTO> publicaciones = service.getFiltered(filter);
        return ResponseEntity.ok(publicaciones);
    }
}
