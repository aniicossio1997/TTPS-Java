package domain.enums;

public enum RolUsuarioEnum {
    // Cada constante llama al constructor con su valor String específico
    ADMINISTRADOR("ADMINISTRADOR"),
    USUARIO_COMUN("USUARIO_COMUN");

    private final String rol; // Atributo para guardar el valor String

    // Constructor que recibe el String
    RolUsuarioEnum(String rol) {
        this.rol = rol;
    }

    public String getRol() {
        return rol;
    }
    @Override
    public String toString() {
        return rol;
    }
}
