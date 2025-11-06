package services;

import domain.models.Usuario;
import persistence.dao.UsuarioDAO; // Importas tu DAO
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UsuarioDAO usuarioDAO; // Inyecta el DAO de usuario

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

        // 1. Buscar al usuario por email usando el DAO
        Usuario usuario = usuarioDAO.findByEmail(email);

        // 2. Comprobar si el usuario existe y la password coincide
        //    (¡IMPORTANTE! En un proyecto real, la password estaría "hasheada"
        //    y aquí usarías un "passwordEncoder.matches(password, usuario.getClave())")
        if (usuario != null && usuario.getPassword().equals(password)) {
            return usuario; // Credenciales correctas
        }

        // 3. Si no coincide o no existe, devolver null
        return null;
    }
}