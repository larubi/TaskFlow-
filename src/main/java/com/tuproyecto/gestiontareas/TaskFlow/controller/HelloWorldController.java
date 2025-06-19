package com.tuproyecto.gestiontareas.TaskFlow.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST básico para probar que la API está operativa y para mostrar
 * un ejemplo de cómo crear endpoints.
 */
@RestController // Esta anotación combina @Controller y @ResponseBody.
// Indica que los métodos de esta clase devuelven datos (ej. JSON, String),
// no vistas HTML. Ideal para APIs REST.

public class HelloWorldController {
    /**
     * Endpoint público que responde a la raíz de la aplicación.
     * Es útil para dar un mensaje de bienvenida antes de la autenticación.
     * Accesible en: http://localhost:8080/
     */
    @GetMapping("/")
    public String welcomePublic() {
        return "¡Bienvenido a la API TaskFlow! Por favor, inicia sesión para acceder a las funcionalidades.";
    }

    /**
     * Endpoint de ejemplo para verificar el estado de la API.
     * Accesible en: http://localhost:8080/api/status
     * Después de iniciar sesión con 'user' y la contraseña generada.
     */
    @GetMapping("/api/status")
    public String getStatus() {
        // En una API REST, es común devolver un JSON. Usamos String para simplicidad.
        return "{\"status\": \"UP\", \"message\": \"TaskFlow API está operativa.\"}";
    }

    /**
     * Endpoint de ejemplo protegido.
     * Accesible en: http://localhost:8080/api/hello
     * Solo después de iniciar sesión con 'user' y la contraseña generada.
     */
    @GetMapping("/api/hello")
    public String sayHello() {
        return "¡Hola desde tu API TaskFlow! El endpoint /api/hello funciona y estás autenticado.";
    }


}
