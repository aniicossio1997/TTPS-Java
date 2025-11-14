package com.grupo20.ttpsspringboot.services;

import com.grupo20.ttpsspringboot.domain.models.Ubicacion;
import com.grupo20.ttpsspringboot.domain.models.Usuario;
import com.grupo20.ttpsspringboot.dtos.UsuarioCreateDTO;
import com.grupo20.ttpsspringboot.dtos.UsuarioSmallDTO;
import com.grupo20.ttpsspringboot.dtos.UsuarioUpdateDTO;
import com.grupo20.ttpsspringboot.exceptions.NotFoundException;
import com.grupo20.ttpsspringboot.persistence.dao.UsuarioDAO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private UbicacionService ubicacionService; // Dependencia para obtener la ubicación

    @Autowired
    private PasswordEncoder passwordEncoder; // Dependencia para hashear contraseñas


    /**
     * Crea un nuevo usuario a partir del DTO.
     * Implementa: Búsqueda de Ubicación, Hasheo de Contraseña y Mapeo DTO -> Entidad.
     *
     * @param usuarioDto DTO con los datos de creación.
     * @return El UsuarioSmallDTO del usuario creado.
     */
    @Transactional
    public UsuarioSmallDTO createUsuario(UsuarioCreateDTO usuarioDto) {
        if (usuarioDAO.findByEmail(usuarioDto.getEmail()) != null) {
            throw new IllegalArgumentException("El email '" + usuarioDto.getEmail() + "' ya está en uso.");
        }
        // Crea la ubicacion proporcionado en el DTO
        Ubicacion ubicacion = ubicacionService.crearUbicacion(usuarioDto.getUbicacion());
        if (ubicacion == null) {
            throw new NotFoundException();
        }

        Usuario nuevoUsuario = usuarioDto.toEntity(ubicacion);

        String hashedPassword = passwordEncoder.encode(usuarioDto.getPassword());
        nuevoUsuario.setPassword(hashedPassword);

        Usuario savedUsuario = usuarioDAO.persist(nuevoUsuario);

        // Devolver la respuesta en formato DTO
        return UsuarioSmallDTO.fromEntity(savedUsuario);
    }


    /**
     * Obtiene una lista de todos los usuarios.
     * @return Lista de UsuarioSmallDTOs.
     */
    @Transactional(readOnly = true)
    public List<UsuarioSmallDTO> getAllUsuarios() {
        return usuarioDAO.getAll().stream()
                .map(UsuarioSmallDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un usuario por su ID.
     * @param id ID del usuario.
     * @return UsuarioSmallDTO.
     */
    @Transactional(readOnly = true)
    public UsuarioSmallDTO getUsuarioById(Long id) {
        Usuario usuario = usuarioDAO.get(id);
        if (usuario == null) {
            throw new NotFoundException();
        }
        return UsuarioSmallDTO.fromEntity(usuario);
    }

    /**
     * Actualiza completamente un usuario existente.
     * NOTA: Idealmente, usaría un UsuarioUpdateDTO.
     * @param id ID del usuario a actualizar.
     * @return El UsuarioSmallDTO del usuario actualizado.
     */
    @Transactional
    public UsuarioSmallDTO updateUsuario(Long id, UsuarioUpdateDTO dto) {
        Usuario existingUsuario = usuarioDAO.get(id);

        if (dto.getNombre() != null) {
            existingUsuario.setNombre(dto.getNombre());
        }
        if (dto.getApellido() != null) {
            existingUsuario.setApellido(dto.getApellido());
        }
        if (dto.getEmail() != null) {
            existingUsuario.setEmail(dto.getEmail());
        }
        if (dto.getNuevaUbicacionId() != null) {
            // Buscar la nueva ubicación y asignarla
            Ubicacion nuevaUbicacion = ubicacionService.getUbicacion(dto.getNuevaUbicacionId());
            if (nuevaUbicacion == null) {
                throw new NotFoundException();
            }
            existingUsuario.setUbicacion(nuevaUbicacion);
        }

        Usuario updatedUsuario = usuarioDAO.update(existingUsuario);
        return UsuarioSmallDTO.fromEntity(updatedUsuario);
    }

}