package config.context;

import domain.models.Usuario;

public class AuthContext {
    private static final ThreadLocal<Usuario> userHolder = new ThreadLocal<>();

    public static void setUser(Usuario user) {
        userHolder.set(user);
    }

    public static Usuario getUser() {
        return userHolder.get();
    }

    public static void clear() {
        userHolder.remove();
    }
}