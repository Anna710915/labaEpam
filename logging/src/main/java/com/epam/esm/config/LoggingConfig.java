package com.epam.esm.config;

import com.epam.esm.logging.LoggingAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan("com.epam.esm")
public class LoggingConfig {

    @Bean
    public LoggingAspect loggingAspect(){
        return new LoggingAspect();
    }
}
