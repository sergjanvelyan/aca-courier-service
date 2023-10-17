package com.aca.acacourierservice.validation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LocalDateValidator.class)
@Documented
@ReportAsSingleViolation
public @interface ValidLocalDate {
    String message() default "Invalid date format (yyyy-MM-dd)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}