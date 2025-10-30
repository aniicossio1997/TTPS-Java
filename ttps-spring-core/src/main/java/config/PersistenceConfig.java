package config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Properties;

@Configuration   // Marks the class as a configuration class for Spring.
@EnableTransactionManagement   //Enables declarative transaction management via Spring's @Transactional annotation.
@ComponentScan(basePackages = {"domain.models", "persistence.impl"})
public class PersistenceConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setUsername("root");
        driverManagerDataSource.setPassword("root");
        driverManagerDataSource.setUrl("jdbc:mysql://localhost:3306/buffet");
        driverManagerDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return driverManagerDataSource;
    }


    /**
     * El localContainerEntityManagerFactoryBean es un componente de Spring que facilita la configuración de la
     * fábrica de administradores de entidades de JPA. Este bean permite la integración de JPA con el contenedor
     * de Spring, proporcionando una forma sencilla de gestionar las entidades y las transacciones.
     * @return
     */

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource());
        emf.setPackagesToScan("domain.models");
        emf.setEntityManagerFactoryInterface(EntityManagerFactory.class);
        JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        emf.setJpaVendorAdapter(jpaVendorAdapter);

        // --- AQUÍ ESTÁ LA PARTE QUE FALTA ---
        // Creamos un objeto de Propiedades
        Properties jpaProperties = new Properties();

        // Esta es la línea clave. Le dice a Hibernate que "cree" las tablas
        // si no existen. Si ya existen, las borra y las vuelve a crear.
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", "create");

        // (Opcional pero recomendado) Esto te mostrará el SQL
        // que Hibernate ejecuta en la consola.
        jpaProperties.setProperty("hibernate.show_sql", "true");
        jpaProperties.setProperty("hibernate.format_sql", "true");

        // Asignamos las propiedades al EntityManagerFactory
        emf.setJpaProperties(jpaProperties);
        // --- FIN DE LA SOLUCIÓN ---

        return emf;
    }



    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }



}

