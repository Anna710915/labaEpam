package com.epam.esm.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * The type Controller configuration. Enables a web MVC structure which
 * imports the Spring MVC configuration from WebMvcConfigurationSupport.
 * Imports ServiceConfig and LoggingConfig classes.
 * @author Anna Merkul
 */
@Configuration
@ComponentScan("com.epam.esm")
@EnableWebMvc
@Import({ServiceConfig.class, LoggingConfig.class})
public class ControllerConfig {

}
