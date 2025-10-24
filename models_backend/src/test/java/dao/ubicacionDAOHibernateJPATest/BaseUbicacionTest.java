package dao.ubicacionDAOHibernateJPATest;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import persistence.dao.*;
import persistence.impl.*;


public class BaseUbicacionTest {

    public static UbicacionDAO _ubicacionDAO;


    @BeforeEach
    void tearDown() {
        _ubicacionDAO= new UbicacionDAOHibernateJPA();
        // 1. LIMPIAMOS SOLO LOS AVISTAMIENTOS
        // Esto asegura que la tabla de avistamientos esté vacía
        // antes de que el @BeforeEach de la clase hija (setUpGet) se ejecute.

        try{
            UsuarioDAO usuarioDAO = new UsuarioDAOHibernateJPA();
            UbicacionDAO ubicacionDAO = new UbicacionDAOHibernateJPA();
            PublicacionDAO publicacionDAO = new PublicacionDAOHibernateJPA();
            AvistamientoDAO avistamientoDAO = new AvistamientoDAOHibernateJPA();
            FotoDAO fotoDAO = new FotoDAOHibernateJPA();
            MedallaDAO medallaDAO = new MedallaDAOHibernateJPA();

            // 1. Borramos Foto (los más dependientes)
            fotoDAO.getAll(null).forEach(e -> fotoDAO.delete(e.getId()));

            // (Añade Medalla aquí si la usas)
            medallaDAO.getAll(null).forEach(e -> medallaDAO.delete(e.getId()));

            // 2. Borramos Avistamiento
            avistamientoDAO.getAll(null).forEach(e -> avistamientoDAO.delete(e.getId()));

            // 3. Borramos Publicacion
            publicacionDAO.getAll(null).forEach(e -> publicacionDAO.delete(e.getId()));

            // 4. Borramos Usuario
            usuarioDAO.getAll(null).forEach(e -> usuarioDAO.delete(e.getId()));

            // 5. Borramos Ubicacion (al final)
            ubicacionDAO.getAll(null).forEach(e -> ubicacionDAO.delete(e.getId()));

            System.out.println("--- Base de datos limpiada ---");
        } catch (Exception e) {

        }

        try {

            _ubicacionDAO.getAll().stream().forEach(a -> _ubicacionDAO.delete(a.getId()));
        } catch (EntityNotFoundException e) {
            // Ignorar si ya estaba limpio
        }
    }

}
