package dao.avistamientoDAOHibernateJPATest;

import domain.models.Avistamiento;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

// 1. HEREDA de la clase base
class AvistamientoUpdateTest extends BaseAvistamientoDAOTest {

    private Avistamiento avistamientoParaUpdate;

    @Test
    @DisplayName("Debe actualizar un Avistamiento correctamente")
    void update_ok() {
        // ACT
        // Creamos un avistamiento que este test va a modificar
        String descripcionOriginal = "Descripción Original";
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -2); // hace dos días
        Date fechaOriginal = cal.getTime();

        avistamientoParaUpdate = new Avistamiento();
        avistamientoParaUpdate.setFecha(fechaOriginal);
        avistamientoParaUpdate.setDescripcion(descripcionOriginal);
        avistamientoParaUpdate.setPublicacion(pubTest1);
        avistamientoParaUpdate.setUbicacion(ubicacionTest);
        avistamientoParaUpdate.setUsuario(usuarioBase);
        avistamientoDAO.persist(avistamientoParaUpdate);

        // Modificamos la descripción
        String nuevaDescripcion = "Descripción Modificada";
        Date fechaModificada = new Date();
        avistamientoParaUpdate.setDescripcion(nuevaDescripcion);
        avistamientoParaUpdate.setFecha(fechaModificada);
        avistamientoDAO.update(avistamientoParaUpdate);

        // ASSERT
        Avistamiento fetched = avistamientoDAO.get(avistamientoParaUpdate.getId());

        assertNotEquals(descripcionOriginal, fetched.getDescripcion());
        assertNotEquals(fechaOriginal, fetched.getFecha());

        assertEquals(nuevaDescripcion, fetched.getDescripcion());
        assertEquals(fechaModificada, fetched.getFecha());


    }
}