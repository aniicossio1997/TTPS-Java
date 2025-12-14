package com.grupo20.ttpsspringboot.services.impl;

import com.grupo20.ttpsspringboot.domain.models.Avistamiento;
import com.grupo20.ttpsspringboot.domain.models.Foto;
import com.grupo20.ttpsspringboot.domain.models.Publicacion;
import com.grupo20.ttpsspringboot.domain.models.Usuario;
import com.grupo20.ttpsspringboot.dtos.FotoLinkDTO;
import com.grupo20.ttpsspringboot.dtos.FotoResponseDTO;
import com.grupo20.ttpsspringboot.exceptions.NotFoundException;
import com.grupo20.ttpsspringboot.persistence.repository.AvistamientoRepository;
import com.grupo20.ttpsspringboot.persistence.repository.FotoRepository;
import com.grupo20.ttpsspringboot.persistence.repository.PublicacionRepository;
import com.grupo20.ttpsspringboot.persistence.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
public class FotoService {

    @Autowired
    private final FotoRepository fotoRepository;
    @Autowired
    private final PublicacionRepository publicacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AvistamientoRepository avistamientoRepository;

    public FotoService(FotoRepository fotoRepository,
                       PublicacionRepository publicacionRepository) {
        this.fotoRepository = fotoRepository;
        this.publicacionRepository = publicacionRepository;
    }


    public Foto getFoto(Long id) {
        return fotoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Foto no encontrada"));
    }
    /**
     * Obtiene el contenido binario de la foto.
     */
    @Transactional(readOnly = true)
    public FotoResponseDTO getFotoByIdUser(Long id) {
        Usuario usuario = this.usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        Foto foto = usuario.getFotoPerfil();

        if (foto == null) {
            return null;
        }
        FotoResponseDTO fotoResponseDTO = new FotoResponseDTO();

        return  FotoResponseDTO.fromEntity(foto);
    }

    public List<FotoLinkDTO> getFotosPublicacionLinks(Long publicacionId) {
        Publicacion p = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new NotFoundException("Publicación no encontrada"));

        return p.getFotos().stream().map(FotoLinkDTO::fromEntity).toList();
    }

    public List<FotoLinkDTO> getFotosAvistamientoLinks(Long avistamientoId) {
        Avistamiento a = avistamientoRepository.findById(avistamientoId)
                .orElseThrow(() -> new NotFoundException("Avistamiento no encontrado"));

        return a.getFotos().stream().map(FotoLinkDTO::fromEntity).toList();
    }

    public FotoLinkDTO getFotoUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));


        return  FotoLinkDTO.fromEntity(usuario.getFotoPerfil());
    }



}
