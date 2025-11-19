package co.edu.uniquindio.GestionRiesgos;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Manejador global de excepciones para la API REST.
 * <p>
 * Esta clase captura las excepciones más comunes y devuelve respuestas JSON
 * amigables con información de error, código HTTP y sugerencias cuando aplica.
 * También gestiona silenciosamente ciertos recursos estáticos para evitar ruido
 * en los logs.
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja errores 405 - Método HTTP no permitido.
     *
     * @param ex la excepción capturada de tipo HttpRequestMethodNotSupportedException
     * @return ResponseEntity con información del error y métodos permitidos
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleMethodNotAllowed(
            HttpRequestMethodNotSupportedException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("error", "METHOD_NOT_ALLOWED");
        error.put("status", 405);
        String method = ex.getMethod() != null ? ex.getMethod() : "DESCONOCIDO";
        error.put("message", "El método HTTP '" + method + "' no está permitido para este endpoint.");

        String[] supportedMethods = ex.getSupportedMethods();
        if (supportedMethods != null && supportedMethods.length > 0) {
            error.put("allowedMethods", supportedMethods);
            error.put("suggestion", "Usa uno de los siguientes métodos: " +
                    String.join(", ", supportedMethods));
        }

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(error);
    }

    /**
     * Maneja errores 404 - Endpoint no encontrado.
     *
     * @param ex la excepción capturada de tipo NoHandlerFoundException
     * @return ResponseEntity con información del error y sugerencia
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NoHandlerFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("error", "NOT_FOUND");
        error.put("status", 404);
        error.put("message", "El endpoint '" + ex.getRequestURL() + "' no existe.");
        error.put("suggestion", "Verifica la URL y asegúrate de que el endpoint esté correctamente escrito.");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Maneja errores de recursos estáticos no encontrados (favicon, robots.txt, etc.).
     * <p>
     * Ignora silenciosamente recursos comunes solicitados automáticamente por navegadores.
     * Para otros recursos estáticos, devuelve 404.
     * </p>
     *
     * @param ex la excepción capturada de tipo NoResourceFoundException
     * @return ResponseEntity indicando el estado del recurso
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(NoResourceFoundException ex) {
        String resourcePath = ex.getResourcePath();

        // Ignorar recursos comunes
        if (resourcePath != null && (
                resourcePath.equals("/favicon.ico") ||
                        resourcePath.startsWith("/.well-known/") ||
                        resourcePath.startsWith("/apple-touch-icon") ||
                        resourcePath.startsWith("/robots.txt") ||
                        resourcePath.startsWith("/sitemap.xml")
        )) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("error", "RESOURCE_NOT_FOUND");
        error.put("status", 404);
        error.put("message", "Recurso estático no encontrado: " + resourcePath);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Maneja errores de validación de argumentos enviados a endpoints.
     *
     * @param ex la excepción capturada de tipo MethodArgumentNotValidException
     * @return ResponseEntity con detalles de los campos inválidos
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("error", "VALIDATION_ERROR");
        error.put("status", 400);
        error.put("message", "Error de validación en los datos enviados.");

        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
                validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage())
        );
        error.put("validationErrors", validationErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Maneja errores de lógica de negocio lanzados mediante IllegalArgumentException.
     *
     * @param ex la excepción capturada de tipo IllegalArgumentException
     * @return ResponseEntity con información del error
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("error", "BAD_REQUEST");
        error.put("status", 400);
        error.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Maneja errores de referencia nula (NullPointerException).
     *
     * @param ex la excepción capturada de tipo NullPointerException
     * @return ResponseEntity con información del error
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Map<String, Object>> handleNullPointer(NullPointerException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("error", "INTERNAL_ERROR");
        error.put("status", 500);
        error.put("message", "Error interno del servidor: Referencia nula detectada.");

        // Mostrar detalles solo en desarrollo
        if (System.getProperty("spring.profiles.active") == null ||
                System.getProperty("spring.profiles.active").equals("dev")) {
            error.put("details", ex.getMessage());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Maneja cualquier excepción no capturada por los demás handlers.
     *
     * @param ex la excepción capturada
     * @return ResponseEntity con información genérica del error y detalles en desarrollo
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("error", "INTERNAL_SERVER_ERROR");
        error.put("status", 500);
        error.put("message", "Ha ocurrido un error inesperado en el servidor.");

        if (System.getProperty("spring.profiles.active") == null ||
                System.getProperty("spring.profiles.active").equals("dev")) {
            error.put("details", ex.getMessage());
            error.put("exceptionType", ex.getClass().getSimpleName());
        }

        // Log del error
        System.err.println("Error no manejado: " + ex.getClass().getName());
        System.err.println("Mensaje: " + (ex.getMessage() != null ? ex.getMessage() : "Sin mensaje"));
        if (System.getProperty("spring.profiles.active") == null ||
                System.getProperty("spring.profiles.active").equals("dev")) {
            ex.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}



