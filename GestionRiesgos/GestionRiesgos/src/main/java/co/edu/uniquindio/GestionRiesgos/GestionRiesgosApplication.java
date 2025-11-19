package co.edu.uniquindio.GestionRiesgos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación de gestión de riesgos.
 * <p>
 * Esta clase arranca el contexto de Spring Boot y pone en marcha
 * la API REST de gestión de desastres y riesgos.
 * </p>
 */
@SpringBootApplication
public class GestionRiesgosApplication {

    /**
     * Método principal que inicia la aplicación Spring Boot.
     *
     * @param args argumentos de línea de comandos (opcional)
     */
    public static void main(String[] args) {
        SpringApplication.run(GestionRiesgosApplication.class, args);
    }
}

