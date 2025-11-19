package co.edu.uniquindio.GestionRiesgos.Enums;

/**
 * Representa los diferentes tipos de recursos disponibles dentro del sistema
 * de gestión de emergencias.
 * 
 * Cada tipo de recurso cuenta con:
 * - Una descripción textual para su identificación.
 * - Una prioridad base que indica su importancia relativa al momento de
 *   asignar o distribuir recursos.
 * 
 * Tipos definidos:
 * - ALIMENTOS  → Prioridad base: 3  
 * - MEDICINAS  → Prioridad base: 4  
 * - EQUIPOS    → Prioridad base: 5  
 */
public enum TipoRecurso {

    /** Recursos alimentarios esenciales para personas afectadas. */
    ALIMENTOS("Alimentos", 3),

    /** Medicamentos y suministros para atención médica. */
    MEDICINAS("Medicinas", 4),

    /** Equipos y herramientas de apoyo para labores de emergencia. */
    EQUIPOS("Equipos", 5);
    
    /** Descripción legible del tipo de recurso. */
    private final String descripcion;

    /** Prioridad base asignada al tipo de recurso. */
    private final int prioridadBase;
    
    /**
     * Constructor del enum TipoRecurso.
     *
     * @param descripcion texto descriptivo del recurso.
     * @param prioridadBase nivel de prioridad inicial del recurso.
     */
    TipoRecurso(String descripcion, int prioridadBase) {
        this.descripcion = descripcion;
        this.prioridadBase = prioridadBase;
    }
    
    /**
     * Obtiene la descripción textual del recurso.
     *
     * @return descripción del recurso.
     */
    public String getDescripcion() {
        return descripcion;
    }
    
    /**
     * Obtiene la prioridad base asociada al recurso.
     *
     * @return prioridad base.
     */
    public int getPrioridadBase() {
        return prioridadBase;
    }
}

