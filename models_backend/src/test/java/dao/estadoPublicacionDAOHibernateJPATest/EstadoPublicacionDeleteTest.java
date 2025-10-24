package dao.estadoPublicacionDAOHibernateJPATest;

import domain.enums.EstadoPublicacionEnum;
import domain.models.EstadoPublicacion;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class EstadoPublicacionDeleteTest extends BaseEstadoPublicacionDAOTest {

    private EstadoPublicacion estadoParaBorrar;

    @BeforeEach
    void setUpDelete() {
        super.setUpBaseEstadoPub();
        estadoParaBorrar = new EstadoPublicacion();
        estadoParaBorrar.setFecha(new Date());
        estadoParaBorrar.setEstado(EstadoPublicacionEnum.ADOPTADO);
        estadoParaBorrar.setPublicacion(publicacionTest);
        estadoPublicacionDAO.persist(estadoParaBorrar);
    }

    @Test
    @DisplayName("Debe borrar un EstadoPublicacion existente")
    void delete_ok() {
        Long id = estadoParaBorrar.getId();
        estadoPublicacionDAO.delete(id);
        assertNull(estadoPublicacionDAO.get(id), "El estado no fue borrado");
    }

    @Test
    @DisplayName("Debe fallar (EntityNotFoundException) si el ID no existe")
    void delete_falla_si_no_existe() {
        assertThrows(EntityNotFoundException.class, () -> {
            estadoPublicacionDAO.delete(9999L);
        }, "Borrar un ID inexistente debería lanzar EntityNotFoundException");
    }
}