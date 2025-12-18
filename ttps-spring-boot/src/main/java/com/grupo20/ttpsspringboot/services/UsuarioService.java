package com.grupo20.ttpsspringboot.services;

import com.grupo20.ttpsspringboot.domain.enums.EstadoUsuarioEnum;
import com.grupo20.ttpsspringboot.domain.enums.RolUsuarioEnum;
import com.grupo20.ttpsspringboot.domain.models.Foto; // Importar Foto
import com.grupo20.ttpsspringboot.domain.models.Medalla;
import com.grupo20.ttpsspringboot.domain.models.Ubicacion;
import com.grupo20.ttpsspringboot.domain.models.Usuario;
import com.grupo20.ttpsspringboot.dtos.*;
import com.grupo20.ttpsspringboot.dtos.mappingService.UbicacionMapperService;
import com.grupo20.ttpsspringboot.dtos.mappingService.UbicacionUpdateMapper;
import com.grupo20.ttpsspringboot.dtos.mappingService.UsuarioCreateMapperService;
import com.grupo20.ttpsspringboot.dtos.mappingService.UsuarioUpdateMapper;
import com.grupo20.ttpsspringboot.exceptions.BadRequestException;
import com.grupo20.ttpsspringboot.exceptions.NotFoundException;
import com.grupo20.ttpsspringboot.persistence.repository.FotoRepository;
import com.grupo20.ttpsspringboot.persistence.repository.UsuarioRepository;
import com.grupo20.ttpsspringboot.services.interfaces.IGeorefService;
import com.grupo20.ttpsspringboot.services.interfaces.IUsuarioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile; // Importar MultipartFile

import java.io.IOException; // Importar IOException
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
public class UsuarioService implements IUsuarioService {

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

