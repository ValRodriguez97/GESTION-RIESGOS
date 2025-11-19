package co.edu.uniquindio.GestionRiesgos.Model;

import co.edu.uniquindio.GestionRiesgos.Enums.TipoRecurso;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Modelo que representa un recurso en el sistema de gestión de desastres.
 * Puede ser alimentos, medicinas, equipos de rescate, etc.
 * Contiene información sobre cantidad, estado, ubicación, prioridad y fecha de vencimiento.
 */
public class Recurso {

    /** Identificador único del recurso */
    private String id;

    /** Nombre del recurso */
    private String nombre;

    /** Tipo de recurso */
    private TipoRecurso tipo;

    /** Cantidad total del recurso */
    private int cantidad;

    /** Cantidad disponible para uso */
    private int cantidadDisponible;

    /** Unidad de medida del recurso (ej. kg, litros) */
    private String unidadMedida;

    /** Fecha de vencimiento del recurso */
    private LocalDate fechaVencimiento;

    /** Estado actual del recurso */
    private EstadoRecurso estado;

    /** Ubicación donde se encuentra el recurso */
    private String ubicacionId;

    /** Descripción adicional del recurso */
    private String descripcion;

    /** Prioridad base para asignación o uso */
    private int prioridad;

    /**
     * Enum que representa el estado del recurso.
     */
    public enum EstadoRecurso {
        DISPONIBLE("Disponible"),
        EN_USO("En Uso"),
        AGOTADO("Agotado"),
        VENCIDO("Vencido"),
        DAÑADO("Dañado"),
        EN_TRANSITO("En Tránsito");

        private final String descripcion;

        EstadoRecurso(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    /**
     * Constructor por defecto. Inicializa el recurso como disponible con prioridad mínima.
     */
    public Recurso() {
        this.estado = EstadoRecurso.DISPONIBLE;
        this.cantidadDisponible = 0;
        this.prioridad = 1;
    }

    /**
     * Constructor con atributos básicos del recurso.
     *
     * @param id Identificador único
     * @param nombre Nombre del recurso
     * @param tipo Tipo de recurso
     * @param cantidad Cantidad total
     * @param unidadMedida Unidad de medida
     * @param ubicacionId Ubicación del recurso
     */
    public Recurso(String id, String nombre, TipoRecurso tipo, int cantidad, String unidadMedida, String ubicacionId) {
        this();
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.cantidadDisponible = cantidad;
        this.unidadMedida = unidadMedida;
        this.ubicacionId = ubicacionId;
        this.prioridad = tipo.getPrioridadBase();
    }

    /**
     * Calcula la prioridad total del recurso considerando tipo, urgencia y fecha de vencimiento.
     *
     * @return Prioridad total como entero
     */
    public int calcularPrioridadTotal() {
        int prioridadTotal = this.prioridad + tipo.getPrioridadBase();

        if (fechaVencimiento != null && fechaVencimiento.isBefore(LocalDate.now().plusDays(7))) {
            prioridadTotal += 2;
        }

        if (calcularPorcentajeDisponible() < 20) {
            prioridadTotal += 3;
        }

        return prioridadTotal;
    }

    /**
     * Calcula el porcentaje de recursos disponibles.
     *
     * @return Porcentaje entre 0.0 y 100.0
     */
    public double calcularPorcentajeDisponible() {
        if (cantidad == 0) return 0.0;
        return (double) cantidadDisponible / cantidad * 100;
    }

    /**
     * Verifica si el recurso está disponible para uso.
     *
     * @return true si está disponible y hay cantidad suficiente
     */
    public boolean estaDisponible() {
        return estado == EstadoRecurso.DISPONIBLE && cantidadDisponible > 0;
    }

    /**
     * Verifica si el recurso está vencido.
     *
     * @return true si la fecha de vencimiento ya pasó
     */
    public boolean estaVencido() {
        return fechaVencimiento != null && fechaVencimiento.isBefore(LocalDate.now());
    }

    /**
     * Reserva una cantidad específica del recurso.
     *
     * @param cantidadAReservar Cantidad a reservar
     * @return true si la reserva fue exitosa
     */
    public boolean reservar(int cantidadAReservar) {
        if (cantidadAReservar <= 0 || cantidadAReservar > cantidadDisponible) {
            return false;
        }

        cantidadDisponible -= cantidadAReservar;
        if (cantidadDisponible == 0) {
            estado = EstadoRecurso.AGOTADO;
        } else {
            estado = EstadoRecurso.EN_USO;
        }
        return true;
    }

    /**
     * Libera una cantidad específica del recurso.
     *
     * @param cantidadALiberar Cantidad a liberar
     */
    public void liberar(int cantidadALiberar) {
        if (cantidadALiberar > 0) {
            cantidadDisponible = Math.min(cantidad, cantidadDisponible + cantidadALiberar);
            if (cantidadDisponible == cantidad) {
                estado = EstadoRecurso.DISPONIBLE;
            }
        }
    }

    /**
     * Actualiza el estado del recurso basado en condiciones actuales.
     */
    public void actualizarEstado() {
        if (estaVencido()) {
            estado = EstadoRecurso.VENCIDO;
        } else if (cantidadDisponible == 0) {
            estado = EstadoRecurso.AGOTADO;
        } else if (cantidadDisponible < cantidad) {
            estado = EstadoRecurso.EN_USO;
        } else {
            estado = EstadoRecurso.DISPONIBLE;
        }
    }

    // Getters y Setters documentados para cada atributo

    

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recurso recurso = (Recurso) o;
        return Objects.equals(id, recurso.id);
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

    public TipoRecurso getTipo() {
        return tipo;
    }

    public void setTipo(TipoRecurso tipo) {
        this.tipo = tipo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public EstadoRecurso getEstado() {
        return estado;
    }

    public void setEstado(EstadoRecurso estado) {
        this.estado = estado;
    }

    public String getUbicacionId() {
        return ubicacionId;
    }

    public void setUbicacionId(String ubicacionId) {
        this.ubicacionId = ubicacionId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format(
            "Recurso{id='%s', nombre='%s', tipo=%s, cantidad=%d/%d %s, estado=%s}",
            id, nombre, tipo.getDescripcion(), cantidadDisponible, cantidad, unidadMedida, estado.getDescripcion()
        );
    }
}
