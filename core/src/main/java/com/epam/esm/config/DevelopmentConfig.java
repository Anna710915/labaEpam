package com.epam.esm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * The type Development config.
 * @author Anna Merkul
 */
@SpringBootApplication(scanBasePackages = "com.epam.esm")
@PropertySource("classpath:application-dev.properties")
@EnableTransactionManagement
@Profile("dev")
public class DevelopmentConfig {

    private final DataSource dataSource;

    /**
     * Instantiates a new Development config.
     *
     * @param dataSource the data source
     */
    @Autowired
    public DevelopmentConfig(DataSource dataSource){
        this.dataSource = dataSource;
    }

    /**
     * Session factory local session factory bean. FactoryBean that creates a Hibernate SessionFactory.
     * This is the usual way to set up a shared Hibernate SessionFactory in a Spring application context;
     * the SessionFactory can then be passed to data access objects via dependency injection.
     *
     * @return the local session factory bean
     */
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan(
                "com.epam.esm");
        return sessionFactory;
    }

    /**
     * Hibernate transaction manager platform transaction manager.
     * This is the central interface in Spring's imperative transaction infrastructure.
     *
     * @return the platform transaction manager
     */
    @Bean
    public PlatformTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager transactionManager
                = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }
}
