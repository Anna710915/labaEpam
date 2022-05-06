package com.epam.esm.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageLanguageUtil {

    private ResourceBundleMessageSource messageSource;

    @Autowired
    public MessageLanguageUtil(ResourceBundleMessageSource messageSource){
        this.messageSource = messageSource;
    }

    public String getMessage(String messageKey){
        return messageSource.getMessage(messageKey, null, LocaleContextHolder.getLocale());
    }
}
