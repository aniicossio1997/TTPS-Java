package persistence.impl;

import domain.models.Foto;
import persistence.dao.FotoDAO;

public class FotoDAOHibernateJPA extends GenericDAOHibernateJPA<Foto> implements FotoDAO {
    public FotoDAOHibernateJPA() {
        super(Foto.class);
    }

}
