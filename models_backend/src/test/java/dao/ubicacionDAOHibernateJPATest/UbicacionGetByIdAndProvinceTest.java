package dao.ubicacionDAOHibernateJPATest;

import domain.models.Ubicacion;

import org.junit.jupiter.api.DisplayName;
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


    // Dentro de la clase UbicacionGetTest

    // (Tu @BeforeEach setupGet() que crea ubiBA1, ubiBA2, ubiCBA, ubiSF está bien)

    @Test
    @DisplayName("Buscar por criterios LIKE (Provincia, Ciudad, Barrio, ID Externo)")
    void findByCriteriaLike_ok() {
        // (Asegúrate que DAOBaseTest.@BeforeEach limpió la BD)
        Ubicacion ubiBA1, ubiBA2, ubiCBA, ubiSF;
        ubiBA1 = new Ubicacion("EXT-BA1", "Buenos Aires", "La Plata", "Centro", -34.921, -57.954);
        _ubicacionDAO.persist(ubiBA1);

        ubiBA2 = new Ubicacion("EXT-BA2", "Buenos Aires", "Moreno", "Puerto", -38.033, -57.534);
        _ubicacionDAO.persist(ubiBA2);

        ubiCBA = new Ubicacion("EXT-CB1", "Cordoba", "Villa Carlos Paz", "Centro", -31.424, -64.497);
        _ubicacionDAO.persist(ubiCBA);

        ubiSF = new Ubicacion("EXT-SF1", "Santa Fe", "Rosario", "Centro", -32.947, -60.630);
        _ubicacionDAO.persist(ubiSF);

        // ... (Test 1: Buscar por all -> "EXT-CB1", "Cordoba", "Villa Carlos Paz", "Centro") ...
        //                     idExterno, provincia, ciudad, barrio
        List<Ubicacion> porProv = _ubicacionDAO.findByCriteriaLike("EXT-CB1", "Cordoba", "Villa Carlos Paz", "Centro");
        assertEquals(1, porProv.size());
        assertTrue(porProv.stream().allMatch(u -> u.getProvincia().equals("Cordoba")));


        //by provincia y barrio
        List<Ubicacion> porProvYBarrio = _ubicacionDAO.findByCriteriaLike(null,   "Buenos", null,   "Centro");
        assertEquals(1, porProvYBarrio.size());
        // ...

        // ... (Test 6: Criterio que no coincide) ...
        //                     idExterno, provincia, ciudad, barrio
        List<Ubicacion> sinResultados = _ubicacionDAO.findByCriteriaLike(null, "Tucuman", null,   null);
        assertTrue(sinResultados.isEmpty());

        // ... (Test 7: Búsqueda sin criterios) ...
        List<Ubicacion> todos = _ubicacionDAO.findByCriteriaLike(null, null, null, null);
        assertEquals(4, todos.size());
    }

}
