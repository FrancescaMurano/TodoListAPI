package com.todolist.app.validation;

    
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Set;

public class RoleValidator implements ConstraintValidator<ValidRoles, Set<String>> {

    private static final Set<String> VALID_ROLES = Set.of("ROLE_ADMIN", "ROLE_USER");

    @Override
    public boolean isValid(Set<String> roles, ConstraintValidatorContext context) {
        if (roles == null || roles.isEmpty()) {
            return false; // Deve avere almeno un ruolo
        }
        return roles.stream().allMatch(VALID_ROLES::contains);
    }
}
