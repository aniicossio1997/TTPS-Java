package com.grupo20.ttpsspringboot.controller;

import com.grupo20.ttpsspringboot.domain.models.Usuario;
import com.grupo20.ttpsspringboot.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/autenticacion") // Este controlador solo responde a "/autenticacion"
public class AuthController {

    @Autowired
    private AuthService authService; // Inyectas el servicio de Auth

    /**
     * Responde a POST /autenticacion
     *
     */
    @PostMapping
    public ResponseEntity<Object> autenticarUsuario(
            @RequestHeader("usuario") String email,
            @RequestHeader("password") String password) {

        // 1. Delega la validación al AuthService
        Usuario usuarioValidado = authService.validarCredenciales(email, password);

        if (usuarioValidado != null) {
            // 2. Éxito: Generar token y añadirlo al header
            String token = usuarioValidado.getId() + "+" + password;

            HttpHeaders headers = new HttpHeaders();
            headers.add("token", token);

            // 3. Devolver 200 OK con el header
            // (La especificación menciona 200 y 204, usaré 200 OK)
            //
            return new ResponseEntity<>(headers, HttpStatus.OK);

        } else {
            // 4. Falla: Devolver 403 Forbidden
            //
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}