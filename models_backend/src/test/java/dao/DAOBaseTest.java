package dao;

import domain.enums.RolUsuarioEnum;
import domain.models.Ubicacion;
import domain.models.Usuario;
import org.junit.jupiter.api.BeforeAll;
import persistence.FactoryDAO;
import persistence.dao.UbicacionDAO;
import persistence.dao.UsuarioDAO;

public class DAOBaseTest {
    protected static Usuario usuarioBase;
    protected static Ubicacion ubicacionBase;

    @BeforeAll
    public static void setup() {
        UbicacionDAO ubiDAO = FactoryDAO.getUbicacionDAO();
        UsuarioDAO userDAO = FactoryDAO.getUsuarioDAO();

        ubicacionBase = new Ubicacion("1234", "Buenos Aires", "La Plata", "Barrio Norte", 12313D, 123213D);
        ubicacionBase = ubiDAO.persist(ubicacionBase);

        usuarioBase = new Usuario(
                "TestNombre",
                "TestApellido",
                "test_base_all_" + System.currentTimeMillis() + "@mail.com",
                "pass123", 10, 0, 0, RolUsuarioEnum.USUARIO_COMUN, ubicacionBase, null
        );
        usuarioBase = userDAO.persist(usuarioBase);
    }
}
