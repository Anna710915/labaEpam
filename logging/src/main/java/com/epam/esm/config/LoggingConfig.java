package com.epam.esm.config;

import com.epam.esm.logging.LoggingAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * The type Logging config is marked with EnableAspectJAutoProxy annotation. This annotation
 * enables support for handling components marked with AspectJ's (Aspect) annotation.
 * @author Anna Merkul
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan("com.epam.esm")
public class LoggingConfig {

    /**
     * Logging aspect.
     *
     * @return the logging aspect
     */
    @Bean
    public LoggingAspect loggingAspect(){
        return new LoggingAspect();
    }
}
