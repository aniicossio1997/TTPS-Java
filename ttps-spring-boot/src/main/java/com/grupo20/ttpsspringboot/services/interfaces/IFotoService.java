package com.grupo20.ttpsspringboot.services.interfaces;

import com.grupo20.ttpsspringboot.domain.models.Foto;
import org.springframework.web.multipart.MultipartFile;

public interface IFotoService {

    Foto guardarFotoPublicacion(Long publicacionId, MultipartFile file);

    Foto getFoto(Long id);
}