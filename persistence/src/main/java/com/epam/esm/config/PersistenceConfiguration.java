package com.epam.esm.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

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

    @Autowired
    public PersistenceConfiguration(Environment environment){
        this.environment = environment;
    }

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

    @Bean
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(dataSource());
    }
}
