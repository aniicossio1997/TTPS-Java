package dao;

// --- Importaciones de Dominio ---
import domain.models.Medalla;
import domain.models.Ubicacion;
import domain.models.Usuario;
import domain.enums.MedallaEnum; // Enum real del usuario
import domain.enums.RolUsuarioEnum;

// --- Importaciones de JUnit ---
import org.junit.jupiter.api.*;

// --- Importaciones de Persistencia (Interfaces DAO) ---
import persistence.dao.MedallaDAO;
import persistence.dao.UbicacionDAO;
import persistence.dao.UsuarioDAO;
import persistence.FactoryDAO;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class MedallaDAOTestImpl  extends CleanBaseBD {



    // Datos de Prueb
    private Ubicacion ubicacionDePrueba;
    private Usuario usuarioDePrueba;



    @BeforeEach
    public void setUp() {
        super.setup();
        // Se crea la Ubicacion
        ubicacionDePrueba = new Ubicacion("123", "Buenos Aires", "La Plata", "Tolosa", 34.8833, 57.9667);
        ubicacionDAO.persist(ubicacionDePrueba);

        // Se crea el Usuario
        usuarioDePrueba = new Usuario("Nombre", "Test", "actualizar@mail.com", "123",10, 0, 0, RolUsuarioEnum.USUARIO_COMUN, ubicacionDePrueba, null);

        usuarioDAO.persist(usuarioDePrueba);
    }


    @Test
    @DisplayName("Persistir")
    void testPersist() {
        Medalla medalla = new Medalla();
        medalla.setTipo(MedallaEnum.RESCATISTA_NIVEL_1);
        medalla.setFechaAsignacion(new Date());
        medalla.setUsuario(usuarioDePrueba);
        medallaDAO.persist(medalla);

        assertNotNull(medalla.getId(), "El ID no debería ser nulo después de persistir");
        Medalla medallaRecuperada = medallaDAO.get(medalla.getId());
        assertNotNull(medallaRecuperada, "La medalla no se guardó correctamente en la BBDD");;
    }

    @Test
    @DisplayName("Recuperar una Medalla por ID")
    void testGet() {
        Date fecha = new Date();
        Medalla medalla = new Medalla();
        medalla.setTipo(MedallaEnum.HEROE_DEL_BARRIO);
        medalla.setFechaAsignacion(fecha);
        medalla.setUsuario(usuarioDePrueba);
        medallaDAO.persist(medalla);

        Medalla medallaRecuperada = medallaDAO.get(medalla.getId());

        assertNotNull(medallaRecuperada, "No se pudo recuperar la medalla de la BBDD");
        assertEquals(medalla.getId(), medallaRecuperada.getId());
        assertEquals(MedallaEnum.HEROE_DEL_BARRIO, medallaRecuperada.getTipo());
        assertEquals(fecha.getTime(), medallaRecuperada.getFechaAsignacion().getTime());
        assertEquals(usuarioDePrueba.getId(), medallaRecuperada.getUsuario().getId());
    }

    @Test
    @DisplayName("Actualizar una Medalla (Fecha Vencimiento)")
    void testUpdate() {
        Medalla medalla = new Medalla();
        medalla.setTipo(MedallaEnum.HEROE_DEL_BARRIO); // CORREGIDO
        medalla.setFechaAsignacion(new Date());
        medalla.setUsuario(usuarioDePrueba);
        assertNull(medalla.getFechaVencimiento());
        medallaDAO.persist(medalla);
        Long idMedalla = medalla.getId();

        Date fechaVencimiento = new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1));

        medalla.setFechaVencimiento(fechaVencimiento);
        medallaDAO.update(medalla);

        Medalla medallaActualizada = medallaDAO.get(idMedalla);
        assertNotNull(medallaActualizada.getFechaVencimiento());
        assertEquals(fechaVencimiento.getTime(), medallaActualizada.getFechaVencimiento().getTime());
    }

    @Test
    @DisplayName("Eliminar una Medalla")
    void testDelete() {
        Medalla medalla = new Medalla();
        medalla.setTipo(MedallaEnum.ANGEL_GUARDIAN); // CORREGIDO
        medalla.setFechaAsignacion(new Date());
        medalla.setUsuario(usuarioDePrueba);
        medallaDAO.persist(medalla);

        Long idMedalla = medalla.getId();
        assertNotNull(medallaDAO.get(idMedalla));

        medallaDAO.delete(idMedalla);

        Medalla medallaBorrada = medallaDAO.get(idMedalla);
        assertNull(medallaBorrada);
    }

    @Test
    @DisplayName("Obtener todas las Medallas")
    void testGetAll() {
        Medalla m1 = new Medalla();
        m1.setTipo(MedallaEnum.RESCATISTA_NIVEL_1); // CORREGIDO
        m1.setFechaAsignacion(new Date());
        m1.setUsuario(usuarioDePrueba);
        medallaDAO.persist(m1);

        Medalla m2 = new Medalla();
        m2.setTipo(MedallaEnum.NUEVO_TUTOR); // CORREGIDO
        m2.setFechaAsignacion(new Date());
        m2.setUsuario(usuarioDePrueba);
        medallaDAO.persist(m2);

        List<Medalla> medallas = medallaDAO.getAll();

        assertNotNull(medallas);
        assertEquals(2, medallas.size());
    }

    @AfterEach
    public void tearDown() {
        // Limpieza en orden inverso de FK
        try {
            List<Medalla> medallas = medallaDAO.getAll();
            for (Medalla m : medallas) medallaDAO.delete(m.getId());
        } catch (Exception e) {
            System.err.println("Error limpiando medallas: " + e.getMessage());
        }

        try {
            List<Usuario> usuarios = usuarioDAO.getAll();
            for (Usuario u : usuarios) usuarioDAO.delete(u.getId());
        } catch (Exception e) {
            System.err.println("Error limpiando usuarios: " + e.getMessage());
        }

        try {
            List<Ubicacion> ubicaciones = ubicacionDAO.getAll();
            for (Ubicacion ub : ubicaciones) ubicacionDAO.delete(ub.getId());
        } catch (Exception e) {
            System.err.println("Error limpiando ubicaciones: " + e.getMessage());
        }
    }


}