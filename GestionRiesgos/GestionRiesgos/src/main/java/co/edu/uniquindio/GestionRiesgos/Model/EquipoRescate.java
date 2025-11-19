package co.edu.uniquindio.GestionRiesgos.Model;

import co.edu.uniquindio.GestionRiesgos.Enums.TipoRecurso;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Modelo que representa un equipo de rescate en el sistema de gestión de desastres.
 */
public class EquipoRescate {
    private String id;
    private String nombre;
    private TipoEquipo tipo;
    private String ubicacionActual;
    private EstadoEquipo estado;
    private int capacidadMaxima;
    private int personalAsignado;
    private String liderEquipo;
    private String especialidad;
    private LocalDateTime fechaCreacion;
    private LocalDateTime ultimaActualizacion;
    private String emergenciaAsignada;
    private int experienciaAnos;
    private boolean disponible;
    private List<Recurso> recursosAsignados;
    
    /**
     * Enum para representar el tipo de equipo de rescate
     */
    public enum TipoEquipo {
        MEDICO("Equipo Médico", 5),
        BOMBEROS("Bomberos", 4),
        BUSQUEDA_RESCATE("Búsqueda y Rescate", 5),
        EVACUACION("Evacuación", 3),
        COMUNICACIONES("Comunicaciones", 2),
        LOGISTICA("Logística", 3),
        INGENIERIA("Ingeniería", 4),
        PSICOLOGIA("Psicología", 2),
        VETERINARIA("Veterinaria", 2),
        ESPECIALIZADO("Especializado", 6);
        
        private final String descripcion;
        private final int prioridadBase;
        
        TipoEquipo(String descripcion, int prioridadBase) {
            this.descripcion = descripcion;
            this.prioridadBase = prioridadBase;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
        
        public int getPrioridadBase() {
            return prioridadBase;
        }
    }
    
    /**
     * Enum para representar el estado del equipo
     */
    public enum EstadoEquipo {
        DISPONIBLE("Disponible"),
        EN_TRANSITO("En tránsito"),
        EN_OPERACION("En operación"),
        EN_DESCANSO("En descanso"),
        MANTENIMIENTO("En mantenimiento"),
        NO_DISPONIBLE("No disponible");
        
        private final String descripcion;
        
        EstadoEquipo(String descripcion) {
            this.descripcion = descripcion;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
    }
    
    public EquipoRescate() {
        this.fechaCreacion = LocalDateTime.now();
        this.ultimaActualizacion = LocalDateTime.now();
        this.estado = EstadoEquipo.DISPONIBLE;
        this.disponible = true;
        this.personalAsignado = 0;
        this.experienciaAnos = 0;
        this.recursosAsignados = new ArrayList<>();
    }
    
    public EquipoRescate(String id, String nombre, TipoEquipo tipo, String ubicacionActual, 
                        int capacidadMaxima, String liderEquipo) {
        this();
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.ubicacionActual = ubicacionActual;
        this.capacidadMaxima = capacidadMaxima;
        this.liderEquipo = liderEquipo;
    }
    
    /**
     * Calcula la eficiencia del equipo basada en experiencia y personal
     */
    public double calcularEficiencia() {
        double eficienciaBase = Math.min(1.0, (double) personalAsignado / capacidadMaxima);
        double factorExperiencia = Math.min(1.0, experienciaAnos / 10.0);
        return (eficienciaBase + factorExperiencia) / 2.0;
    }
    
    /**
     * Calcula la prioridad del equipo para asignación
     */
    public int calcularPrioridadAsignacion() {
        int prioridad = tipo.getPrioridadBase();
        
        if (experienciaAnos > 10) prioridad += 3;
        else if (experienciaAnos > 5) prioridad += 2;
        else if (experienciaAnos > 2) prioridad += 1;
        
        if (disponible && estado == EstadoEquipo.DISPONIBLE) prioridad += 2;
        
        if (estado == EstadoEquipo.EN_OPERACION) prioridad -= 3;
        else if (estado == EstadoEquipo.EN_TRANSITO) prioridad -= 1;
        
        return Math.max(1, prioridad);
    }
    
    /**
     * Verifica si el equipo puede ser asignado a una emergencia
     */
    public boolean puedeSerAsignado() {
        return disponible && 
        estado == EstadoEquipo.DISPONIBLE && 
        personalAsignado > 0 && 
        personalAsignado <= capacidadMaxima;
    }
    
    /**
     * Asigna el equipo a una emergencia
     */
    public boolean asignarAEmergencia(String emergenciaId) {
        if (puedeSerAsignado()) {
            this.emergenciaAsignada = emergenciaId;
            this.estado = EstadoEquipo.EN_OPERACION;
            this.disponible = false;
            this.ultimaActualizacion = LocalDateTime.now();
            return true;
        }
        return false;
    }
    
    /**
     * Libera el equipo de su asignación actual
     */
    public void liberarDeAsignacion() {
        this.emergenciaAsignada = null;
        this.estado = EstadoEquipo.DISPONIBLE;
        this.disponible = true;
        this.ultimaActualizacion = LocalDateTime.now();
    }
    