    @Autowired
    private IGeorefService georefService;

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
            throw new BadRequestException("El email '" + usuarioDto.getEmail() + "' ya está en uso.");

        }

        // 3. Llamas al servicio de Georef pasando lat y lon
        // Como tu método acepta Strings, se los pasas directo.
        if (usuarioDto.ubicacion.getLatitud() != null && usuarioDto.ubicacion.getLongitud() != null) {
            UbicacionCreateDTO dtoNuevo = georefService.getUbicacionFormateada(
                            usuarioDto.ubicacion.getLatitud().toString(),
                            usuarioDto.ubicacion.getLongitud().toString(),
                            UbicacionCreateDTO.class);

            // 4. Verificas que la respuesta no sea null y extraes los datos
            if (dtoNuevo != null) {
                // Sobrescribís los datos de ubicación en el DTO original
                usuarioDto.ubicacion.setProvincia(dtoNuevo.getProvincia());
                usuarioDto.ubicacion.setDepartamento(dtoNuevo.getDepartamento());
                usuarioDto.ubicacion.setMunicipio(dtoNuevo.getMunicipio());
                usuarioDto.ubicacion.setIdExternoProvincia(dtoNuevo.getIdExternoProvincia());
                usuarioDto.ubicacion.setIdExternoDepartamento(dtoNuevo.getIdExternoDepartamento());
                usuarioDto.ubicacion.setIdExternoMunicipio(dtoNuevo.getIdExternoMunicipio());
            }
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
    public UsuarioDetallelDTO getUsuarioById(Long id) {
        Optional<Usuario> usuarioOpt;
        usuarioOpt = usuarioRepository.findById(id);
        Usuario usuario = usuarioOpt.orElseThrow(() ->
                new EntityNotFoundException("Usuario no encontrado con ID: " + id));

        Date now = new Date();
        List<Medalla> medallas = usuario.getMedallas().stream()
                .filter(medalla ->  medalla.getFechaVencimiento() == null || medalla.getFechaVencimiento().after(now)).toList();

        UsuarioDetallelDTO dto =  UsuarioDetallelDTO.fromEntity(usuario);
        dto.setMedallas(medallas.stream().map(MedallaDTO::fromEntity).toList());

        return dto;
    }

    /**
     * Actualiza completamente un usuario existente.
     * NOTA: Idealmente, usaría un UsuarioUpdateDTO.
     * @param id ID del usuario a actualizar.
     * @return El UsuarioSmallDTO del usuario actualizado.
     */
    @Transactional
    public UsuarioSmallDTO updateUsuario(Long id, UsuarioUpdateDTO dto, MultipartFile file) {
        Usuario existingUsuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado para actualizar con ID: " + id));

        String newEmail = dto.getEmail();
        if (newEmail != null && !newEmail.isBlank()) {

            // si el email lo tiene OTRO usuario => error
            if (usuarioRepository.existsByEmailIgnoreCaseAndIdNot(newEmail.trim(), id)) {
                throw new BadRequestException("El email '" + newEmail + "' ya está en uso.");
            }

        }

        // 1) Actualizar campos de texto (nombre, apellido, etc.)
        usuarioUpdateMapper.updateFromDto(dto, existingUsuario);

        // 2) Actualizar ubicación
        if (dto.getUbicacion() != null) {
            Ubicacion ubicacion = existingUsuario.getUbicacion();
            if (dto.getUbicacion().getLatitud() != null && dto.getUbicacion().getLongitud() != null) {
                UbicacionUpdateDTO dtoNuevo = georefService.getUbicacionFormateada(
                        dto.getUbicacion().getLatitud().toString(),
                        dto.getUbicacion().getLongitud().toString(),
                        UbicacionUpdateDTO.class);

                // 4. Verificas que la respuesta no sea null y extraes los datos
                if (dtoNuevo != null) {
                    dto.setUbicacion(dtoNuevo);
                }
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
            try {
                foto.setContent(file.getBytes());
            } catch (IOException e) {
                // Convertís IO -> tu APIException (sale con JSON lindo)
                throw new BadRequestException("No se pudo leer el archivo de imagen. Probá con otra imagen.");
            }


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

    @Override
    public List<UsuarioSmallDTO> ranking() {
        //return usuarioRepository.ranking().stream().map(UsuarioSmallDTO::fromEntity).collect(Collectors.toList());
        return  usuarioRepository.findTop100ByPuntosGreaterThanOrderByPuntosDesc(1).stream().map(UsuarioSmallDTO::fromEntity).collect(Collectors.toList());
    }

    /**
     * Obtiene el contenido binario de la foto.
     */
    @Transactional(readOnly = true)
    public FotoResponseDTO getFotoByIdUser(Long id) {
        Usuario usuario = this.usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        Foto foto = usuario.getFotoPerfil();

        if (foto == null) {
            return null;
        }
        FotoResponseDTO fotoResponseDTO = new FotoResponseDTO();

        return  FotoResponseDTO.fromEntity(foto);
    }


    @Transactional
    public void restablecerPassword(Long id, RestablecerPasswordRequestDTO entityToEdit ) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        // 1) Confirmación
        if (!entityToEdit.getNuevoPassword().equals(entityToEdit.getConfirmarPassword())) {
            throw new com.grupo20.ttpsspringboot.exceptions.BadRequestException("La nueva contraseña y la confirmación no coinciden");
        }

        // 2) Validar password vieja (contra hash)
        if (!passwordEncoder.matches(entityToEdit.getPasswordOld(), usuario.getPassword())) {
            throw new BadRequestException("La contraseña actual es incorrecta");
        }

        // 4) Guardar hash
        usuario.setPassword(passwordEncoder.encode(entityToEdit.getNuevoPassword()));
        usuarioRepository.save(usuario);
    }

    @Transactional
    public UsuarioSmallDTO cambiarEstado(Long id, UsuarioCambioEstadoRequestDTO estadoRequestDTO) {
        // 1. Buscas el usuario (si no existe, lanzas error)
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        //no permitir deshabilitar al último admin
        if (
                usuario.getRol() == RolUsuarioEnum.ADMINISTRADOR &&
                        usuario.getEstado() == EstadoUsuarioEnum.HABILITADO &&
                        estadoRequestDTO.getEstado() != EstadoUsuarioEnum.HABILITADO
        ) {
            long adminsHabilitados = usuarioRepository.countByRolAndEstado(
                    RolUsuarioEnum.ADMINISTRADOR,
                    EstadoUsuarioEnum.HABILITADO
            );

            if (adminsHabilitados <= 1) {
                throw new BadRequestException(
                        "No se puede dar de baja al último administrador del sistema"
                );
            }
        }
        // 2. Modificas el campo en el objeto Java
        usuario.setEstado(estadoRequestDTO.getEstado());

        // 3. Guardas. El método save devuelve la entidad ya actualizada.
        return UsuarioSmallDTO.fromEntity(usuario);
    }
}