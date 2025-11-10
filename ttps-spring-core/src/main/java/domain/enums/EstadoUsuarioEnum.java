package domain.enums;

public enum EstadoUsuarioEnum {
    // Each constant calls the constructor with its descriptive String
    HABILITADO("HABILITADO"),
    BAJA_VOLUNTARIA("BAJA_VOLUNTARIA"),
    BLOQUEADO_POR_ADMIN("BLOQUEADO_POR_ADMIN");

    private final String descripcion;

    // Constructor that receives the String description
    EstadoUsuarioEnum(String descripcion) {
        this.descripcion = descripcion;
    }

    // Getter to retrieve the readable description
    public String getDescripcion() {
        return descripcion;
    }
    @Override
    public String toString() {
        return descripcion;
    }
}