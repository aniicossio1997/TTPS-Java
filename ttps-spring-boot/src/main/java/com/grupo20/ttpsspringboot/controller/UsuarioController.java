package com.grupo20.ttpsspringboot.controller;

import com.grupo20.ttpsspringboot.dtos.*;
import com.grupo20.ttpsspringboot.dtos.bases.ApiResponseDTO;
import com.grupo20.ttpsspringboot.services.impl.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid; // Necesario para validar el DTO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.MediaType;

@Tag(name = "Usuarios")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController extends BaseController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioSmallDTO>> getAllUsuarios() {
        List<UsuarioSmallDTO> usuarios = usuarioService.getAllUsuarios();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDetallelDTO> getUsuarioById(@PathVariable Long id) {
        try {
            UsuarioDetallelDTO usuario = usuarioService.getUsuarioById(id);
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Código 404
        }
    }

    @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UsuarioSmallDTO> updateUsuario(
            @PathVariable Long id,

            // --- INICIO DEL CAMBIO ---
            // Esta anotación le dice a Swagger: "La parte 'data' es JSON, no binario"
            @Parameter(
                    description = "Datos del usuario",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioUpdateDTO.class)
                    )
            )
            @RequestPart("data") @Valid UsuarioUpdateDTO usuarioDto,
            // --- FIN DEL CAMBIO ---

            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            // Tu lógica sigue igual...
            UsuarioSmallDTO updatedUsuario = usuarioService.updateUsuario(id, usuarioDto, file);
            return new ResponseEntity<>(updatedUsuario, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}/foto")
    public ResponseEntity<FotoResponseDTO> getFotoUsuario(@PathVariable Long id) {
        FotoResponseDTO foto = usuarioService.getFotoByIdUser(id);

        if (foto == null || foto.getContent() == null) {
            return ResponseEntity.noContent().build(); // 204
        }

        return ResponseEntity.ok(foto); // application/json
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<ApiResponseDTO> cambiarPassword( @PathVariable Long id, @Valid @RequestBody RestablecerPasswordRequestDTO dto) {

        usuarioService.restablecerPassword(id, dto);
        return ResponseEntity.ok(
                new ApiResponseDTO(true, 200, "Contraseña actualizada correctamente")
        );
    }


}