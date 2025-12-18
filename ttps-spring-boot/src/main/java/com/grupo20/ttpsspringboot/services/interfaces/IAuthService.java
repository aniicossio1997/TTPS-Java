package com.grupo20.ttpsspringboot.services.interfaces;

import com.grupo20.ttpsspringboot.domain.models.Usuario;

public interface IAuthService {

    Usuario validarCredenciales(String email, String password);
}