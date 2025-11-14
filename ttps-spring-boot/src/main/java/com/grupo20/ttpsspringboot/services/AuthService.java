package com.grupo20.ttpsspringboot.services;

import com.grupo20.ttpsspringboot.domain.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.grupo20.ttpsspringboot.persistence.dao.UsuarioDAO;

@Service
public class AuthService {

    @Autowired
    private UsuarioDAO usuarioDAO; // Inyecta el DAO de usuario

    @Autowired
    private PasswordEncoder passwordEncoder; // Dependencia para hashear contraseñas


    @Transactional(readOnly = true) // Es una operación de solo lectura
    public Usuario validarToken(String token) {
        String[] parts = token.split("-");
        if (parts.length > 2) return null;
        Long userId = Long.parseLong(parts[0]);
        String password = parts[1];
        Usuario usuario = usuarioDAO.get(userId);
        if (usuario.getPassword().equals(password)){
            return usuario;
        };
        return null;
    }
        /**
         * Valida las credenciales del usuario.
         * @param email El email (que viene en el header 'usuario')
         * @param password La password (que viene en el header 'password')
         * @return El objeto Usuario si la validación es exitosa, null en caso contrario.
         */
    @Transactional(readOnly = true) // Es una operación de solo lectura
    public Usuario validarCredenciales(String email, String password) {

        Usuario usuario = usuarioDAO.findByEmail(email);

        // --- AQUI ESTÁ EL CAMBIO ---
        // Ya no usamos .equals()

        // 2. Compara la contraseña de texto plano del login
        //    con el hash guardado en la base de datos.
        if (usuario != null && passwordEncoder.matches(password, usuario.getPassword())) {
            return usuario; // Credenciales correctas
        }
        // -------------------------

        return null; // Email no encontrado o contraseña incorrecta
    }
}