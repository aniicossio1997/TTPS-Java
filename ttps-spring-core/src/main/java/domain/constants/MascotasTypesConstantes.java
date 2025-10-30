package domain.constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public final class MascotasTypesConstantes {

    // --- Categorías Principales ---
    public static final String ESPECIE_PERRO = "Perro";
    public static final String ESPECIE_GATO = "Gato";
    public static final String ESPECIE_OTRO = "Otro";

    // --- Listas de Razas ---
    public static final List<String> RAZAS_PERRO = Arrays.asList(
            "Labrador Retriever",
            "Bulldog Francés",
            "Ovejero Alemán",
            "Golden Retriever",
            "Caniche (Poodle)",
            "Beagle",
            "Boxer",
            "Dachshund (Salchicha)",
            "Siberiano",
            "Pitbull",
            "Chihuahua",
            "Yorkshire Terrier",
            "Schnauzer",
            "Mestizo",
            "Otro" // Opción para razas no listadas
    );

    public static final List<String> RAZAS_GATO = Arrays.asList(
            "Siamés",
            "Persa",
            "Maine Coon",
            "Ragdoll",
            "Bengalí",
            "Esfinge (Sphynx)",
            "British Shorthair",
            "Común Europeo",
            "Mestizo",
            "Otro" // Opción para razas no listadas
    );

    // --- Mapa para relacionar Categoría con sus Razas ---
    public static final Map<String, List<String>> RAZAS_POR_CATEGORIA;

    static {
        Map<String, List<String>> map = new HashMap<>();
        map.put(ESPECIE_PERRO, RAZAS_PERRO);
        map.put(ESPECIE_GATO, RAZAS_GATO);
        // Hacemos el mapa inmutable para que no se pueda modificar
        RAZAS_POR_CATEGORIA = Collections.unmodifiableMap(map);
    }

    // Constructor privado para evitar que la clase sea instanciada
    private MascotasTypesConstantes() {}
}