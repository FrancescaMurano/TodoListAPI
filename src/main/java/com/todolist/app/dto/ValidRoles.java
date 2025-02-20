package com.todolist.app.dto;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = RoleValidator.class) // Collega il Validator
@Target({ElementType.FIELD}) // Può essere usata solo su campi
@Retention(RetentionPolicy.RUNTIME) // Attiva a runtime
public @interface ValidRoles {
    String message() default "Ruolo non valido! Usa solo ROLE_ADMIN o ROLE_USER.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
