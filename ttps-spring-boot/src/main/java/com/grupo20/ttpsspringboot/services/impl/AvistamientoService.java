package com.grupo20.ttpsspringboot.services.impl;

import com.grupo20.ttpsspringboot.domain.models.Avistamiento;
import com.grupo20.ttpsspringboot.domain.models.Publicacion;
import com.grupo20.ttpsspringboot.domain.models.Ubicacion;
import com.grupo20.ttpsspringboot.domain.models.Usuario;
import com.grupo20.ttpsspringboot.dtos.AvistamientoCreateDTO;
import com.grupo20.ttpsspringboot.dtos.AvistamientoFilterDTO;
import com.grupo20.ttpsspringboot.exceptions.NotFoundException;
import com.grupo20.ttpsspringboot.persistence.repository.AvistamientoRepository;
import com.grupo20.ttpsspringboot.persistence.repository.PublicacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class AvistamientoService {

    @Autowired
    private PublicacionRepository publicacionRepository;

    @Autowired
    private AvistamientoRepository avistamientoRepository;

    @Autowired
    private UbicacionService ubicacionService;

    @Transactional
    public Avistamiento create(Usuario usuario, AvistamientoCreateDTO dto) {
        Avistamiento avistamiento = dto.toEntity();
        avistamiento.setUsuario(usuario);

        avistamiento.setFecha(new Date());

        Publicacion publicacion = publicacionRepository.findById(dto.getPublicacionId())
                .orElseThrow(() -> new NotFoundException("Publicación no encontrada"));


        avistamiento.setPublicacion(publicacion);

        Ubicacion ubicacion = ubicacionService.crearUbicacion(dto.getUbicacion());

        avistamiento.setUbicacion(ubicacion);

        avistamientoRepository.save(avistamiento);

        return avistamiento;
    }

    @Transactional
    public List<Avistamiento> getFiltered(AvistamientoFilterDTO dto) {
       return avistamientoRepository.getByFilters(dto);
    }

    @Transactional
    public Avistamiento get(Long id) {
        Avistamiento avistamiento = avistamientoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Avistamiento no encontrado"));;

        return  avistamiento;
    }

}
