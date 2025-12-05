package com.grupo20.ttpsspringboot.services;

import com.grupo20.ttpsspringboot.domain.models.Ubicacion;
import com.grupo20.ttpsspringboot.domain.models.Usuario;
import com.grupo20.ttpsspringboot.dtos.UsuarioCreateDTO;
import com.grupo20.ttpsspringboot.dtos.UsuarioSmallDTO;
import com.grupo20.ttpsspringboot.dtos.UsuarioUpdateDTO;
import com.grupo20.ttpsspringboot.dtos.mappingService.UbicacionMapperService;
import com.grupo20.ttpsspringboot.dtos.mappingService.UsuarioMapperService;
import com.grupo20.ttpsspringboot.exceptions.NotFoundException;
import com.grupo20.ttpsspringboot.persistence.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.grupo20.ttpsspringboot.domain.enums.EstadoUsuarioEnum;
import com.grupo20.ttpsspringboot.domain.enums.RolUsuarioEnum;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UbicacionService ubicacionService; // Dependencia para obtener la ubicación

    @Autowired
    private PasswordEncoder passwordEncoder; // Dependencia para hashear contraseñas

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UbicacionMapperService ubicacionMapper;

    @Autowired
    private UsuarioMapperService usuarioMapper;

    /**
     * Crea un nuevo usuario a partir del DTO.
     * Implementa: Búsqueda de Ubicación, Hasheo de Contraseña y Mapeo DTO -> Entidad.
     *
     * @param usuarioDto DTO con los datos de creación.
     * @return El UsuarioSmallDTO del usuario creado.
     */
    @Transactional
    public UsuarioSmallDTO createUsuario(UsuarioCreateDTO usuarioDto) {
        System.out.println("Usuario recibido: " + usuarioDto);
        System.out.println("Email: " + usuarioDto.getEmail());

        if(usuarioDto.getEmail() == null){
            throw new IllegalArgumentException("El email es obligatorio");
        }
        var existe = usuarioRepository.findByEmail(usuarioDto.getEmail()).isPresent();
        if (existe) {
            throw new IllegalArgumentException("El email '" + usuarioDto.getEmail() + "' ya está en uso.");
        }

        // 1) Hashear la contraseña en el DTO
        String hashedPassword = passwordEncoder.encode(usuarioDto.getPassword());
        usuarioDto.setPassword(hashedPassword);

        // 2) Mapear DTO -> Usuario (incluyendo Ubicacion anidada)
        Usuario nuevoUsuario = usuarioMapper.toEntity(usuarioDto);

        // 3) Guardar usuario -> cascada guarda también la Ubicacion
        Usuario savedUsuario = usuarioRepository.save(nuevoUsuario);

        // 4) Devolver DTO de respuesta
        return UsuarioSmallDTO.fromEntity(savedUsuario);
    }


    /**
     * Obtiene una lista de todos los usuarios.
     * @return Lista de UsuarioSmallDTOs.
     */
    @Transactional(readOnly = true)
    public List<UsuarioSmallDTO> getAllUsuarios() {
        List<Usuario> usuarios;
        usuarios = usuarioRepository.findAll();
        return usuarios.stream()
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
        Optional<Usuario> usuarioOpt;
        usuarioOpt = usuarioRepository.findById(id);
        Usuario usuario = usuarioOpt.orElseThrow(() ->
                new EntityNotFoundException("Usuario no encontrado con ID: " + id));
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
        Usuario existingUsuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado para actualizar con ID: " + id));

        if (dto.getNombre() != null) {
            existingUsuario.setNombre(dto.getNombre());
        }
        if (dto.getApellido() != null) {
            existingUsuario.setApellido(dto.getApellido());
        }
        if (dto.getEmail() != null) {
            existingUsuario.setEmail(dto.getEmail());
        }


        ubicacionService.updateUbicacion(existingUsuario.getUbicacion().getId(),dto.getUbicacion());

        Usuario updatedUsuario = usuarioRepository.save(existingUsuario);
        return UsuarioSmallDTO.fromEntity(updatedUsuario);
    }

}