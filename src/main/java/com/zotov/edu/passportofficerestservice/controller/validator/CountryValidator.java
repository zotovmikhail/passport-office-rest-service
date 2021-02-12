package com.zotov.edu.passportofficerestservice.controller.validator;

import com.neovisionaries.i18n.CountryCode;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class CountryValidator implements ConstraintValidator<Country, String> {
    @Override
    public boolean isValid(String country, ConstraintValidatorContext constraintValidatorContext) {
        return Objects.isNull(country) || Objects.nonNull(CountryCode.getByCode(country.toUpperCase()));
    }
}
