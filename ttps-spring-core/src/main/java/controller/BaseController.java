package controller;

import config.context.AuthContext;
import domain.models.Usuario;

public abstract class BaseController {
    public Usuario getUsuario(){
        return AuthContext.getUser();
    }
}
