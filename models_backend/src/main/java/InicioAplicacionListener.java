import domain.models.Ubicacion;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.*;
import persistence.FactoryDAO;
import persistence.dao.UbicacionDAO;

import java.util.List;

@WebListener // ¡Importante! Esto registra el listener automáticamente
public class InicioAplicacionListener implements ServletContextListener, HttpSessionListener, HttpSessionAttributeListener {

    public InicioAplicacionListener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        /* This method is called when the servlet context is initialized(when the Web application is deployed). */
        // (1) Obtenemos el Contexto de la Aplicación
        ServletContext context = sce.getServletContext();

        System.out.println("--- HOLA-INICIO V1---");
        // (2) Imprimimos en la consola (como pediste)
        System.out.println("--- Aplicación Iniciada: Cargando datos iniciales ---");

        try {
            // (3) Usamos tu FactoryDAO para obtener el DAO
                        UbicacionDAO ubicacionDAO = FactoryDAO.getUbicacionDAO();

            // (4) Obtenemos la lista de ubicaciones
                        List<Ubicacion> listaUbicaciones = ubicacionDAO.getAll();

            // (5) ¡Simplemente imprime la lista!
                        System.out.println("--- Listado de Ubicaciones Cargadas ANTES ---");
                        System.out.println(listaUbicaciones);
            System.out.println("----------------------------------------");

            //creamos una ubicacion nueva
/*            Ubicacion nuevaUbicacion = new  Ubicacion();
            nuevaUbicacion.setBarrio("Altos");
            nuevaUbicacion.setCiudad("Moreno");
            nuevaUbicacion.setIdExterno("ID-EXT-PRUEBA-001");
            nuevaUbicacion.setLatitud(-90.0);
            nuevaUbicacion.setLongitud(-90.0);
            nuevaUbicacion.setProvincia("San Luis");

            ubicacionDAO.persist(nuevaUbicacion);*/


            System.out.println("OK: " + listaUbicaciones.size() + " ubicaciones cargadas en el ServletContext.");

        } catch (Exception e) {
            System.err.println("ERROR al cargar la lista de ubicaciones: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("-----------------------------------------------------");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        /* This method is called when the servlet Context is undeployed or Application Server shuts down. */
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        /* Session is created. */
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        /* Session is destroyed. */
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is added to a session. */
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is removed from a session. */
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent sbe) {
        /* This method is called when an attribute is replaced in a session. */
    }
}