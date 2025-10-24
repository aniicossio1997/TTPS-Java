package persistence.dao;

import domain.models.Ubicacion;

import java.util.List;

/**
 * Interfaz específica para Ubicacion.
 * 
 */
public interface UbicacionDAO extends GenericDAO<Ubicacion> {


    public List<Ubicacion> findByProvincia(String provincia);

    public List<Ubicacion> findByCriteriaLike(String idExterno, String provincia, String ciudad, String barrio );
}