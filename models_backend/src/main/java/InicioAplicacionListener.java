import domain.models.Ubicacion;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import persistence.FactoryDAO;
import persistence.dao.UbicacionDAO;

import java.util.List;

public class InicioAplicacionListener implements ServletContextListener, HttpSessionListener, HttpSessionAttributeListener {

    public InicioAplicacionListener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        /* This method is called when the servlet context is initialized(when the Web application is deployed). */
        // (1) Obtenemos el Contexto de la Aplicación
        ServletContext context = sce.getServletContext();

        // (2) Imprimimos en la consola (como pediste)
        System.out.println("--- Aplicación Iniciada: Cargando datos iniciales ---");

        try {
            // (3) Usamos tu FactoryDAO para obtener el DAO
            UbicacionDAO ubicacionDAO = FactoryDAO.getUbicacionDAO(); // [cite: 266, 271]

            // (4) Obtenemos la lista de ubicaciones (ordenadas por provincia, por ejemplo)
            List<Ubicacion> listaUbicaciones = ubicacionDAO.getAll("provincia"); // [cite: 153]

            // (5) Guardamos la lista en el ServletContext
            // "listaDeUbicaciones" será la "llave" para recuperarla después
            context.setAttribute("listaDeUbicaciones", listaUbicaciones);

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