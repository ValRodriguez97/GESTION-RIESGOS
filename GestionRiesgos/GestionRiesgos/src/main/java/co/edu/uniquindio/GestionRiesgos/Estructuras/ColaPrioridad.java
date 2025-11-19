package co.edu.uniquindio.GestionRiesgos.Estructuras;

import co.edu.uniquindio.GestionRiesgos.Model.Evacuacion;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Clase que implementa una cola de prioridad para la gestión de evacuaciones,
 * ordenando las solicitudes según su nivel de urgencia y prioridad calculada.
 * 
 * Esta estructura permite:
 * - Insertar evacuaciones con prioridades dinámicas.
 * - Obtener la siguiente evacuación más urgente.
 * - Consultar estadísticas operativas.
 * - Mantener un historial de evacuaciones procesadas.
 * 
 * El criterio de ordenamiento combina:
 *  - Prioridad (descendente)
 *  - Fecha de inicio (ascendente, en caso de empate)
 */
public class ColaPrioridad {

    /** Cola de prioridad que almacena evacuaciones activas */
    private PriorityQueue<Evacuacion> colaEvacuaciones;

    /** Historial de evacuaciones procesadas */
    private List<Evacuacion> historialEvacuaciones;

    /**
     * Crea una cola de prioridad configurada para ordenar las evacuaciones
     * según su prioridad calculada y fecha de inicio.
     */
    public ColaPrioridad() {
        // Ordenar por prioridad descendente (mayor prioridad primero)
        this.colaEvacuaciones = new PriorityQueue<>(Comparator
            .comparingInt(Evacuacion::calcularPrioridad)
            .reversed()
            .thenComparing(Evacuacion::getFechaInicio));
        
        this.historialEvacuaciones = new ArrayList<>();
    }

    /**
     * Agrega una nueva evacuación a la cola de prioridad.
     *
     * @param evacuacion Evacuación a agregar. Si es null, no se agrega.
     */
    public void agregarEvacuacion(Evacuacion evacuacion) {
        if (evacuacion != null) {
            colaEvacuaciones.offer(evacuacion);
        }
    }

    /**
     * Obtiene y remueve la evacuación con mayor prioridad de la cola.
     * La evacuación procesada se registra automáticamente en el historial.
     *
     * @return La evacuación con mayor prioridad, o null si la cola está vacía.
     */
    public Evacuacion obtenerSiguienteEvacuacion() {
        Evacuacion evacuacion = colaEvacuaciones.poll();
        if (evacuacion != null) {
            historialEvacuaciones.add(evacuacion);
        }
        return evacuacion;
    }

    /**
     * Consulta la evacuación con mayor prioridad sin removerla de la cola.
     *
     * @return La evacuación más prioritaria o null si está vacía.
     */
    public Evacuacion verSiguienteEvacuacion() {
        return colaEvacuaciones.peek();
    }

    /**
     * Verifica si la cola se encuentra vacía.
     *
     * @return true si no hay evacuaciones en la cola, false de lo contrario.
     */
    public boolean estaVacia() {
        return colaEvacuaciones.isEmpty();
    }

    /**
     * Obtiene la cantidad de evacuaciones activas en la cola.
     *
     * @return Número de evacuaciones pendientes.
     */
    public int obtenerTamano() {
        return colaEvacuaciones.size();
    }

    /**
     * Obtiene una lista con todas las evacuaciones en la cola, sin removerlas.
     *
     * @return Lista de evacuaciones activas.
     */
    public List<Evacuacion> obtenerTodasLasEvacuaciones() {
        return new ArrayList<>(colaEvacuaciones);
    }

    /**
     * Obtiene el historial completo de evacuaciones procesadas.
     *
     * @return Lista con evacuaciones ya atendidas.
     */
    public List<Evacuacion> obtenerHistorial() {
        return new ArrayList<>(historialEvacuaciones);
    }

    /**
     * Reordena manualmente la cola de prioridad, recalculando las prioridades.
     * Útil cuando cambian factores externos que afectan la prioridad.
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
     * Filtra evacuaciones según su estado actual.
     *
     * @param estado Estado deseado.
     * @return Lista de evacuaciones que coinciden con el estado.
     */
    public List<Evacuacion> obtenerEvacuacionesPorEstado(Evacuacion.EstadoEvacuacion estado) {
        return colaEvacuaciones.stream()
            .filter(evacuacion -> evacuacion.getEstado() == estado)
            .toList();
    }

    /**
     * Obtiene una lista de evacuaciones que tienen alta prioridad
     * (prioridad igual o superior a 5).
     *
     * @return Lista de evacuaciones críticas.
     */
    public List<Evacuacion> obtenerEvacuacionesCriticas() {
        return colaEvacuaciones.stream()
            .filter(evacuacion -> evacuacion.calcularPrioridad() >= 5)
            .toList();
    }

    /**
     * Genera un reporte con estadísticas detalladas de la cola,
     * incluyendo cantidad de evacuaciones activas, procesadas y estados.
     *
     * @return Cadena con estadísticas formateadas.
     */
    public String generarEstadisticas() {
        StringBuilder stats = new StringBuilder();
        stats.append("=== ESTADÍSTICAS DE COLA DE PRIORIDAD ===\n");
        stats.append("Evacuaciones en cola: ").append(obtenerTamano()).append("\n");
        stats.append("Evacuaciones procesadas: ").append(historialEvacuaciones.size()).append("\n");

        if (!estaVacia()) {
            Evacuacion siguiente = verSiguienteEvacuacion();
            stats.append("Siguiente evacuación: ").append(siguiente.getId())
                 .append(" (Prioridad: ").append(siguiente.calcularPrioridad()).append(")\n");
        }

        for (Evacuacion.EstadoEvacuacion estado : Evacuacion.EstadoEvacuacion.values()) {
            int count = obtenerEvacuacionesPorEstado(estado).size();
            if (count > 0) {
                stats.append("Estado ").append(estado.getDescripcion())
                     .append(": ").append(count).append("\n");
            }
        }

        return stats.toString();
    }

    /**
     * Elimina de la cola las evacuaciones que ya han sido completadas
     * o canceladas.
     */
    public void limpiarCompletadas() {
        colaEvacuaciones.removeIf(evacuacion -> 
            evacuacion.getEstado() == Evacuacion.EstadoEvacuacion.COMPLETADA ||
            evacuacion.getEstado() == Evacuacion.EstadoEvacuacion.CANCELADA);
    }

    /**
     * Calcula el tiempo promedio de procesamiento de las evacuaciones
     * finalizadas, medido en horas.
     *
     * @return Tiempo promedio en horas; 0.0 si no hay datos suficientes.
     */
    public double calcularTiempoPromedioProcesamiento() {
        if (historialEvacuaciones.isEmpty()) return 0.0;

        double tiempoTotal = historialEvacuaciones.stream()
            .filter(evacuacion -> evacuacion.getFechaFin() != null)
            .mapToLong(evacuacion -> 
                java.time.Duration.between(evacuacion.getFechaInicio(), evacuacion.getFechaFin())
                                  .toHours())
            .sum();

        return tiempoTotal / historialEvacuaciones.size();
    }

    /** @return Alias de {@link #obtenerTamano()} */
    public int getTamano() {
        return obtenerTamano();
    }

    /** @return Alias de {@link #estaVacia()} */
    public boolean isVacia() {
        return estaVacia();
    }

    @Override
    public String toString() {
        return String.format("ColaPrioridad{tamaño=%d, historial=%d}",
                obtenerTamano(), historialEvacuaciones.size());
    }
}


