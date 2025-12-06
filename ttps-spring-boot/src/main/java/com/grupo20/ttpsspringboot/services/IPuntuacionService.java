package com.grupo20.ttpsspringboot.services;

import com.grupo20.ttpsspringboot.domain.models.Publicacion;
import com.grupo20.ttpsspringboot.domain.models.Usuario;

import java.util.List;

public interface IPuntuacionService {

    void otorgarPuntajePorAdopcion(Usuario usuario);

    void otorgarPuntajesPorHallazgo(Publicacion publicacion, List<Long> agradecimientos);

    void otorgarPuntosPorReporte(Usuario usuario);
}