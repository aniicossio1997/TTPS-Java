package com.grupo20.ttpsspringboot.controller;


import com.grupo20.ttpsspringboot.domain.models.Usuario;
import com.grupo20.ttpsspringboot.dtos.UsuarioSmallDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "me")
@RestController
@RequestMapping("/api/me")
class MeController extends BaseController  {

    @GetMapping
    public ResponseEntity<UsuarioSmallDTO> me() {
        // 1. Obtenemos el usuario del contexto de seguridad gracias a BaseController
        Usuario usuarioLogueado = getUsuario();

        // 2. Si por alguna razón es null (aunque el filtro de seguridad debería evitarlo), retornamos 401 or 404
        if (usuarioLogueado == null) {
            return ResponseEntity.notFound().build();
        }

        // 3. Convertimos a DTO para no exponer password ni datos internos
        UsuarioSmallDTO dto = UsuarioSmallDTO.fromEntity(usuarioLogueado);

        return ResponseEntity.ok(dto);
    }

}
