package co.edu.uniquindio.GestionRiesgos.Enums;

/**
 * Enum que representa los niveles de urgencia en el sistema
 * 
 */
public enum NivelUrgencia {
    BAJA(1, "Baja", "Verde"),
    MEDIA(2, "Media", "Amarillo"),
    ALTA(3, "Alta", "Naranja"),
    CRITICA(4, "Crítica", "Rojo");
    
    private final int valor;
    private final String descripcion;
    private final String color;
    
    NivelUrgencia(int valor, String descripcion, String color) {
        this.valor = valor;
        this.descripcion = descripcion;
        this.color = color;
    }
    
    public int getValor() {
        return valor;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public String getColor() {
        return color;
    }
}

