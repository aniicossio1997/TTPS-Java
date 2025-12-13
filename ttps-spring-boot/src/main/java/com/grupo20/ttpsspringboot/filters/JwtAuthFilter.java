package com.grupo20.ttpsspringboot.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupo20.ttpsspringboot.domain.models.Usuario;
import com.grupo20.ttpsspringboot.dtos.ErrorDTO;
import com.grupo20.ttpsspringboot.exceptions.NotFoundException;
import com.grupo20.ttpsspringboot.persistence.repository.UsuarioRepository;
import com.grupo20.ttpsspringboot.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    private UsuarioRepository  usuarioRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws IOException, ServletException {

        // 1. Ignorar rutas públicas
        String path = request.getServletPath();
        // 1. Rutas que NO requieren token (se saltan la validación)
                if (path.startsWith("/api/auth")
                        || path.startsWith("/api/public/")
                        //|| (path.startsWith("/api/usuarios"))
                        || path.startsWith("/v3/api-docs")
                        || path.startsWith("/swagger-ui")
                        || path.startsWith("/swagger-ui.html")) {
                    chain.doFilter(request, response);
                    return;
                }

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            Jws<Claims> jws = JwtUtils.validateToken(token, jwtSecret);

            if (jws != null) {
                try {
                    // INTENTAMOS buscar al usuario
                    Claims claims = jws.getBody();
                    Long id = claims.get("id", Long.class);

                    Usuario usuario = usuarioRepository.findById(id)
                            .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

                    // Si existe, autenticamos
                    var auth = new UsernamePasswordAuthenticationToken(usuario, null, Collections.emptyList());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);

                    chain.doFilter(request, response);
                    return;

                } catch (Exception e) {
                    // SI FALLA (Usuario no existe, etc), limpiamos contexto y devolvemos JSON
                    SecurityContextHolder.clearContext();
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.setContentType("application/json");
                    ErrorDTO error = new ErrorDTO(HttpStatus.FORBIDDEN.value(), "El usuario del token no existe o no es válido", "auth_error");
                    response.getWriter().write(mapper.writeValueAsString(error));
                    return; // Importante: cortamos ejecución aquí
                }
            }
        }

        // Si no hay token o es inválido, respondemos 401
        ErrorDTO errorDTO = new ErrorDTO(HttpStatus.UNAUTHORIZED.value(), "Token inválido o ausente", "invalid-token");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(mapper.writeValueAsString(errorDTO));
    }
}
