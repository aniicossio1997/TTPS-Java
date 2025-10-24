package dao;

import domain.enums.RolUsuarioEnum;
import domain.models.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.FactoryDAO;
import persistence.dao.*;

import java.util.Date;
import java.util.List;

public class FotoDAOTestImpl {

    // Son estáticos porque solo se inicializan una vez
    protected static UsuarioDAO usuarioDAO;
    protected static UbicacionDAO ubicacionDAO;

    // Cada test tendrá su propia instancia de 'usuarioBase'.
    protected Usuario usuarioBase;
    protected Ubicacion ubicacionBase;

    /**
     * Se ejecuta UNA SOLA VEZ al principio.
     * Solo inicializa los DAOs.
     */
    @BeforeAll
    public static void setupDaos() {
        usuarioDAO = FactoryDAO.getUsuarioDAO();
        ubicacionDAO = FactoryDAO.getUbicacionDAO();

        try {
            UbicacionDAO ubicacionDAO = FactoryDAO.getUbicacionDAO();
            UsuarioDAO usuarioDAO = FactoryDAO.getUsuarioDAO();

            usuarioDAO.getAll(null).forEach(u -> usuarioDAO.delete(u.getId()));

            // 4. Borramos Ubicaciones (al final, cuando nadie las usa)
            ubicacionDAO.getAll(null).forEach(u -> ubicacionDAO.delete(u.getId()));

        } catch (EntityNotFoundException e) {
            // Ignorar si ya estaba limpio
        }
    }

