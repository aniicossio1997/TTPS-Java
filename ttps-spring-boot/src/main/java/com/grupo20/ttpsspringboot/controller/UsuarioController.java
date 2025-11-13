package com.grupo20.ttpsspringboot.controller;

import com.grupo20.ttpsspringboot.domain.models.Usuario;
import com.grupo20.ttpsspringboot.dtos.UsuarioCreateDTO;
import com.grupo20.ttpsspringboot.dtos.UsuarioSmallDTO;
import com.grupo20.ttpsspringboot.dtos.UsuarioUpdateDTO;
import com.grupo20.ttpsspringboot.services.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid; // Necesario para validar el DTO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuarios")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Crea un nuevo usuario recibiendo el DTO.
     */
    @PostMapping
    public ResponseEntity<UsuarioSmallDTO> createUsuario(
            @Valid @RequestBody UsuarioCreateDTO usuarioDto) {

        try {
            // Llama al servicio con el DTO
            UsuarioSmallDTO createdUsuario = usuarioService.createUsuario(usuarioDto);
            return new ResponseEntity<>(createdUsuario, HttpStatus.CREATED); // Código 201
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping
    public ResponseEntity<List<UsuarioSmallDTO>> getAllUsuarios() {
        List<UsuarioSmallDTO> usuarios = usuarioService.getAllUsuarios();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioSmallDTO> getUsuarioById(@PathVariable Long id) {
        try {
            UsuarioSmallDTO usuario = usuarioService.getUsuarioById(id);
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Código 404
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioSmallDTO> updateUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioUpdateDTO usuarioDto) { // Usa UsuarioUpdateDTO

        try {
            // Llama al servicio con el nuevo DTO
            UsuarioSmallDTO updatedUsuario = usuarioService.updateUsuario(id, usuarioDto);
            return new ResponseEntity<>(updatedUsuario, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Código 404

        } catch (Exception e) {
            // Puede capturar error si la nueva UbicacionId no existe
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Código 400
        }
    }
}