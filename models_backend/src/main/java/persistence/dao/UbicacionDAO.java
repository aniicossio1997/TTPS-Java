package persistence.dao;

import domain.models.Ubicacion;

import java.util.List;

/**
 * Interfaz específica para Ubicacion.
 * Extiende GenericDAO e incluye métodos particulares[cite: 56, 93].
 */
public interface UbicacionDAO extends GenericDAO<Ubicacion> {

    // Ejemplo de método específico [cite: 56]
    public List<Ubicacion> findByProvincia(String provincia);

    public Ubicacion findByIdExterno(String idExterno);
}