    @BeforeEach
    public void setup() {
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

    private FotoDAO fotoDAO = FactoryDAO.getFotoDAO();
    private PublicacionDAO publicacionDAO = FactoryDAO.getPublicacionDAO();
    private AvistamientoDAO avistamientoDAO = FactoryDAO.getAvistamientoDAO();

    private Publicacion publicacionTest;
    private Avistamiento avistamientoTest;

    private static final byte[] FOTO_CONTENT = new byte[]{1, 2, 3, 4};
    private static final byte[] FOTO_CONTENT_USUARIO = new byte[]{5, 6, 7, 8};
    private static final byte[] FOTO_CONTENT_MODIFICADA = new byte[]{9, 9, 9, 9};

    @BeforeEach
    public void setupTests() {
        // Aseguramos que usuarioBase y ubicacionBase existan (gracias a DAOTestBase/@BeforeAll)

        // 1. Crear Publicacion base (necesita usuario y ubicacion)
        publicacionTest = new Publicacion();
        publicacionTest.setNombre("Test Pub");
        publicacionTest.setFecha(new Date());
        publicacionTest.setEspecie("Perro");
        publicacionTest.setUsuario(usuarioBase);
        publicacionTest.setUbicacion(ubicacionBase);
        publicacionTest = publicacionDAO.persist(publicacionTest);

        // 2. Crear Avistamiento base (necesita usuario, ubicacion y publicacion)
        avistamientoTest = new Avistamiento();
        avistamientoTest.setFecha(new Date());
        avistamientoTest.setUsuario(usuarioBase);
        avistamientoTest.setUbicacion(ubicacionBase);
        avistamientoTest.setPublicacion(publicacionTest);
        avistamientoTest = avistamientoDAO.persist(avistamientoTest);
    }

    @Test
    public void testPersist() {
        Foto foto = new Foto();
        foto.setNombre("TestFoto.jpg");
        foto.setContent(FOTO_CONTENT);
        foto.setPublicacion(publicacionTest);

        Foto persistedFoto = fotoDAO.persist(foto);

        Assertions.assertNotNull(persistedFoto);
        Assertions.assertNotNull(persistedFoto.getId());
        Assertions.assertEquals("TestFoto.jpg", persistedFoto.getNombre());
        Assertions.assertArrayEquals(FOTO_CONTENT, persistedFoto.getContent());
    }

    @Test
    public void testUpdate() {
        Foto fotoBase = new Foto();
        fotoBase.setNombre("Base_Foto.jpg");
        fotoBase.setContent(FOTO_CONTENT);
        fotoBase.setPublicacion(publicacionTest);
        fotoBase = fotoDAO.persist(fotoBase);

        Long idOriginal = fotoBase.getId();
        String nuevoNombre = "Foto_Editada.png";

        // Modificar la entidad
        fotoBase.setNombre(nuevoNombre);
        fotoBase.setContent(FOTO_CONTENT_MODIFICADA);

        Foto updatedFoto = fotoDAO.update(fotoBase);

        // Verificar la actualización
        Assertions.assertNotNull(updatedFoto);
        Assertions.assertEquals(idOriginal, updatedFoto.getId());
        Assertions.assertEquals(nuevoNombre, updatedFoto.getNombre(), "El nombre debe ser actualizado.");
        Assertions.assertArrayEquals(FOTO_CONTENT_MODIFICADA, updatedFoto.getContent(), "El contenido debe ser actualizado.");

        // Verificar que se haya modificado
        Foto fotoObtenida = fotoDAO.get(idOriginal);
        Assertions.assertEquals(nuevoNombre, fotoObtenida.getNombre(), "El GET debe retornar el nombre actualizado.");
    }

    @Test
    public void testDelete() {
        Foto fotoBase = new Foto();
        fotoBase.setNombre("Base_Foto.jpg");
        fotoBase.setContent(FOTO_CONTENT);
        fotoBase.setPublicacion(publicacionTest);
        fotoBase = fotoDAO.persist(fotoBase);

        // Usamos fotoBase (persistida en @BeforeEach)
        Long idAEliminar = fotoBase.getId();

        // Eliminar la entidad por ID
        fotoDAO.delete(idAEliminar);

        // Verificar que la entidad ya no existe
        Foto fotoEliminada = fotoDAO.get(idAEliminar);
        Assertions.assertNull(fotoEliminada, "La foto debe ser null después de la eliminación.");
    }

    @Test
    public void testGetByPublicacion() {
        Foto foto1 = new Foto();
        foto1.setNombre("Pub_1.jpg");
        foto1.setContent(FOTO_CONTENT);
        foto1.setPublicacion(publicacionTest);
        fotoDAO.persist(foto1);

        Foto foto2 = new Foto();
        foto2.setNombre("Pub_2.jpg");
        foto2.setContent(FOTO_CONTENT);
        foto2.setPublicacion(publicacionTest);
        fotoDAO.persist(foto2);

        List<Foto> fotos = fotoDAO.getFotosByPublicacion(publicacionTest.getId());

        Assertions.assertEquals(2, fotos.size());
        Assertions.assertTrue(fotos.stream().anyMatch(f -> f.getNombre().equals("Pub_1.jpg")));
    }

    @Test
    public void testGetByAvistamiento() {
        // Persistir una foto para el avistamiento de prueba
        Foto foto = new Foto();
        foto.setNombre("Avistamiento_1.jpg");
        foto.setContent(FOTO_CONTENT);
        foto.setAvistamiento(avistamientoTest);
        fotoDAO.persist(foto);

        List<Foto> fotos = fotoDAO.getFotosByAvistamiento(avistamientoTest.getId());

        Assertions.assertEquals(1, fotos.size());
        Assertions.assertEquals("Avistamiento_1.jpg", fotos.getFirst().getNombre());
    }

    @Test
    public void testGetByUsuario() {
        // Persistir una foto como foto de perfil para el usuarioBase
        Foto fotoPerfil = new Foto();
        fotoPerfil.setNombre("Perfil_User.jpg");
        fotoPerfil.setContent(FOTO_CONTENT_USUARIO);
        fotoPerfil.setUsuario(usuarioBase); // Asignación OneToOne
        fotoDAO.persist(fotoPerfil);

        Foto fotoObtenida = fotoDAO.getFotoByUsuario(usuarioBase.getId());

        Assertions.assertNotNull(fotoObtenida);
        Assertions.assertEquals("Perfil_User.jpg", fotoObtenida.getNombre());
        Assertions.assertArrayEquals(FOTO_CONTENT_USUARIO, fotoObtenida.getContent());
    }
}
