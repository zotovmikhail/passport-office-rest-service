package com.zotov.edu.passportofficerestservice.controller.validator;

import com.neovisionaries.i18n.CountryCode;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CountryValidator implements ConstraintValidator<Country, String> {
    @Override
    public boolean isValid(String country, ConstraintValidatorContext constraintValidatorContext) {
        return CountryCode.getByCode(country.toUpperCase()) != null;
    }
}
