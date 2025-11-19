package co.edu.uniquindio.GestionRiesgos.Model;

import co.edu.uniquindio.GestionRiesgos.Enums.NivelUrgencia;
import co.edu.uniquindio.GestionRiesgos.Estructuras.Ruta;

import java.time.LocalDateTime;
import java.util.Objects;


/**
 * Clase que representa una evacuación en el sistema de gestión de desastres.
 * Una evacuación está relacionada con una ruta específica para el traslado de personas.
 */
public class Evacuacion {
    private String id;
    private String nombre;
    private String descripcion;
    private Ruta ruta;
    private NivelUrgencia nivelUrgencia;
    private int personasAEvacuar;
    private int personasEvacuadas;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private EstadoEvacuacion estado;
    private String responsable;
    private String zonaOrigen;
    private String zonaDestino;
    
    /**
     * Enum para representar el estado de la evacuación
     */
    public enum EstadoEvacuacion {
        PLANIFICADA("Planificada"),
        EN_PROGRESO("En Progreso"),
        COMPLETADA("Completada"),
        CANCELADA("Cancelada"),
        SUSPENDIDA("Suspendida");
        
        private final String descripcion;
        
        EstadoEvacuacion(String descripcion) {
            this.descripcion = descripcion;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
    }
    
    public Evacuacion() {
        this.estado = EstadoEvacuacion.PLANIFICADA;
        this.personasEvacuadas = 0;
        this.fechaInicio = LocalDateTime.now();
    }
    
    public Evacuacion(String id, String nombre, Ruta ruta, NivelUrgencia nivelUrgencia) {
        this();
        this.id = id;
        this.nombre = nombre;
        this.ruta = ruta;
        this.nivelUrgencia = nivelUrgencia;
        if (ruta != null) {
            this.zonaOrigen = ruta.getOrigen().getId();
            this.zonaDestino = ruta.getDestino().getId();
        }
    }
    
    /**
     * Calcula la prioridad de la evacuación basada en urgencia y cantidad de personas
     */
    public int calcularPrioridad() {
        int prioridad = nivelUrgencia.getValor();
        
        if (personasAEvacuar > 10000) prioridad += 3;
        else if (personasAEvacuar > 5000) prioridad += 2;
        else if (personasAEvacuar > 1000) prioridad += 1;
        
        if (ruta != null) {
            // Prioridad adicional basada en la distancia de la ruta
            if (ruta.getDistancia() > 100) prioridad += 1;
            if (ruta.getNivelRiesgo() > 0.7) prioridad += 2;
        }
        
        return prioridad;
    }
    
    /**
     * Calcula el porcentaje de evacuación completada
     */
    public double calcularPorcentajeCompletado() {
        if (personasAEvacuar == 0) return 0.0;
        return (double) personasEvacuadas / personasAEvacuar * 100;
    }
    
    /**
     * Verifica si la evacuación está completada
     */
    public boolean estaCompletada() {
        return estado == EstadoEvacuacion.COMPLETADA || 
               (personasEvacuadas >= personasAEvacuar && personasAEvacuar > 0);
    }
    
    /**
     * Calcula el tiempo estimado de evacuación basado en la ruta
     */
    public double calcularTiempoEstimado() {
        if (ruta == null) return 0.0;
        
        // Tiempo base de la ruta + tiempo adicional por cantidad de personas
        double tiempoBase = ruta.getTiempoEstimado();
        double tiempoAdicional = (personasAEvacuar / 100.0) * 0.5; // 0.5 horas por cada 100 personas
        
        return tiempoBase + tiempoAdicional;
    }
    
    /**
     * Actualiza el progreso de la evacuación
     */
    public void actualizarProgreso(int personasEvacuadas) {
        this.personasEvacuadas = Math.min(personasEvacuadas, personasAEvacuar);
        
        if (estaCompletada()) {
            this.estado = EstadoEvacuacion.COMPLETADA;
            this.fechaFin = LocalDateTime.now();
        } else if (this.personasEvacuadas > 0) {
            this.estado = EstadoEvacuacion.EN_PROGRESO;
        }
    }
    
    /**
     * Inicia la evacuación
     */
    public void iniciarEvacuacion() {
        if (estado == EstadoEvacuacion.PLANIFICADA) {
            this.estado = EstadoEvacuacion.EN_PROGRESO;
            this.fechaInicio = LocalDateTime.now();
        }
    }
    
    /**
     * Cancela la evacuación
     */
    public void cancelarEvacuacion() {
        this.estado = EstadoEvacuacion.CANCELADA;
        this.fechaFin = LocalDateTime.now();
    }
    
    /**
     * Suspende la evacuación
     */
    public void suspenderEvacuacion() {
        if (estado == EstadoEvacuacion.EN_PROGRESO) {
            this.estado = EstadoEvacuacion.SUSPENDIDA;
        }
    }
    
    /**
     * Reanuda una evacuación suspendida
     */
    public void reanudarEvacuacion() {
        if (estado == EstadoEvacuacion.SUSPENDIDA) {
            this.estado = EstadoEvacuacion.EN_PROGRESO;
        }
    }
    
    // Getters y Setters
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
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public Ruta getRuta() {
        return ruta;
    }
    
    public void setRuta(Ruta ruta) {
        this.ruta = ruta;
        if (ruta != null) {
            this.zonaOrigen = ruta.getOrigen().getId();
            this.zonaDestino = ruta.getDestino().getId();
        }
    }
    
    public NivelUrgencia getNivelUrgencia() {
        return nivelUrgencia;
    }
    
    public void setNivelUrgencia(NivelUrgencia nivelUrgencia) {
        this.nivelUrgencia = nivelUrgencia;
    }
    
    public int getPersonasAEvacuar() {
        return personasAEvacuar;
    }
    
    public void setPersonasAEvacuar(int personasAEvacuar) {
        this.personasAEvacuar = Math.max(0, personasAEvacuar);
    }
    
    public int getPersonasEvacuadas() {
        return personasEvacuadas;
    }
    
    public void setPersonasEvacuadas(int personasEvacuadas) {
        this.personasEvacuadas = Math.max(0, Math.min(personasEvacuadas, personasAEvacuar));
    }
    
    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }
    
    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    
    public LocalDateTime getFechaFin() {
        return fechaFin;
    }
    
    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }
    
    public EstadoEvacuacion getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoEvacuacion estado) {
        this.estado = estado;
    }
    
    public String getResponsable() {
        return responsable;
    }
    
    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }
    
    public String getZonaOrigen() {
        return zonaOrigen;
    }
    
    public void setZonaOrigen(String zonaOrigen) {
        this.zonaOrigen = zonaOrigen;
    }
    
    public String getZonaDestino() {
        return zonaDestino;
    }
    
    public void setZonaDestino(String zonaDestino) {
        this.zonaDestino = zonaDestino;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Evacuacion that = (Evacuacion) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return String.format("Evacuacion{id='%s', nombre='%s', estado=%s, progreso=%.1f%%, ruta=%s}", 
            id, nombre, estado.getDescripcion(), calcularPorcentajeCompletado(), 
            ruta != null ? ruta.getId() : "Sin ruta");
    }
}

