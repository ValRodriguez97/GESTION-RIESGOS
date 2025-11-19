package co.edu.uniquindio.GestionRiesgos.Model;

import co.edu.uniquindio.GestionRiesgos.Enums.NivelUrgencia;
import co.edu.uniquindio.GestionRiesgos.Enums.TipoRecurso;
import co.edu.uniquindio.GestionRiesgos.Enums.TipoRuta;
import co.edu.uniquindio.GestionRiesgos.Estructuras.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Clase central del sistema de gestión de desastres naturales.
 * Gestiona usuarios, recursos, evacuaciones, rutas, zonas y equipos de rescate.
 * Además, integra estructuras de datos como grafo, cola de prioridad, mapa de recursos y árbol de distribución.
 */
public class SistemaGestionDesastres {

    private List<Usuario> usuarios;
    private List<Recurso> recursos;
    private List<Evacuacion> evacuaciones;
    private List<Ruta> rutas;
    private List<Zona> zonas;
    private List<EquipoRescate> equipos;

    private GrafoDirigido grafoDirigido;
    private ColaPrioridad colaPrioridad;
    private MapaRecursos mapaRecursos;
    private ArbolDistribucion arbolDistribucion;

    /**
     * Constructor que inicializa todas las estructuras de datos.
     */
    public SistemaGestionDesastres() {
        this.usuarios = new ArrayList<>();
        this.recursos = new ArrayList<>();
        this.evacuaciones = new ArrayList<>();
        this.rutas = new ArrayList<>();
        this.zonas = new ArrayList<>();
        this.equipos = new ArrayList<>();
        this.grafoDirigido = new GrafoDirigido();
        this.colaPrioridad = new ColaPrioridad();
        this.mapaRecursos = new MapaRecursos();
        this.arbolDistribucion = new ArbolDistribucion();
    }

    // ----------------------------
    // Inicialización del sistema
    // ----------------------------

    /**
     * Reinicia las estructuras de datos del sistema.
     */
    public void inicializarSistema() {
        this.grafoDirigido = new GrafoDirigido();
        this.colaPrioridad = new ColaPrioridad();
        this.mapaRecursos = new MapaRecursos();
        this.arbolDistribucion = new ArbolDistribucion();
        System.out.println("Sistema de Gestión de Desastres inicializado correctamente.");
    }

    // ----------------------------
    // Métodos de agregación
    // ----------------------------

    /**
     * Agrega un usuario al sistema si no existe previamente.
     *
     * @param usuario Usuario a agregar.
     * @return true si se agregó correctamente; false si ya existe o es null.
     */
    public boolean agregarUsuario(Usuario usuario) {
        if (usuario != null && !usuarios.contains(usuario)) {
            return usuarios.add(usuario);
        }
        return false;
    }

    /**
     * Agrega un recurso al sistema.
     * Además, lo asigna a las rutas asociadas a su ubicación si existe.
     *
     * @param recurso Recurso a agregar.
     * @return true si se agregó correctamente; false si ya existe o es null.
     */
    public boolean agregarRecurso(Recurso recurso) {
        if (recurso == null || recursos.contains(recurso)) return false;
        recursos.add(recurso);

        if (recurso.getUbicacionId() != null && !recurso.getUbicacionId().isEmpty()) {
            List<Ruta> rutasAsociadas = rutas.stream()
                    .filter(ruta -> (ruta.getOrigen() != null && ruta.getOrigen().getId().equals(recurso.getUbicacionId()))
                            || (ruta.getDestino() != null && ruta.getDestino().getId().equals(recurso.getUbicacionId())))
                    .toList();

            for (Ruta ruta : rutasAsociadas) {
                asignarRecursoARuta(recurso, ruta);
            }
        }
        return true;
    }

    /**
     * Asigna un recurso a una ruta específica.
     *
     * @param recurso Recurso a asignar.
     * @param ruta    Ruta a la que se asigna.
     * @return true si se realizó correctamente.
     */
    public boolean asignarRecursoARuta(Recurso recurso, Ruta ruta) {
        if (recurso == null || ruta == null) return false;
        mapaRecursos.agregarRecurso(recurso, ruta);
        return true;
    }

