package dao.ubicacionDAOHibernateJPATest;

import domain.models.Ubicacion;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*; // Importaciones de JUnit 5

import persistence.impl.UbicacionDAOHibernateJPA;

import static org.junit.jupiter.api.Assertions.*;

class UbicacionDeleteTest extends  BaseUbicacionTest {

    private Ubicacion ubicacionDePrueba; // Entidad de prueba

    @BeforeAll
    public static void setUpAll() {
        // 1. Creamos el DAO una sola vez
        _ubicacionDAO = new UbicacionDAOHibernateJPA();
    }

    @BeforeEach
    public void setUp() {
        // 2. ANTES de cada test, creamos y persistimos una ubicación
        ubicacionDePrueba = new Ubicacion();
        ubicacionDePrueba.setLatitud(-32.89);
        ubicacionDePrueba.setLongitud(-68.85);
        _ubicacionDAO.persist(ubicacionDePrueba);

    }


    // --- Tests ---

    @Test
    @DisplayName("Debe eliminar una ubicación existente por ID")
    void delete_by_id_ok() {
        // ARRANGE
        // @BeforeEach ya creó 'ubicacionDePrueba'
        Long id = ubicacionDePrueba.getId();

        // ACT
        _ubicacionDAO.delete(id);

        // ASSERT
        // Verificamos que ya no existe en la BD
        assertNull(_ubicacionDAO.get(id), "La ubicación no fue eliminada");
    }


    @Test
    @DisplayName("Debe lanzar EntityNotFoundException al borrar un ID inexistente")
    void delete_by_id_not_found_throws_exception() {
        // ARRANGE
        Long idInexistente = 99999L; // Un ID que sabemos que no existe
        Long idExistente = ubicacionDePrueba.getId(); // El ID que sí existe

        // ACT & ASSERT
        // Verificamos que la llamada a delete() lance la excepción
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class, // 1. La excepción que esperamos
                () -> {
                    _ubicacionDAO.delete(idInexistente); // 2. El código que debe fallar
                },
                "Debería lanzar EntityNotFoundException si el ID no existe"
        );

        // Verificar el mensaje de la excepción
        String mensaje = exception.getMessage();
        assertTrue(mensaje.contains("No se encontró la entidad Ubicacion"));
        assertTrue(mensaje.contains("9999"));

        // Verificar que la entidad original sigue en la BD
        assertNotNull(_ubicacionDAO.get(idExistente), "El borrado fallido eliminó otra entidad");
    }

}