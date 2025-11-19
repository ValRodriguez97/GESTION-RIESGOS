package co.edu.uniquindio.GestionRiesgos.Model;

import co.edu.uniquindio.GestionRiesgos.Enums.Rol;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un administrador del sistema de gestión de desastres
 * Hereda de Usuario y tiene permisos completos del sistema
 */
public class Administrador extends Usuario {
    private List<String> permisosEspeciales;
    private String departamento;
    private int nivelAutorizacion;
    
    public Administrador() {
        super();
        this.permisosEspeciales = new ArrayList<>();
        this.rol = Rol.ADMINISTRADOR;
        this.nivelAutorizacion = 5; // Máximo nivel
        inicializarPermisosEspeciales();
    }
    
    public Administrador(String id, String nombre, String apellido, String email, 
                        String username, String password, String departamento) {
        super(id, nombre, apellido, email, username, password, Rol.ADMINISTRADOR);
        this.permisosEspeciales = new ArrayList<>();
        this.departamento = departamento;
        this.nivelAutorizacion = 5;
        inicializarPermisosEspeciales();
    }
    
    /**
     * Inicializa los permisos especiales del administrador
     */
    private void inicializarPermisosEspeciales() {
        permisosEspeciales.add("gestionar_usuarios");
        permisosEspeciales.add("configurar_sistema");
        permisosEspeciales.add("ver_todos_recursos");
        permisosEspeciales.add("gestionar_recursos");
        permisosEspeciales.add("ver_estadisticas");
        permisosEspeciales.add("monitorear_emergencias");
        permisosEspeciales.add("ver_mapa");
        permisosEspeciales.add("ver_rutas");
        permisosEspeciales.add("crear_emergencias");
        permisosEspeciales.add("eliminar_emergencias");
        permisosEspeciales.add("modificar_emergencias");
        permisosEspeciales.add("gestionar_equipos");
        permisosEspeciales.add("asignar_recursos");
        permisosEspeciales.add("generar_reportes");
        permisosEspeciales.add("configurar_alertas");
    }
    
    // Métodos específicos del administrador
    /**
     * Verifica si el administrador tiene un permiso especial
     */
    public boolean tienePermisoEspecial(String permiso) {
        return permisosEspeciales.contains(permiso.toLowerCase());
    }
    
    /**
     * Agrega un permiso especial al administrador
     */
    public void agregarPermisoEspecial(String permiso) {
        if (!permisosEspeciales.contains(permiso.toLowerCase())) {
            permisosEspeciales.add(permiso.toLowerCase());
        }
    }
    
    /**
     * Remueve un permiso especial del administrador
     */
    public void removerPermisoEspecial(String permiso) {
        permisosEspeciales.remove(permiso.toLowerCase());
    }
    
    /**
     * Obtiene todos los permisos especiales
     */
    public List<String> getPermisosEspeciales() {
        return new ArrayList<>(permisosEspeciales);
    }
    
    /**
     * Verifica si puede autorizar operaciones de alto nivel
     */
    public boolean puedeAutorizarAltoNivel() {
        return nivelAutorizacion >= 4;
    }
    
    /**
     * Verifica si puede gestionar otros administradores
     */
    public boolean puedeGestionarAdministradores() {
        return nivelAutorizacion >= 5;
    }
    
    /**
     * Genera un reporte de actividad del administrador
     */
    public String generarReporteActividad() {
        StringBuilder reporte = new StringBuilder();
        reporte.append("=== REPORTE DE ACTIVIDAD DEL ADMINISTRADOR ===\n");
        reporte.append("ID: ").append(getId()).append("\n");
        reporte.append("Nombre: ").append(obtenerNombreCompleto()).append("\n");
        reporte.append("Departamento: ").append(departamento).append("\n");
        reporte.append("Nivel de Autorización: ").append(nivelAutorizacion).append("\n");
        reporte.append("Fecha de Creación: ").append(getFechaCreacion()).append("\n");
        reporte.append("Último Acceso: ").append(getUltimoAcceso()).append("\n");
        reporte.append("Estado: ").append(isActivo() ? "Activo" : "Inactivo").append("\n");
        reporte.append("Permisos Especiales: ").append(permisosEspeciales.size()).append("\n");
        return reporte.toString();
    }
    
    
    public String getDepartamento() {
        return departamento;
    }
    
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    
    public int getNivelAutorizacion() {
        return nivelAutorizacion;
    }
    
    public void setNivelAutorizacion(int nivelAutorizacion) {
        this.nivelAutorizacion = Math.max(1, Math.min(5, nivelAutorizacion));
    }
    
    @Override
    public String toString() {
        return String.format("Administrador{id='%s', nombre='%s', departamento='%s', nivel=%d}", 
        getId(), obtenerNombreCompleto(), departamento, nivelAutorizacion);
    }
}

