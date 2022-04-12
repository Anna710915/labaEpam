package com.epam.esm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * The type Service configuration is used as business logic configuration.
 * Supports transactions. Imports PersistenceConfiguration class.
 * @author Anna Merkul
 */
@Configuration
@EnableTransactionManagement
@ComponentScan("com.epam.esm")
@Import(PersistenceConfiguration.class)
public class ServiceConfig {

    private final DataSource dataSource;

    /**
     * Instantiates a new Service config.
     *
     * @param dataSource the data source
     */
    @Autowired
    public ServiceConfig(DataSource dataSource){
        this.dataSource = dataSource;
    }

    /**
     * Data source transaction manager data source transaction manager.
     * This class is capable of working in any environment with any JDBC driver.
     * The DataSource that this transaction manager operates on needs to return
     * independent Connections. The Connections may come from a pool (the typical case),
     * but the DataSource must not return thread-scoped / request-scoped Connections
     * or the like. This transaction manager will associate Connections with thread-bound
     * transactions itself, according to the specified propagation behavior.
     * It assumes that a separate, independent Connection can be obtained
     * even during an ongoing transaction.
     *
     * @return the data source transaction manager
     */
    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(){
        return new DataSourceTransactionManager(dataSource);
    }
}
