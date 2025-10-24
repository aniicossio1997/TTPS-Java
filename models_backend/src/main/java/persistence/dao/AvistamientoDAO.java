package persistence.dao;

import domain.models.Avistamiento;

import java.util.List;

public interface AvistamientoDAO  extends GenericDAO<Avistamiento> {
    public List<Avistamiento> getByIdPublicacion(Long publicacionId);
}
