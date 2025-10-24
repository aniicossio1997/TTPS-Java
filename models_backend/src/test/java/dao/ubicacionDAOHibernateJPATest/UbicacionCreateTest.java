package dao.ubicacionDAOHibernateJPATest;

import domain.models.Ubicacion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class UbicacionCreateTest extends BaseUbicacionTest {

    @Test
    void create_persist_ok() {
        Ubicacion u = new Ubicacion();
        u.setIdExterno("EXT-1");
        u.setProvincia("Buenos Aires");
        u.setCiudad("La Plata");
        u.setBarrio("Centro");
        u.setLatitud(-34.921);
        u.setLongitud(-57.954);

        assertTrue(_ubicacionDAO.getAll().size()==0);

        _ubicacionDAO.persist(u);

        assertNotNull(u.getId());
        Assertions.assertEquals(_ubicacionDAO.getAll().size(),1, "Debe haber una ubicacion en la base de datos");
    }

    @Test
    void crear_falla_si_latitud_es_null() {
        Ubicacion u = new Ubicacion();
        u.setLongitud(-58.0); // solo long
        var ex = assertThrows(IllegalArgumentException.class, () -> _ubicacionDAO.persist(u));
        assertTrue(ex.getMessage().toLowerCase().contains("latitud"));
    }


    @Test
    void crear_falla_si_longitud_es_null() {

        Ubicacion u = new Ubicacion();
        u.setLatitud(-34.0); // solo lat
        var ex = assertThrows(IllegalArgumentException.class, () -> _ubicacionDAO.persist(u));
        assertTrue(ex.getMessage().toLowerCase().contains("longitud"));
    }
}