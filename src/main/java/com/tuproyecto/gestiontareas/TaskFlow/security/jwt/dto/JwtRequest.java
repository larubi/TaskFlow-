package com.tuproyecto.gestiontareas.TaskFlow.security.jwt.dto;

import java.io.Serializable;

/**
 * DTO para la solicitud de autenticación (login).
 * Contiene el nombre de usuario y la contraseña enviados por el cliente.
 */
public class JwtRequest implements Serializable {

    private static final long serialVersionUID = 592646858351532950L;

    private String username;
    private String password;

    // Se necesita un constructor por defecto para la deserialización de JSON
    public JwtRequest() {
    }

    public JwtRequest(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
