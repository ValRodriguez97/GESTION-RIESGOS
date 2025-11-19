package co.edu.uniquindio.GestionRiesgos.Model;

import co.edu.uniquindio.GestionRiesgos.Enums.Rol;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un operador de emergencia del sistema de gestión de desastres.
 * Hereda de Usuario y tiene permisos específicos para operaciones de emergencia.
 * Contiene información sobre especialidad, certificaciones, ubicación y disponibilidad.
 */
public class OperadorEmergencia extends Usuario {

    /** Especialidad principal del operador */
    private String especialidad;

    /** Ubicación asignada para la operación */
    private String ubicacionAsignada;

    /** Años de experiencia del operador */
    private int experienciaAnos;

    /** Lista de certificaciones del operador */
    private List<String> certificaciones;

    /** Indica si el operador está disponible para asignación */
    private boolean disponible;

    /** Fecha y hora de la última asignación */
    private LocalDateTime ultimaAsignacion;

    /**
     * Constructor por defecto. Inicializa las certificaciones básicas y marca al operador como disponible.
     */
    public OperadorEmergencia() {
        super();
        this.rol = Rol.OPERADOR_EMERGENCIA;
        this.certificaciones = new ArrayList<>();
        this.disponible = true;
        this.experienciaAnos = 0;
        inicializarCertificaciones();
    }

    /**
     * Constructor con información básica del operador.
     *
     * @param id Identificador único
     * @param nombre Nombre del operador
     * @param apellido Apellido del operador
     * @param email Correo electrónico
     * @param username Nombre de usuario
     * @param password Contraseña
     * @param especialidad Especialidad del operador
     * @param ubicacionAsignada Ubicación asignada
     */
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
     * Inicializa las certificaciones básicas del operador.
     */
    private void inicializarCertificaciones() {
        certificaciones.add("Primeros Auxilios");
        certificaciones.add("Manejo de Emergencias");
        certificaciones.add("Comunicaciones de Emergencia");
    }

    /**
     * Verifica si el operador posee una certificación específica.
     *
     * @param certificacion Nombre de la certificación
     * @return true si tiene la certificación
     */
    public boolean tieneCertificacion(String certificacion) {
        return certificaciones.contains(certificacion);
    }

    /**
     * Agrega una nueva certificación al operador.
     *
     * @param certificacion Nombre de la certificación
     */
    public void agregarCertificacion(String certificacion) {
        if (!certificaciones.contains(certificacion)) {
            certificaciones.add(certificacion);
        }
    }

    /**
     * Calcula la eficiencia del operador basada en experiencia y certificaciones.
     *
     * @return Eficiencia entre 0.0 y 1.0
     */
    public double calcularEficiencia() {
        double factorExperiencia = Math.min(1.0, experienciaAnos / 10.0);
        double factorCertificaciones = Math.min(1.0, certificaciones.size() / 10.0);
        return (factorExperiencia + factorCertificaciones) / 2.0;
    }

    /**
     * Verifica si el operador puede ser asignado a una emergencia.
     *
     * @return true si está disponible y activo
     */
    public boolean puedeSerAsignado() {
        return disponible && isActivo() && experienciaAnos > 0;
    }

    /**
     * Asigna el operador a una ubicación específica.
     *
     * @param ubicacion Nombre de la ubicación
     */
    public void asignarAUbicacion(String ubicacion) {
        this.ubicacionAsignada = ubicacion;
        this.ultimaAsignacion = LocalDateTime.now();
        this.disponible = false;
    }

    /**
     * Libera al operador de su asignación actual.
     */
    public void liberarDeAsignacion() {
        this.ubicacionAsignada = null;
        this.disponible = true;
    }

    /**
     * Verifica si el operador necesita descanso.
     *
     * @return true si han pasado más de 12 horas desde la última asignación
     */
    public boolean necesitaDescanso() {
        if (ultimaAsignacion == null) return false;
        return ultimaAsignacion.isBefore(LocalDateTime.now().minusHours(12));
    }

    /**
     * Calcula la prioridad del operador para asignación.
     *
     * @return Valor de prioridad (mínimo 1)
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
     * Genera un reporte de actividad del operador.
     *
     * @return String con resumen de información y estado
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

    // Getters y Setters documentados para cada atributo

    @Override
    public String toString() {
        return String.format(
            "OperadorEmergencia{id='%s', nombre='%s', especialidad='%s', disponible=%s}",
            getId(), obtenerNombreCompleto(), especialidad, disponible
        );
    }
}

