package com.grupo20.ttpsspringboot.controller;

import com.grupo20.ttpsspringboot.domain.models.Foto;
import com.grupo20.ttpsspringboot.dtos.PublicacionDTO;
import com.grupo20.ttpsspringboot.dtos.PublicacionFilterDTO;
import com.grupo20.ttpsspringboot.dtos.UsuarioSmallDTO;
import com.grupo20.ttpsspringboot.dtos.bases.PaginateBaseDTO;
import com.grupo20.ttpsspringboot.services.IPublicacionService;
import com.grupo20.ttpsspringboot.services.IUsuarioService;
import com.grupo20.ttpsspringboot.services.impl.FotoService;
import com.grupo20.ttpsspringboot.services.impl.PublicacionService;
import com.grupo20.ttpsspringboot.services.impl.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Public")
@RestController
@RequestMapping("/api/public")
public class PublicController extends BaseController {

    @Autowired
    private IPublicacionService service;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private FotoService fotoService;

    @GetMapping("/publicaciones")
    public ResponseEntity<PaginateBaseDTO<PublicacionDTO>> getPublicaciones(@ModelAttribute PublicacionFilterDTO filter) {
        PaginateBaseDTO<PublicacionDTO> publicaciones = service.getFiltered(filter);
        return ResponseEntity.ok(publicaciones);
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<UsuarioSmallDTO>> get() {
        return ResponseEntity.ok(usuarioService.ranking());
    }




    @Operation(summary = "Obtiene un LINK una foto dado el ID de la foto")
    @GetMapping("/{fotoId}/raw")
    public ResponseEntity<byte[]> getFotoRaw(@PathVariable Long fotoId) {
        Foto foto = fotoService.getFoto(fotoId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(foto.getContentType()))
                .body(foto.getContent());
    }

}
