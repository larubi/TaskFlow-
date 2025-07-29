package com.tuproyecto.gestiontareas.TaskFlow.service;

import com.tuproyecto.gestiontareas.TaskFlow.model.User;
import com.tuproyecto.gestiontareas.TaskFlow.repository.UserRepository;
import com.tuproyecto.gestiontareas.TaskFlow.security.jwt.dto.RegisterRequest; // Importa tu DTO de registro
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections; // Para Collections.singletonList()

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Inyectamos el PasswordEncoder

    public User registerUser(RegisterRequest registerRequest) {
        // 1. Verificar si el nombre de usuario ya existe
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya está en uso.");
        }

        // 2. Verificar si el email ya existe
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("El email '" + registerRequest.getEmail() + "' ya está registrado.");
        }

            // 3. Crear el nuevo objeto User
            User newUser = new User();
            newUser.setUsername(registerRequest.getUsername());
            newUser.setEmail(registerRequest.getEmail());
            // Encriptar la contraseña antes de guardarla
            newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

            // 4. Asignar un rol por defecto
            newUser.setRoles(Collections.singletonList("ROLE_USER")); // Un usuario registrado por defecto es USER

            // 5. Guardar el usuario en la base de datos
            return userRepository.save(newUser);
        }
    }

