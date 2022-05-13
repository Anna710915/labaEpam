package com.epam.esm.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * The type Persistence config.
 * @author Anna Merkul
 */
@SpringBootApplication(scanBasePackages = {"com.epam.esm"})
//@EnableJpaRepositories(basePackages = "com.epam.esm.repository")
@PropertySource("classpath:application-production.properties")
@EnableTransactionManagement
@Profile("production")
public class PersistenceConfig {

    private final Environment environment;

    /**
     * Instantiates a new Persistence config.
     *
     * @param environment the environment
     */
    @Autowired
    public PersistenceConfig(Environment environment){
        this.environment = environment;
    }

    /**
     * Data source. A factory for connections to the physical data source
     * that this DataSource object represents.
     *
     * @return the data source
     */
    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(environment.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(environment.getProperty("spring.datasource.url"));
        dataSource.setUsername(environment.getProperty("spring.datasource.username"));
        dataSource.setPassword(environment.getProperty("spring.datasource.password"));
        dataSource.setMaxActive(Integer.parseInt(environment.getProperty("spring.datasource.hikari.maximum-pool-size")));
        return dataSource;
    }

    /**
     * Session factory local session factory bean.
     * FactoryBean that creates a Hibernate SessionFactory.
     *
     * @return the local session factory bean
     */
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(
                "com.epam.esm");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }


    /**
     * Hibernate transaction manager platform transaction manager.
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

    private Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty(
                "hibernate.hbm2ddl.auto", "update");
        hibernateProperties.setProperty(
                "hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        return hibernateProperties;
    }
}
