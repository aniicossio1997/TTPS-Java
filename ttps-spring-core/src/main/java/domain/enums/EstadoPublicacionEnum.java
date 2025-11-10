package domain.enums;

public enum EstadoPublicacionEnum {
    // Each constant calls the constructor with its descriptive String
    PERDIDO_PROPIO("Perdido Propio"),
    PERDIDO_AJENO("Perdido Ajeno"),
    RECUPERADO("Recuperado"),
    ADOPTADO("Adoptado");

    private final String descripcion;

    // Constructor that receives the String description
    EstadoPublicacionEnum(String descripcion) {
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