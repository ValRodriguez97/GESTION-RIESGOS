package co.edu.uniquindio.GestionRiesgos.Estructuras;

import co.edu.uniquindio.GestionRiesgos.Enums.NivelUrgencia;

import java.util.Objects;

/**
 * Clase que representa un nodo en el grafo dirigido del sistema
 * Un nodo puede ser una ubicación, zona de emergencia, etc.
 */
public class Nodo {
    private String id;
    private String nombre;
    private double coordenadaX;
    private double coordenadaY;
    private NivelUrgencia nivelUrgencia;
    private int capacidadMaxima;
    private int personasActuales;
    private TipoNodo tipo;
    private boolean activo;
    
    /**
     * Enum para representar el tipo de nodo
     */
    public enum TipoNodo {
        CIUDAD("Ciudad"),
        REFUGIO("Refugio"),
        CENTRO_AYUDA("Centro de Ayuda"),
        HOSPITAL("Hospital"),
        BASE_OPERACIONES("Base de Operaciones"),
        ZONA_EVACUACION("Zona de Evacuación"),
        PUNTO_EMERGENCIA("Punto de Emergencia");
        
        private final String descripcion;
        
        TipoNodo(String descripcion) {
            this.descripcion = descripcion;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
    }
    
    public Nodo() {
        this.activo = true;
        this.personasActuales = 0;
        this.nivelUrgencia = NivelUrgencia.BAJA;
    }
    
    public Nodo(String id, String nombre, double coordenadaX, double coordenadaY, 
                TipoNodo tipo, int capacidadMaxima) {
        this();
        this.id = id;
        this.nombre = nombre;
        this.coordenadaX = coordenadaX;
        this.coordenadaY = coordenadaY;
        this.tipo = tipo;
        this.capacidadMaxima = capacidadMaxima;
    }
    
    /**
     * Calcula el porcentaje de ocupación del nodo
     */
    public double calcularPorcentajeOcupacion() {
        if (capacidadMaxima == 0) return 0.0;
        return (double) personasActuales / capacidadMaxima * 100;
    }
    
    /**
     * Verifica si el nodo tiene capacidad disponible
     */
    public boolean tieneCapacidadDisponible() {
        return personasActuales < capacidadMaxima;
    }
    
    /**
     * Calcula la capacidad disponible
     */
    public int obtenerCapacidadDisponible() {
        return Math.max(0, capacidadMaxima - personasActuales);
    }
    
    /**
     * Calcula la distancia euclidiana a otro nodo
     */
    public double calcularDistancia(Nodo otro) {
        double deltaX = this.coordenadaX - otro.coordenadaX;
        double deltaY = this.coordenadaY - otro.coordenadaY;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
    
    /**
     * Verifica si el nodo está en estado crítico
     */
    public boolean estaEnEstadoCritico() {
        return nivelUrgencia == NivelUrgencia.CRITICA || (nivelUrgencia == NivelUrgencia.ALTA && calcularPorcentajeOcupacion() > 90);
    }
    
    /**
     * Actualiza el nivel de urgencia basado en la ocupación
     */
    public void actualizarNivelUrgencia() {
        double porcentajeOcupacion = calcularPorcentajeOcupacion();
        
        if (porcentajeOcupacion >= 95) {
            this.nivelUrgencia = NivelUrgencia.CRITICA;
        } else if (porcentajeOcupacion >= 80) {
            this.nivelUrgencia = NivelUrgencia.ALTA;
        } else if (porcentajeOcupacion >= 60) {
            this.nivelUrgencia = NivelUrgencia.MEDIA;
        } else {
            this.nivelUrgencia = NivelUrgencia.BAJA;
        }
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public double getCoordenadaX() {
        return coordenadaX;
    }
    
    public void setCoordenadaX(double coordenadaX) {
        this.coordenadaX = coordenadaX;
    }
    
    public double getCoordenadaY() {
        return coordenadaY;
    }
    
    public void setCoordenadaY(double coordenadaY) {
        this.coordenadaY = coordenadaY;
    }
    
    public NivelUrgencia getNivelUrgencia() {
        return nivelUrgencia;
    }
    
    public void setNivelUrgencia(NivelUrgencia nivelUrgencia) {
        this.nivelUrgencia = nivelUrgencia;
    }
    
    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }
    
    public void setCapacidadMaxima(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }
    
    public int getPersonasActuales() {
        return personasActuales;
    }
    
    public void setPersonasActuales(int personasActuales) {
        this.personasActuales = Math.max(0, Math.min(personasActuales, capacidadMaxima));
        actualizarNivelUrgencia();
    }
    
    public TipoNodo getTipo() {
        return tipo;
    }
    
    public void setTipo(TipoNodo tipo) {
        this.tipo = tipo;
    }
    
    public boolean isActivo() {
        return activo;
    }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nodo nodo = (Nodo) o;
        return Objects.equals(id, nodo.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return String.format("Nodo{id='%s', nombre='%s', tipo=%s, urgencia=%s, ocupacion=%.1f%%}", id, nombre, tipo.getDescripcion(), nivelUrgencia.getDescripcion(), calcularPorcentajeOcupacion());
    }
}

