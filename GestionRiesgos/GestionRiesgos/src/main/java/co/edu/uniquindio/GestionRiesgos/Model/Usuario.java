package co.edu.uniquindio.GestionRiesgos.Model;

import co.edu.uniquindio.GestionRiesgos.Enums.Rol;

import java.time.LocalDateTime;
import java.time.Duration;


/**
 * Clase que representa un usuario del sistema de gestión de desastres.
 * Un usuario tiene credenciales, un rol que define sus permisos y estado de actividad.
 */
public class Usuario {

    /** Identificador único del usuario */
    private String id;

    /** Nombre del usuario */
    private String nombre;

    /** Apellido del usuario */
    private String apellido;

    /** Correo electrónico */
    private String email;

    /** Teléfono de contacto */
    private String telefono;

    /** Nombre de usuario para login */
    private String username;

    /** Contraseña (en texto plano, se recomienda usar hash en producción) */
    private String password;

    /** Rol del usuario (define permisos y privilegios) */
    protected Rol rol;

    /** Indica si el usuario está activo en el sistema */
    private boolean activo;

    /** Fecha de creación del usuario */
    private LocalDateTime fechaCreacion;

    /** Fecha del último acceso */
    private LocalDateTime ultimoAcceso;

    /** Duración máxima de inactividad antes de considerar al usuario inactivo */
    private static final Duration INACTIVIDAD_MAX = Duration.ofHours(24);

    // -------------------- Constructores --------------------

    /**
     * Constructor por defecto.
     * Inicializa el usuario como activo y registra la fecha de creación.
     */
    public Usuario() {
        this.activo = true;
        this.fechaCreacion = LocalDateTime.now();
    }

    /**
     * Constructor completo para crear un usuario con todos sus datos.
     *
     * @param id Identificador único
     * @param nombre Nombre del usuario
     * @param apellido Apellido del usuario
     * @param email Correo electrónico
     * @param username Nombre de usuario
     * @param password Contraseña
     * @param rol Rol del usuario
     */
    public Usuario(String id, String nombre, String apellido, String email,
                   String username, String password, Rol rol) {
        this();
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    // -------------------- Métodos funcionales --------------------

    /**
     * Verifica si el usuario tiene permiso para realizar una acción.
     *
     * @param accion Nombre de la acción
     * @return true si puede realizarla; false en caso contrario
     */
    public boolean puedeRealizarAccion(String accion) {
        if (!activo || accion == null) return false;

        switch (accion.toLowerCase()) {
            case "gestionar_usuarios":
            case "configurar_sistema":
            case "ver_todos_recursos":
                return rol != null && rol.esAdministrador();
            case "gestionar_recursos":
            case "ver_estadisticas":
            case "monitorear_emergencias":
                return rol != null && rol.puedeGestionarRecursos();
            case "ver_mapa":
            case "ver_rutas":
                return true;
            default:
                return false;
        }
    }

    /**
     * Actualiza la fecha y hora del último acceso del usuario al sistema.
     */
    public void actualizarUltimoAcceso() {
        this.ultimoAcceso = LocalDateTime.now();
    }

    /**
     * Verifica si el usuario ha estado inactivo por más de 24 horas.
     *
     * @return true si está inactivo, false si está activo
     */
    public boolean estaInactivo() {
        return ultimoAcceso != null && ultimoAcceso.isBefore(LocalDateTime.now().minus(INACTIVIDAD_MAX));
    }

    /**
     * Obtiene el nombre completo del usuario concatenando nombre y apellido.
     *
     * @return Nombre completo
     */
    public String obtenerNombreCompleto() {
        return nombre + " " + apellido;
    }

    /**
     * Verifica si las credenciales proporcionadas coinciden con las del usuario.
     *
     * @param username Nombre de usuario ingresado
     * @param password Contraseña ingresada
     * @return true si coinciden y el usuario está activo, false en caso contrario
     */
    public boolean verificarCredenciales(String username, String password) {
        return this.username.equals(username) && this.password.equals(password) && activo;
    }

    /**
     * Cambia la contraseña del usuario si la actual coincide.
     *
     * @param passwordActual Contraseña actual
     * @param passwordNueva Nueva contraseña
     * @return true si se cambió correctamente, false si falla la validación
     */
    public boolean cambiarPassword(String passwordActual, String passwordNueva) {
        if (this.password.equals(passwordActual) &&
            passwordNueva != null && !passwordNueva.trim().isEmpty()) {
            this.password = passwordNueva;
            return true;
        }
        return false;
    }

    /**
     * Desactiva el usuario (no podrá iniciar sesión ni realizar acciones).
     */
    public void desactivar() {
        this.activo = false;
    }

    /**
     * Activa el usuario.
     */
    public void activar() {
        this.activo = true;
    }

    // -------------------- Getters y Setters --------------------

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public void setPassword(String password) { this.password = password; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public boolean isActivo() { return activo; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }

    public LocalDateTime getUltimoAcceso() { return ultimoAcceso; }

    // -------------------- Métodos estándar --------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario usuario)) return false;
        return id != null && id.equals(usuario.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.format("Usuario{id='%s', nombre='%s', rol=%s, activo=%s}",
                id, obtenerNombreCompleto(), rol != null ? rol.getDescripcion() : "N/A", activo);
    }
}