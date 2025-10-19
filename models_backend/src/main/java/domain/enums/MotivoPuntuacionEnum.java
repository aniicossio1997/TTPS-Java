package domain.enums;


public enum MotivoPuntuacionEnum {
    // Cada constante llama al constructor con su descripción
    HALLAZGO_MASCOTA_BUSCADA("Hallazgo de mascota buscada"),
    REPORTE_MASCOTA_PERDida("Reportar una mascota perdida"),
    ADOPCION_MASCOTA("Adoptar una mascota");

    private final String descripcion;

    // Constructor que solo recibe la descripción
    MotivoPuntuacionEnum(String descripcion) {
        this.descripcion = descripcion;
    }

    // Getter para obtener la descripción legible
    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}