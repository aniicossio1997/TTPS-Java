package domain.constants;

public final class Puntuacion {

    /**
     * Constructor privado para evitar que la clase sea instanciada.
     */
    private Puntuacion() {}

    /**
     * Puntos otorgados por el hallazgo de una mascota buscada.
     */
    public static final int POR_HALLAZGO_MASCOTA = 10;

    /**
     * Puntos otorgados por reportar una mascota perdida (propia o ajena).
     */
    public static final int POR_REPORTE_MASCOTA = 10;

    /**
     * Puntos otorgados al usuario que decide adoptar una mascota encontrada.
     */
    public static final int POR_ADOPCION_MASCOTA = 20;
}