package domain.constants;

public final class MedallaRequisitos {

    /**
     * Constructor privado para evitar que la clase sea instanciada.
     */
    private MedallaRequisitos() {}

    // --- Requisitos para medalla "Rescatista" ---
    /**
     * Cantidad de mascotas ayudadas para alcanzar el Nivel 1.
     */
    public static final int REQUISITO_RESCATISTA_NIVEL_1 = 5;

    /**
     * Cantidad de mascotas ayudadas para alcanzar el Nivel 2.
     */
    public static final int REQUISITO_RESCATISTA_NIVEL_2 = 10;

    /**
     * Cantidad de mascotas ayudadas para alcanzar el Nivel 3.
     */
    public static final int REQUISITO_RESCATISTA_NIVEL_3 = 20;


    // --- Requisitos para otras medallas ---
    /**
     * Cantidad de casos en los que ayude necesarios en la propia zona para obtener "Héroe del Barrio".
     */
    public static final int REQUISITO_CASOS_HEROE_DEL_BARRIO = 5;

    /**
     * Cantidad de mascotas en tránsito para obtener "Ángel Guardián".
     */
    public static final int REQUISITO_MASCOTAS_ANGEL_GUARDIAN = 5;


    // --- Parámetros de medallas ---
    /**
     * Días de vigencia de la medalla "Nuevo Tutor" tras una adopción.
     */
    public static final int DIAS_VIGENCIA_NUEVO_TUTOR = 7;
}