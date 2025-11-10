package persistence.dao;

import domain.models.Usuario;

public interface UsuarioDAO extends GenericDAO<Usuario>{

    public Usuario findByEmail(String email);
}
