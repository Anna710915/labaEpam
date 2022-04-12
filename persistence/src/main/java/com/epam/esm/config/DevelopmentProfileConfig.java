package com.epam.esm.config;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

/**
 * The type Development profile configuration is a configuration java class.
 * Used for integration tests with an in-memory embedded database.
 * @author Anna Merkul
 */
@Configuration
@ComponentScan("com.epam.esm")
@Profile("dev")
public class DevelopmentProfileConfig {

    private static final String SCHEMA = "classpath:schema.sql";
    private static final String START_DATA = "classpath:data.sql";

    /**
     * Data source. An embedded database runs as part of an application
     * instead of as a separate database server that an application connects to.
     * The embedded database is used for development and testing purposes.
     * @return the data source
     */
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript(SCHEMA)
                .addScript(START_DATA)
                .build();
    }

    /**
     * Jdbc template . This class provides simple access to a database through JDBC.
     * the DataSource is injected via constructor injection.
     *
     * @return the jdbc template
     */
    @Bean
    public JdbcTemplate jdbcTemplate(){
        return new JdbcTemplate(dataSource());
    }
}
