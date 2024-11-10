package com.example.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class EnumValidator implements ConstraintValidator<ValidEnum, String> {
    private Class<? extends Enum<?>> enumClass;


    @Override
    public void initialize(ValidEnum annotation) {
        this.enumClass = annotation.enumClass();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;  // Null values are handled by other annotations like @NotNull
        }

        try {

            Enum.valueOf((Class<? extends Enum>)enumClass, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
