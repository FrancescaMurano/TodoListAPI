package com.todolist.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

import com.todolist.app.validation.ValidRoles;

public class RegisterRequest { // gestire la validazione di richiesta registrazione utente

    @NotBlank(message = "username is mandatory")
    @Size(min=3, max=20, message = "size between 3 and 20 chars")
    private String username;

    @NotBlank(message = "password is mandatory")
    @Size(min=3, max=20, message = "size between 6 and 20 chars")
    private String password;

    @ValidRoles
    private Set<String> roles;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Set<String> getRoles() {
        return roles;
    }
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
