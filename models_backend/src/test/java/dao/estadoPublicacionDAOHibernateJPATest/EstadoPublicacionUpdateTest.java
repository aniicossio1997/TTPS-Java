package dao.estadoPublicacionDAOHibernateJPATest;

import domain.enums.EstadoPublicacionEnum;
import domain.models.EstadoPublicacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class EstadoPublicacionUpdateTest extends BaseEstadoPublicacionDAOTest {

    private EstadoPublicacion estadoParaUpdate;

    @BeforeEach
    void setUpUpdate() {
        estadoParaUpdate = new EstadoPublicacion();
        estadoParaUpdate.setFecha(new Date());
        estadoParaUpdate.setEstado(EstadoPublicacionEnum.PERDIDO_PROPIO); // Estado inicial
        estadoParaUpdate.setPublicacion(publicacionTest);
        estadoPublicacionDAO.persist(estadoParaUpdate);
    }

    @Test
    @DisplayName("Debe actualizar un EstadoPublicacion correctamente")
    void update_ok() {
        // ACT
        estadoParaUpdate.setEstado(EstadoPublicacionEnum.RECUPERADO); // Cambiamos el estado
        estadoPublicacionDAO.update(estadoParaUpdate);

        // ASSERT
        EstadoPublicacion fetched = estadoPublicacionDAO.get(estadoParaUpdate.getId());
        assertEquals(EstadoPublicacionEnum.RECUPERADO, fetched.getEstado(), "El estado se actualizó");
        assertNotNull(fetched.getFecha(), "La fecha no debería ser null"); // Verificar otros campos si es necesario
    }
}
