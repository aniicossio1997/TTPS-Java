package persistence.impl;

import domain.models.Medalla;
import org.springframework.stereotype.Repository; // Se agrega esta importación
import persistence.dao.MedallaDAO;

@Repository // Se añade la anotación
public class MedallaDAOHibernateJPA extends GenericDAOHibernateJPA<Medalla> implements MedallaDAO {
    public MedallaDAOHibernateJPA() {
        super(Medalla.class);
    }
}