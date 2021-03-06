package com.zotov.edu.passportofficerestservice.controller.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {CountryValidator.class})
public @interface Country {
    String message() default "is not found in the list of available countries.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
