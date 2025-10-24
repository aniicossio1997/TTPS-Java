package dao.AvistamientoDAOHibernateJPATest;

import domain.models.Avistamiento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.impl.AvistamientoDAOHibernateJPA;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// 1. HEREDA de la clase base
class AvistamientoGetTest extends BaseAvistamientoDAOTest {


    private Avistamiento av1, av2, av3; // Entidades específicas para Get

    /**
     * Este @BeforeEach se ejecuta DESPUÉS del @BeforeEach de la clase base.
     * Crea los avistamientos que vamos a buscar en los tests.
     */
    @BeforeEach
    void setUpGet() {

        var avistamientoDAO = new AvistamientoDAOHibernateJPA();
        av1 = new Avistamiento();
        av1.setFecha(new Date());
        av1.setDescripcion("Visto en Pub 1 (A)");
        av1.setPublicacion(pubTest1); av1.setUbicacion(ubicacionTest); av1.setUsuario(usuarioBase);
        avistamientoDAO.persist(av1);

        av2 = new Avistamiento();
        av2.setFecha(new Date()); av2.setDescripcion("Visto en Pub 1 (B)");
        av2.setPublicacion(pubTest1); av2.setUbicacion(ubicacionTest); av2.setUsuario(usuarioBase);
        avistamientoDAO.persist(av2);

        av3 = new Avistamiento();
        av3.setFecha(new Date()); av3.setDescripcion("Visto en Pub 2");
        av3.setPublicacion(pubTest2); av3.setUbicacion(ubicacionTest); av3.setUsuario(usuarioBase);
        avistamientoDAO.persist(av3);
    }

    @Test
    @DisplayName("Debe recuperar por ID (get)")
    void get_by_id_ok() {
        Avistamiento fetched = avistamientoDAO.get(av1.getId());
        assertNotNull(fetched);
        assertEquals("Visto en Pub 1 (A)", fetched.getDescripcion());
    }

    @Test
    @DisplayName("Debe devolver null si el ID (get) no existe")
    void get_by_id_not_found() {
        assertNull(avistamientoDAO.get(9999L));
    }

    @Test
    @DisplayName("Debe recuperar por ID de Publicación (getByIdPublicacion)")
    void get_by_id_publicacion_ok() {
        List<Avistamiento> dePub1 = avistamientoDAO.getByIdPublicacion(pubTest1.getId());
        List<Avistamiento> dePub2 = avistamientoDAO.getByIdPublicacion(pubTest2.getId());

        assertEquals(2, dePub1.size(), "Debería haber 2 avistamientos para Pub1");
        assertEquals(1, dePub2.size(), "Debería haber 1 avistamiento para Pub2");
        assertEquals("Visto en Pub 2", dePub2.get(0).getDescripcion());
    }

    @Test
    @DisplayName("Debe devolver lista vacía si el ID de Publicación no existe")
    void get_by_id_publicacion_not_found() {
        List<Avistamiento> vacia = avistamientoDAO.getByIdPublicacion(9999L);
        assertNotNull(vacia);
        assertTrue(vacia.isEmpty());
    }

    @Test
    @DisplayName("Debe devolver todos los Avistamientos (getAll)")
    void getAll_ok() {
        List<Avistamiento> todos = avistamientoDAO.getAll();
        var size = todos.size();
        var expectedSize = 3;
        var ok = size == expectedSize;
        assertTrue(ok, "Se esperaban " + expectedSize + " avistamientos, pero se encontraron " + size);
        assertEquals(3, todos.size()); // av1, av2, av3 creados en el setUpGet
    }


}