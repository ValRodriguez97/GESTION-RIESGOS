package co.edu.uniquindio.GestionRiesgos.Estructuras;


import co.edu.uniquindio.GestionRiesgos.Enums.TipoRuta;
import co.edu.uniquindio.GestionRiesgos.Model.Zona;

import java.util.Objects;

/**
 * Clase que representa una ruta entre dos zonas en el grafo dirigido
 */
public class Ruta {
    private String id;
    private Zona origen;
    private Zona destino;
    private double distancia;
    private double tiempoEstimado;
    private TipoRuta tipo;
    private boolean activa;
    private int capacidadMaxima;
    private int capacidadActual;
    private double nivelRiesgo;
    private String descripcion;
    
    public Ruta() {
        this.activa = true;
        this.capacidadActual = 0;
        this.nivelRiesgo = 0.0;
    }
    
    public Ruta(String id, Zona origen, Zona destino, double distancia, double tiempoEstimado, TipoRuta tipo) {
        this();
        this.id = id;
        this.origen = origen;
        this.destino = destino;
        this.distancia = distancia;
        this.tiempoEstimado = tiempoEstimado;
        this.tipo = tipo;
    }
    
    /**
     * Calcula el porcentaje de ocupación de la ruta
     */
    public double calcularPorcentajeOcupacion() {
        if (capacidadMaxima == 0) return 0.0;
        return (double) capacidadActual / capacidadMaxima * 100;
    }
    
    /**
     * Verifica si la ruta tiene capacidad disponible
     */
    public boolean tieneCapacidadDisponible() {
        return capacidadActual < capacidadMaxima;
    }
    
    /**
     * Calcula la capacidad disponible
     */
    public int obtenerCapacidadDisponible() {
        return Math.max(0, capacidadMaxima - capacidadActual);
    }
    
    /**
     * Calcula la velocidad promedio de la ruta
     */
    public double calcularVelocidadPromedio() {
        if (tiempoEstimado == 0) return 0.0;
        return distancia / tiempoEstimado;
    }
    
    /**
     * Verifica si la ruta está congestionada
     */
    public boolean estaCongestionada() {
        return calcularPorcentajeOcupacion() > 80;
    }
    
    /**
     * Verifica si la ruta es segura para transitar
     */
    public boolean esSegura() {
        return nivelRiesgo < 0.5 && !estaCongestionada();
    }
    
    /**
     * Calcula el tiempo de viaje considerando el tráfico
     */
    public double calcularTiempoConTrafico() {
        double factorCongestion = estaCongestionada() ? 1.5 : 1.0;
        double factorRiesgo = nivelRiesgo > 0.7 ? 1.3 : 1.0;
        return tiempoEstimado * factorCongestion * factorRiesgo;
    }
    
    /**
     * Actualiza la capacidad actual de la ruta
     */
    public boolean actualizarCapacidad(int nuevaCapacidad) {
        if (nuevaCapacidad >= 0 && nuevaCapacidad <= capacidadMaxima) {
            this.capacidadActual = nuevaCapacidad;
            return true;
        }
        return false;
    }
    
    /**
     * Incrementa la capacidad actual
     */
    public boolean incrementarCapacidad(int incremento) {
        if (incremento > 0 && capacidadActual + incremento <= capacidadMaxima) {
            this.capacidadActual += incremento;
            return true;
        }
        return false;
    }
    
    /**
     * Decrementa la capacidad actual
     */
    public boolean decrementarCapacidad(int decremento) {
        if (decremento > 0 && capacidadActual - decremento >= 0) {
            this.capacidadActual -= decremento;
            return true;
        }
        return false;
    }
    
    /**
     * Calcula la prioridad de la ruta para evacuación
     */
    public int calcularPrioridadEvacuacion() {
        int prioridad = 1;
        
        switch (tipo) {
            case AEREA:
                prioridad += 5; // Rutas aéreas tienen mayor prioridad
                break;
            case TERRESTRE:
                prioridad += 3; // Rutas terrestres tienen prioridad media
                break;
            case MARITIMA:
                prioridad += 2; // Rutas marítimas tienen menor prioridad
                break;
        }
        
        if (esSegura()) prioridad += 2;
        
        if (estaCongestionada()) prioridad -= 2;
        
        if (nivelRiesgo > 0.7) prioridad -= 3;
        
        return Math.max(1, prioridad);
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public Zona getOrigen() {
        return origen;
    }
    
    public void setOrigen(Zona origen) {
        this.origen = origen;
    }
    
    public Zona getDestino() {
        return destino;
    }
    
    public void setDestino(Zona destino) {
        this.destino = destino;
    }
    
    public double getDistancia() {
        return distancia;
    }
    
    public void setDistancia(double distancia) {
        this.distancia = Math.max(0, distancia);
    }
    
    public double getTiempoEstimado() {
        return tiempoEstimado;
    }
    
    public void setTiempoEstimado(double tiempoEstimado) {
        this.tiempoEstimado = Math.max(0, tiempoEstimado);
    }
    
    public TipoRuta getTipo() {
        return tipo;
    }
    
    public void setTipo(TipoRuta tipo) {
        this.tipo = tipo;
    }
    
    public boolean isActiva() {
        return activa;
    }
    
    public void setActiva(boolean activa) {
        this.activa = activa;
    }
    
    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }
    
    public void setCapacidadMaxima(int capacidadMaxima) {
        this.capacidadMaxima = Math.max(0, capacidadMaxima);
    }
    
    public int getCapacidadActual() {
        return capacidadActual;
    }
    
    public void setCapacidadActual(int capacidadActual) {
        this.capacidadActual = Math.max(0, Math.min(capacidadActual, capacidadMaxima));
    }
    
    public double getNivelRiesgo() {
        return nivelRiesgo;
    }
    
    public void setNivelRiesgo(double nivelRiesgo) {
        this.nivelRiesgo = Math.max(0.0, Math.min(1.0, nivelRiesgo));
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
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
        return String.format("Ruta{id='%s', origen=%s, destino=%s, distancia=%.2f, tipo=%s, activa=%s}", id, origen.getNombre(), destino.getNombre(), distancia, tipo.getDescripcion(), activa);
    }
}

