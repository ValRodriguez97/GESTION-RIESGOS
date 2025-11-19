package co.edu.uniquindio.GestionRiesgos.Enums;

/**
 * Representa los diferentes tipos de rutas que pueden utilizarse en el sistema
 * para realizar el transporte o distribución de recursos.
 * 
 * Cada tipo de ruta cuenta con una descripción textual que facilita su uso en la
 * interfaz y en la lógica de asignación de rutas.
 * 
 * Tipos definidos:
 * - TERRESTRE → Rutas por vía terrestre (carreteras, caminos).
 * - AEREA     → Rutas por vía aérea (helicópteros, aviones).
 * - MARITIMA  → Rutas marítimas o fluviales (barcos, lanchas).
 */
public enum TipoRuta {

    /** Ruta realizada por tierra mediante vehículos terrestres. */
    TERRESTRE("Terrestre"),

    /** Ruta realizada por aire utilizando aeronaves. */
    AEREA("Aérea"),

    /** Ruta realizada por mar o ríos con embarcaciones. */
    MARITIMA("Marítima");
    
    /** Descripción textual del tipo de ruta. */
    private final String descripcion;
    
    /**
     * Constructor del enum TipoRuta.
     *
     * @param descripcion texto descriptivo del tipo de ruta.
     */
    TipoRuta(String descripcion) {
        this.descripcion = descripcion;
    }
    
    /**
     * Obtiene la descripción legible del tipo de ruta.
     *
     * @return descripción del tipo de ruta.
     */
    public String getDescripcion() {
        return descripcion;
    }
}

