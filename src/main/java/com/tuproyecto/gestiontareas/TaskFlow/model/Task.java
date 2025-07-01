package com.tuproyecto.gestiontareas.TaskFlow.model;

import jakarta.persistence.*; // Importa las anotaciones de JPA
import lombok.Getter;       // Anotaciones de Lombok
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime; // Para la fecha de creación/actualización

/**
 * Entidad que representa una Tarea en la aplicación TaskFlow.
 * Se mapeará a una tabla en la base de datos.
 */
@Entity // Indica que esta clase es una entidad JPA y se mapeará a una tabla de BD.
@Table(name = "tasks") // Opcional: Define el nombre de la tabla en la BD. Si no se especifica, usa el nombre de la clase.
@Getter // Anotación de Lombok para generar automáticamente los getters para todos los campos.
@Setter // Anotación de Lombok para generar automáticamente los setters para todos los campos.
@NoArgsConstructor // Anotación de Lombok para generar un constructor sin argumentos. Requerido por JPA.
@AllArgsConstructor // Anotación de Lombok para generar un constructor con todos los argumentos. Útil para pruebas.

public class Task {

    @Id // Marca el campo como la clave primaria de la tabla.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura la generación automática del ID.
    // IDENTITY es común para bases de datos que auto-incrementan.
    private Long id; // Identificador único de la tarea

    @Column(nullable = false) // Indica que la columna no puede ser nula en la BD.
    private String title; // Título o nombre de la tarea

    @Column(length = 1000) // Limita la longitud del campo en la BD.
    private String description; // Descripción detallada de la tarea

    @Column(nullable = false)
    private boolean completed = false; // Estado de la tarea (true si está completada, false por defecto)

    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate; // Fecha y hora de creación de la tarea

    @Column(name = "last_modified_date") // Fecha de última actualización
    private LocalDateTime lastModifiedDate;

    @Column(name = "due_date") // Fecha límite para completar la tarea (puede ser nula)
    private LocalDateTime dueDate;

    // Métodos de ciclo de vida de JPA para gestionar las fechas automáticamente
    @PrePersist // Se ejecuta antes de que la entidad sea persistida (guardada por primera vez)
    protected void onCreate() {
        this.creationDate = LocalDateTime.now();
        this.lastModifiedDate = LocalDateTime.now();
    }

    // Podrías añadir un @PreUpdate si también quieres una fecha de última actualización
    @PreUpdate
    protected void onUpdate() {
       this.lastModifiedDate = LocalDateTime.now();
     }
}
