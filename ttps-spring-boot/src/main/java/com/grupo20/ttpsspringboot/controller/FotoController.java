package com.grupo20.ttpsspringboot.controller;

import com.grupo20.ttpsspringboot.domain.models.Foto;
import com.grupo20.ttpsspringboot.dtos.FotoLinkDTO;
import com.grupo20.ttpsspringboot.dtos.FotoResponseDTO;
import com.grupo20.ttpsspringboot.services.impl.FotoService;
import com.grupo20.ttpsspringboot.services.impl.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Fotos")
@RestController
@RequestMapping("/api/fotos")
public class FotoController extends BaseController {
    @Autowired
    private FotoService fotoService;


    @Operation(summary = "Obtiene un LINK una foto dado el ID de la foto")
    @GetMapping("/{fotoId}/raw")
    public ResponseEntity<byte[]> getFotoRaw(@PathVariable Long fotoId) {
        Foto foto = fotoService.getFoto(fotoId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(foto.getContentType()))
                .body(foto.getContent());
    }

}
