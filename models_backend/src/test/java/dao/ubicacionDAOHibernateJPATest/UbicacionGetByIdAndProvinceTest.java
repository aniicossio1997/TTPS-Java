package dao.ubicacionDAOHibernateJPATest;

import domain.models.Ubicacion;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import persistence.dao.UbicacionDAO;
import persistence.impl.UbicacionDAOHibernateJPA;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UbicacionGetByIdAndProvinceTest {
   public static UbicacionDAO ubicacionDAO = new UbicacionDAOHibernateJPA();

   @AfterEach
   void tearDown() {
        // 1. LIMPIAMOS SOLO LOS AVISTAMIENTOS
        // Esto asegura que la tabla de avistamientos esté vacía
        // antes de que el @BeforeEach de la clase hija (setUpGet) se ejecute.
        try {
            ubicacionDAO.getAll().stream().forEach(a -> ubicacionDAO.delete(a.getId()));
        } catch (EntityNotFoundException e) {
            // Ignorar si ya estaba limpio
        }
    }

    @Test
    void get_by_id_ok() {
        UbicacionDAO dao = new UbicacionDAOHibernateJPA();

        Ubicacion ubicacionNew = new Ubicacion();
        ubicacionNew.setIdExterno("EXT-2");
        ubicacionNew.setProvincia("Córdoba");
        ubicacionNew.setCiudad("ciudad");
        ubicacionNew.setLatitud(-31.420);
        ubicacionNew.setLongitud(-64.188);

        dao.persist(ubicacionNew);
        Ubicacion result = dao.get(ubicacionNew.getId());

        assertNotNull(result);
        assertEquals("Córdoba", result.getProvincia());
        assertEquals("ciudad", result.getCiudad());

    }

    @Test
    void get_devuelve_null_si_no_existe() {
        UbicacionDAO dao = new UbicacionDAOHibernateJPA();
        Ubicacion result = dao.get(9999L);
        assertNull(result);
    }

    @Test
    void findByProvincia_devuelve_correctos() {
        // Arrange
        UbicacionDAO dao = new UbicacionDAOHibernateJPA();

        dao.persist(new Ubicacion("EXT-10", "Buenos Aires", "La plata", "Abasto", -34.4, -58.6));
        dao.persist(new Ubicacion("EXT-10", "Buenos Aires", "Moreno", "Altos", -34.4, -58.6));
        dao.persist(new Ubicacion("EXT-10", "Salta", "Tigre", "Tolosa", -34.4, -58.6));
        dao.persist(new Ubicacion("EXT-10", "Formosa", "Tigre", "Tolosa", -34.4, -58.6));


        List<Ubicacion> lista = dao.findByProvincia("Buenos Aires");
        // Assertions
        assertEquals(2, lista.size());
        assertTrue(lista.stream().allMatch(u -> u.getProvincia().equals("Buenos Aires")));
    }


}
