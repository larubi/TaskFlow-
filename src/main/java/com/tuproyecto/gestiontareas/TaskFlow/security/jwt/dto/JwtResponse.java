package com.tuproyecto.gestiontareas.TaskFlow.security.jwt.dto;

import java.io.Serializable;

/**
 * DTO para la respuesta de autenticación.
 * Contiene el token JWT generado después de un login exitoso.
 */
public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwttoken;

    public JwtResponse(String jwttoken) {
        this.jwttoken = jwttoken;
    }

    public String getToken() {
        return this.jwttoken;
    }
}
