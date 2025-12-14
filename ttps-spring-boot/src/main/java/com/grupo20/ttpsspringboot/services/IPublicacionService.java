package com.grupo20.ttpsspringboot.services;

import com.grupo20.ttpsspringboot.domain.models.Publicacion;
import com.grupo20.ttpsspringboot.domain.models.Usuario;
import com.grupo20.ttpsspringboot.dtos.PublicacionCreateDTO;
import com.grupo20.ttpsspringboot.dtos.PublicacionDTO;
import com.grupo20.ttpsspringboot.dtos.PublicacionFilterDTO;
import com.grupo20.ttpsspringboot.dtos.PublicacionUpdateDTO;
import com.grupo20.ttpsspringboot.dtos.bases.PaginateBaseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IPublicacionService {

    Publicacion create(Usuario usuario, PublicacionCreateDTO dto, List<MultipartFile> files);

    Publicacion get(Long id);

    PaginateBaseDTO<PublicacionDTO> getFiltered(PublicacionFilterDTO filter);

    Publicacion update(Long id, Usuario usuario, PublicacionUpdateDTO dto, List<MultipartFile> files);

    void delete(Long id, Usuario usuario);
}