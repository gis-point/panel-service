package com.microgis.validation;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckPhoneNumberValidator implements ConstraintValidator<CheckPhoneNumber, String> {

    @Override
    public final boolean isValid(final String value, final ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value)) {
            return true;
        }
        return value.length() == 13 && value.startsWith("+380");
    }
}
