package com.grupo20.ttpsspringboot.services;

import com.grupo20.ttpsspringboot.domain.models.Avistamiento;
import com.grupo20.ttpsspringboot.domain.models.Usuario;
import com.grupo20.ttpsspringboot.dtos.AvistamientoCreateDTO;
import com.grupo20.ttpsspringboot.dtos.AvistamientoFilterDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IAvistamientoService {

    Avistamiento create(Usuario usuario, AvistamientoCreateDTO dto,  List<MultipartFile> files);

    List<Avistamiento> getFiltered(AvistamientoFilterDTO dto);

    Avistamiento get(Long id);
}