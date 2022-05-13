package com.epam.esm.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

/**
 * The type Controller config.
 * @author Anna Merkul
 */
@SpringBootApplication(scanBasePackages = "com.epam.esm",
        exclude = HibernateJpaAutoConfiguration.class)
public class ControllerConfig {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(ControllerConfig.class, args);
    }
}
