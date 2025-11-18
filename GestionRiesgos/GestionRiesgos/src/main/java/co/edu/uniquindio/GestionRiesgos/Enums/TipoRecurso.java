package co.edu.uniquindio.GestionRiesgos.Enums;

/**
 * Enum que representa los tipos de recursos en el sistema
 * 
 */
public enum TipoRecurso {
    ALIMENTOS("Alimentos", 3),
    MEDICINAS("Medicinas", 4),
    EQUIPOS("Equipos", 5);
    
    private final String descripcion;
    private final int prioridadBase;
    
    TipoRecurso(String descripcion, int prioridadBase) {
        this.descripcion = descripcion;
        this.prioridadBase = prioridadBase;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public int getPrioridadBase() {
        return prioridadBase;
    }
}
