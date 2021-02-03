package com.zotov.edu.passportofficerestservice.controller.validator;

import com.neovisionaries.i18n.CountryCode;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CountryValidator implements ConstraintValidator<Country, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return CountryCode.getByCode(s.toUpperCase()) != null;
    }
}
