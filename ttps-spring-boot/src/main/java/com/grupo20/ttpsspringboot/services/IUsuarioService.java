package com.grupo20.ttpsspringboot.services;

import com.grupo20.ttpsspringboot.dtos.UsuarioCreateDTO;
import com.grupo20.ttpsspringboot.dtos.UsuarioSmallDTO;
import com.grupo20.ttpsspringboot.dtos.UsuarioUpdateDTO;

import java.util.List;

public interface IUsuarioService {

    UsuarioSmallDTO createUsuario(UsuarioCreateDTO usuarioDto);

    List<UsuarioSmallDTO> getAllUsuarios();

    UsuarioSmallDTO getUsuarioById(Long id);

    UsuarioSmallDTO updateUsuario(Long id, UsuarioUpdateDTO dto);
}