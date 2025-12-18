package com.grupo20.ttpsspringboot.services;

import com.grupo20.ttpsspringboot.domain.enums.EstadoUsuarioEnum;
import com.grupo20.ttpsspringboot.domain.models.Usuario;
import com.grupo20.ttpsspringboot.exceptions.ForbiddenException;
import com.grupo20.ttpsspringboot.persistence.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Dependencia para hashear contraseñas


    @Transactional(readOnly = true) // Es una operación de solo lectura
    public Usuario validarToken(String token) {
        String[] parts = token.split("-");
        if (parts.length > 2) return null;
        Long userId = Long.parseLong(parts[0]);
        String password = parts[1];
        Usuario usuario = usuarioRepository.findById(userId).orElse(null);
        if (usuario != null && usuario.getPassword().equals(password)) {
            return usuario;
        }
        ;
        return null;
    }

    /**
     * Valida las credenciales del usuario.
     *
     * @param email    El email (que viene en el header 'usuario')
     * @param password La password (que viene en el header 'password')
     * @return El objeto Usuario si la validación es exitosa, null en caso contrario.
     */
    @Transactional(readOnly = true)
    public Usuario validarCredenciales(String email, String password) {

        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ForbiddenException("Usuario no encontrado o contraseña incorrecta")
        );

        // 1️⃣ Estado del usuario
        if (usuario.getEstado() != EstadoUsuarioEnum.HABILITADO) {

            switch (usuario.getEstado()) {
                case BLOQUEADO_POR_ADMIN ->
                        throw new ForbiddenException("Tu cuenta fue bloqueada por un administrador");

                case BAJA_VOLUNTARIA ->
                        throw new ForbiddenException("La cuenta se encuentra dada de baja");

                default ->
                        throw new ForbiddenException("La cuenta no se encuentra habilitada");
            }
        }
        //  Compara la contraseña de texto plano del login con el hash guardado en la base de datos.
        if (usuario != null && passwordEncoder.matches(password, usuario.getPassword())) {
            return usuario; // Credenciales correctas
        }

        throw new ForbiddenException("Usuario no encontrado o contraseña incorrecta");

    }
}