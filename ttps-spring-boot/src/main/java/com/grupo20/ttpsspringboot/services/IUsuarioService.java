package com.grupo20.ttpsspringboot.services;

import com.grupo20.ttpsspringboot.dtos.UsuarioCreateDTO;
import com.grupo20.ttpsspringboot.dtos.UsuarioDetallelDTO;
import com.grupo20.ttpsspringboot.dtos.UsuarioSmallDTO;
import com.grupo20.ttpsspringboot.dtos.UsuarioUpdateDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IUsuarioService {

    UsuarioSmallDTO createUsuario(UsuarioCreateDTO usuarioDto);

    List<UsuarioSmallDTO> getAllUsuarios();

    UsuarioDetallelDTO getUsuarioById(Long id);

    UsuarioSmallDTO updateUsuario(Long id, UsuarioUpdateDTO dto, MultipartFile file) throws IOException;

    List<UsuarioSmallDTO> ranking();
}