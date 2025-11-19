package co.edu.uniquindio.GestionRiesgos.Estructuras;

import co.edu.uniquindio.GestionRiesgos.Enums.NivelUrgencia;

import java.util.Objects;

/**
 * Clase que representa un nodo en el grafo dirigido del sistema de gestión de desastres.
 * Un nodo puede ser una ubicación, zona de emergencia, hospital, refugio, etc.
 */
public class Nodo {

    // ==============================
    //          ATRIBUTOS
    // ==============================

    /** Identificador único del nodo */
    private String id;

    /** Nombre del nodo */
    private String nombre;

    /** Coordenada X del nodo (para cálculo de distancia) */
    private double coordenadaX;

    /** Coordenada Y del nodo (para cálculo de distancia) */
    private double coordenadaY;

    /** Nivel de urgencia actual del nodo */
    private NivelUrgencia nivelUrgencia;

    /** Capacidad máxima de personas que puede albergar el nodo */
    private int capacidadMaxima;

    /** Número actual de personas en el nodo */
    private int personasActuales;

    /** Tipo de nodo (Ciudad, Refugio, Hospital, etc.) */
    private TipoNodo tipo;

    /** Estado de actividad del nodo */
    private boolean activo;

    // ==============================
    //            ENUMS
    // ==============================

    /**
     * Enum que representa los posibles tipos de nodo.
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

        /**
         * Obtiene la descripción legible del tipo de nodo.
         * 
         * @return descripción del nodo
         */
        public String getDescripcion() {
            return descripcion;
        }
    }

    // ==============================
    //          CONSTRUCTORES
    // ==============================

    /**
     * Constructor por defecto.
     * Inicializa el nodo como activo, con nivel de urgencia baja y sin personas.
     */
    public Nodo() {
        this.activo = true;
        this.personasActuales = 0;
        this.nivelUrgencia = NivelUrgencia.BAJA;
    }

    /**
     * Constructor que inicializa todos los atributos principales del nodo.
     * 
     * @param id Identificador único
     * @param nombre Nombre del nodo
     * @param coordenadaX Coordenada X
     * @param coordenadaY Coordenada Y
     * @param tipo Tipo de nodo
     * @param capacidadMaxima Capacidad máxima de personas
     */
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

    // ==============================
    //          MÉTODOS
    // ==============================

    /**
     * Calcula el porcentaje de ocupación actual del nodo.
     * 
     * @return Porcentaje de ocupación (0 a 100)
     */
    public double calcularPorcentajeOcupacion() {
        if (capacidadMaxima == 0) return 0.0;
        return (double) personasActuales / capacidadMaxima * 100;
    }

    /**
     * Verifica si el nodo tiene capacidad disponible para recibir más personas.
     * 
     * @return true si hay capacidad disponible, false si está lleno
     */
    public boolean tieneCapacidadDisponible() {
        return personasActuales < capacidadMaxima;
    }

    /**
     * Obtiene el número de personas que aún se pueden alojar en el nodo.
     * 
     * @return Capacidad disponible (>=0)
     */
    public int obtenerCapacidadDisponible() {
        return Math.max(0, capacidadMaxima - personasActuales);
    }

    /**
     * Calcula la distancia euclidiana a otro nodo.
     * 
     * @param otro Nodo destino
     * @return Distancia euclidiana
     */
    public double calcularDistancia(Nodo otro) {
        double deltaX = this.coordenadaX - otro.coordenadaX;
        double deltaY = this.coordenadaY - otro.coordenadaY;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    /**
     * Verifica si el nodo se encuentra en estado crítico.
     * Un nodo es crítico si su nivel de urgencia es crítico o alto con ocupación > 90%.
     * 
     * @return true si está en estado crítico, false de lo contrario
     */
    public boolean estaEnEstadoCritico() {
        return nivelUrgencia == NivelUrgencia.CRITICA || 
               (nivelUrgencia == NivelUrgencia.ALTA && calcularPorcentajeOcupacion() > 90);
    }

    /**
     * Actualiza automáticamente el nivel de urgencia basado en la ocupación actual.
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

    // ==============================
    //            GETTERS & SETTERS
    // ==============================

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getCoordenadaX() { return coordenadaX; }
    public void setCoordenadaX(double coordenadaX) { this.coordenadaX = coordenadaX; }

    public double getCoordenadaY() { return coordenadaY; }
    public void setCoordenadaY(double coordenadaY) { this.coordenadaY = coordenadaY; }

    public NivelUrgencia getNivelUrgencia() { return nivelUrgencia; }
    public void setNivelUrgencia(NivelUrgencia nivelUrgencia) { this.nivelUrgencia = nivelUrgencia; }

    public int getCapacidadMaxima() { return capacidadMaxima; }
    public void setCapacidadMaxima(int capacidadMaxima) { this.capacidadMaxima = capacidadMaxima; }

    public int getPersonasActuales() { return personasActuales; }
    public void setPersonasActuales(int personasActuales) {
        this.personasActuales = Math.max(0, Math.min(personasActuales, capacidadMaxima));
        actualizarNivelUrgencia();
    }

    public TipoNodo getTipo() { return tipo; }
    public void setTipo(TipoNodo tipo) { this.tipo = tipo; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    // ==============================
    //            OVERRIDE
    // ==============================

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
        return String.format("Nodo{id='%s', nombre='%s', tipo=%s, urgencia=%s, ocupacion=%.1f%%}", 
                id, nombre, tipo.getDescripcion(), nivelUrgencia.getDescripcion(), calcularPorcentajeOcupacion());
    }
}