    /**
     * Agrega una evacuación al sistema y la encola en la cola de prioridad.
     *
     * @param evacuacion Evacuación a agregar.
     * @return true si se agregó correctamente.
     */
    public boolean agregarEvacuacion(Evacuacion evacuacion) {
        if (evacuacion != null && !evacuaciones.contains(evacuacion)) {
            evacuaciones.add(evacuacion);
            colaPrioridad.agregarEvacuacion(evacuacion);
            return true;
        }
        return false;
    }

    /**
     * Agrega una ruta al sistema y al grafo.
     *
     * @param ruta Ruta a agregar.
     * @return true si se agregó correctamente.
     */
    public boolean agregarRuta(Ruta ruta) {
        if (ruta != null && !rutas.contains(ruta)) {
            rutas.add(ruta);
            grafoDirigido.agregarArista(ruta);
            return true;
        }
        return false;
    }

    /**
     * Agrega una zona al sistema.
     *
     * @param zona Zona a agregar.
     * @return true si se agregó correctamente.
     */
    public boolean agregarZona(Zona zona) {
        if (zona != null && !zonas.contains(zona)) {
            zonas.add(zona);
            return true;
        }
        return false;
    }

    /**
     * Agrega un equipo de rescate al sistema.
     *
     * @param equipo Equipo a agregar.
     * @return true si se agregó correctamente.
     */
    public boolean agregarEquipo(EquipoRescate equipo) {
        if (equipo != null && !equipos.contains(equipo)) {
            equipos.add(equipo);
            return true;
        }
        return false;
    }

    // ----------------------------
    // Búsquedas por ID
    // ----------------------------

    public Zona buscarZona(String id) {
        if (id == null) return null;
        return zonas.stream().filter(z -> z.getId().equals(id)).findFirst().orElse(null);
    }

    public Recurso buscarRecurso(String id) {
        if (id == null) return null;
        return recursos.stream().filter(r -> r.getId().equals(id)).findFirst().orElse(null);
    }

    public EquipoRescate buscarEquipo(String id) {
        if (id == null) return null;
        return equipos.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
    }

    public Ruta buscarRuta(String id) {
        if (id == null) return null;
        return rutas.stream().filter(r -> r.getId().equals(id)).findFirst().orElse(null);
    }

    public Usuario buscarUsuarioPorId(String id) {
        if (id == null) return null;
        return usuarios.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
    }

    // ----------------------------
    // Gestión de rutas
    // ----------------------------

    /**
     * Conecta dos zonas creando una ruta y agregándola al sistema y al grafo.
     */
    public Ruta conectarZonas(String idRuta, String idOrigen, String idDestino,
                              double distancia, double tiempo, TipoRuta tipo) {
        Zona origen = buscarZona(idOrigen);
        Zona destino = buscarZona(idDestino);
        if (origen == null || destino == null) return null;

        Ruta ruta = new Ruta(idRuta, origen, destino, distancia, tiempo, tipo);
        if (agregarRuta(ruta)) return ruta;
        return null;
    }

    /**
     * Retorna la ruta más corta entre dos zonas (por distancia).
     */
    public List<Zona> calcularRutaMasCorta(String idOrigen, String idDestino) {
        Zona o = buscarZona(idOrigen);
        Zona d = buscarZona(idDestino);
        if (o == null || d == null) return List.of();
        return grafoDirigido.calcularRutaMasCorta(o, d);
    }

    /**
     * Retorna la ruta más rápida entre dos zonas (por tiempo).
     */
    public Ruta calcularRutaMasRapida(String idOrigen, String idDestino) {
        Zona o = buscarZona(idOrigen);
        Zona d = buscarZona(idDestino);
        if (o == null || d == null) return null;
        return grafoDirigido.calcularRutaMasRapida(o, d);
    }

