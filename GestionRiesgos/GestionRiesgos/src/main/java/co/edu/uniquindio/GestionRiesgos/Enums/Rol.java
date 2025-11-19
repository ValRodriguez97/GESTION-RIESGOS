package co.edu.uniquindio.GestionRiesgos.Enums;

/**
 * Representa los diferentes roles de usuario dentro del sistema.
 * 
 * Cada rol define:
 * - Una descripción legible para la interfaz.
 * - Un nivel de acceso numérico, donde un valor mayor indica mayores permisos.
 * 
 * Roles disponibles:
 * ADMINISTRADOR (nivel 1): Acceso total al sistema.  
 * OPERADOR_EMERGENCIA (nivel 2): Acceso operativo para gestión de emergencias.
 */
public enum Rol {

    /** Rol con mayores privilegios dentro del sistema. */
    ADMINISTRADOR("Administrador", 1),

    /** Rol encargado de la gestión operativa durante emergencias. */
    OPERADOR_EMERGENCIA("Operador de Emergencia", 2);

    /** Descripción textual del rol. */
    private final String descripcion;

    /** Nivel de acceso asociado al rol. */
    private final int nivelAcceso;

    /**
     * Constructor del enum Rol.
     *
     * @param descripcion texto descriptivo del rol.
     * @param nivelAcceso nivel de acceso asignado.
     */
    Rol(String descripcion, int nivelAcceso) {
        this.descripcion = descripcion;
        this.nivelAcceso = nivelAcceso;
    }

    /**
     * Obtiene la descripción legible del rol.
     *
     * @return descripción del rol.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Obtiene el nivel de acceso asignado.
     *
     * @return nivel de acceso.
     */
    public int getNivelAcceso() {
        return nivelAcceso;
    }

    /**
     * Indica si el rol corresponde a un administrador.
     *
     * @return true si el rol es ADMINISTRADOR, false en caso contrario.
     */
    public boolean esAdministrador() {
        return this == ADMINISTRADOR;
    }

    /**
     * Determina si este rol tiene permisos para gestionar recursos.
     * 
     * Tanto ADMINISTRADOR como OPERADOR_EMERGENCIA pueden hacerlo.
     *
     * @return true si tiene permisos para gestión de recursos.
     */
    public boolean puedeGestionarRecursos() {
        return this == ADMINISTRADOR || this == OPERADOR_EMERGENCIA;
    }

    /**
     * Determina si este rol tiene permisos para ver estadísticas del sistema.
     * 
     * Ambos roles tienen acceso a esta funcionalidad.
     *
     * @return true si puede visualizar estadísticas.
     */
    public boolean puedeVerEstadisticas() {
        return this == ADMINISTRADOR || this == OPERADOR_EMERGENCIA;
    }
}


