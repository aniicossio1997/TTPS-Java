package com.grupo20.ttpsspringboot.services.impl;

import com.grupo20.ttpsspringboot.domain.models.Foto;
import com.grupo20.ttpsspringboot.domain.models.Publicacion;
import com.grupo20.ttpsspringboot.exceptions.NotFoundException;
import com.grupo20.ttpsspringboot.persistence.repository.FotoRepository;
import com.grupo20.ttpsspringboot.persistence.repository.PublicacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class FotoService {

    @Autowired
    private final FotoRepository fotoRepository;
    @Autowired
    private final PublicacionRepository publicacionRepository;

    public FotoService(FotoRepository fotoRepository,
                       PublicacionRepository publicacionRepository) {
        this.fotoRepository = fotoRepository;
        this.publicacionRepository = publicacionRepository;
    }

    public Foto guardarFotoPublicacion(Long publicacionId, MultipartFile file) {
        try {
            Publicacion publicacion = publicacionRepository.findById(publicacionId)
                    .orElseThrow(() -> new NotFoundException("Publicación no encontrada"));

            Foto foto = new Foto();
            foto.setNombre(file.getOriginalFilename());
            foto.setContent(file.getBytes()); // 👈 acá va el BLOB
            foto.setPublicacion(publicacion);

            return fotoRepository.save(foto);

        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la imagen", e);
        }
    }

    public Foto getFoto(Long id) {
        return fotoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Foto no encontrada"));
    }
}