    /**
     * Retorna la ruta más segura entre dos zonas.
     */
    public Ruta calcularRutaMasSegura(String idOrigen, String idDestino) {
        Zona o = buscarZona(idOrigen);
        Zona d = buscarZona(idDestino);
        if (o == null || d == null) return null;
        return grafoDirigido.calcularRutaMasSegura(o, d);
    }

    // ----------------------------
    // Gestión de recursos a zonas
    // ----------------------------

    /**
     * Asigna un recurso a una zona, disminuyendo el stock disponible.
     */
    public boolean asignarRecursoAZona(String recursoId, String zonaDestinoId, int cantidad) {
        Recurso r = buscarRecurso(recursoId);
        Zona z = buscarZona(zonaDestinoId);
        if (r == null || z == null || cantidad <= 0) return false;
        if (!r.reservar(cantidad)) return false;

        Recurso copia = new Recurso(r.getId() + "-Z" + z.getId(),
                r.getNombre(), r.getTipo(), cantidad, r.getUnidadMedida(), z.getId());
        recursos.add(copia);
        return true;
    }

    /**
     * Retorna un resumen de recursos agrupado por tipo.
     */
    public Map<TipoRecurso, Integer> resumenRecursosPorTipo() {
        Map<TipoRecurso, Integer> totales = new HashMap<>();
        for (Recurso r : recursos) {
            totales.merge(r.getTipo(), r.getCantidadDisponible(), Integer::sum);
        }
        return totales;
    }

    // ----------------------------
    // Gestión de equipos
    // ----------------------------

    /**
     * Traslada un equipo a una zona y lo deja disponible.
     */
    public boolean asignarEquipoAZona(String equipoId, String zonaId) {
        EquipoRescate eq = buscarEquipo(equipoId);
        Zona z = buscarZona(zonaId);
        if (eq == null || z == null) return false;

        eq.actualizarUbicacion(z.getId());
        eq.setEstado(EquipoRescate.EstadoEquipo.DISPONIBLE);
        return true;
    }

    // ----------------------------
    // Evacuaciones
    // ----------------------------

    /**
     * Planifica una evacuación entre dos zonas y la encola.
     */
    public Evacuacion planificarEvacuacionEntreZonas(String idEvac, String idZonaOrigen,
                                                     String idZonaDestino, int personasAEvacuar,
                                                     NivelUrgencia urgencia, String responsable) {
        Zona origen = buscarZona(idZonaOrigen);
        Zona destino = buscarZona(idZonaDestino);
        if (origen == null || destino == null || personasAEvacuar <= 0) return null;

        Ruta rutaElegida = calcularRutaMasRapida(idZonaOrigen, idZonaDestino);

        Evacuacion ev = new Evacuacion();
        ev.setId(idEvac);
        ev.setNombre("Evacuación " + idEvac);
        ev.setNivelUrgencia(urgencia != null ? urgencia : origen.getNivelUrgencia());
        ev.setPersonasAEvacuar(personasAEvacuar);
        ev.setResponsable(responsable);

        if (rutaElegida != null) ev.setRuta(rutaElegida);
        else {
            ev.setZonaOrigen(idZonaOrigen);
            ev.setZonaDestino(idZonaDestino);
        }

        if (agregarEvacuacion(ev)) {
            colaPrioridad.priorizar();
            return ev;
        }
        return null;
    }

    /**
     * Procesa la siguiente evacuación en la cola de prioridad.
     */
    public Evacuacion procesarSiguienteEvacuacion() {
        Evacuacion e = colaPrioridad.obtenerSiguienteEvacuacion();
        if (e != null) e.setEstado(Evacuacion.EstadoEvacuacion.EN_PROGRESO);
        return e;
    }

