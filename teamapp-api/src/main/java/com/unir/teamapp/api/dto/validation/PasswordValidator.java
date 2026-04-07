package com.unir.teamapp.api.dto.validation;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.unir.teamapp.api.annotation.ValidatePasswordCheck;
import com.unir.teamapp.api.dto.RegisterRequestDTO;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PasswordValidator implements ConstraintValidator<ValidatePasswordCheck, RegisterRequestDTO> {
    
    @Value("${password.restriction.expression}")
    private String passwordExpression;
    
    @Override
    public boolean isValid(RegisterRequestDTO dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true;
        }
        
        if (StringUtils.isBlank(dto.getPassword()) || StringUtils.isBlank(dto.getConfirmPassword())) {
            return true;
        }

        boolean valid = true;
        context.disableDefaultConstraintViolation();

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            context.buildConstraintViolationWithTemplate("Las contraseñas no coinciden")
                .addPropertyNode("confirmPassword")
                .addConstraintViolation();
            valid = false;
        }

        if (!validarPasswordRestrictions(dto.getPassword())) {
            context.buildConstraintViolationWithTemplate("La contraseña no cumple el formato requerido")
                .addPropertyNode("password")
                .addConstraintViolation();
            valid = false;
        }

        return valid;
    }

    private boolean validarPasswordRestrictions(String password) {
        return Pattern.compile(passwordExpression).matcher(password).matches();   
    }

}
