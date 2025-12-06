package com.grupo20.ttpsspringboot.controller;

import com.grupo20.ttpsspringboot.domain.models.Usuario;
import com.grupo20.ttpsspringboot.dtos.AuthSessionDTO;
import com.grupo20.ttpsspringboot.dtos.UsuarioCreateDTO;
import com.grupo20.ttpsspringboot.dtos.UsuarioSmallDTO;
import com.grupo20.ttpsspringboot.services.impl.AuthService;
import com.grupo20.ttpsspringboot.services.impl.UsuarioService;
import com.grupo20.ttpsspringboot.utils.JwtUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Auth")
@RestController
@RequestMapping("/api/auth") // Este controlador solo responde a "/autenticacion"
public class AuthController {

    @Autowired
    private AuthService authService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<AuthSessionDTO> login(
            @RequestHeader("usuario") String email,
            @RequestHeader("password") String password) {

        Usuario usuarioValidado = authService.validarCredenciales(email, password);

        if (usuarioValidado != null) {

            Map claims = Map.of("email", usuarioValidado.getEmail(), "id", usuarioValidado.getId(), "rol", usuarioValidado.getRol());

            String token = JwtUtils.generateToken(claims, jwtSecret);

            AuthSessionDTO dto = new AuthSessionDTO();

            dto.setToken(token);
            dto.setUsuario(UsuarioSmallDTO.fromEntity(usuarioValidado));

            return new ResponseEntity<>(dto, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Crea un nuevo usuario recibiendo el DTO.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthSessionDTO> createUsuario(
            @Valid @RequestBody UsuarioCreateDTO usuarioDto) {

        try {
            // Llama al servicio con el DTO
            usuarioDto.setEmail(usuarioDto.getEmail().toLowerCase().trim());
            UsuarioSmallDTO usuarioValidado = usuarioService.createUsuario(usuarioDto);

            Map claims = Map.of("email",
                    usuarioValidado.getEmail(), "id", usuarioValidado.getId(), "rol", usuarioValidado.getRol());

            String token = JwtUtils.generateToken(claims, jwtSecret);

            AuthSessionDTO dto = new AuthSessionDTO();

            dto.setToken(token);
            dto.setUsuario(usuarioValidado);

            return new ResponseEntity<>(dto, HttpStatus.OK);

        } catch (EntityNotFoundException | IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}