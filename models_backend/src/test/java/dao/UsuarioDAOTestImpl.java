package dao;

import domain.enums.MedallaEnum;
import domain.models.Medalla;
import domain.models.Ubicacion;
import domain.models.Usuario;
import domain.enums.RolUsuarioEnum;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;

import persistence.dao.*;
import persistence.FactoryDAO;
import persistence.impl.*;

import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioDAOTestImpl {

    // DAOs
    private static UsuarioDAO usuarioDAO;
    private static UbicacionDAO ubicacionDAO;

    // Ubicacion de prueba
    private Ubicacion ubicacionDePrueba;

    @BeforeAll
    public static void setUpClass() {
        // Se usa FactoryDAO para obtener las instancias
        usuarioDAO = FactoryDAO.getUsuarioDAO();
        ubicacionDAO = FactoryDAO.getUbicacionDAO();
    }

    @BeforeEach
    public void setUp() {

        // Antes de CADA test, se crea una Ubicacion .
        ubicacionDePrueba = new Ubicacion("123", "Buenos Aires", "La Plata", "Tolosa", 34.8833, 57.9667);
        ubicacionDAO.persist(ubicacionDePrueba);
    }

    @Test
    @DisplayName("Persistir")
    void testPersist() {
        Usuario nuevoUsuario = new Usuario("nuevo", "Test", "test1@mail.com", "123",10, 0, 0, RolUsuarioEnum.USUARIO_COMUN, ubicacionDePrueba, null);
        usuarioDAO.persist(nuevoUsuario);

        assertNotNull(nuevoUsuario.getId(), "El ID no debería ser nulo después de persistir");
        Usuario usuarioRecuperado = usuarioDAO.get(nuevoUsuario.getId());
        assertNotNull(usuarioRecuperado, "No se pudo recuperar el usuario de la BBDD");
    }

    @Test
    @DisplayName("Recuperar un Usuario por ID")
    void testGet() {
        Usuario nuevoUsuario = new Usuario("nuevo", "Test", "test@mail.com", "123",10, 0, 0, RolUsuarioEnum.USUARIO_COMUN, ubicacionDePrueba, null);
        usuarioDAO.persist(nuevoUsuario);

        Usuario usuario = usuarioDAO.get(nuevoUsuario.getId());

        assertNotNull(usuario, "No se pudo recuperar el usuario de la BBDD");
        assertEquals(nuevoUsuario.getId(), usuario.getId());
        assertEquals(nuevoUsuario.getNombre(), usuario.getNombre());
        assertEquals(nuevoUsuario.getEmail(), usuario.getEmail());
        assertEquals(nuevoUsuario.getUbicacion().getId(), usuario.getUbicacion().getId());
    }

    @Test
    @DisplayName("Actualizar un Usuario")
    void testUpdate() {
        Usuario usuario = new Usuario("Nombre", "Test", "actualizar@mail.com", "123",10, 0, 0, RolUsuarioEnum.USUARIO_COMUN, ubicacionDePrueba, null);

        usuarioDAO.persist(usuario);

        Long idUsuario = usuario.getId();

        usuario.setNombre("NombreActualizado");
        usuarioDAO.update(usuario);

        Usuario usuarioActualizado = usuarioDAO.get(idUsuario);
        assertNotNull(usuarioActualizado);
        assertEquals("NombreActualizado", usuarioActualizado.getNombre(), "El nombre no se actualizó correctamente");
    }

    @Test
    @DisplayName("Eliminar un Usuario")
    void testDelete() {
        Usuario usuario = new Usuario("Borrar", "Test", "borrar@mail.com", "123",10, 0, 0, RolUsuarioEnum.USUARIO_COMUN, ubicacionDePrueba, null);
        usuarioDAO.persist(usuario);

        Long idUsuario = usuario.getId();
        assertNotNull(usuarioDAO.get(idUsuario), "El usuario debería existir antes de ser borrado");

        usuarioDAO.delete(usuario.getId());

        Usuario usuarioBorrado = usuarioDAO.get(idUsuario);
        assertNull(usuarioBorrado, "El usuario no debería existir después de ser borrado");
    }

    @Test
    @DisplayName("Recuperar todos los Usuarios")
    void testGetAll() {

        Usuario u1 = new Usuario("User", "Uno", "user1@mail.com", "123",10, 0, 0, RolUsuarioEnum.USUARIO_COMUN, ubicacionDePrueba, null);
        usuarioDAO.persist(u1);

        Usuario u2 = new Usuario("User", "Dos", "user2@mail.com", "456",10, 0, 0, RolUsuarioEnum.ADMINISTRADOR, ubicacionDePrueba, null);
        usuarioDAO.persist(u2);

        List<Usuario> usuarios = usuarioDAO.getAll();

        assertNotNull(usuarios, "La lista no debe ser nula");
        assertEquals(2, usuarios.size(), "Debe haber exactamente 2 usuarios en la lista");
    }

    @Test
    @DisplayName("Testear restricción de Email Único")
    void testEmailUnico() {
        String emailRepetido = "repetido@dominio.com";

        Usuario u1 = new Usuario("User", "Uno", emailRepetido, "123",10, 0, 0, RolUsuarioEnum.USUARIO_COMUN, ubicacionDePrueba, null);
        usuarioDAO.persist(u1);

        Usuario u2 = new Usuario("User", "Dos", emailRepetido, "456",10, 0, 0, RolUsuarioEnum.ADMINISTRADOR, ubicacionDePrueba, null);

        assertThrows(org.hibernate.exception.ConstraintViolationException.class, () -> {
            usuarioDAO.persist(u2);
        }, "Debería lanzarse una excepción de constraint (email único)");
    }

    @Test
    @DisplayName("Recuperar un Usuario por Email")
    void testFindByEmail() {
        String emailBusqueda = "usuario.buscado@mail.com";

        Usuario usuario = new Usuario("Usuario", "Buscado", emailBusqueda, "pass123",10, 0, 0, RolUsuarioEnum.USUARIO_COMUN, ubicacionDePrueba, null);
        usuarioDAO.persist(usuario);

        Long idUsuario = usuario.getId(); // Guardamos el ID para comparar

        Usuario usuarioEncontrado = usuarioDAO.findByEmail(emailBusqueda);

        assertNotNull(usuarioEncontrado, "El usuario debería haberse encontrado");
        assertEquals(emailBusqueda, usuarioEncontrado.getEmail(), "El email no coincide");
        assertEquals(idUsuario, usuarioEncontrado.getId(), "El ID no coincide");
    }

    @AfterEach
    public void tearDown() {
        // Después de CADA test, se limpia la BBDD.

        // Se borran todos los Usuarios (para evitar fallos de Foreign Key)
        try {
            List<Usuario> usuarios = usuarioDAO.getAll();
            for (Usuario u : usuarios) {
                usuarioDAO.delete(u.getId());
            }
        } catch (Exception e) {
            System.err.println("Error limpiando usuarios: " + e.getMessage());
        }

        // Se borra la Ubicacion
        try {
            ubicacionDAO.delete(ubicacionDePrueba.getId());
        } catch (Exception e) {
            System.err.println("Error limpiando ubicación: " + e.getMessage());
        }
    }


}