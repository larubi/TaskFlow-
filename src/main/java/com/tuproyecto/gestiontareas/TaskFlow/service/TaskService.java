package com.tuproyecto.gestiontareas.TaskFlow.service;

import com.tuproyecto.gestiontareas.TaskFlow.model.Task;
import com.tuproyecto.gestiontareas.TaskFlow.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired; // Para inyección de dependencias
import org.springframework.stereotype.Service; // Anotación para indicar que es un servicio

import java.util.List;
import java.util.Optional; // Para manejar casos donde un elemento puede no encontrarse

/**
 * Servicio que encapsula la lógica de negocio para la gestión de Tareas.
 * Interactúa con TaskRepository para el acceso a datos.
 */
@Service // Indica a Spring que esta clase es un componente de servicio gestionado por él.
public class TaskService {

    private final TaskRepository taskRepository; // Inyectaremos el repositorio aquí

    // Inyección de dependencias a través del constructor
    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Guarda una nueva tarea o actualiza una existente.
     * @param task La tarea a guardar/actualizar.
     * @return La tarea guardada.
     */
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    /**
     * Obtiene una tarea por su ID.
     * @param id El ID de la tarea.
     * @return Un Optional que contiene la tarea si se encuentra, o vacío si no.
     */
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    /**
     * Obtiene todas las tareas.
     * @return Una lista de todas las tareas.
     */
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    /**
     * Elimina una tarea por su ID.
     * @param id El ID de la tarea a eliminar.
     */
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    /**
     * Actualiza el estado de completado de una tarea.
     * @param id El ID de la tarea.
     * @param completed El nuevo estado de completado.
     * @return La tarea actualizada si se encuentra, o Optional.empty() si no.
     */
    public Optional<Task> updateTaskCompletion(Long id, boolean completed) {
        Optional<Task> existingTaskOptional = taskRepository.findById(id);
        if (existingTaskOptional.isPresent()) {
            Task existingTask = existingTaskOptional.get();
            existingTask.setCompleted(completed);
            // El @PreUpdate en la entidad Task se encargará de actualizar lastModifiedDate.
            return Optional.of(taskRepository.save(existingTask));
        }
        return Optional.empty();
    }
}
