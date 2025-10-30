package persistence.dao;

import domain.models.Foto;
import java.util.List;

public interface FotoDAO extends GenericDAO<Foto> {
    public List<Foto> getFotosByPublicacion(Long publicacionId);

    public List<Foto> getFotosByAvistamiento(Long avistamientoId);

    public Foto getFotoByUsuario(Long usuarioId);
}