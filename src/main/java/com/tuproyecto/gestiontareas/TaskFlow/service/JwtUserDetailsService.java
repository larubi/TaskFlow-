package com.tuproyecto.gestiontareas.TaskFlow.service;

import com.tuproyecto.gestiontareas.TaskFlow.model.User; // Asegúrate de importar tu clase User
import com.tuproyecto.gestiontareas.TaskFlow.repository.UserRepository; // Importar UserRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList; // No necesario, pero lo dejo por si acaso
import java.util.Collections; // Para Collections.emptyList() si no hay roles

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository; // Inyectamos el UserRepository

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca el usuario en la base de datos usando el UserRepository
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con username: " + username));

        // Retorna el objeto User (que implementa UserDetails)
        return user;
    }
}