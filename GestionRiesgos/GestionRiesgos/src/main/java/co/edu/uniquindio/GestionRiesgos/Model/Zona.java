package co.edu.uniquindio.GestionRiesgos.Model;

import co.edu.uniquindio.GestionRiesgos.Enums.NivelUrgencia;

import java.util.Objects;

/**
 * Clase que representa una zona en el sistema de gestión de desastres.
 * Una zona es un área geográfica que puede estar afectada por desastres
 * y tiene atributos como nivel de riesgo, población afectada y radio de cobertura.
 */
public class Zona {

    /** Identificador único de la zona */
    private String id;

    /** Nivel de urgencia o riesgo de la zona */
    private NivelUrgencia nivelRiesgo;

    /** Nombre de la zona */
    private String nombre;

    /** Descripción adicional de la zona */
    private String descripcion;

    /** Coordenada X de la ubicación */
    private double coordenadaX;

    /** Coordenada Y de la ubicación */
    private double coordenadaY;

    /** Cantidad de población afectada por desastres */
    private int poblacionAfectada;

    /** Indica si la zona está activa en el sistema */
    private boolean activa;

    /** Radio de cobertura en metros */
    private int radio;

    // -------------------- Constructores --------------------

    /**
     * Constructor por defecto.
     * Inicializa la zona como activa, nivel de riesgo bajo, población afectada 0
     * y radio por defecto de 500 metros.
     */
    public Zona() {
        this.activa = true;
        this.poblacionAfectada = 0;
        this.nivelRiesgo = NivelUrgencia.BAJA;
        this.radio = 500; // Radio por defecto: 500 metros
    }

    /**
     * Constructor con id, nombre y nivel de riesgo.
     *
     * @param id Identificador único de la zona
     * @param nombre Nombre de la zona
     * @param nivelRiesgo Nivel de urgencia o riesgo
     */
    public Zona(String id, String nombre, NivelUrgencia nivelRiesgo) {
        this();
        this.id = id;
        this.nombre = nombre;
        this.nivelRiesgo = nivelRiesgo;
    }

    // -------------------- Métodos funcionales --------------------

    /**
     * Verifica si la zona está en estado crítico.
     *
     * @return true si el nivel de riesgo es crítico o alto con población afectada mayor a 1000
     */
    public boolean estaEnEstadoCritico() {
        return nivelRiesgo == NivelUrgencia.CRITICA ||
               (nivelRiesgo == NivelUrgencia.ALTA && poblacionAfectada > 1000);
    }

    /**
     * Actualiza el nivel de riesgo basado en la población afectada.
     * No se llama automáticamente desde setPoblacionAfectada para respetar la configuración manual.
     */
    public void actualizarNivelRiesgo() {
        if (poblacionAfectada > 5000) {
            this.nivelRiesgo = NivelUrgencia.CRITICA;
        } else if (poblacionAfectada > 2000) {
            this.nivelRiesgo = NivelUrgencia.ALTA;
        } else if (poblacionAfectada > 500) {
            this.nivelRiesgo = NivelUrgencia.MEDIA;
        } else {
            this.nivelRiesgo = NivelUrgencia.BAJA;
        }
    }

    /**
     * Calcula la prioridad de evacuación de la zona.
     *
     * @return Prioridad basada en nivel de riesgo y población afectada
     */
    public int calcularPrioridadEvacuacion() {
        int prioridad = nivelRiesgo.getValor();

        if (poblacionAfectada > 10000) prioridad += 3;
        else if (poblacionAfectada > 5000) prioridad += 2;
        else if (poblacionAfectada > 1000) prioridad += 1;

        return prioridad;
    }

    // -------------------- Getters y Setters --------------------

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public NivelUrgencia getNivelRiesgo() { return nivelRiesgo; }
    public void setNivelRiesgo(NivelUrgencia nivelRiesgo) { this.nivelRiesgo = nivelRiesgo; }

    /** Alias para compatibilidad con ColaPrioridad */
    public NivelUrgencia getNivelUrgencia() { return nivelRiesgo; }

    /** Alias para compatibilidad con ColaPrioridad */
    public void setNivelUrgencia(NivelUrgencia nivelUrgencia) { this.nivelRiesgo = nivelUrgencia; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getCoordenadaX() { return coordenadaX; }
    public void setCoordenadaX(double coordenadaX) { this.coordenadaX = coordenadaX; }

    public double getCoordenadaY() { return coordenadaY; }
    public void setCoordenadaY(double coordenadaY) { this.coordenadaY = coordenadaY; }

    public int getPoblacionAfectada() { return poblacionAfectada; }

    /**
     * Ajusta la población afectada. No actualiza automáticamente el nivel de riesgo.
     *
     * @param poblacionAfectada Número de personas afectadas (mínimo 0)
     */
    public void setPoblacionAfectada(int poblacionAfectada) {
        this.poblacionAfectada = Math.max(0, poblacionAfectada);
    }

    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }

    public int getRadio() { return radio; }

    /**
     * Ajusta el radio de cobertura entre 100 m y 10 km.
     *
     * @param radio Radio en metros
     */
    public void setRadio(int radio) {
        this.radio = Math.max(100, Math.min(10000, radio));
    }

    // -------------------- Métodos estándar --------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Zona zona)) return false;
        return Objects.equals(id, zona.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Zona{id='%s', nombre='%s', nivelRiesgo=%s, poblacionAfectada=%d, radio=%dm}",
                id, nombre, nivelRiesgo.name(), poblacionAfectada, radio);
    }
}

