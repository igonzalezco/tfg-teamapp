package com.unir.teamapp.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.unir.teamapp.api.dto.validation.PasswordValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = PasswordValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidatePasswordCheck {

    String menssage() default "La contraseña no son válidas";

    Class<? extends Payload>[] payload() default {};

}
