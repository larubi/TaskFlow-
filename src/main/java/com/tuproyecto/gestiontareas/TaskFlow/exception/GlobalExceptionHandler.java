package com.tuproyecto.gestiontareas.TaskFlow.exception;

import com.tuproyecto.gestiontareas.TaskFlow.security.jwt.dto.ErrorResponse; // Importa tu DTO de error
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException; // Para errores de validación
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest; // Para obtener detalles de la petición
import org.springframework.validation.FieldError; // Para errores de campo en validación

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 1. Manejar excepciones de Validación (@Valid en DTO)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {

        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    String fieldName = (error instanceof FieldError) ? ((FieldError) error).getField() : error.getObjectName();
                    String errorMessage = error.getDefaultMessage();
                    return fieldName + ": " + errorMessage;
                })
                .collect(Collectors.toList());

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Errores de validación en la petición.",
                request.getDescription(false),
                errors
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // 2. Manejar excepciones personalizadas
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // Por defecto, es un error del servidor

        // Si el mensaje de la excepción indica un conflicto, cambia el status
        if (ex.getMessage().contains("ya está en uso") || ex.getMessage().contains("ya está registrado")) {
            status = HttpStatus.CONFLICT; // 409 Conflict
        }

        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(), // "Internal Server Error" o "Conflict"
                ex.getMessage(), // Mensaje de tu excepción
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, status);
    }

    // 3. Manejador para cualquier otra excepción no capturada
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Ocurrió un error inesperado en el servidor.",
                request.getDescription(false)
        );
        // loguear la excepción para depuración
        ex.printStackTrace();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}