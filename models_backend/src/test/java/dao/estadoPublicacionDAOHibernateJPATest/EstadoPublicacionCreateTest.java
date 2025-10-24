package dao.estadoPublicacionDAOHibernateJPATest;


import domain.enums.EstadoPublicacionEnum;
import domain.models.EstadoPublicacion;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class EstadoPublicacionCreateTest extends BaseEstadoPublicacionDAOTest {

    @Test
    @DisplayName("Debe persistir un EstadoPublicacion correctamente")
    void persist_ok() {
        // ARRANGE
        EstadoPublicacion estado = new EstadoPublicacion();
        estado.setFecha(new Date());
        estado.setEstado(EstadoPublicacionEnum.PERDIDO_PROPIO);
        estado.setPublicacion(publicacionTest); // Usa la Publicacion de la clase base

        // ACT
        estadoPublicacionDAO.persist(estado);

        // ASSERT
        assertNotNull(estado.getId(), "El ID no debe ser nulo");
        EstadoPublicacion fetched = estadoPublicacionDAO.get(estado.getId());
        assertEquals(EstadoPublicacionEnum.PERDIDO_PROPIO, fetched.getEstado());
    }

    @Test
    @DisplayName("Debe fallar (PersistenceException) si falta la Publicacion")
    void persist_falla_sin_publicacion() {
        // ARRANGE
        EstadoPublicacion estado = new EstadoPublicacion();
        estado.setFecha(new Date());
        estado.setEstado(EstadoPublicacionEnum.RECUPERADO);
        // Falta estado.setPublicacion(publicacionTest);

        // ACT & ASSERT
        assertThrows(PersistenceException.class, () -> {
            estadoPublicacionDAO.persist(estado);
        }, "Debería fallar por FK null en publicacion_id");
    }

    @Test
    @DisplayName("Debe fallar (PersistenceException) si falta Fecha o Estado")
    void persist_falla_sin_fecha_o_estado() {
        // Sin Fecha
        EstadoPublicacion sinFecha = new EstadoPublicacion();
        sinFecha.setEstado(EstadoPublicacionEnum.ADOPTADO);
        sinFecha.setPublicacion(publicacionTest);
        assertThrows(PersistenceException.class, () -> {
            estadoPublicacionDAO.persist(sinFecha);
        }, "Debería fallar por fecha null");

        // Sin Estado
        EstadoPublicacion sinEstado = new EstadoPublicacion();
        sinEstado.setFecha(new Date());
        sinEstado.setPublicacion(publicacionTest);
        assertThrows(PersistenceException.class, () -> {
            estadoPublicacionDAO.persist(sinEstado);
        }, "Debería fallar por estado null");
    }
}