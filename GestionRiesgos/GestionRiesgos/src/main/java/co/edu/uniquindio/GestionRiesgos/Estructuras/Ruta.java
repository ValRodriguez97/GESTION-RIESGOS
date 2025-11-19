package co.edu.uniquindio.GestionRiesgos.Estructuras;


import co.edu.uniquindio.GestionRiesgos.Enums.TipoRuta;
import co.edu.uniquindio.GestionRiesgos.Model.Zona;

import java.util.Objects;

/**
 * Clase que representa una ruta entre dos zonas en el grafo dirigido del sistema de gestión de desastres.
 * Cada ruta tiene atributos como distancia, tiempo estimado, tipo, nivel de riesgo y capacidad.
 */
public class Ruta {

    // ==============================
    //          ATRIBUTOS
    // ==============================

    /** Identificador único de la ruta */
    private String id;

    /** Zona de origen de la ruta */
    private Zona origen;

    /** Zona de destino de la ruta */
    private Zona destino;

    /** Distancia de la ruta (en kilómetros o unidades definidas) */
    private double distancia;

    /** Tiempo estimado de recorrido (en horas o unidades definidas) */
    private double tiempoEstimado;

    /** Tipo de ruta (aérea, terrestre, marítima, etc.) */
    private TipoRuta tipo;

    /** Indica si la ruta está activa */
    private boolean activa;

    /** Capacidad máxima de personas o vehículos que la ruta puede soportar */
    private int capacidadMaxima;

    /** Capacidad actualmente ocupada en la ruta */
    private int capacidadActual;

    /** Nivel de riesgo de la ruta (0.0 seguro – 1.0 muy riesgoso) */
    private double nivelRiesgo;

    /** Descripción adicional de la ruta */
    private String descripcion;

    // ==============================
    //          CONSTRUCTORES
    // ==============================

    /**
     * Constructor por defecto.
     * Inicializa la ruta como activa, sin ocupación y con nivel de riesgo 0.
     */
    public Ruta() {
        this.activa = true;
        this.capacidadActual = 0;
        this.nivelRiesgo = 0.0;
    }

    /**
     * Constructor que inicializa los atributos principales de la ruta.
     * 
     * @param id Identificador único
     * @param origen Zona de origen
     * @param destino Zona de destino
     * @param distancia Distancia de la ruta
     * @param tiempoEstimado Tiempo estimado de recorrido
     * @param tipo Tipo de ruta
     */
    public Ruta(String id, Zona origen, Zona destino, double distancia, double tiempoEstimado, TipoRuta tipo) {
        this();
        this.id = id;
        this.origen = origen;
        this.destino = destino;
        this.distancia = distancia;
        this.tiempoEstimado = tiempoEstimado;
        this.tipo = tipo;
    }

    // ==============================
    //          MÉTODOS PRINCIPALES
    // ==============================

    /**
     * Calcula el porcentaje de ocupación de la ruta.
     * 
     * @return Porcentaje de ocupación (0 a 100)
     */
    public double calcularPorcentajeOcupacion() {
        if (capacidadMaxima == 0) return 0.0;
        return (double) capacidadActual / capacidadMaxima * 100;
    }

    /**
     * Verifica si la ruta tiene capacidad disponible.
     * 
     * @return true si hay capacidad disponible, false si está llena
     */
    public boolean tieneCapacidadDisponible() {
        return capacidadActual < capacidadMaxima;
    }

    /**
     * Calcula la capacidad disponible en la ruta.
     * 
     * @return Capacidad restante (>=0)
     */
    public int obtenerCapacidadDisponible() {
        return Math.max(0, capacidadMaxima - capacidadActual);
    }

    /**
     * Calcula la velocidad promedio de la ruta basada en distancia y tiempo estimado.
     * 
     * @return Velocidad promedio
     */
    public double calcularVelocidadPromedio() {
        if (tiempoEstimado == 0) return 0.0;
        return distancia / tiempoEstimado;
    }

    /**
     * Verifica si la ruta está congestionada (ocupación > 80%).
     * 
     * @return true si está congestionada, false de lo contrario
     */
    public boolean estaCongestionada() {
        return calcularPorcentajeOcupacion() > 80;
    }

