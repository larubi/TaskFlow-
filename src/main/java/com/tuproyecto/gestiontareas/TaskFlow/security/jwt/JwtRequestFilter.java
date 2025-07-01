package com.tuproyecto.gestiontareas.TaskFlow.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//Marca la clase como un componente gestionado por Spring.
//extends OncePerRequestFilter: Garantiza que el filtro se ejecute solo una vez por cada solicitud HTTP.
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsService jwtUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public JwtRequestFilter(UserDetailsService jwtUserDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /*
  Este método es donde se implementa la lógica del filtro
Obtiene el encabezado Authorization de la solicitud.
Comprueba si el encabezado existe y empieza con "Bearer ".
Extrae el token real y, usando JwtTokenUtil, intenta obtener el nombre de usuario.
Maneja las excepciones si el token no es válido (ej. IllegalArgumentException) o ha expirado (ExpiredJwtException).
Si se obtiene un nombre de usuario válido y no hay autenticación existente en el contexto de seguridad (lo que indica que la solicitud aún no ha sido autenticada), carga los UserDetails del usuario usando un UserDetailsService (que crearemos a continuación).
Finalmente, valida el token usando jwtTokenUtil.validateToken(). Si es válido, crea un UsernamePasswordAuthenticationToken y lo establece en el SecurityContextHolder, lo que esencialmente "autentica" al usuario para Spring Security para esa solicitud.
chain.doFilter(request, response): Pasa la solicitud a la siguiente cadena de filtros o al controlador.
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        // JWT Token está en el formato "Bearer token". Se remueve la palabra Bearer para obtener solo el Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.extractUsername(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String or is null");
        }

        // Una vez que se obtiene el token, se valida
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

            // Si el token es válido, se configura Spring Security para establecer la autenticación
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Después de configurar la autenticación en el Contexto,
                // especificamos que el usuario actual está autenticado. Así, pasa las configuraciones de Spring Security
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
}