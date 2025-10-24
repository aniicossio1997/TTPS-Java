package dao.estadoPublicacionDAOHibernateJPATest;

import dao.DAOBaseTest;
import domain.models.Publicacion;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import persistence.FactoryDAO;
import persistence.dao.EstadoPublicacionDAO; // DAO a probar
import persistence.dao.PublicacionDAO;       // DAO de dependencia

import java.util.Date;

/**
 * Clase Base para los tests de EstadoPublicacionDAO.
 * Hereda de DAOBaseTest para limpieza y datos base.
 * Prepara una Publicacion fresca antes de cada test.
 */
public abstract class BaseEstadoPublicacionDAOTest extends DAOBaseTest {

    // DAOs necesarios
    protected static EstadoPublicacionDAO estadoPublicacionDAO;
    protected static PublicacionDAO publicacionDAO; // Necesario para crear la dependencia

    // Entidad de prueba (la dependencia)
    protected Publicacion publicacionTest; // Publicacion a la que se asociarán los estados

    @BeforeAll
    static void setUpAllEstadoPub() {
        // Inicializamos los DAOs específicos una sola vez
        estadoPublicacionDAO = FactoryDAO.getEstadoPublicacionDAO(); // Asumo que tienes este método en FactoryDAO
        publicacionDAO = FactoryDAO.getPublicacionDAO();
        // Los DAOs de DAOBaseTest (usuarioDAO, ubicacionDAO, etc.) ya están inicializados
    }

    /**
     * Se ejecuta DESPUÉS del @BeforeEach de DAOBaseTest.
     * Crea una Publicacion fresca para asociar los EstadoPublicacion.
     * 'usuarioBase' y 'ubicacionBase' ya existen.
     */
    @BeforeEach
    void setUpBaseEstadoPub() {
        // Creamos la Publicacion necesaria como dependencia
        super.setup();
        publicacionTest = new Publicacion();
        publicacionTest.setNombre("Test Pub");
        publicacionTest.setFecha(new Date());
        publicacionTest.setEspecie("Perro");
        publicacionTest.setUsuario(usuarioBase);
        publicacionTest.setUbicacion(ubicacionBase);
        publicacionTest = publicacionDAO.persist(publicacionTest);
    }

    // No necesitas @AfterEach aquí. La limpieza general la hace DAOBaseTest.@BeforeEach
    // No necesitas @AfterAll aquí. El cierre lo hace DAOBaseTest.@AfterAll
}