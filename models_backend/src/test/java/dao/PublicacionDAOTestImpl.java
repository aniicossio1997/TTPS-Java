package dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import domain.models.Publicacion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import persistence.FactoryDAO;
import persistence.dao.PublicacionDAO;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.*;

public class PublicacionDAOTestImpl extends DAOBaseTest {

    private final PublicacionDAO publicacionDAO = FactoryDAO.getPublicacionDAO();

    @BeforeAll
    public static void setupPublicaciones() {
        cargarPublicacionesDesdeJson();
    }

    private static void cargarPublicacionesDesdeJson() {
        // Cargamos 20 publicaciones iniciales desde un json, para que no quede super largo el test
        Gson gson = new Gson();
        PublicacionDAO publicacionDAO = FactoryDAO.getPublicacionDAO();
        Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();

        try (InputStreamReader reader = new InputStreamReader(
                Objects.requireNonNull(PublicacionDAOTestImpl.class.getClassLoader().getResourceAsStream("publicaciones_test_data.json")))) {

            List<Map<String, Object>> dataList = gson.fromJson(reader, listType);

            for (Map<String, Object> data : dataList) {

                String nombre = (String) data.get("nombre");
                String especie = (String) data.get("especie");
                String raza = (String) data.get("raza");
                String color = (String) data.get("color");
                String tamanio = (String) data.get("tamanio");
                String descripcion = (String) data.get("descripcion");

                // Gson lee los números como Double por defecto, hay que convertir a int
                int diasAtras = ((Double) data.get("dias_atras")).intValue();

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH, -diasAtras);

                Publicacion publicacion = new Publicacion();
                publicacion.setNombre(nombre);
                publicacion.setDescripcion(descripcion);
                publicacion.setFecha(cal.getTime());
                publicacion.setColor(color);
                publicacion.setEspecie(especie);
                publicacion.setRaza(raza);
                publicacion.setTamanio(tamanio);

                publicacion.setUsuario(usuarioBase);
                publicacion.setUbicacion(ubicacionBase);

                publicacionDAO.persist(publicacion);
            }

        } catch (Exception e) {
            Assertions.fail("Error al cargar datos JSON de prueba: " + e.getMessage());
        }
    }

    @Test
    public void testPersist() {
        Publicacion publicacion = new Publicacion();
        publicacion.setNombre("Perdido - Cachorro Golden");
        publicacion.setDescripcion("Cachorro muy juguetón, se perdió cerca del parque.");
        publicacion.setFecha(new Date());
        publicacion.setColor("Dorado");
        publicacion.setEspecie("Perro");
        publicacion.setRaza("Golden Retriever");
        publicacion.setTamanio("Mediano");
        publicacion.setUsuario(usuarioBase);
        publicacion.setUbicacion(ubicacionBase);

        Publicacion persistedPublicacion = publicacionDAO.persist(publicacion);

        Assertions.assertNotNull(persistedPublicacion, "La publicación persistida no debería ser null.");
        Assertions.assertNotNull(persistedPublicacion.getId(), "El ID de la publicación debe ser generado.");
        Assertions.assertNotNull(persistedPublicacion.getEstados(), "El ID de la publicación debe ser generado.");
        Assertions.assertEquals("Perdido - Cachorro Golden", persistedPublicacion.getNombre(), "El nombre debe coincidir.");
    }

    @Test
    public void testGet() {
        Publicacion publicacion = new Publicacion();
        publicacion.setNombre("Perdido - Cachorro Dalmata");
        publicacion.setDescripcion("Cachorro muy juguetón, se perdió cerca del parque.");
        publicacion.setFecha(new Date());
        publicacion.setColor("Dorado");
        publicacion.setEspecie("Perro");
        publicacion.setRaza("Dalmata");
        publicacion.setTamanio("Pequeño");
        publicacion.setUsuario(usuarioBase);
        publicacion.setUbicacion(ubicacionBase);

        publicacionDAO.persist(publicacion);

        Publicacion retrievedPublicacion = publicacionDAO.get(publicacion.getId());

        Assertions.assertNotNull(retrievedPublicacion, "La publicación persistida no debería ser null.");
        Assertions.assertNotNull(retrievedPublicacion.getId(), "El ID de la publicación debe ser generado.");
        Assertions.assertNotNull(retrievedPublicacion.getEstados(), "El ID de la publicación debe ser generado.");
        Assertions.assertEquals("Perdido - Cachorro Dalmata", retrievedPublicacion.getNombre(), "El nombre debe coincidir.");
    }

    @Test
    public void testUpdate() {
        // Persistir una publicación base
        Publicacion publicacionOriginal = new Publicacion();
        publicacionOriginal.setNombre("Perdido - Lab");
        publicacionOriginal.setDescripcion("Descripción inicial");
        publicacionOriginal.setFecha(new Date());
        publicacionOriginal.setColor("Negro");
        publicacionOriginal.setEspecie("Perro");
        publicacionOriginal.setRaza("Labrador");
        publicacionOriginal.setTamanio("Grande");
        publicacionOriginal.setUsuario(usuarioBase);
        publicacionOriginal.setUbicacion(ubicacionBase);

        Publicacion persistedPublicacion = publicacionDAO.persist(publicacionOriginal);
        Long idOriginal = persistedPublicacion.getId();

        // Modificar la entidad y llamar a update
        String nuevoNombre = "ENCONTRADO - Lab";
        String nuevaDescripcion = "Hemos encontrado al perro. Actualizando descripción.";
        persistedPublicacion.setNombre(nuevoNombre);
        persistedPublicacion.setDescripcion(nuevaDescripcion);

        Publicacion updatedPublicacion = publicacionDAO.update(persistedPublicacion);

        // Verificar la actualización
        Assertions.assertNotNull(updatedPublicacion);
        Assertions.assertEquals(idOriginal, updatedPublicacion.getId());
        Assertions.assertEquals(nuevoNombre, updatedPublicacion.getNombre(), "El nombre debe ser actualizado.");
        Assertions.assertEquals(nuevaDescripcion, updatedPublicacion.getDescripcion(), "La descripción debe ser actualizada.");

        // Verificar que el cambio se mantiene al hacer un GET posterior
        Publicacion publicacionObtenida = publicacionDAO.get(idOriginal);
        Assertions.assertEquals(nuevoNombre, publicacionObtenida.getNombre(), "El GET debe retornar el nombre actualizado.");
    }

    @Test
    public void testDelete() {
        Publicacion publicacionAEliminar = new Publicacion();
        publicacionAEliminar.setNombre("Publicacion a eliminar");
        publicacionAEliminar.setFecha(new Date());
        publicacionAEliminar.setEspecie("Gato");
        publicacionAEliminar.setUsuario(usuarioBase);
        publicacionAEliminar.setUbicacion(ubicacionBase);

        Publicacion persistedPublicacion = publicacionDAO.persist(publicacionAEliminar);
        Long idAEliminar = persistedPublicacion.getId();

        publicacionDAO.delete(idAEliminar);

        Publicacion publicacionEliminada = publicacionDAO.get(idAEliminar);
        Assertions.assertNull(publicacionEliminada, "La publicación debe ser null después de la eliminación.");
    }

    @Test
    public void testBusquedaConFiltrosPorEspecie() {
        // 2. Buscar publicaciones de especie Gato
        List<Publicacion> resultados = publicacionDAO.getPublicacionesByCaracteristicas(
                null, "Gato", null, null, null, null, null, 0, 10);

        // 3. Verificar que solo se obtienen los 5 gatos
        Assertions.assertEquals(5, resultados.size());
    }

    @Test
    public void testFiltroPorDiasRecientes() {

        // Publicaciones recientes (días_atras: 1, 2) son: Labrador, Pitbull, Ovejero Alemán, Gato Negro.
        Calendar hoy = Calendar.getInstance();
        Calendar haceDosDias = Calendar.getInstance();
        haceDosDias.add(Calendar.DAY_OF_MONTH, -3); // Rango de 0 a 2 días

        List<Publicacion> resultados = publicacionDAO.getPublicacionesByCaracteristicas(
                null, null, null, null, null, // filtros de caracteristicas
                haceDosDias.getTime(),        // fechaDesde (hace 3 días)
                hoy.getTime(),                // fechaHasta (hoy)
                0, 10                         // paginación
        );

        // Deben ser 4: Labrador (1), Pitbull (2), Ovejero Alemán (1), Gato Negro (2).
        Assertions.assertEquals(4, resultados.size());
    }

    @Test
    public void testPaginacionOffsetConFiltro() {
        // Filtrar solo Perros (hay 5), y obtener el 3ro y 4to resultado.
        List<Publicacion> resultados = publicacionDAO.getPublicacionesByCaracteristicas(
                null,// nombre
                "Perro",        // especie (5 coincidencias)
                null, null, null,
                null, null,     // fechas
                2,              // offset: Salta los primeros 2 resultados
                2               // maxResults: Obtener 2 resultados
        );

        //El conteo debe ser 2 (los resultados 3 y 4)
        Assertions.assertEquals(2, resultados.size());

        // Verificar que realmente son perros (el filtro funcionó)
        Assertions.assertEquals("Perro", resultados.get(0).getEspecie());
        Assertions.assertEquals("Perro", resultados.get(1).getEspecie());
    }

    @Test
    public void testFiltroPorRazaInexistente() {
        List<Publicacion> resultados = publicacionDAO.getPublicacionesByCaracteristicas(
                null, null,
                "Doberman",     // raza inexistente
                null, null,
                null, null,     // fechas
                0, 10
        );

        Assertions.assertTrue(resultados.isEmpty());
    }

}