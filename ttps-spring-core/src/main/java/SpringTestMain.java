
import domain.models.Ubicacion;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import config.PersistenceConfig;

import javax.sql.DataSource;
import persistence.dao.UbicacionDAO; // <-- ¡Importa el DAO!

public class SpringTestMain {
	public static void main(String[] args) {
		 // Create a new AnnotationConfigApplicationContext
        /*AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        
        // Register the configuration class (PersistenceConfig in this case)
        ctx.register(PersistenceConfig.class);


        // Refresh the context to initialize Spring beans
        ctx.refresh();

     	
		DataSource dataSource = ctx.getBean(DataSource.class);
        System.out.println("DataSource cargado: " + dataSource);


        Ubicacion e = ctx.getBean("Ubicacion", Ubicacion.class);
		System.out.println("Bean cargado: "+e.hola());

        // Don't forget to close the context to free main.resources
        ctx.close();
		*/

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(PersistenceConfig.class);
        ctx.refresh();

        System.out.println("Contexto de Spring cargado...");

        // 1. Pide a Spring el BEAN del DAO (el @Repository)
        //    (No pidas un bean de "Ubicacion")
        UbicacionDAO ubicacionDAO = ctx.getBean(UbicacionDAO.class);
        System.out.println("DAO cargado: " + ubicacionDAO.getClass().getName());

        // 2. Crea un OBJETO de datos (un POJO, no un bean)
        Ubicacion nuevaUbicacion = new Ubicacion();
        nuevaUbicacion.setProvincia("Buenos Aires");
        nuevaUbicacion.setCiudad("La Plata");
        nuevaUbicacion.setBarrio("Casco Urbano");
        nuevaUbicacion.setLatitud(-34.921389);
        nuevaUbicacion.setLongitud(-57.954444);

        try {
            // 3. Usa el DAO para persistir el objeto.
            //    Como tu GenericDAO es @Transactional, Spring
            //    iniciará y cerrará la transacción por ti.
            Ubicacion ubicacionGuardada = ubicacionDAO.persist(nuevaUbicacion);

            System.out.println("--- ¡ÉXITO! ---");
            System.out.println("Se guardó la ubicación con ID: " + ubicacionGuardada.getId());
            System.out.println("Datos: " + ubicacionGuardada.getCiudad());

            // 4. (Opcional) Verifiquemos que se guardó
            Ubicacion recuperada = ubicacionDAO.get(ubicacionGuardada.getId());
            System.out.println("Recuperada de la BBDD: " + recuperada);

        } catch (Exception e) {
            System.err.println("Error al persistir la ubicación: " + e.getMessage());
            e.printStackTrace();
        }

        ctx.close();

	
	}

}
