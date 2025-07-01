package com.tuproyecto.gestiontareas.TaskFlow.service;


import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Servicio de detalles de usuario para la autenticación JWT.
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final PasswordEncoder bcryptEncoder; // Se inyectará el PasswordEncoder configurarado en SecurityConfig

    // Inyección de dependencia a través del constructor
    public JwtUserDetailsService(PasswordEncoder bcryptEncoder) {
        this.bcryptEncoder = bcryptEncoder;
    }

    /**
     * Carga los detalles del usuario por nombre de usuario.

     * @param username El nombre de usuario.
     * @return UserDetails del usuario.
     * @throws UsernameNotFoundException Si el usuario no se encuentra.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // La contraseña "password" se codificará usando bcryptEncoder.
        if ("user".equals(username)) {
            return User.withUsername("user")
                    .password(bcryptEncoder.encode("password")) // Usa el encoder inyectado
                    .roles("USER")
                    .build();
        } else {
            throw new UsernameNotFoundException("Usuario no encontrado con nombre de usuario: " + username);
        }
    }
}
