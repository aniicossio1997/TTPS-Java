package dao.AvistamientoDAOHibernateJPATest;

import domain.models.Avistamiento;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

// 1. HEREDA de la clase base
class AvistamientoDeleteTest extends BaseAvistamientoDAOTest {

    private Avistamiento avistamientoParaBorrar;

    @BeforeEach
    void setUpDelete() {
        // Creamos una entidad solo para borrarla
        avistamientoParaBorrar = new Avistamiento();
        avistamientoParaBorrar.setFecha(new Date());
        avistamientoParaBorrar.setDescripcion("Para Borrar");

        avistamientoParaBorrar.setPublicacion(pubTest1);
        avistamientoParaBorrar.setUbicacion(ubicacionTest);
        avistamientoParaBorrar.setUsuario(usuarioBase);
        avistamientoDAO.persist(avistamientoParaBorrar);
    }

    @Test
    @DisplayName("Debe borrar un Avistamiento existente")
    void delete_ok() {
        Long id = avistamientoParaBorrar.getId();
        avistamientoDAO.delete(id);
        assertNull(avistamientoDAO.get(id));
    }

    @Test
    @DisplayName("Debe fallar (EntityNotFoundException) si el ID no existe")
    void delete_falla_si_no_existe() {
        assertThrows(EntityNotFoundException.class, () -> {
            avistamientoDAO.delete(9999L);
        });
    }
}