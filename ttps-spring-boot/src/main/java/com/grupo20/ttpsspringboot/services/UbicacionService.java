package com.grupo20.ttpsspringboot.services;

import com.grupo20.ttpsspringboot.domain.models.Ubicacion;
import com.grupo20.ttpsspringboot.dtos.UbicacionCreateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.grupo20.ttpsspringboot.persistence.dao.UbicacionDAO;

import java.util.List;

/**
 * Capa de Servicio para la entidad Ubicacion.
 * * Anotada con @Service para que Spring la detecte y la inyecte
 * donde se necesite (ej. en el UbicacionRestController).
 *
 * Anotada con @Transactional para que Spring maneje las transacciones
 * a nivel de servicio.
 */
@Service
@Transactional
public class UbicacionService {

    // 1. Inyectamos la interfaz del DAO, no la implementación
    @Autowired
    private UbicacionDAO ubicacionDAO;

    // 2. Lógica de negocio (validación)
    //    Esto estaba en tu DAO y lo movimos aquí, que es donde corresponde.
    private void _validar(Ubicacion u) {
        if (u == null) {
            throw new IllegalArgumentException("La ubicación no puede ser null.");
        }
        if (u.getLatitud() == null) {
            throw new IllegalArgumentException("Latitud obligatoria (no puede ser null).");
        }
        if (u.getLongitud() == null) {
            throw new IllegalArgumentException("Longitud obligatoria (no puede ser null).");
        }
    }

    // --- Métodos CRUD básicos ---

    /**
     * Crea una nueva ubicación previa validación.
     * @param u La entidad Ubicacion a persistir.
     * @return La entidad persistida con su ID.
     */
    public Ubicacion crearUbicacion(Ubicacion u) {
        _validar(u); // Aplicamos la lógica de negocio
        return ubicacionDAO.persist(u);
    }

    public Ubicacion crearUbicacion(UbicacionCreateDTO dto) {
        Ubicacion u = dto.toEntity();
        _validar(u); // Aplicamos la lógica de negocio
        return ubicacionDAO.persist(u);
    }

    /**
     * Actualiza una ubicación existente previa validación.
     * @param u La entidad Ubicacion a actualizar.
     * @return La entidad actualizada.
     */
    public Ubicacion actualizarUbicacion(Ubicacion u) {
        _validar(u); // Aplicamos la lógica de negocio
        return ubicacionDAO.update(u);
    }

    /**
     * Busca una ubicación por su ID.
     * @Transactional(readOnly = true) es una optimización para consultas.
     * @param id El ID de la ubicación.
     * @return La entidad Ubicacion o null si no se encuentra.
     */
    @Transactional(readOnly = true)
    public Ubicacion getUbicacion(Long id) {
        return ubicacionDAO.get(id);
    }

    /**
     * Devuelve todas las ubicaciones.
     * @return Una lista de todas las entidades Ubicacion.
     */
    @Transactional(readOnly = true)
    public List<Ubicacion> getAllUbicaciones() {
        return ubicacionDAO.getAll();
    }

    /**
     * Elimina una ubicación por su ID.
     * @param id El ID de la ubicación a eliminar.
     */
    public void eliminarUbicacion(Long id) {
        // Aquí podrías agregar lógica de negocio, por ejemplo:
        // 1. Verificar que el ID existe (aunque el DAO ya lo hace)
        // 2. Verificar que la ubicación no esté siendo usada por un Usuario
        //    o Publicacion antes de borrar (regla de integridad de negocio).
        ubicacionDAO.delete(id);
    }

    // --- Métodos Específicos (del UbicacionDAO) ---

    /**
     * Busca ubicaciones por provincia.
     * @param provincia El nombre de la provincia.
     * @return Lista de ubicaciones que coinciden.
     */
    @Transactional(readOnly = true)
    public List<Ubicacion> buscarPorProvincia(String provincia) {
        return ubicacionDAO.findByProvincia(provincia);
    }

    /**
     * Búsqueda flexible por múltiples criterios (LIKE).
     * @param idExterno
     * @param provincia
     * @param ciudad
     * @param barrio
     * @return Lista de ubicaciones que coinciden.
     */
    @Transactional(readOnly = true)
    public List<Ubicacion> buscarPorCriterio(String idExterno, String provincia, String ciudad, String barrio) {
        return ubicacionDAO.findByCriteriaLike(idExterno, provincia, ciudad, barrio);
    }
}