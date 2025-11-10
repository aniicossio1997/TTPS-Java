package com.grupo20.ttpsspringboot.dtos;

import com.grupo20.ttpsspringboot.domain.models.Avistamiento;
import lombok.Data;

@Data
public class AuthSessionDTO {

    private String token;
    private  UsuarioSmallDTO usuario;

}
