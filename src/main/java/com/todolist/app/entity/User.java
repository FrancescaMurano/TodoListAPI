package com.todolist.app.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Permette la scrittura ma non la lettura
    @ElementCollection(fetch = FetchType.EAGER) // Crea una tabella separata per i ruoli
    @CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "id"))
    private Set<String> roles;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Permette la scrittura ma non la lettura
    private String password;

    public User(){}

    public User(String username, String password, Set<String> role) {
        this.username = username;
        this.password = password;
        this.roles = role;
    }

    public User(Long id, String username, String password, Set<String> role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", role=" + roles + ", password=" + password + "]";
    }

}
