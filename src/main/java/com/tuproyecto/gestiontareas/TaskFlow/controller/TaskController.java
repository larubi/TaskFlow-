package com.tuproyecto.gestiontareas.TaskFlow.controller;


import com.tuproyecto.gestiontareas.TaskFlow.model.Task;
import com.tuproyecto.gestiontareas.TaskFlow.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // Para los códigos de estado HTTP
import org.springframework.http.ResponseEntity; // Para construir respuestas HTTP completas
import org.springframework.web.bind.annotation.*; // Anotaciones REST comunes

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de Tareas.
 * Define los endpoints HTTP para realizar operaciones CRUD sobre las tareas.
 */
@RestController // Indica que esta clase es un controlador REST y manejará solicitudes HTTP.
@RequestMapping("/api/tasks") // Define la ruta base para todos los endpoints de este controlador.
// Todos los métodos aquí responderán a /api/tasks/...
public class TaskController {

    private final TaskService taskService; // Inyectaremos el servicio de tareas

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Endpoint para crear una nueva tarea.
     * TODO HTTP: POST
     * Ruta: /api/tasks
     * Cuerpo de la solicitud: JSON con los datos de la tarea
     * Respuesta: 201 Created y la tarea creada en formato JSON.
     */
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task savedTask = taskService.saveTask(task);
        return new ResponseEntity<>(savedTask, HttpStatus.CREATED); // Devuelve 201 Created
    }

    /**
     * Endpoint para obtener todas las tareas.
     * Método HTTP: GET
     * Ruta: /api/tasks
     * Respuesta: 200 OK y una lista de tareas en formato JSON.
     */
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK); // Devuelve 200 OK
    }

    /**
     * Endpoint para obtener una tarea por su ID.
     * Método HTTP: GET
     * Ruta: /api/tasks/{id}
     * Respuesta: 200 OK y la tarea en JSON si se encuentra, o 404 Not Found si no.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Optional<Task> task = taskService.getTaskById(id);
        return task.map(value -> new ResponseEntity<>(value, HttpStatus.OK)) // Si existe, devuelve 200 OK
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Si no, devuelve 404 Not Found
    }

    /**
     * Endpoint para actualizar una tarea existente.
     * Método HTTP: PUT
     * Ruta: /api/tasks/{id}
     * Cuerpo de la solicitud: JSON con los datos actualizados de la tarea.
     * Respuesta: 200 OK y la tarea actualizada si se encuentra, o 404 Not Found si no.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
        Optional<Task> existingTaskOptional = taskService.getTaskById(id);
        if (existingTaskOptional.isPresent()) {
            Task existingTask = existingTaskOptional.get();
            existingTask.setTitle(taskDetails.getTitle());
            existingTask.setDescription(taskDetails.getDescription());
            existingTask.setCompleted(taskDetails.isCompleted());
            existingTask.setDueDate(taskDetails.getDueDate()); // Actualiza también la fecha de vencimiento

            Task updatedTask = taskService.saveTask(existingTask); // save() actualiza si el ID existe
            return new ResponseEntity<>(updatedTask, HttpStatus.OK); // Devuelve 200 OK
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Devuelve 404 Not Found
    }

    /**
     * Endpoint para actualizar parcialmente el estado de completado de una tarea.
     * Método HTTP: PATCH
     * Ruta: /api/tasks/{id}/complete
     * Cuerpo de la solicitud: JSON con {"completed": true/false}
     * Respuesta: 200 OK y la tarea actualizada si se encuentra, o 404 Not Found si no.
     */
    @PatchMapping("/{id}/complete")
    public ResponseEntity<Task> completeTask(@PathVariable Long id, @RequestBody boolean completed) {
        Optional<Task> updatedTask = taskService.updateTaskCompletion(id, completed);
        return updatedTask.map(task -> new ResponseEntity<>(task, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint para eliminar una tarea.
     * Método HTTP: DELETE
     * Ruta: /api/tasks/{id}
     * Respuesta: 204 No Content si se elimina con éxito, o 404 Not Found si no existe.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTask(@PathVariable Long id) {
        Optional<Task> task = taskService.getTaskById(id);
        if (task.isPresent()) {
            taskService.deleteTask(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Devuelve 204 No Content (eliminado con éxito, sin contenido)
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Devuelve 404 Not Found
    }
}
