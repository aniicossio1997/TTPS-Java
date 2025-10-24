package dao;

import domain.enums.RolUsuarioEnum;
import domain.models.Publicacion;
import domain.models.Ubicacion;
import domain.models.Usuario;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import persistence.FactoryDAO;
import persistence.dao.*;

import java.util.List;

public class DAOBaseTest {

    // Son estáticos porque solo se inicializan una vez
    protected static UsuarioDAO usuarioDAO;
    protected static UbicacionDAO ubicacionDAO;

    // Cada test tendrá su propia instancia de 'usuarioBase'.
    protected Usuario usuarioBase;
    protected Ubicacion ubicacionBase;


    protected static PublicacionDAO publicacionDAO;
    protected static AvistamientoDAO avistamientoDAO;
    protected static FotoDAO fotoDAO;

    /**
     * Se ejecuta UNA SOLA VEZ al principio.
     * Solo inicializa los DAOs.
     */
    @BeforeAll
    public static void setupDaos() {
        usuarioDAO = FactoryDAO.getUsuarioDAO();
        ubicacionDAO = FactoryDAO.getUbicacionDAO();


        publicacionDAO = FactoryDAO.getPublicacionDAO();
        avistamientoDAO = FactoryDAO.getAvistamientoDAO();
        fotoDAO = FactoryDAO.getFotoDAO();
    }

    @BeforeEach
    public void setup() {
        System.out.println("--- Limpiando la base de datos al final de todos los tests ---");
        try {
            // 1. Borramos Foto (los más dependientes)
            fotoDAO.getAll(null).forEach(e -> fotoDAO.delete(e.getId()));

            // (Añade Medalla aquí si la usas)
            // medallaDAO.getAll(null).forEach(e -> medallaDAO.delete(e.getId()));

            // 2. Borramos Avistamiento
            avistamientoDAO.getAll(null).forEach(e -> avistamientoDAO.delete(e.getId()));

            // 3. Borramos Publicacion
            publicacionDAO.getAll(null).forEach(e -> publicacionDAO.delete(e.getId()));

            // 4. Borramos Usuario
            usuarioDAO.getAll(null).forEach(e -> usuarioDAO.delete(e.getId()));

            // 5. Borramos Ubicacion (al final)
            ubicacionDAO.getAll(null).forEach(e -> ubicacionDAO.delete(e.getId()));

            System.out.println("--- Base de datos limpiada ---");

        } catch (EntityNotFoundException e) {
            // Ignorar si alguna entidad ya fue borrada
            System.err.println("Advertencia durante la limpieza final: " + e.getMessage());
        } catch (Exception e) {
            // Captura cualquier otro error durante la limpieza
            System.err.println("ERROR durante la limpieza final de la base de datos:");
            e.printStackTrace();
        } finally {
            // --- CERRAMOS LA CONEXIÓN (SIEMPRE) ---

        }


        ubicacionBase = new Ubicacion("1234", "Buenos Aires", "La Plata", "Barrio Norte", 12313D, 123213D);
        ubicacionBase = ubicacionDAO.persist(ubicacionBase);

        usuarioBase = new Usuario(
                "TestNombre",
                "TestApellido",
                "test_base_all_" + System.currentTimeMillis() + "@mail.com",
                "pass123", 10, 0, 0, RolUsuarioEnum.USUARIO_COMUN, ubicacionBase, null
        );
        usuarioBase = usuarioDAO.persist(usuarioBase);
    }



}
