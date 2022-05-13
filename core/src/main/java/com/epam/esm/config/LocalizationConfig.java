package com.epam.esm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.Locale;

/**
 * The type Localization config is a class configuration for localization the application messages.
 * @author Anna Merkul
 */
@Configuration
@ComponentScan(basePackages = "com.epam.esm.config")
public class LocalizationConfig implements WebMvcConfigurer {

    /**
     * Locale resolver locale resolver. Interface for web-based locale resolution strategies
     * that allows for both locale resolution via the request and locale modification via request and response.
     *
     * @return the locale resolver
     */
    @Bean
    public LocaleResolver localeResolver() {
        final AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setSupportedLocales(Arrays.asList(new Locale("ru"), new Locale("en")));
        resolver.setDefaultLocale(Locale.ENGLISH);
        return resolver;
    }

    /**
     * Message source resource bundle message source. MessageSource implementation that
     * accesses resource bundles using specified basenames. This class relies on the underlying
     * JDK's ResourceBundle implementation, in combination with the JDK's standard message parsing
     * provided by MessageFormat.
     *
     * @return the resource bundle message source
     */
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
        resourceBundleMessageSource.setBasename("messages");
        resourceBundleMessageSource.setDefaultEncoding("UTF-8");
        resourceBundleMessageSource.setUseCodeAsDefaultMessage(true);
        return resourceBundleMessageSource;
    }
}
