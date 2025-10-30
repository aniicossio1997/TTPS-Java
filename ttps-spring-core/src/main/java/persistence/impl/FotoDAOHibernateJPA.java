package persistence.impl;

import domain.models.Foto;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository; // Importación clave
import persistence.dao.FotoDAO;

// import persistence.EMF; // No se usa
// import jakarta.persistence.EntityManager; // No se usa

import java.util.List;

@Repository // Anotación para que Spring lo gestione
public class FotoDAOHibernateJPA extends GenericDAOHibernateJPA<Foto> implements FotoDAO {

    public FotoDAOHibernateJPA() {
        super(Foto.class);
    }

    @Override
    public List<Foto> getFotosByPublicacion(Long publicacionId) {
        // Se usa getEntityManager() de la clase padre
        TypedQuery<Foto> consulta = getEntityManager().createQuery(
                "SELECT f FROM Foto f WHERE f.publicacion.id = :pubId", Foto.class);
        consulta.setParameter("pubId", publicacionId);
        return consulta.getResultList();
    }

    @Override
    public List<Foto> getFotosByAvistamiento(Long avistamientoId) {
        // Implementación del nuevo método
        TypedQuery<Foto> consulta = getEntityManager().createQuery(
                "SELECT f FROM Foto f WHERE f.avistamiento.id = :avistamientoId", Foto.class);
        consulta.setParameter("avistamientoId", avistamientoId);
        return consulta.getResultList();
    }

    @Override
    public Foto getFotoByUsuario(Long usuarioId) {
        Foto foto = null;
        try {
            TypedQuery<Foto> consulta = getEntityManager().createQuery(
                    "SELECT f FROM Foto f WHERE f.usuario.id = :usuarioId", Foto.class);
            consulta.setParameter("usuarioId", usuarioId);
            foto = consulta.getSingleResult();
        } catch (NoResultException e) {
            // No se encontró foto, se retorna null (lo cual es correcto)
            foto = null;
        }
        return foto;
    }
}