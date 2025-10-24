package dao.estadoPublicacionDAOHibernateJPATest;

import domain.enums.EstadoPublicacionEnum;
import domain.models.EstadoPublicacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List; // Importa List si vas a usar getAll

import static org.junit.jupiter.api.Assertions.*;

class EstadoPublicacionGetTest extends BaseEstadoPublicacionDAOTest {

    private EstadoPublicacion estado1, estado2; // Estados creados para buscar

    /**
     * Crea datos específicos para los tests de Get.
     */
    @BeforeEach
    void setUpGet() {
        super.setUpBaseEstadoPub();
        // Crea dos estados para la misma publicación
        estado1 = new EstadoPublicacion();
        estado1.setFecha(new Date(System.currentTimeMillis() - 10000)); // Un poco antes
        estado1.setEstado(EstadoPublicacionEnum.PERDIDO_AJENO);
        estado1.setPublicacion(publicacionTest);
        estadoPublicacionDAO.persist(estado1);

        estado2 = new EstadoPublicacion();
        estado2.setFecha(new Date()); // Ahora
        estado2.setEstado(EstadoPublicacionEnum.RECUPERADO);
        estado2.setPublicacion(publicacionTest);
        estadoPublicacionDAO.persist(estado2);
    }

    @Test
    @DisplayName("Debe recuperar por ID (get)")
    void get_by_id_ok() {
        EstadoPublicacion fetched = estadoPublicacionDAO.get(estado1.getId());
        assertNotNull(fetched);
        assertEquals(EstadoPublicacionEnum.PERDIDO_AJENO, fetched.getEstado());
    }

    @Test
    @DisplayName("Debe devolver null si el ID (get) no existe")
    void get_by_id_not_found() {
        assertNull(estadoPublicacionDAO.get(9999L));
    }

    @Test
    @DisplayName("Debe recuperar todos los Estados (getAll)")
    void getAll_ok() {
        // Además de estado1 y estado2, podrían existir otros si la limpieza
        // en DAOBaseTest no incluye EstadoPublicacion todavía.
        // Por seguridad, contamos cuántos hay al principio del test.
        int countAntes = estadoPublicacionDAO.getAll().size(); // Deberían ser 2

        // Creamos uno más para asegurarnos que getAll funciona
        EstadoPublicacion estado3 = new EstadoPublicacion();
        estado3.setFecha(new Date()); estado3.setEstado(EstadoPublicacionEnum.ADOPTADO);
        estado3.setPublicacion(publicacionTest);
        estadoPublicacionDAO.persist(estado3);

        List<EstadoPublicacion> todos = estadoPublicacionDAO.getAll();
        assertEquals(countAntes + 1, todos.size(), "Debería encontrar todos los estados creados");
    }
}