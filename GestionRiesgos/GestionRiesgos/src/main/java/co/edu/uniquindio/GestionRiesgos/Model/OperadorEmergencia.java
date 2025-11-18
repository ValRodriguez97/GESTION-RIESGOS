package co.edu.uniquindio.GestionRiesgos.Model;

import co.edu.uniquindio.Gestion.de.Riesgos.Enums.Rol;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un operador de emergencia del sistema de gestión de desastres
 * Hereda de Usuario y tiene permisos específicos para operaciones de emergencia
 */
public class OperadorEmergencia extends Usuario {
    private String especialidad;
    private String ubicacionAsignada;
    private int experienciaAnos;
    private List<String> certificaciones;
    private boolean disponible;
    private LocalDateTime ultimaAsignacion;
    
    public OperadorEmergencia() {
        super();
        this.rol = Rol.OPERADOR_EMERGENCIA;
        this.certificaciones = new ArrayList<>();
        this.disponible = true;
        this.experienciaAnos = 0;
        inicializarCertificaciones();
    }
    
    public OperadorEmergencia(String id, String nombre, String apellido, String email, String username, String password, String especialidad, String ubicacionAsignada) {
        super(id, nombre, apellido, email, username, password, Rol.OPERADOR_EMERGENCIA);
        this.especialidad = especialidad;
        this.ubicacionAsignada = ubicacionAsignada;
        this.certificaciones = new ArrayList<>();
        this.disponible = true;
        this.experienciaAnos = 0;
        inicializarCertificaciones();
    }
    
    /**
     * Inicializa las certificaciones básicas del operador
     */
    private void inicializarCertificaciones() {
        certificaciones.add("Primeros Auxilios");
        certificaciones.add("Manejo de Emergencias");
        certificaciones.add("Comunicaciones de Emergencia");
    }
    
    /**
     * Verifica si el operador tiene una certificación específica
     */
    public boolean tieneCertificacion(String certificacion) {
        return certificaciones.contains(certificacion);
    }
    
    /**
     * Agrega una nueva certificación
     */
    public void agregarCertificacion(String certificacion) {
        if (!certificaciones.contains(certificacion)) {
            certificaciones.add(certificacion);
        }
    }
    
    /**
     * Calcula la eficiencia del operador basada en experiencia y certificaciones
     */
    public double calcularEficiencia() {
        double factorExperiencia = Math.min(1.0, experienciaAnos / 10.0);
        double factorCertificaciones = Math.min(1.0, certificaciones.size() / 10.0);
        return (factorExperiencia + factorCertificaciones) / 2.0;
    }
    
    /**
     * Verifica si el operador puede ser asignado a una emergencia
     */
    public boolean puedeSerAsignado() {
        return disponible && isActivo() && experienciaAnos > 0;
    }
    
    /**
     * Asigna el operador a una ubicación específica
     */
    public void asignarAUbicacion(String ubicacion) {
        this.ubicacionAsignada = ubicacion;
        this.ultimaAsignacion = LocalDateTime.now();
        this.disponible = false;
    }
    
    /**
     * Libera al operador de su asignación actual
     */
    public void liberarDeAsignacion() {
        this.ubicacionAsignada = null;
        this.disponible = true;
    }
    
    /**
     * Verifica si el operador necesita descanso
     */
    public boolean necesitaDescanso() {
        if (ultimaAsignacion == null) return false;
        return ultimaAsignacion.isBefore(LocalDateTime.now().minusHours(12));
    }
    
    /**
     * Calcula la prioridad del operador para asignación
     */
    public int calcularPrioridadAsignacion() {
        int prioridad = 1;
        
        if (experienciaAnos > 10) prioridad += 3;
        else if (experienciaAnos > 5) prioridad += 2;
        else if (experienciaAnos > 2) prioridad += 1;
        
        prioridad += certificaciones.size();
        
        if (disponible) prioridad += 2;
        
        if (necesitaDescanso()) prioridad -= 2;
        
        return Math.max(1, prioridad);
    }
    
    /**
     * Genera un reporte de actividad del operador
     */
    public String generarReporteActividad() {
        StringBuilder reporte = new StringBuilder();
        reporte.append("=== REPORTE DE ACTIVIDAD DEL OPERADOR ===\n");
        reporte.append("ID: ").append(getId()).append("\n");
        reporte.append("Nombre: ").append(obtenerNombreCompleto()).append("\n");
        reporte.append("Especialidad: ").append(especialidad).append("\n");
        reporte.append("Ubicación Asignada: ").append(ubicacionAsignada != null ? ubicacionAsignada : "Sin asignar").append("\n");
        reporte.append("Experiencia: ").append(experienciaAnos).append(" años\n");
        reporte.append("Disponible: ").append(disponible ? "Sí" : "No").append("\n");
        reporte.append("Eficiencia: ").append(String.format("%.1f%%", calcularEficiencia() * 100)).append("\n");
        reporte.append("Certificaciones: ").append(certificaciones.size()).append("\n");
        return reporte.toString();
    }

    public String getEspecialidad() {
        return especialidad;
    }
    
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
    
    public String getUbicacionAsignada() {
        return ubicacionAsignada;
    }
    
    public void setUbicacionAsignada(String ubicacionAsignada) {
        this.ubicacionAsignada = ubicacionAsignada;
    }
    
    public int getExperienciaAnos() {
        return experienciaAnos;
    }
    
    public void setExperienciaAnos(int experienciaAnos) {
        this.experienciaAnos = Math.max(0, experienciaAnos);
    }
    
    public List<String> getCertificaciones() {
        return new ArrayList<>(certificaciones);
    }
    
    public void setCertificaciones(List<String> certificaciones) {
        this.certificaciones = new ArrayList<>(certificaciones);
    }
    
    public boolean isDisponible() {
        return disponible;
    }
    
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
    
    public LocalDateTime getUltimaAsignacion() {
        return ultimaAsignacion;
    }
    
    public void setUltimaAsignacion(LocalDateTime ultimaAsignacion) {
        this.ultimaAsignacion = ultimaAsignacion;
    }
    
    @Override
    public String toString() {
        return String.format("OperadorEmergencia{id='%s', nombre='%s', especialidad='%s', disponible=%s}", getId(), obtenerNombreCompleto(), especialidad, disponible);
    }
}
