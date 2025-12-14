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


    // URL: /api/fotos/avistamiento/123
    @Operation(summary = "Obtener links de fotos de un avistamiento")
    @GetMapping("/avistamiento/{avistamientoId}")
    public ResponseEntity<List<FotoLinkDTO>> getFotosAvistamiento(@PathVariable Long avistamientoId) {

        List<FotoLinkDTO> fotos = fotoService.getFotosAvistamientoLinks(avistamientoId);

        if (fotos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(fotos);
    }

    // URL: /api/fotos/publicacion/456
    @Operation(summary = "Obtener links de fotos de una publicación")
    @GetMapping("/publicacion/{publicacionId}")
    public ResponseEntity<List<FotoLinkDTO>> getFotosPublicacion(@PathVariable Long publicacionId) {

        List<FotoLinkDTO> fotos = fotoService.getFotosPublicacionLinks(publicacionId);

        if (fotos.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(fotos);
    }

    // URL: /api/fotos/usuario/456
    @Operation(summary = "Obtiene un LINK una foto de un usuario")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<FotoLinkDTO> getFotoUsuario(@PathVariable Long usuarioId) {

        FotoLinkDTO foto = fotoService.getFotoUsuario(usuarioId);

        if (foto ==null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(foto);
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
