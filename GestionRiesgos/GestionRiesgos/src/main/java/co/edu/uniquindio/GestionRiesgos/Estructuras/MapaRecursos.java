package co.edu.uniquindio.GestionRiesgos.Estructuras;

import co.edu.uniquindio.GestionRiesgos.Model.Recurso;
import co.edu.uniquindio.GestionRiesgos.Enums.TipoRecurso;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Clase que representa un mapa de recursos que asocia recursos con rutas
 * para facilitar la gestión y distribución de recursos en el sistema.
 */
public class MapaRecursos {

    // ==============================
    //        ATRIBUTOS
    // ==============================

    /** Mapa que asocia cada ruta con la lista de recursos disponibles en ella */
    private Map<Ruta, List<Recurso>> recursosPorRuta;

    /** Mapa que almacena todos los recursos por su ID */
    private Map<String, Recurso> mapaRecursos;

    /** Mapa que asocia cada recurso con las rutas en las que está disponible */
    private Map<String, List<Ruta>> rutasPorRecurso;

    // ==============================
    //       CONSTRUCTOR
    // ==============================

    /**
     * Constructor que inicializa las estructuras de datos del mapa de recursos.
     */
    public MapaRecursos() {
        this.recursosPorRuta = new HashMap<>();
        this.mapaRecursos = new HashMap<>();
        this.rutasPorRecurso = new HashMap<>();
    }

    // ==============================
    //      MÉTODOS DE AGREGAR
    // ==============================

    /**
     * Agrega un recurso a una ruta específica y actualiza los mapas internos.
     *
     * @param recurso El recurso que se desea agregar.
     * @param ruta La ruta a la que se asignará el recurso.
     */
    public void agregarRecurso(Recurso recurso, Ruta ruta) {
        if (recurso != null && ruta != null) {
            mapaRecursos.put(recurso.getId(), recurso);

            recursosPorRuta.computeIfAbsent(ruta, k -> new ArrayList<>()).add(recurso);

            rutasPorRecurso.computeIfAbsent(recurso.getId(), k -> new ArrayList<>()).add(ruta);
        }
    }

    // ==============================
    //       MÉTODOS DE OBTENCIÓN
    // ==============================

    /**
     * Obtiene todos los recursos asociados a una ruta específica.
     *
     * @param ruta La ruta que se desea consultar.
     * @return Lista de recursos disponibles en la ruta. Retorna lista vacía si no hay recursos.
     */
    public List<Recurso> obtenerRecursos(Ruta ruta) {
        return recursosPorRuta.getOrDefault(ruta, new ArrayList<>());
    }

    /**
     * Obtiene todas las rutas asociadas a un recurso específico.
     *
     * @param recurso El recurso que se desea consultar.
     * @return Lista de rutas en las que se encuentra el recurso. Retorna lista vacía si no hay rutas.
     */
    public List<Ruta> obtenerRutas(Recurso recurso) {
        return rutasPorRecurso.getOrDefault(recurso.getId(), new ArrayList<>());
    }

    /**
     * Obtiene un recurso a partir de su ID.
     *
     * @param id El identificador único del recurso.
     * @return El recurso correspondiente o null si no existe.
     */
    public Recurso obtenerRecurso(String id) {
        return mapaRecursos.get(id);
    }

    /**
     * Obtiene todos los recursos existentes en el mapa.
     *
     * @return Lista de todos los recursos.
     */
    public List<Recurso> obtenerTodosLosRecursos() {
        return new ArrayList<>(mapaRecursos.values());
    }

    /**
     * Obtiene todas las rutas presentes en el mapa.
     *
     * @return Lista de todas las rutas.
     */
    public List<Ruta> obtenerTodasLasRutas() {
        return new ArrayList<>(recursosPorRuta.keySet());
    }

