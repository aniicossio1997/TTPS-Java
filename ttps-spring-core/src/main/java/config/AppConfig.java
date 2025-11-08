package config;

import interceptors.AuthInterceptor;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

@Configuration
@EnableWebMvc
@EnableTransactionManagement // <-- Movido de PersistenceConfig
@ComponentScan(basePackages = {
        "domain.models",
        "persistence.impl",
        "services",
        "controller",
        "exceptions"
})
public class AppConfig implements WebMvcConfigurer {

    // --- Configuración Web (ya la tenías) ---
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter());
    }

    // --- Beans de Persistencia (Movidos desde PersistenceConfig) ---

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setUsername("root");
        driverManagerDataSource.setPassword("root");
        driverManagerDataSource.setUrl("jdbc:mysql://localhost:3306/ttps_java_grupo20");
        driverManagerDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return driverManagerDataSource;
    }

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
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", "update");

        // (Opcional pero recomendado) Esto te mostrará el SQL
        // que Hibernate ejecuta en la consola.
        jpaProperties.setProperty("hibernate.show_sql", "true");
        jpaProperties.setProperty("hibernate.format_sql", "true");

        // Asignamos las propiedades al EntityManagerFactory
        emf.setJpaProperties(jpaProperties);
        // --- FIN DE LA SOLUCIÓN ---

        emf.setJpaProperties(jpaProperties);
        return emf;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }
}