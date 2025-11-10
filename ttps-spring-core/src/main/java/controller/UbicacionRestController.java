package controller;


import domain.models.Ubicacion;
import domain.models.Usuario;
import services.UbicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar las Ubicaciones.
 * Expone los endpoints de la API para el CRUD y búsquedas.
 */
@RestController
@RequestMapping("/api/ubicaciones") // <-- Asegúrate de que tenga la barra inicial
public class UbicacionRestController extends BaseController {

    @Autowired
    private UbicacionService ubicacionService; // Inyecta la capa de servicio

    // --- Endpoint 1: GET /api/ubicaciones (Obtener todas) ---
    @GetMapping
    public ResponseEntity<List<Ubicacion>> getAllUbicaciones() {
        List<Ubicacion> ubicaciones = ubicacionService.getAllUbicaciones();
        if (ubicaciones.isEmpty()) {
            // Si no hay ubicaciones, devuelve 204 No Content 
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        
        return new ResponseEntity<>(ubicaciones, HttpStatus.OK);
    }

    // --- Endpoint 2: GET /api/ubicaciones/{id} (Obtener una por ID) ---
    @GetMapping("/{id}")
    public ResponseEntity<Ubicacion> getUbicacionById(@PathVariable("id") Long id) {
        Ubicacion ubicacion = ubicacionService.getUbicacion(id);
        if (ubicacion == null) {
            // Si no se encuentra, devuelve 404 Not Found 
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ubicacion, HttpStatus.OK);
    }

    // --- Endpoint 3: POST /api/ubicaciones (Crear una nueva) ---
    @PostMapping
    public ResponseEntity<Ubicacion> createUbicacion(@RequestBody Ubicacion ubicacion) { 
        try {
            Ubicacion nuevaUbicacion = ubicacionService.crearUbicacion(ubicacion);
            // Devuelve 201 Created y la ubicación creada 
            return new ResponseEntity<>(nuevaUbicacion, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Si falla la validación del servicio (ej. lat/lon nulas)
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // --- Endpoint 4: PUT /api/ubicaciones/{id} (Actualizar una existente) ---
    @PutMapping("/{id}")
    public ResponseEntity<Ubicacion> updateUbicacion(@PathVariable("id") Long id, @RequestBody Ubicacion ubicacion) { 
        Ubicacion ubicacionExistente = ubicacionService.getUbicacion(id);
        if (ubicacionExistente == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            Ubicacion ubicacionActualizada = ubicacionService.actualizarUbicacion(ubicacion);
            return new ResponseEntity<>(ubicacionActualizada, HttpStatus.OK); 
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // --- Endpoint 5: DELETE /api/ubicaciones/{id} (Eliminar una por ID) ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUbicacion(@PathVariable("id") Long id) { 
        Ubicacion ubicacion = ubicacionService.getUbicacion(id);
        if (ubicacion == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ubicacionService.eliminarUbicacion(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // --- Endpoint 6: GET /api/ubicaciones/buscar (Búsqueda por criterios) ---
    @GetMapping("/buscar")
    public ResponseEntity<List<Ubicacion>> buscarPorCriterios(
            @RequestParam(required = false) String idExterno,
            @RequestParam(required = false) String provincia,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) String barrio) {

        List<Ubicacion> ubicaciones = ubicacionService.buscarPorCriterio(idExterno, provincia, ciudad, barrio);

        if (ubicaciones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(ubicaciones, HttpStatus.OK);
    }
}