    /**
     * Determina si la ruta es segura para transitar.
     * 
     * @return true si es segura (riesgo < 0.5 y no congestionada)
     */
    public boolean esSegura() {
        return nivelRiesgo < 0.5 && !estaCongestionada();
    }

    /**
     * Calcula el tiempo de viaje considerando congestión y nivel de riesgo.
     * 
     * @return Tiempo estimado ajustado
     */
    public double calcularTiempoConTrafico() {
        double factorCongestion = estaCongestionada() ? 1.5 : 1.0;
        double factorRiesgo = nivelRiesgo > 0.7 ? 1.3 : 1.0;
        return tiempoEstimado * factorCongestion * factorRiesgo;
    }

    /**
     * Actualiza la capacidad actual de la ruta.
     * 
     * @param nuevaCapacidad Nueva capacidad ocupada
     * @return true si la actualización fue válida, false si no
     */
    public boolean actualizarCapacidad(int nuevaCapacidad) {
        if (nuevaCapacidad >= 0 && nuevaCapacidad <= capacidadMaxima) {
            this.capacidadActual = nuevaCapacidad;
            return true;
        }
        return false;
    }

    /**
     * Incrementa la capacidad ocupada de la ruta.
     * 
     * @param incremento Cantidad a incrementar
     * @return true si se pudo incrementar, false si excede capacidad máxima
     */
    public boolean incrementarCapacidad(int incremento) {
        if (incremento > 0 && capacidadActual + incremento <= capacidadMaxima) {
            this.capacidadActual += incremento;
            return true;
        }
        return false;
    }

    /**
     * Decrementa la capacidad ocupada de la ruta.
     * 
     * @param decremento Cantidad a decrementar
     * @return true si se pudo decrementar, false si resultaría negativa
     */
    public boolean decrementarCapacidad(int decremento) {
        if (decremento > 0 && capacidadActual - decremento >= 0) {
            this.capacidadActual -= decremento;
            return true;
        }
        return false;
    }

    /**
     * Calcula la prioridad de la ruta para operaciones de evacuación.
     * 
     * @return Prioridad (mayor valor indica mayor prioridad)
     */
    public int calcularPrioridadEvacuacion() {
        int prioridad = 1;

        switch (tipo) {
            case AEREA: prioridad += 5; break;
            case TERRESTRE: prioridad += 3; break;
            case MARITIMA: prioridad += 2; break;
        }

        if (esSegura()) prioridad += 2;
        if (estaCongestionada()) prioridad -= 2;
        if (nivelRiesgo > 0.7) prioridad -= 3;

        return Math.max(1, prioridad);
    }

    // ==============================
    //            GETTERS & SETTERS
    // ==============================

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Zona getOrigen() { return origen; }
    public void setOrigen(Zona origen) { this.origen = origen; }

    public Zona getDestino() { return destino; }
    public void setDestino(Zona destino) { this.destino = destino; }

    public double getDistancia() { return distancia; }
    public void setDistancia(double distancia) { this.distancia = Math.max(0, distancia); }

    public double getTiempoEstimado() { return tiempoEstimado; }
    public void setTiempoEstimado(double tiempoEstimado) { this.tiempoEstimado = Math.max(0, tiempoEstimado); }

    public TipoRuta getTipo() { return tipo; }
    public void setTipo(TipoRuta tipo) { this.tipo = tipo; }

    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }

    public int getCapacidadMaxima() { return capacidadMaxima; }
    public void setCapacidadMaxima(int capacidadMaxima) { this.capacidadMaxima = Math.max(0, capacidadMaxima); }

    public int getCapacidadActual() { return capacidadActual; }
    public void setCapacidadActual(int capacidadActual) { this.capacidadActual = Math.max(0, Math.min(capacidadActual, capacidadMaxima)); }

    public double getNivelRiesgo() { return nivelRiesgo; }
    public void setNivelRiesgo(double nivelRiesgo) { this.nivelRiesgo = Math.max(0.0, Math.min(1.0, nivelRiesgo)); }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    // ==============================
    //            OVERRIDE
    // ==============================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ruta ruta = (Ruta) o;
        return Objects.equals(id, ruta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Ruta{id='%s', origen=%s, destino=%s, distancia=%.2f, tipo=%s, activa=%s}", 
                id, origen.getNombre(), destino.getNombre(), distancia, tipo.getDescripcion(), activa);
    }
}

