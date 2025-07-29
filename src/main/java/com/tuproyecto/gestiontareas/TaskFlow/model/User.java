package com.tuproyecto.gestiontareas.TaskFlow.model;

import jakarta.persistence.*;
import java.util.Collection; // Importar Collection para el método getAuthorities
import java.util.List; // Importar List para el método getAuthorities
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.stream.Collectors; // Importar Collectors

@Entity
@Table(name = "users") // Nombre de la tabla en la base de datos
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    // Campo para los roles
    @ElementCollection(fetch = FetchType.EAGER) // Cargar roles
    @CollectionTable(
            name = "user_roles", // Nombre de tu tabla de unión existente
            joinColumns = @JoinColumn(name = "user_id") // Columna que une con la tabla 'users'
    )
    @Column(name = "roles") // <--- Nombre de la columna que contiene el valor del rol en 'user_roles'
    private List<String> roles; // "ROLE_USER", "ROLE_ADMIN"

    // Constructores
    public User() {
    }

    public User(String username, String password, String email, List<String> roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    // Métodos de UserDetails (requeridos por Spring Security)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Implementar lógica de expiración de cuenta real
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Implementar lógica de bloqueo de cuenta real
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Implementar lógica de expiración de credenciales real
    }

    @Override
    public boolean isEnabled() {
        return true; // Implementar lógica de habilitación/deshabilitación de cuenta real
    }
}
