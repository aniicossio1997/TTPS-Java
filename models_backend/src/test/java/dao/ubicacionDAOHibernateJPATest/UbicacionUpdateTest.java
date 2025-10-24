package dao.ubicacionDAOHibernateJPATest;

import domain.models.Ubicacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;


class UbicacionUpdateTest extends BaseUbicacionTest {

    private Ubicacion ubicacionPrueba; // Objeto de prueba

    @BeforeEach
    public void setUp() {

        // Creamos un objeto "fresco" ANTES de cada test
        ubicacionPrueba = new Ubicacion();
        ubicacionPrueba.setLatitud(-34.92136);
        ubicacionPrueba.setLongitud(-57.9545);
        ubicacionPrueba.setProvincia("Buenos Aires");
        ubicacionPrueba.setCiudad("La Plata");
        ubicacionPrueba.setBarrio("Barrio");
        ubicacionPrueba.setIdExterno("LP-TEST-001");
    }

    @Test
    void update_ok() {
        // ARRANGE (Organizar)
        // Asumimos que @BeforeEach ya persistió 'ubicacionDePrueba'
        // Guardamos los datos originales para comparar
        _ubicacionDAO.persist(ubicacionPrueba);
        Long id = ubicacionPrueba.getId();
        String ciudadOriginal = ubicacionPrueba.getCiudad(); // Ej: "Tigre"

        // ACT (Actuar)
        // 1. Obtenemos la entidad que VAMOS a modificar
        Ubicacion ubiParaActualizar = _ubicacionDAO.get(id);
        assertNotNull(ubiParaActualizar, "La entidad a actualizar no se encontró");

        // 2. Modificamos el objeto RECUPERADO (ubiParaActualizar)
        // ¡Este era el error! Debes modificar ubiParaActualizar, NO ubicacionPrueba
        ubiParaActualizar.setLatitud(-20.0);
        ubiParaActualizar.setLongitud(-2.0);
        ubiParaActualizar.setProvincia("Santa Fe");
        ubiParaActualizar.setCiudad("ciudad update");
        ubiParaActualizar.setBarrio("barrio update");
        ubiParaActualizar.setIdExterno("LP-TEST-001-UPDATED");

        // 3. Enviamos las actualizaciones a la BD
        _ubicacionDAO.update(ubiParaActualizar);

        // ASSERT (Verificar)
        // 1. Volvemos a obtener la entidad desde la BD para asegurar
        //    que los cambios se persistieron correctamente.
        Ubicacion ubiRefrescada = _ubicacionDAO.get(id);
        assertNotNull(ubiRefrescada, "La entidad no se encontró después de actualizar");

        // 2. Comparamos atributo por atributo usando assertAll
        assertAll("Verifica todos los atributos modificados",
                () -> assertEquals("ciudad update", ubiRefrescada.getCiudad(), "La ciudad no se actualizó"),
                () -> assertEquals("Santa Fe", ubiRefrescada.getProvincia(), "La provincia no se actualizó"),
                () -> assertEquals("barrio update", ubiRefrescada.getBarrio(), "El barrio no se actualizó"),
                () -> assertEquals("LP-TEST-001-UPDATED", ubiRefrescada.getIdExterno(), "El ID externo no se actualizó"),
                () -> assertEquals(-20.0, ubiRefrescada.getLatitud(), "La latitud no se actualizó"),
                () -> assertEquals(-2.0, ubiRefrescada.getLongitud(), "La longitud no se actualizó"),
                () -> assertEquals(id, ubiRefrescada.getId(), "El ID principal (PK) no debe cambiar"),
                () -> assertNotEquals(ciudadOriginal, ubiRefrescada.getCiudad(), "La ciudad debería ser diferente a la original")
        );
    }

    @Test
    void update_sin_latitud() {
        // ARRANGE
        _ubicacionDAO.persist(ubicacionPrueba);
        ubicacionPrueba.setLatitud(null);
        ubicacionPrueba.setLongitud(-60.0);

        var ex = assertThrows(IllegalArgumentException.class, () -> _ubicacionDAO.update(ubicacionPrueba));
        assertTrue(ex.getMessage().toLowerCase().contains("latitud"));
    }

    @Test
    void update_sin_longitud() {
        // ARRANGE
        _ubicacionDAO.persist(ubicacionPrueba);
        ubicacionPrueba.setLatitud(-10.0);
        ubicacionPrueba.setLongitud(null);

        var ex = assertThrows(IllegalArgumentException.class, () -> _ubicacionDAO.update(ubicacionPrueba));
        assertTrue(ex.getMessage().toLowerCase().contains("longitud"));
    }

}