    /**
     * Actualiza la ubicación del equipo
     */
    public void actualizarUbicacion(String nuevaUbicacion) {
        this.ubicacionActual = nuevaUbicacion;
        this.ultimaActualizacion = LocalDateTime.now();
        
        if (estado == EstadoEquipo.EN_TRANSITO) {
            this.estado = EstadoEquipo.DISPONIBLE;
        }
    }
    
    /**
     * Asigna personal al equipo
     */
    public boolean asignarPersonal(int cantidad) {
        if (cantidad > 0 && personalAsignado + cantidad <= capacidadMaxima) {
            this.personalAsignado += cantidad;
            this.ultimaActualizacion = LocalDateTime.now();
            return true;
        }
        return false;
    }
    
    /**
     * Retira personal del equipo
     */
    public boolean retirarPersonal(int cantidad) {
        if (cantidad > 0 && personalAsignado - cantidad >= 0) {
            this.personalAsignado -= cantidad;
            this.ultimaActualizacion = LocalDateTime.now();
            return true;
        }
        return false;
    }
    
    /**
     * Verifica si el equipo necesita descanso
     */
    public boolean necesitaDescanso() {
        if (estado == EstadoEquipo.EN_OPERACION) {
            long horasOperacion = java.time.Duration.between(ultimaActualizacion, LocalDateTime.now()).toHours();
            return horasOperacion > 12; // Más de 12 horas en operación
        }
        return false;
    }
    
    /**
     * Calcula el tiempo de respuesta estimado a una ubicación
     */
    public int calcularTiempoRespuesta(String ubicacionDestino) {
        int tiempoBase = 30;
        int factorTipo = tipo.getPrioridadBase();
        int factorExperiencia = Math.max(1, 10 - experienciaAnos);
        
        return tiempoBase + (factorTipo * factorExperiencia);
    }
    
    /**
     * Asigna un recurso al equipo
     */
    public boolean asignarRecurso(Recurso recurso) {
        if (recurso != null && !recursosAsignados.contains(recurso)) {
            recursosAsignados.add(recurso);
            this.ultimaActualizacion = LocalDateTime.now();
            return true;
        }
        return false;
    }
    
    /**
     * Remueve un recurso del equipo
     */
    public boolean removerRecurso(Recurso recurso) {
        if (recurso != null && recursosAsignados.remove(recurso)) {
            this.ultimaActualizacion = LocalDateTime.now();
            return true;
        }
        return false;
    }
    
    /**
     * Obtiene todos los recursos asignados al equipo
     */
    public List<Recurso> getRecursosAsignados() {
        return new ArrayList<>(recursosAsignados);
    }
    
    /**
     * Obtiene recursos por tipo
     */
    public List<Recurso> obtenerRecursosPorTipo(TipoRecurso tipo) {
        return recursosAsignados.stream().filter(recurso -> recurso.getTipo() == tipo).toList();
    }
    
    /**
     * Calcula el valor total de los recursos asignados
     */
    public int calcularValorTotalRecursos() {
        return recursosAsignados.stream()
            .mapToInt(Recurso::getCantidadDisponible)
            .sum();
    }
    
    /**
     * Verifica si el equipo tiene un recurso específico
     */
    public boolean tieneRecurso(String recursoId) {
        return recursosAsignados.stream()
            .anyMatch(recurso -> recurso.getId().equals(recursoId));
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
    
    public TipoEquipo getTipo() {
        return tipo;
    }
    
    public void setTipo(TipoEquipo tipo) {
        this.tipo = tipo;
    }
    
    public String getUbicacionActual() {
        return ubicacionActual;
    }
    
    public void setUbicacionActual(String ubicacionActual) {
        this.ubicacionActual = ubicacionActual;
    }
    
    public EstadoEquipo getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoEquipo estado) {
        this.estado = estado;
    }
    
    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }
    
    public void setCapacidadMaxima(int capacidadMaxima) {
        this.capacidadMaxima = Math.max(1, capacidadMaxima);
    }
    
    public int getPersonalAsignado() {
        return personalAsignado;
    }
    
    public void setPersonalAsignado(int personalAsignado) {
        this.personalAsignado = Math.max(0, Math.min(personalAsignado, capacidadMaxima));
    }
    
    public String getLiderEquipo() {
        return liderEquipo;
    }
    
    public void setLiderEquipo(String liderEquipo) {
        this.liderEquipo = liderEquipo;
    }
    
    public String getEspecialidad() {
        return especialidad;
    }
    
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public LocalDateTime getUltimaActualizacion() {
        return ultimaActualizacion;
    }
    
    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }
    
    public String getEmergenciaAsignada() {
        return emergenciaAsignada;
    }
    
    public void setEmergenciaAsignada(String emergenciaAsignada) {
        this.emergenciaAsignada = emergenciaAsignada;
    }
    
    public int getExperienciaAnos() {
        return experienciaAnos;
    }
    
    public void setExperienciaAnos(int experienciaAnos) {
        this.experienciaAnos = Math.max(0, experienciaAnos);
    }
    
    public boolean isDisponible() {
        return disponible;
    }
    
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EquipoRescate that = (EquipoRescate) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return String.format("EquipoRescate{id='%s', nombre='%s', tipo=%s, estado=%s, personal=%d/%d, eficiencia=%.1f%%}", id, nombre, tipo.getDescripcion(), estado.getDescripcion(), personalAsignado, capacidadMaxima, calcularEficiencia() * 100);
    }
}

