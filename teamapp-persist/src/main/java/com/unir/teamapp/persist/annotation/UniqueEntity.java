package com.unir.teamapp.persist.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.unir.teamapp.persist.validator.UniqueEntityValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = UniqueEntityValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE})
public @interface UniqueEntity {

    String message() default "Ya existe otra entidad con las mismas propiedades";

    String[] fields() default {};

    Class<?>[] groups() default{};

    Class<? extends Payload>[] payload() default {};
    
    boolean hasEnabledField() default false;
}
