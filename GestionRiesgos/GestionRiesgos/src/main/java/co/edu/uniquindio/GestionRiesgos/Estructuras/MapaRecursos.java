package co.edu.uniquindio.GestionRiesgos.Estructuras;

import co.edu.uniquindio.Gestion.de.Riesgos.Model.Recurso;
import co.edu.uniquindio.Gestion.de.Riesgos.Enums.TipoRecurso;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Clase que representa un mapa de recursos que asocia recursos con rutas
 * para facilitar la gestión y distribución de recursos en el sistema
 */
public class MapaRecursos {
    private Map<Ruta, List<Recurso>> recursosPorRuta;
    private Map<String, Recurso> mapaRecursos;
    private Map<String, List<Ruta>> rutasPorRecurso;
    
    public MapaRecursos() {
        this.recursosPorRuta = new HashMap<>();
        this.mapaRecursos = new HashMap<>();
        this.rutasPorRecurso = new HashMap<>();
    }
    
    /**
     * Agrega un recurso a una ruta específica
     */
    public void agregarRecurso(Recurso recurso, Ruta ruta) {
        if (recurso != null && ruta != null) {
            mapaRecursos.put(recurso.getId(), recurso);
            
            recursosPorRuta.computeIfAbsent(ruta, k -> new ArrayList<>()).add(recurso);
            
            rutasPorRecurso.computeIfAbsent(recurso.getId(), k -> new ArrayList<>()).add(ruta);
        }
    }
    
    /**
     * Obtiene todos los recursos asociados a una ruta
     */
    public List<Recurso> obtenerRecursos(Ruta ruta) {
        return recursosPorRuta.getOrDefault(ruta, new ArrayList<>());
    }
    
    /**
     * Obtiene todas las rutas asociadas a un recurso
     */
    public List<Ruta> obtenerRutas(Recurso recurso) {
        return rutasPorRecurso.getOrDefault(recurso.getId(), new ArrayList<>());
    }
    
    /**
     * Obtiene un recurso por ID
     */
    public Recurso obtenerRecurso(String id) {
        return mapaRecursos.get(id);
    }
    
    /**
     * Obtiene todos los recursos del mapa
     */
    public List<Recurso> obtenerTodosLosRecursos() {
        return new ArrayList<>(mapaRecursos.values());
    }
    
    /**
     * Obtiene todas las rutas del mapa
     */
    public List<Ruta> obtenerTodasLasRutas() {
        return new ArrayList<>(recursosPorRuta.keySet());
    }
    
    /**
     * Obtiene recursos por tipo
     */
    public List<Recurso> obtenerRecursosPorTipo(TipoRecurso tipo) {
        return mapaRecursos.values().stream().filter(recurso -> recurso.getTipo() == tipo).collect(Collectors.toList());
    }
    
    /**
     * Obtiene recursos disponibles
     */
    public List<Recurso> obtenerRecursosDisponibles() {
        return mapaRecursos.values().stream()
            .filter(Recurso::estaDisponible)
            .collect(Collectors.toList());
    }
    
    /**
     * Obtiene recursos por ubicación
     */
    public List<Recurso> obtenerRecursosPorUbicacion(String ubicacionId) {
        return mapaRecursos.values().stream()
            .filter(recurso -> ubicacionId.equals(recurso.getUbicacionId()))
            .collect(Collectors.toList());
    }
    
    /**
     * Calcula el total de recursos por tipo
     */
    public Map<TipoRecurso, Integer> calcularTotalPorTipo() {
        Map<TipoRecurso, Integer> totales = new HashMap<>();
        
        for (Recurso recurso : mapaRecursos.values()) {
            totales.merge(recurso.getTipo(), recurso.getCantidadDisponible(), Integer::sum);
        }
        
        return totales;
    }
    
    /**
     * Calcula la distribución de recursos por ruta
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
    
    /**
     * Busca recursos por criterios específicos
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
    
    /**
     * Actualiza la cantidad de un recurso
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
     * Remueve un recurso del mapa
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
    
    /**
     * Obtiene estadísticas del mapa de recursos
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
     * Obtiene el resumen de recursos críticos
     */
    public List<Recurso> obtenerRecursosCriticos() {
        return mapaRecursos.values().stream()
            .filter(recurso -> recurso.calcularPrioridadTotal() >= 7)
            .sorted(Comparator.comparingInt(Recurso::calcularPrioridadTotal).reversed())
            .collect(Collectors.toList());
    }
    
    /**
     * Verifica si hay recursos suficientes para una ruta
     */
    public boolean hayRecursosSuficientes(Ruta ruta, int cantidadRequerida) {
        List<Recurso> recursos = obtenerRecursos(ruta);
        int totalDisponible = recursos.stream()
            .mapToInt(Recurso::getCantidadDisponible)
            .sum();
        return totalDisponible >= cantidadRequerida;
    }
    
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

