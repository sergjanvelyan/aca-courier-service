package com.aca.acacourierservice.validation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateValidator implements ConstraintValidator<ValidLocalDate, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null||value.isEmpty()) {
            return true;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate.parse(value, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}

