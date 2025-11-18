package co.edu.uniquindio.GestionRiesgos.Model;

import co.edu.uniquindio.Gestion.de.Riesgos.Enums.Rol;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Clase base que representa un usuario del sistema de gestión de desastres
 */
public class Usuario {
    private String id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String username;
    private String password;
    protected Rol rol;
    private boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime ultimoAcceso;
    
    // Constructores
    public Usuario() {
        this.activo = true;
        this.fechaCreacion = LocalDateTime.now();
    }
    
    public Usuario(String id, String nombre, String apellido, String email, String username, String password, Rol rol) {
        this();
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.username = username;
        this.password = password;
        this.rol = rol;
    }
    
    /**
     * Verifica si el usuario puede realizar una acción específica
     */
    public boolean puedeRealizarAccion(String accion) {
        if (!activo) return false;
        
        switch (accion.toLowerCase()) {
            case "gestionar_usuarios":
            case "configurar_sistema":
            case "ver_todos_recursos":
                return rol.esAdministrador();
            case "gestionar_recursos":
            case "ver_estadisticas":
            case "monitorear_emergencias":
                return rol.puedeGestionarRecursos();
            case "ver_mapa":
            case "ver_rutas":
                return true;
            default:
                return false;
        }
    }
    
    /**
     * Actualiza el último acceso del usuario
     */
    public void actualizarUltimoAcceso() {
        this.ultimoAcceso = LocalDateTime.now();
    }
    
    /**
     * Verifica si el usuario está inactivo por mucho tiempo
     */
    public boolean estaInactivo() {
        if (ultimoAcceso == null) return false;
        return ultimoAcceso.isBefore(LocalDateTime.now().minusHours(24));
    }
    
    /**
     * Obtiene el nombre completo del usuario
     */
    public String obtenerNombreCompleto() {
        return nombre + " " + apellido;
    }
    
    /**
     * Verifica si las credenciales son válidas
     */
    public boolean verificarCredenciales(String username, String password) {
        return this.username.equals(username) && this.password.equals(password) && activo;
    }
    
    /**
     * Cambia la contraseña del usuario
     */
    public boolean cambiarPassword(String passwordActual, String passwordNueva) {
        if (this.password.equals(passwordActual) && passwordNueva != null && !passwordNueva.trim().isEmpty()) {
            this.password = passwordNueva;
            return true;
        }
        return false;
    }
    
    /**
     * Desactiva el usuario
     */
    public void desactivar() {
        this.activo = false;
    }
    
    /**
     * Activa el usuario
     */
    public void activar() {
        this.activo = true;
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
    
    public String getApellido() {
        return apellido;
    }
    
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Rol getRol() {
        return rol;
    }
    
    public void setRol(Rol rol) {
        this.rol = rol;
    }
    
    public boolean isActivo() {
        return activo;
    }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public LocalDateTime getUltimoAcceso() {
        return ultimoAcceso;
    }
    
    public void setUltimoAcceso(LocalDateTime ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id) || Objects.equals(username, usuario.username);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
    
    @Override
    public String toString() {
        return String.format("Usuario{id='%s', nombre='%s', rol=%s, activo=%s}", id, obtenerNombreCompleto(), rol.getDescripcion(), activo);
    }
}
