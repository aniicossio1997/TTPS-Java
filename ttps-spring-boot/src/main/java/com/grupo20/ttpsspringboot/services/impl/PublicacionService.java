package com.grupo20.ttpsspringboot.services.impl;

import com.grupo20.ttpsspringboot.domain.models.Publicacion;
import com.grupo20.ttpsspringboot.domain.models.Ubicacion;
import com.grupo20.ttpsspringboot.domain.models.Usuario;
import com.grupo20.ttpsspringboot.dtos.PublicacionCreateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.grupo20.ttpsspringboot.persistence.dao.PublicacionDAO;
import com.grupo20.ttpsspringboot.persistence.dao.UsuarioDAO;
import com.grupo20.ttpsspringboot.services.UbicacionService;

@Service
public class PublicacionService {
    @Autowired
    private UsuarioDAO usuarioDAO;
    @Autowired
    private PublicacionDAO publicacionDAO;

    @Autowired
    private UbicacionService ubicacionService;

    @Transactional
    public Publicacion create(Usuario usuario, PublicacionCreateDTO dto) {
        Publicacion publicacion = dto.toEntity();
        publicacion.setUsuario(usuario);

        Ubicacion ubicacion = ubicacionService.crearUbicacion(dto.getUbicacion());

        publicacion.setUbicacion(ubicacion);

        publicacionDAO.persist(publicacion);

        return publicacion;
    }


}
