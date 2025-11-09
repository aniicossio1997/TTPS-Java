package com.grupo20.ttpsspringboot.services.impl;

import com.grupo20.ttpsspringboot.domain.models.Avistamiento;
import com.grupo20.ttpsspringboot.domain.models.Publicacion;
import com.grupo20.ttpsspringboot.domain.models.Ubicacion;
import com.grupo20.ttpsspringboot.domain.models.Usuario;
import com.grupo20.ttpsspringboot.dtos.AvistamientoCreateDTO;
import com.grupo20.ttpsspringboot.dtos.AvistamientoFilterDTO;
import com.grupo20.ttpsspringboot.exceptions.NotFoundException;
import com.grupo20.ttpsspringboot.persistence.dao.AvistamientoDAO;
import com.grupo20.ttpsspringboot.persistence.dao.PublicacionDAO;
import com.grupo20.ttpsspringboot.services.UbicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class AvistamientoService {

    @Autowired
    private PublicacionDAO publicacionDAO;

    @Autowired
    private AvistamientoDAO avistamientoDAO;

    @Autowired
    private UbicacionService ubicacionService;

    @Transactional
    public Avistamiento create(Usuario usuario, AvistamientoCreateDTO dto) {
        Avistamiento avistamiento = dto.toEntity();
        avistamiento.setUsuario(usuario);

        avistamiento.setFecha(new Date());

        Publicacion publicacion = publicacionDAO.get(dto.getPublicacionId());
        if (publicacion == null) {
            throw new NotFoundException();
        }

        avistamiento.setPublicacion(publicacion);

        Ubicacion ubicacion = ubicacionService.crearUbicacion(dto.getUbicacion());

        avistamiento.setUbicacion(ubicacion);

        avistamientoDAO.persist(avistamiento);

        return avistamiento;
    }

    @Transactional
    public List<Avistamiento> getFiltered(AvistamientoFilterDTO dto) {
       return avistamientoDAO.getByFilters(dto);
    }

    @Transactional
    public Avistamiento get(Long id) {
        Avistamiento avistamiento = avistamientoDAO.get(id);
        if (avistamiento == null) {
            throw new NotFoundException();
        }
        return  avistamiento;
    }

}
