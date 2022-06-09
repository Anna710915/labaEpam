package com.epam.esm.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

/**
 * The type Message language util.
 * @author Anna Merkul
 */
@Component
public class MessageLanguageUtil {

    private ResourceBundleMessageSource messageSource;

    /**
     * Instantiates a new Message language util.
     *
     * @param messageSource the message source
     */
    @Autowired
    public MessageLanguageUtil(ResourceBundleMessageSource messageSource){
        this.messageSource = messageSource;
    }

    /**
     * Get message string.
     *
     * @param messageKey the message key
     * @return the string
     */
    public String getMessage(String messageKey){
        return messageSource.getMessage(messageKey, null, LocaleContextHolder.getLocale());
    }
}
