package co.edu.uniquindio.GestionRiesgos.Enums;

/**
 * Representa los diferentes niveles de urgencia que pueden asignarse dentro del sistema
 * de gestión de emergencias o incidencias.
 * 
 * Cada nivel incluye:
 * - Un valor numérico que determina su prioridad.
 * - Una descripción en texto del nivel.
 * - Un color asociado para facilitar su representación visual.
 * 
 * Los niveles se organizan desde menor a mayor urgencia:
 * BAJA (1)  → Color Verde  
 * MEDIA (2) → Color Amarillo  
 * ALTA (3)  → Color Naranja  
 * CRITICA (4) → Color Rojo
 */
public enum NivelUrgencia {

    /** Urgencia de nivel bajo: situaciones sin riesgo inmediato. */
    BAJA(1, "Baja", "Verde"),

    /** Urgencia de nivel medio: requiere atención moderada. */
    MEDIA(2, "Media", "Amarillo"),

    /** Urgencia alta: necesidad de atención prioritaria. */
    ALTA(3, "Alta", "Naranja"),

    /** Urgencia crítica: requiere respuesta inmediata. */
    CRITICA(4, "Crítica", "Rojo");
    
    /** Valor numérico asociado al nivel de urgencia. */
    private final int valor;

    /** Descripción textual del nivel de urgencia. */
    private final String descripcion;

    /** Color representativo del nivel de urgencia. */
    private final String color;
    
    /**
     * Constructor del enum.
     * 
     * @param valor Valor numérico de prioridad del nivel.
     * @param descripcion Descripción del nivel.
     * @param color Color asociado al nivel para uso visual.
     */
    NivelUrgencia(int valor, String descripcion, String color) {
        this.valor = valor;
        this.descripcion = descripcion;
        this.color = color;
    }
    
    /**
     * Obtiene el valor numérico del nivel de urgencia.
     * 
     * @return valor de prioridad.
     */
    public int getValor() {
        return valor;
    }
    
    /**
     * Obtiene la descripción textual del nivel de urgencia.
     * 
     * @return la descripción del nivel.
     */
    public String getDescripcion() {
        return descripcion;
    }
    
    /**
     * Obtiene el color representativo del nivel de urgencia.
     * 
     * @return color asociado.
     */
    public String getColor() {
        return color;
    }
}

