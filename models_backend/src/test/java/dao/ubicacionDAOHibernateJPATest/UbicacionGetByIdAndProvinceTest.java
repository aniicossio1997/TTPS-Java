package dao.ubicacionDAOHibernateJPATest;

import domain.models.Ubicacion;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import persistence.dao.UbicacionDAO;
import persistence.impl.UbicacionDAOHibernateJPA;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UbicacionGetByIdAndProvinceTest extends  BaseUbicacionTest {


    @Test
    void get_by_id_ok() {
        UbicacionDAO _ubicacionDAO = new UbicacionDAOHibernateJPA();

        Ubicacion ubicacionNew = new Ubicacion();
        ubicacionNew.setIdExterno("EXT-2");
        ubicacionNew.setProvincia("Córdoba");
        ubicacionNew.setCiudad("ciudad");
        ubicacionNew.setLatitud(-31.420);
        ubicacionNew.setLongitud(-64.188);

        BaseUbicacionTest._ubicacionDAO.persist(ubicacionNew);
        Ubicacion result = BaseUbicacionTest._ubicacionDAO.get(ubicacionNew.getId());

        assertNotNull(result);
        assertEquals("Córdoba", result.getProvincia());
        assertEquals("ciudad", result.getCiudad());

    }

    @Test
    void get_devuelve_null_si_no_existe() {

        Ubicacion result = BaseUbicacionTest._ubicacionDAO.get(9999L);
        assertNull(result);
    }

    @Test
    void findByProvincia_devuelve_correctos() {
        // Arrange

        BaseUbicacionTest._ubicacionDAO.persist(new Ubicacion("EXT-10", "Buenos Aires", "La plata", "Abasto", -34.4, -58.6));
        BaseUbicacionTest._ubicacionDAO.persist(new Ubicacion("EXT-10", "Buenos Aires", "Moreno", "Altos", -34.4, -58.6));
        BaseUbicacionTest._ubicacionDAO.persist(new Ubicacion("EXT-10", "Salta", "Tigre", "Tolosa", -34.4, -58.6));
        BaseUbicacionTest._ubicacionDAO.persist(new Ubicacion("EXT-10", "Formosa", "Tigre", "Tolosa", -34.4, -58.6));


        List<Ubicacion> lista = _ubicacionDAO.findByProvincia("Buenos Aires");
        // Assertions
        assertEquals(2, lista.size());
        assertTrue(lista.stream().allMatch(u -> u.getProvincia().equals("Buenos Aires")));
    }


}
