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

public class DAOBaseTest extends CleanBaseBD{


    /**
     * Se ejecuta UNA SOLA VEZ al principio.
     * Solo inicializa los DAOs.
     */


    @BeforeEach
    public void setup() {
        System.out.println("--- Limpiando la base de datos al final de todos los tests ---");
        super.setup();

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
