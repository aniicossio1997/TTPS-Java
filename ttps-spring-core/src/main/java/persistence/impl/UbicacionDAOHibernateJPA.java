package persistence.impl;

import domain.models.Ubicacion;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository; // Se agrega esta importación
import persistence.dao.UbicacionDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository // Se añade la anotación
public class UbicacionDAOHibernateJPA extends GenericDAOHibernateJPA<Ubicacion> implements UbicacionDAO {

    public UbicacionDAOHibernateJPA() {
        super(Ubicacion.class);
    }

    @Override
    public List<Ubicacion> findByProvincia(String provincia) {
        // Se elimina la creación manual del EntityManager y el try/finally
        Query consulta = getEntityManager().createQuery("SELECT u FROM " + getPersistentClass().getSimpleName() +
                " u WHERE u.provincia = :prov");
        consulta.setParameter("prov", provincia);
        return (List<Ubicacion>) consulta.getResultList();
    }


    @Override
    public Ubicacion update(Ubicacion u) {
        _validarParaCrear(u);
        return super.update(u);
    }

    @Override
    public Ubicacion persist(Ubicacion u) {
        _validarParaCrear(u);
        return super.persist(u);
    }

    @Override
    public Ubicacion getByUsuarioId(Long usuarioId) {
        TypedQuery<Ubicacion> consulta = getEntityManager().createQuery("SELECT u FROM " + getPersistentClass().getSimpleName() +
                " u WHERE u.usuario.id = :usuarioId", getPersistentClass());
        consulta.setParameter("usuarioId", usuarioId);
        return consulta.getSingleResultOrNull();
    }

    @Override
    public List<Ubicacion> findByCriteriaLike(String idExterno, String provincia, String ciudad, String barrio ) {
        // Se elimina la creación manual del EntityManager y el try/finally
        StringBuilder jpql = new StringBuilder("SELECT u FROM Ubicacion u WHERE 1=1");
        Map<String, String> parameters = new HashMap<>();

        if (provincia != null && !provincia.trim().isEmpty()) {
            jpql.append(" AND LOWER(u.provincia) LIKE LOWER(:provinciaParam)");
            parameters.put("provinciaParam", "%" + provincia.trim() + "%");
        }
        if (ciudad != null && !ciudad.trim().isEmpty()) {
            jpql.append(" AND LOWER(u.ciudad) LIKE LOWER(:ciudadParam)");
            parameters.put("ciudadParam", "%" + ciudad.trim() + "%");
        }
        if (barrio != null && !barrio.trim().isEmpty()) {
            jpql.append(" AND LOWER(u.barrio) LIKE LOWER(:barrioParam)");
            parameters.put("barrioParam", "%" + barrio.trim() + "%");
        }
        if (idExterno != null && !idExterno.trim().isEmpty()) {
            jpql.append(" AND LOWER(u.idExterno) LIKE LOWER(:idExternoParam)");
            parameters.put("idExternoParam", "%" + idExterno.trim() + "%");
        }

        TypedQuery<Ubicacion> query = getEntityManager().createQuery(jpql.toString(), Ubicacion.class);

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        return query.getResultList();
    }


    private void _validarParaCrear(Ubicacion u) {
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
}