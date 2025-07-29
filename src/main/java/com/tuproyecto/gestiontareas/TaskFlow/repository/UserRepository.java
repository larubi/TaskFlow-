package com.tuproyecto.gestiontareas.TaskFlow.repository;

import com.tuproyecto.gestiontareas.TaskFlow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Método para buscar un usuario por su nombre de usuario
    Optional<User> findByUsername(String username);

    // Método para buscar un usuario por su email
    Optional<User> findByEmail(String email);
}
