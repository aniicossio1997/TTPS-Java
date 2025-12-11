package com.grupo20.ttpsspringboot.services.impl;

import com.grupo20.ttpsspringboot.domain.models.Foto; // Importar Foto
import com.grupo20.ttpsspringboot.domain.models.Ubicacion;
import com.grupo20.ttpsspringboot.domain.models.Usuario;
import com.grupo20.ttpsspringboot.dtos.UsuarioCreateDTO;
import com.grupo20.ttpsspringboot.dtos.UsuarioSmallDTO;
import com.grupo20.ttpsspringboot.dtos.UsuarioUpdateDTO;
import com.grupo20.ttpsspringboot.dtos.mappingService.UbicacionMapperService;
import com.grupo20.ttpsspringboot.dtos.mappingService.UbicacionUpdateMapper;
import com.grupo20.ttpsspringboot.dtos.mappingService.UsuarioCreateMapperService;
import com.grupo20.ttpsspringboot.dtos.mappingService.UsuarioUpdateMapper;
import com.grupo20.ttpsspringboot.exceptions.NotFoundException;
import com.grupo20.ttpsspringboot.persistence.repository.FotoRepository;
import com.grupo20.ttpsspringboot.persistence.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile; // Importar MultipartFile

import java.io.IOException; // Importar IOException
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
    private UsuarioCreateMapperService usuarioCreateMapper;

    @Autowired
    private UsuarioUpdateMapper usuarioUpdateMapper;

    @Autowired
    private UbicacionUpdateMapper ubicacionUpdateMapper;

    @Autowired
    private FotoRepository fotoRepository;

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
        Usuario nuevoUsuario = usuarioCreateMapper.toEntity(usuarioDto);

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
    public UsuarioSmallDTO updateUsuario(Long id, UsuarioUpdateDTO dto, MultipartFile file) throws IOException {
        Usuario existingUsuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado para actualizar con ID: " + id));

        // 1) Actualizar campos de texto (nombre, apellido, etc.)
        usuarioUpdateMapper.updateFromDto(dto, existingUsuario);

        // 2) Actualizar ubicación
        if (dto.getUbicacion() != null) {
            Ubicacion ubicacion = existingUsuario.getUbicacion();
            if (ubicacion == null) {
                throw new IllegalStateException("El usuario no tiene ubicación asociada");
            }
            ubicacionUpdateMapper.updateFromDto(dto.getUbicacion(), ubicacion);
        }

        // 3) Lógica de la Foto (Solo si viene un archivo y no está vacío)
        // ¿Hay un archivo "real"?
        boolean hayArchivoNuevo = (file != null && !file.isEmpty());

        if (hayArchivoNuevo) {
            // CREAR O PISAR FOTO
            Foto foto = existingUsuario.getFotoPerfil();
            if (foto == null) {
                foto = new Foto();
                foto.setUsuario(existingUsuario);    // lado dueño
                existingUsuario.setFotoPerfil(foto); // lado inverso
            }

            foto.setNombre(file.getOriginalFilename());
            foto.setContent(file.getBytes());
        } else {
            // NO HAY ARCHIVO → ELIMINAR FOTO SI EXISTE
            Foto foto = existingUsuario.getFotoPerfil();
            if (foto != null) {
                // Romper relación
                existingUsuario.setFotoPerfil(null);
                foto.setUsuario(null);

                // Gracias a orphanRemoval = true en Usuario.fotoPerfil,
                // con solo romper la relación Hibernate debería borrar la foto.
                // Si querés ser explícita, también podés:
                fotoRepository.delete(foto);
            }
        }

        // 4) Guardar usuario (la cascada guarda/actualiza la ubicación y la foto)
        Usuario updatedUsuario = usuarioRepository.save(existingUsuario);

        return UsuarioSmallDTO.fromEntity(updatedUsuario);
    }

    /**
     * Borra la foto de perfil.
     */
    @Transactional
    public void eliminarFotoPerfil(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        // Al ponerlo en null, 'orphanRemoval = true' se encarga de borrarla de la BD
        if (usuario.getFotoPerfil() != null) {
            usuario.setFotoPerfil(null);
            usuarioRepository.save(usuario);
        }
    }

    /**
     * Obtiene el contenido binario de la foto.
     */
    @Transactional(readOnly = true)
    public byte[] obtenerFotoPerfil(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        if (usuario.getFotoPerfil() == null) {
            throw new NotFoundException("El usuario no tiene foto de perfil");
        }

        return usuario.getFotoPerfil().getContent();
    }

    @Transactional
    public void guardarFotoPerfil(Long id, MultipartFile file) throws IOException {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        Foto foto = usuario.getFotoPerfil();

        if (foto == null) {
            // Si no tiene foto, creamos una nueva y vinculamos
            foto = new Foto();
            foto.setUsuario(usuario);
            usuario.setFotoPerfil(foto);
        }

        // Actualizamos los datos (funciona tanto para create como update)
        foto.setNombre(file.getOriginalFilename());
        foto.setContent(file.getBytes());

        // Al guardar usuario, se guarda/actualiza la foto en cascada
        usuarioRepository.save(usuario);
    }
}