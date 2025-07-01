package com.tuproyecto.gestiontareas.TaskFlow.controller;

import com.tuproyecto.gestiontareas.TaskFlow.security.jwt.dto.JwtRequest;
import com.tuproyecto.gestiontareas.TaskFlow.security.jwt.dto.JwtResponse;

// Importaciones para clases JWT específicas
import com.tuproyecto.gestiontareas.TaskFlow.security.jwt.JwtTokenUtil;
import com.tuproyecto.gestiontareas.TaskFlow.service.JwtUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la autenticación de usuarios y generación de JWT.
 */
@RestController
@CrossOrigin // Permite solicitudes desde otros orígenes (frontend, etc.)
@RequestMapping("/authenticate") // La URL base para este controlador
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    /**
     * Endpoint para iniciar sesión y obtener un token JWT.
     * Recibe un JwtRequest con nombre de usuario y contraseña.
     * @param authenticationRequest DTO que contiene el nombre de usuario y la contraseña.
     * @return ResponseEntity con el token JWT si la autenticación es exitosa.
     * @throws Exception Si las credenciales son inválidas (usuario deshabilitado o credenciales incorrectas).
     */
    @PostMapping
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        // Intenta autenticar al usuario con el AuthenticationManager
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        // Si la autenticación es exitosa, carga los detalles del usuario
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        // Genera el token JWT usando los detalles del usuario
        final String token = jwtTokenUtil.generateToken(userDetails);

        // Devuelve el token en un objeto JwtResponse
        return ResponseEntity.ok(new JwtResponse(token));
    }

    /**
     * Método auxiliar para realizar la autenticación de las credenciales.
     * Lanza excepciones si la autenticación falla por usuario deshabilitado o credenciales incorrectas.
     * @param username Nombre de usuario.
     * @param password Contraseña.
     * @throws Exception USER_DISABLED si el usuario está deshabilitado, INVALID_CREDENTIALS si la contraseña es incorrecta.
     */
    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            // Se lanza si el usuario está deshabilitado
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            // Se lanza si la contraseña no coincide
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
