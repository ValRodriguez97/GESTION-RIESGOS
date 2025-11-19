package co.edu.uniquindio.GestionRiesgos.Estructuras;

import co.edu.uniquindio.GestionRiesgos.Model.Evacuacion;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Clase que implementa una cola de prioridad para gestionar evacuaciones
 * según su nivel de urgencia y prioridad
 */
public class ColaPrioridad {
    private PriorityQueue<Evacuacion> colaEvacuaciones;
    private List<Evacuacion> historialEvacuaciones;
    
    public ColaPrioridad() {
        // Ordenar por prioridad descendente (mayor prioridad primero)
        this.colaEvacuaciones = new PriorityQueue<>(Comparator
            .comparingInt(Evacuacion::calcularPrioridad)
            .reversed()
            .thenComparing(evacuacion -> evacuacion.getFechaInicio()));
        this.historialEvacuaciones = new ArrayList<>();
    }
    
    /**
     * Agrega una evacuación a la cola de prioridad
     */
    public void agregarEvacuacion(Evacuacion evacuacion) {
        if (evacuacion != null) {
            colaEvacuaciones.offer(evacuacion);
        }
    }
    
    /**
     * Obtiene y remueve la evacuación con mayor prioridad
     */
    public Evacuacion obtenerSiguienteEvacuacion() {
        Evacuacion evacuacion = colaEvacuaciones.poll();
        if (evacuacion != null) {
            historialEvacuaciones.add(evacuacion);
        }
        return evacuacion;
    }
    
    /**
     * Obtiene la evacuación con mayor prioridad sin removerla
     */
    public Evacuacion verSiguienteEvacuacion() {
        return colaEvacuaciones.peek();
    }
    
    /**
     * Verifica si la cola está vacía
     */
    public boolean estaVacia() {
        return colaEvacuaciones.isEmpty();
    }
    
    /**
     * Obtiene el tamaño de la cola
     */
    public int obtenerTamano() {
        return colaEvacuaciones.size();
    }
    
    /**
     * Obtiene todas las evacuaciones en la cola (sin removerlas)
     */
    public List<Evacuacion> obtenerTodasLasEvacuaciones() {
        return new ArrayList<>(colaEvacuaciones);
    }
    
    /**
     * Obtiene el historial de evacuaciones procesadas
     */
    public List<Evacuacion> obtenerHistorial() {
        return new ArrayList<>(historialEvacuaciones);
    }
    
    /**
     * Prioriza las evacuaciones según criterios específicos
     */
    public void priorizar() {
        
        List<Evacuacion> evacuaciones = new ArrayList<>(colaEvacuaciones);
        colaEvacuaciones.clear();
        
        evacuaciones.sort(Comparator
            .comparingInt(Evacuacion::calcularPrioridad)
            .reversed()
            .thenComparing(Evacuacion::getFechaInicio));
        
        for (Evacuacion evacuacion : evacuaciones) {
            colaEvacuaciones.offer(evacuacion);
        }
    }
    
    /**
     * Obtiene evacuaciones por estado
     */
    public List<Evacuacion> obtenerEvacuacionesPorEstado(Evacuacion.EstadoEvacuacion estado) {
        return colaEvacuaciones.stream()
            .filter(evacuacion -> evacuacion.getEstado() == estado)
            .toList();
    }
    
    /**
     * Obtiene evacuaciones críticas (alta prioridad)
     */
    public List<Evacuacion> obtenerEvacuacionesCriticas() {
        return colaEvacuaciones.stream()
            .filter(evacuacion -> evacuacion.calcularPrioridad() >= 5)
            .toList();
    }
    
    /**
     * Calcula estadísticas de la cola
     */
    public String generarEstadisticas() {
        StringBuilder stats = new StringBuilder();
        stats.append("=== ESTADÍSTICAS DE COLA DE PRIORIDAD ===\n");
        stats.append("Evacuaciones en cola: ").append(obtenerTamano()).append("\n");
        stats.append("Evacuaciones procesadas: ").append(historialEvacuaciones.size()).append("\n");
        
        if (!estaVacia()) {
            Evacuacion siguiente = verSiguienteEvacuacion();
            stats.append("Siguiente evacuación: ").append(siguiente.getId()).append(" (Prioridad: ").append(siguiente.calcularPrioridad()).append(")\n");
        }
        
        for (Evacuacion.EstadoEvacuacion estado : Evacuacion.EstadoEvacuacion.values()) {
            int count = obtenerEvacuacionesPorEstado(estado).size();
            if (count > 0) {
                stats.append("Estado ").append(estado.getDescripcion()).append(": ").append(count).append("\n");
            }
        }
        
        return stats.toString();
    }
    
    /**
     * Limpia la cola de evacuaciones completadas
     */
    public void limpiarCompletadas() {
        colaEvacuaciones.removeIf(evacuacion -> 
            evacuacion.getEstado() == Evacuacion.EstadoEvacuacion.COMPLETADA ||
            evacuacion.getEstado() == Evacuacion.EstadoEvacuacion.CANCELADA);
    }
    
    /**
     * Obtiene el tiempo promedio de procesamiento
     */
    public double calcularTiempoPromedioProcesamiento() {
        if (historialEvacuaciones.isEmpty()) return 0.0;
        
        double tiempoTotal = historialEvacuaciones.stream()
            .filter(evacuacion -> evacuacion.getFechaFin() != null)
            .mapToLong(evacuacion -> 
                java.time.Duration.between(evacuacion.getFechaInicio(), evacuacion.getFechaFin()).toHours())
            .sum();
        
        return tiempoTotal / historialEvacuaciones.size();
    }
    
    public int getTamano() {
        return obtenerTamano();
    }
    
    public boolean isVacia() {
        return estaVacia();
    }
    
    @Override
    public String toString() {
        return String.format("ColaPrioridad{tamaño=%d, historial=%d}", obtenerTamano(), historialEvacuaciones.size());
    }
}

