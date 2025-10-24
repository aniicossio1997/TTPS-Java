package dao.AvistamientoDAOHibernateJPATest;

import domain.models.Avistamiento;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.dao.AvistamientoDAO;
import persistence.impl.AvistamientoDAOHibernateJPA;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

// 1. HEREDA de la clase base
class AvistamientoCreateTest extends BaseAvistamientoDAOTest {

    protected AvistamientoDAO avistamientoDAO ;

    @Test
    @DisplayName("Debe persistir un Avistamiento correctamente")
    void persist_ok() {
        avistamientoDAO = new AvistamientoDAOHibernateJPA();

        // ARRANGE
        // Usa las dependencias (pubTest1, etc) creadas en el @BeforeEach de la clase base
        Avistamiento av = new Avistamiento();
        av.setFecha(new Date());
        av.setDescripcion("Visto en Pub 1");
        av.setPublicacion(pubTest1);
        av.setUbicacion(ubicacionTest);
        av.setUsuario(usuarioBase);

        var sizeBefore = avistamientoDAO.getAll().size();
        assertEquals(sizeBefore, 0, "No debería haber avistamientos antes de la prueba");
        assertNull(av.getId(), "El ID debe ser nulo antes de persistir");

        // ACT
        avistamientoDAO.persist(av); // Usa el DAO de la clase base


        // ASSERT
        assertNotNull(av.getId(), "El ID no debe ser nulo después de persistir");
        Avistamiento fetched = avistamientoDAO.get(av.getId());
        assertEquals("Visto en Pub 1", fetched.getDescripcion());
    }

    @Test
    @DisplayName("Debe fallar (PersistenceException) si faltan dependencias")
    void persist_falla_sin_dependencias() {
        avistamientoDAO = new AvistamientoDAOHibernateJPA();
        // ARRANGE
        Avistamiento av = new Avistamiento();
        av.setFecha(new Date());
        av.setDescripcion("Visto en Pub 1");
        av.setFecha(new Date());
        // Faltan setPublicacion, setUbicacion, setUsuario (que son 'nullable=false')

        // ACT & ASSERT
        assertThrows(PersistenceException.class, () -> {
            avistamientoDAO.persist(av);
        }, "Debería lanzar PersistenceException por constraints de FK nulas");
    }
}
