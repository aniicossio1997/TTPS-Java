package dao.AvistamientoDAOHibernateJPATest;

import dao.DAOBaseTest;
import domain.models.Publicacion;
import domain.models.Ubicacion;
import domain.models.Usuario;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import persistence.EMF;
import persistence.dao.AvistamientoDAO;
import persistence.dao.PublicacionDAO;
import persistence.dao.UbicacionDAO;
import persistence.dao.UsuarioDAO;
import persistence.impl.AvistamientoDAOHibernateJPA;
import persistence.impl.PublicacionDAOHibernateJPA;
import persistence.impl.UbicacionDAOHibernateJPA;
import persistence.impl.UsuarioDAOHibernateJPA;

import java.util.Date;

/**
 * CLASE BASE ABSTRACTA para los tests de Avistamiento.
 * Se encarga de la configuración y limpieza de la BD
 * y de crear las dependENCIAS (Usuario, Ubicacion, Publicacion).
 * Los tests específicos (Create, Get, Update, Delete) HEREDARÁN de esta clase.
 */
public class BaseAvistamientoDAOTest extends DAOBaseTest {

    // --- DAOs ---
    // Son 'protected' para que las clases hijas puedan usarlos
    protected static AvistamientoDAO avistamientoDAO ;
    protected static PublicacionDAO publicacionDAO;
    protected static UbicacionDAO ubicacionDAO;

    // --- Entidades Base ---
    // Entidades de prueba que se crean antes de cada test
    protected Ubicacion ubicacionTest;
    protected Publicacion pubTest1;
    protected Publicacion pubTest2; // Para pruebas de getByIdPublicacion

    @BeforeAll
    static void setUpAll() {
        // 1. Inicializamos los DAOs una sola vez

        avistamientoDAO = new AvistamientoDAOHibernateJPA();
        publicacionDAO = new PublicacionDAOHibernateJPA();
        ubicacionDAO = new UbicacionDAOHibernateJPA();


    }


    /**
     * Se ejecuta ANTES de CADA test en las clases hijas.
     * Limpia la BD y crea el "mundo base" de dependencias.
     */
    @BeforeEach
    void setUpBase() {

        // 3. CREACIÓN DEL MUNDO BASE
        ubicacionTest = new Ubicacion();
        ubicacionTest.setLatitud(-34.0);
        ubicacionTest.setLongitud(-58.0);
        ubicacionTest.setIdExterno("id-ext-test-001");
        ubicacionTest.setProvincia("Buenos Aires");
        ubicacionDAO.persist(ubicacionTest);



        pubTest1 = new Publicacion();
        // ASUNCIÓN: Rellena los campos obligatorios de Publicacion
        pubTest1.setNombre("Perdido - Cachorro Dalmata");
        pubTest1.setDescripcion("Cachorro muy juguetón, se perdió cerca del parque.");
        pubTest1.setFecha(new Date());
        pubTest1.setColor("Dorado");
        pubTest1.setEspecie("Perro");
        pubTest1.setRaza("Dalmata");
        pubTest1.setTamanio("Pequeño");

        pubTest1.setUbicacion(ubicacionTest);
        pubTest1.setUsuario(usuarioBase);
        publicacionDAO.persist(pubTest1);

        pubTest2 = new Publicacion();
        // ASUNCIÓN: Rellena los campos obligatorios de Publicacion
        pubTest2.setNombre("Perdido - Cachorro Dalmata");
        pubTest2.setDescripcion("Cachorro muy juguetón, se perdió cerca del parque.");
        pubTest2.setFecha(new Date());
        pubTest2.setColor("Dorado");
        pubTest2.setEspecie("Perro");
        pubTest2.setRaza("Dalmata");
        pubTest2.setTamanio("Pequeño");

        pubTest2.setUbicacion(ubicacionTest);
        pubTest2.setUsuario(usuarioBase);
        publicacionDAO.persist(pubTest2);


    }


    @AfterEach
    void tearDown() {
        // 1. LIMPIAMOS SOLO LOS AVISTAMIENTOS
        // Esto asegura que la tabla de avistamientos esté vacía
        // antes de que el @BeforeEach de la clase hija (setUpGet) se ejecute.
        try {
            avistamientoDAO.getAll(null).forEach(a -> avistamientoDAO.delete(a.getId()));
        } catch (EntityNotFoundException e) {
            // Ignorar si ya estaba limpio
        }
    }
}
