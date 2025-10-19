package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DBConnection {

	private static final String UNIDAD_DE_PERSISTENCIA = "miUP";
	private static DBConnection instancia;
	private EntityManagerFactory emf;
	private EntityManager em;

	private DBConnection() {
		emf = Persistence.createEntityManagerFactory(UNIDAD_DE_PERSISTENCIA);
		em = emf.createEntityManager();
	}

	public static DBConnection getInstance() {
		if (instancia == null) {
			instancia = new DBConnection();

		}
		return instancia;
	}

	public EntityManager getEntityManager() {
		return em;
	}

}
