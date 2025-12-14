package com.grupo20.ttpsspringboot.services;

import com.grupo20.ttpsspringboot.domain.models.Ubicacion;
import com.grupo20.ttpsspringboot.dtos.UbicacionCreateDTO;
import com.grupo20.ttpsspringboot.dtos.UbicacionUpdateDTO;

import java.util.List;

public interface IUbicacionService {

    Ubicacion crearUbicacion(UbicacionCreateDTO dto);

    Ubicacion updateUbicacion(Long id, UbicacionUpdateDTO dto);

    Ubicacion getUbicacion(Long id);

    List<Ubicacion> getAllUbicaciones();

    List<Ubicacion> buscarPorProvincia(String provincia);

    List<Ubicacion> buscarPorCriterio( String idExternoProvincia, String provincia,String idExternoMunicipio, String municipio,  String idExternoDepartamento,String departamento);
}