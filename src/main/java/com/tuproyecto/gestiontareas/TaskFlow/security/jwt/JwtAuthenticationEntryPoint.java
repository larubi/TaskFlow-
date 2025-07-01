package com.tuproyecto.gestiontareas.TaskFlow.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;

/**
 * Clase que maneja los intentos de acceso no autorizados para JWT.
 * Se activa cuando un usuario no autenticado intenta acceder a un recurso protegido.
 * Devuelve un error 401 Unauthorized.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -7858869558953243875L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // Esto se ejecutará cuando un usuario no autenticado intente acceder a un recurso protegido.
        // Aquí enviamos una respuesta de error 401 Unauthorized.
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
