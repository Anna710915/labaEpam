package com.epam.esm.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * The type Persistence configuration class is a production class
 * which connects to the real database. The parameters of a connection
 * is taken from the property file named "database.properties".
 * @author Anna Merkul
 */
@Configuration
@ComponentScan("com.epam.esm")
@Profile("prod")
@PropertySource("classpath:database.properties")
public class PersistenceConfiguration {

    private static final String DATABASE_DRIVER = "db.driver";
    private static final String DATABASE_URL = "db.url";
    private static final String DATABASE_USER_NAME = "user";
    private static final String DATABASE_PASSWORD = "password";
    private static final String INIT_POOL_SIZE = "initialSize";
    private static final String MAX_ACTIVE_THREAD = "maxActive";

    private final Environment environment;

    /**
     * Instantiates a new Persistence configuration. The parameter representing the environment in which
     * the current application is running. Models two key aspects of the application environment:
     * profiles and properties. Methods related to property access are exposed via the PropertyResolver superinterface.
     *
     * @param environment the environment
     */
    @Autowired
    public PersistenceConfiguration(Environment environment){
        this.environment = environment;
    }

    /**
     * Data source. DataSource is part of the JDBC specification and can be seen as a generalized connection factory.
     * It allows a container or a framework to hide connection pooling and transaction management issues from the
     * application code.
     *
     * @return the data source
     */
    @Bean
    public DataSource dataSource(){
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(environment.getProperty(DATABASE_DRIVER));
        dataSource.setUrl(environment.getProperty(DATABASE_URL));
        dataSource.setUsername(environment.getProperty(DATABASE_USER_NAME));
        dataSource.setPassword(environment.getProperty(DATABASE_PASSWORD));
        dataSource.setInitialSize(Integer.parseInt(environment.getProperty(INIT_POOL_SIZE)));
        dataSource.setMaxActive(Integer.parseInt(environment.getProperty(MAX_ACTIVE_THREAD)));
        return dataSource;
    }

    /**
     * Jdbc template. This class provides simple access to a database through JDBC.
     * the DataSource is injected via constructor injection.
     *
     * @return the jdbc template
     */
    @Bean
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(dataSource());
    }
}
