package com.epam.esm.config;

import com.epam.esm.repository.impl.TagRepositoryImpl;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.epam.esm")
//@Profile("prod")
@PropertySource("classpath:database.properties")
public class PersistenceConfiguration {

    private Environment environment;

    @Autowired
    public PersistenceConfiguration(Environment environment){
        this.environment = environment;
    }

    @Bean
    public DataSource dataSource(){
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(environment.getProperty("db.driver"));
        dataSource.setUrl(environment.getProperty("db.url"));
        dataSource.setUsername(environment.getProperty("user"));
        dataSource.setPassword(environment.getProperty("password"));
        dataSource.setInitialSize(Integer.parseInt(environment.getProperty("initialSize")));
        dataSource.setMaxActive(Integer.parseInt(environment.getProperty("maxActive")));
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public TagRepositoryImpl tagRepository(){
        return new TagRepositoryImpl(jdbcTemplate());
    }
}
