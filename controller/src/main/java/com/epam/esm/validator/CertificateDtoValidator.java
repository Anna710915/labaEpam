package com.epam.esm.validator;

import com.epam.esm.dto.CertificateDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CertificateDtoValidator implements Validator {

    private static final String NAME_REGEXP = "[A-z0-9-\s'\"]{2,100}";
    private static final String DESCRIPTION_REGEXP = "[^<>]+";

    @Override
    public boolean supports(Class<?> clazz) {
        return CertificateDto.class.equals(clazz);
    }


    @Override
    public void validate(Object target, Errors errors) {
        CertificateDto certificateDto = (CertificateDto) target;
        if(BlankOrEmpty(certificateDto.getName()) || !certificateDto.getName().matches(NAME_REGEXP)){
            errors.rejectValue("name", "The name should contain letters, digits, [-'\"] or some space and should not be empty");
        }
        if(certificateDto.getDescription() != null &&
                (certificateDto.getDescription().matches(DESCRIPTION_REGEXP) ||
                certificateDto.getDescription().length() > 120)){
            errors.rejectValue("description", "The description max length is 120 letters and mustn't to contain '<>'");
        }
        if(certificateDto.getPrice().doubleValue() > 999999.99 || certificateDto.getPrice().doubleValue() < 0){
            errors.rejectValue("price", "It is a positive field. The max value is 999999.99");
        }
        if(certificateDto.getDuration() < 0 || certificateDto.getDuration() > 365){
            errors.rejectValue("duration", "The duration range between 0 and 365 days");
        }
    }

    private boolean BlankOrEmpty(String line){
        return line == null || line.isBlank();
    }
}