    /**
     * Obtiene los recursos de un tipo específico.
     *
     * @param tipo El tipo de recurso a filtrar.
     * @return Lista de recursos que pertenecen al tipo indicado.
     */
    public List<Recurso> obtenerRecursosPorTipo(TipoRecurso tipo) {
        return mapaRecursos.values().stream()
                .filter(recurso -> recurso.getTipo() == tipo)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene los recursos que actualmente están disponibles.
     *
     * @return Lista de recursos disponibles.
     */
    public List<Recurso> obtenerRecursosDisponibles() {
        return mapaRecursos.values().stream()
                .filter(Recurso::estaDisponible)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene recursos filtrados por su ubicación.
     *
     * @param ubicacionId El ID de la ubicación a consultar.
     * @return Lista de recursos en la ubicación indicada.
     */
    public List<Recurso> obtenerRecursosPorUbicacion(String ubicacionId) {
        return mapaRecursos.values().stream()
                .filter(recurso -> ubicacionId.equals(recurso.getUbicacionId()))
                .collect(Collectors.toList());
    }

    // ==============================
    //     MÉTODOS DE CÁLCULO
    // ==============================

    /**
     * Calcula el total de recursos disponibles por tipo.
     *
     * @return Mapa que asocia cada tipo de recurso con la suma de su cantidad disponible.
     */
    public Map<TipoRecurso, Integer> calcularTotalPorTipo() {
        Map<TipoRecurso, Integer> totales = new HashMap<>();
        for (Recurso recurso : mapaRecursos.values()) {
            totales.merge(recurso.getTipo(), recurso.getCantidadDisponible(), Integer::sum);
        }
        return totales;
    }

    /**
     * Calcula la distribución total de recursos por ruta.
     *
     * @return Mapa que asocia cada ruta con la suma de recursos disponibles en ella.
     */
    public Map<Ruta, Integer> calcularDistribucionPorRuta() {
        Map<Ruta, Integer> distribucion = new HashMap<>();
        for (Map.Entry<Ruta, List<Recurso>> entry : recursosPorRuta.entrySet()) {
            int total = entry.getValue().stream()
                    .mapToInt(Recurso::getCantidadDisponible)
                    .sum();
            distribucion.put(entry.getKey(), total);
        }
        return distribucion;
    }

    // ==============================
    //       BÚSQUEDA Y FILTRO
    // ==============================

    /**
     * Busca recursos según un criterio y valor dado.
     *
     * @param criterio Nombre del criterio (nombre, tipo, estado, ubicacion)
     * @param valor Valor a buscar según el criterio.
     * @return Lista de recursos que cumplen con el criterio.
     */
    public List<Recurso> buscarRecursos(String criterio, Object valor) {
        return mapaRecursos.values().stream()
                .filter(recurso -> {
                    switch (criterio.toLowerCase()) {
                        case "nombre":
                            return recurso.getNombre().toLowerCase().contains(valor.toString().toLowerCase());
                        case "tipo":
                            return recurso.getTipo().equals(valor);
                        case "estado":
                            return recurso.getEstado().equals(valor);
                        case "ubicacion":
                            return valor.toString().equals(recurso.getUbicacionId());
                        default:
                            return false;
                    }
                })
                .collect(Collectors.toList());
    }

    // ==============================
    //       ACTUALIZACIÓN
    // ==============================

    /**
     * Actualiza la cantidad disponible de un recurso.
     *
     * @param recursoId El ID del recurso.
     * @param nuevaCantidad La nueva cantidad disponible.
     * @return true si se actualizó correctamente, false si no existe el recurso.
     */
    public boolean actualizarCantidadRecurso(String recursoId, int nuevaCantidad) {
        Recurso recurso = mapaRecursos.get(recursoId);
        if (recurso != null) {
            recurso.setCantidadDisponible(nuevaCantidad);
            return true;
        }
        return false;
    }

    /**
     * Remueve un recurso del mapa y de todas las rutas donde esté asignado.
     *
     * @param recursoId El ID del recurso a eliminar.
     * @return true si se eliminó correctamente, false si no existe.
     */
    public boolean removerRecurso(String recursoId) {
        Recurso recurso = mapaRecursos.remove(recursoId);
        if (recurso != null) {
            for (List<Recurso> recursos : recursosPorRuta.values()) {
                recursos.removeIf(r -> r.getId().equals(recursoId));
            }
            rutasPorRecurso.remove(recursoId);
            return true;
        }
        return false;
    }

    // ==============================
    //      MÉTODOS DE ESTADÍSTICA
    // ==============================

    /**
     * Genera un resumen estadístico del mapa de recursos.
     *
     * @return Cadena con información sobre recursos totales, distribución y disponibilidad.
     */
    public String generarEstadisticas() {
        StringBuilder stats = new StringBuilder();
        stats.append("=== ESTADÍSTICAS DEL MAPA DE RECURSOS ===\n");
        stats.append("Total de recursos: ").append(mapaRecursos.size()).append("\n");
        stats.append("Total de rutas: ").append(recursosPorRuta.size()).append("\n");

        Map<TipoRecurso, Integer> totalesPorTipo = calcularTotalPorTipo();
        stats.append("\nRecursos por tipo:\n");
        for (Map.Entry<TipoRecurso, Integer> entry : totalesPorTipo.entrySet()) {
            stats.append("- ").append(entry.getKey().getDescripcion())
                    .append(": ").append(entry.getValue()).append("\n");
        }

        long recursosDisponibles = obtenerRecursosDisponibles().size();
        stats.append("\nRecursos disponibles: ").append(recursosDisponibles)
                .append(" de ").append(mapaRecursos.size()).append("\n");

        Map<Ruta, Integer> distribucion = calcularDistribucionPorRuta();
        stats.append("\nDistribución por ruta:\n");
        for (Map.Entry<Ruta, Integer> entry : distribucion.entrySet()) {
            stats.append("- Ruta ").append(entry.getKey().getId())
                    .append(": ").append(entry.getValue()).append(" recursos\n");
        }

        return stats.toString();
    }

    /**
     * Obtiene los recursos críticos, aquellos con prioridad total alta.
     *
     * @return Lista de recursos críticos ordenados por prioridad descendente.
     */
    public List<Recurso> obtenerRecursosCriticos() {
        return mapaRecursos.values().stream()
                .filter(recurso -> recurso.calcularPrioridadTotal() >= 7)
                .sorted(Comparator.comparingInt(Recurso::calcularPrioridadTotal).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Verifica si hay recursos suficientes para cubrir una cantidad requerida en una ruta.
     *
     * @param ruta La ruta a consultar.
     * @param cantidadRequerida Cantidad mínima de recursos necesaria.
     * @return true si hay suficientes recursos, false en caso contrario.
     */
    public boolean hayRecursosSuficientes(Ruta ruta, int cantidadRequerida) {
        List<Recurso> recursos = obtenerRecursos(ruta);
        int totalDisponible = recursos.stream()
                .mapToInt(Recurso::getCantidadDisponible)
                .sum();
        return totalDisponible >= cantidadRequerida;
    }

    // ==============================
    //           GETTERS
    // ==============================

    public Map<Ruta, List<Recurso>> getRecursosPorRuta() {
        return new HashMap<>(recursosPorRuta);
    }

    public Map<String, Recurso> getMapaRecursos() {
        return new HashMap<>(mapaRecursos);
    }

    public Map<String, List<Ruta>> getRutasPorRecurso() {
        return new HashMap<>(rutasPorRecurso);
    }

    public int getTotalRecursos() {
        return mapaRecursos.size();
    }

    public int getTotalRutas() {
        return recursosPorRuta.size();
    }

    @Override
    public String toString() {
        return String.format("MapaRecursos{recursos=%d, rutas=%d}", mapaRecursos.size(), recursosPorRuta.size());
    }
}


