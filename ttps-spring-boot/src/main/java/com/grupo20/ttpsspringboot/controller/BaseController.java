package com.grupo20.ttpsspringboot.controller;

import config.context.AuthContext;
import com.grupo20.ttpsspringboot.domain.models.Usuario;

public abstract class BaseController {
    public Usuario getUsuario(){
        return AuthContext.getUser();
    }
}
