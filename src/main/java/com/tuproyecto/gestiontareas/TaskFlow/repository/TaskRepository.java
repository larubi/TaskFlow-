package com.tuproyecto.gestiontareas.TaskFlow.repository;

import com.tuproyecto.gestiontareas.TaskFlow.model.Task; // Importa tu clase Task
import org.springframework.data.jpa.repository.JpaRepository; // Importa JpaRepository
import org.springframework.stereotype.Repository; // Importa la anotación @Repository

/**
 * Interfaz de repositorio para la entidad Task.
 * Proporciona métodos CRUD (Crear, Leer, Actualizar, Borrar) y más,
 * automáticamente generados por Spring Data JPA.
 */
@Repository // Indica a Spring que esta interfaz es un componente de repositorio gestionado por él.
public interface TaskRepository extends JpaRepository<Task, Long> {
    // JpaRepository toma dos parámetros:
    // 1. La entidad con la que trabajará el repositorio (Task).
    // 2. El tipo de datos de la clave primaria de esa entidad (Long, ya que el ID de Task es Long).

    // Aquí puedes añadir métodos personalizados si los necesitas, por ejemplo:
    // List<Task> findByCompleted(boolean completed);
    // List<Task> findByTitleContaining(String title);
}