    /**
     * Completa una evacuación y actualiza la población en zonas.
     */
    public void completarEvacuacion(Evacuacion ev, int personasEvacuadas) {
        if (ev == null) return;

        int nuevo = Math.max(ev.getPersonasEvacuadas(), personasEvacuadas);
        ev.actualizarProgreso(nuevo);
        ev.setEstado(Evacuacion.EstadoEvacuacion.COMPLETADA);

        String idO = ev.getZonaOrigen() != null ? ev.getZonaOrigen()
                : (ev.getRuta() != null ? ev.getRuta().getOrigen().getId() : null);
        String idD = ev.getZonaDestino() != null ? ev.getZonaDestino()
                : (ev.getRuta() != null ? ev.getRuta().getDestino().getId() : null);

        Zona origen = buscarZona(idO);
        Zona destino = buscarZona(idD);

        int mov = ev.getPersonasEvacuadas();
        if (origen != null) origen.setPoblacionAfectada(Math.max(0, origen.getPoblacionAfectada() - mov));
        if (destino != null) destino.setPoblacionAfectada(destino.getPoblacionAfectada() + mov);
    }

    /**
     * Limpia de la cola evacuaciones COMPLETADAS o CANCELADAS.
     */
    public void limpiarEvacuacionesCompletadas() {
        colaPrioridad.limpiarCompletadas();
    }

    // ----------------------------
    // Reportes y estadísticas
    // ----------------------------

    /**
     * Retorna estadísticas generales del sistema.
     */
    public String obtenerEstadisticasGenerales() {
        StringBuilder stats = new StringBuilder();
        stats.append("=== ESTADÍSTICAS DEL SISTEMA ===\n")
             .append("Usuarios: ").append(usuarios.size()).append("\n")
             .append("Recursos: ").append(recursos.size()).append("\n")
             .append("Evacuaciones: ").append(evacuaciones.size()).append("\n")
             .append("Rutas: ").append(rutas.size()).append("\n")
             .append("Zonas: ").append(zonas.size()).append("\n")
             .append("Equipos: ").append(equipos.size()).append("\n")
             .append("\n").append(grafoDirigido.generarEstadisticas())
             .append("\n").append(colaPrioridad.generarEstadisticas());

        return stats.toString();
    }

    /**
     * Genera un reporte general extendido.
     */
    public String generarReporteGeneral() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== REPORTE GENERAL ===\n")
          .append("Usuarios: ").append(usuarios.size()).append("\n")
          .append("Zonas: ").append(zonas.size()).append("\n")
          .append("Rutas: ").append(rutas.size()).append("\n")
          .append("Recursos: ").append(recursos.size()).append("\n")
          .append("Equipos: ").append(equipos.size()).append("\n")
          .append("Evacuaciones: ").append(evacuaciones.size()).append("\n\n")
          .append(grafoDirigido.generarEstadisticas()).append("\n\n")
          .append(colaPrioridad.generarEstadisticas()).append("\n\n")
          .append(mapaRecursos.generarEstadisticas());

        return sb.toString();
    }

    // ----------------------------
    // Getters (devuelven copias para seguridad)
    // ----------------------------

    public List<Usuario> getUsuarios() { return new ArrayList<>(usuarios); }
    public List<Recurso> getRecursos() { return new ArrayList<>(recursos); }
    public List<Evacuacion> getEvacuaciones() { return new ArrayList<>(evacuaciones); }
    public List<Ruta> getRutas() { return new ArrayList<>(rutas); }
    public List<Zona> getZonas() { return new ArrayList<>(zonas); }
    public List<EquipoRescate> getEquipos() { return new ArrayList<>(equipos); }
    public GrafoDirigido getGrafoDirigido() { return grafoDirigido; }
    public ColaPrioridad getColaPrioridad() { return colaPrioridad; }
    public MapaRecursos getMapaRecursos() { return mapaRecursos; }
    public ArbolDistribucion getArbolDistribucion() { return arbolDistribucion; }

    @Override
    public String toString() {
        return String.format(
                "SistemaGestionDesastres{usuarios=%d, recursos=%d, evacuaciones=%d, rutas=%d, zonas=%d, equipos=%d}",
                usuarios.size(), recursos.size(), evacuaciones.size(), rutas.size(), zonas.size(), equipos.size());
    }
}
