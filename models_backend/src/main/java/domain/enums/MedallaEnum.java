package domain.enums;

public enum MedallaEnum {
    // Cada constante llama al constructor con su nombre descriptivo
    RESCATISTA_NIVEL_1("Rescatista Nivel 1"),
    RESCATISTA_NIVEL_2("Rescatista Nivel 2"),
    RESCATISTA_NIVEL_3("Rescatista Nivel 3"),
    HEROE_DEL_BARRIO("Héroe del Barrio"),
    ANGEL_GUARDIAN("Ángel Guardián"),
    NUEVO_TUTOR("Nuevo Tutor");

    private final String nombre;

    // Constructor que recibe el nombre de la medalla
    MedallaEnum(String nombre) {
        this.nombre = nombre;
    }

    // Getter para obtener el nombre legible
    public String getNombre() {
        return nombre;
    }
    @Override
    public String toString() {
        return nombre;
    }
}