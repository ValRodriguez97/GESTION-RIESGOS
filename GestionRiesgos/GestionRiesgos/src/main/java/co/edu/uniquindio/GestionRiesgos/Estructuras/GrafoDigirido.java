package co.edu.uniquindio.GestionRiesgos.Estructuras;

import co.edu.uniquindio.Gestion.de.Riesgos.Interfaces.ICalcularRuta;
import co.edu.uniquindio.Gestion.de.Riesgos.Model.Zona;

import java.util.*;

/**
 * Clase que implementa un grafo dirigido para representar
 * las conexiones entre zonas en el sistema de gestión de desastres
 */
public class GrafoDirigido implements ICalcularRuta {
    private List<Nodo> nodos;
    private List<Ruta> aristas;
    private Map<String, Nodo> mapaNodos;
    private Map<String, List<Ruta>> listaAdyacencia;
    private ICalcularRuta calculadorRutas;
    
    public GrafoDirigido() {
        this.nodos = new ArrayList<>();
        this.aristas = new ArrayList<>();
        this.mapaNodos = new HashMap<>();
        this.listaAdyacencia = new HashMap<>();
    }
    
    public GrafoDirigido(ICalcularRuta calculadorRutas) {
        this();
        this.calculadorRutas = calculadorRutas;
    }
    
    /**
     * Agrega un nodo al grafo
     */
    public void agregarNodo(Nodo nodo) {
        if (nodo != null && !mapaNodos.containsKey(nodo.getId())) {
            nodos.add(nodo);
            mapaNodos.put(nodo.getId(), nodo);
            listaAdyacencia.put(nodo.getId(), new ArrayList<>());
        }
    }
    
    /**
     * Agrega una arista (ruta) al grafo
     */
    public void agregarArista(Ruta ruta) {
        if (ruta != null && ruta.getOrigen() != null && ruta.getDestino() != null) {
            Nodo nodoOrigen = new Nodo();
            nodoOrigen.setId(ruta.getOrigen().getId());
            nodoOrigen.setNombre(ruta.getOrigen().getNombre());
            nodoOrigen.setCoordenadaX(ruta.getOrigen().getCoordenadaX());
            nodoOrigen.setCoordenadaY(ruta.getOrigen().getCoordenadaY());
            nodoOrigen.setTipo(Nodo.TipoNodo.CIUDAD); // Default type
            
            Nodo nodoDestino = new Nodo();
            nodoDestino.setId(ruta.getDestino().getId());
            nodoDestino.setNombre(ruta.getDestino().getNombre());
            nodoDestino.setCoordenadaX(ruta.getDestino().getCoordenadaX());
            nodoDestino.setCoordenadaY(ruta.getDestino().getCoordenadaY());
            nodoDestino.setTipo(Nodo.TipoNodo.CIUDAD); // Default type
            
            agregarNodo(nodoOrigen);
            agregarNodo(nodoDestino);
            
            aristas.add(ruta);
            listaAdyacencia.get(ruta.getOrigen().getId()).add(ruta);
        }
    }
    
    /**
     * Obtiene un nodo por ID
     */
    public Nodo obtenerNodo(String id) {
        return mapaNodos.get(id);
    }
    
    /**
     * Obtiene todas las rutas desde un nodo origen
     */
    public List<Ruta> obtenerRutasDesde(String idOrigen) {
        return listaAdyacencia.getOrDefault(idOrigen, new ArrayList<>());
    }
    
    /**
     * Obtiene todas las rutas hacia un nodo destino
     */
    public List<Ruta> obtenerRutasHacia(String idDestino) {
        return aristas.stream()
            .filter(ruta -> ruta.getDestino().getId().equals(idDestino))
            .toList();
    }
    
    /**
     * Verifica si existe una ruta entre dos nodos
     */
    public boolean existeRuta(String idOrigen, String idDestino) {
        return listaAdyacencia.getOrDefault(idOrigen, new ArrayList<>())
            .stream()
            .anyMatch(ruta -> ruta.getDestino().getId().equals(idDestino));
    }
    
    @Override
    public List<Zona> calcularRutaMasCorta(Zona origen, Zona destino) {
        if (origen == null || destino == null) return new ArrayList<>();
        
        Map<String, Double> distancias = new HashMap<>();
        Map<String, Zona> predecesores = new HashMap<>();
        PriorityQueue<Zona> colaPrioridad = new PriorityQueue<>(
            Comparator.comparingDouble(zona -> distancias.getOrDefault(zona.getId(), Double.MAX_VALUE))
        );
        
        for (Nodo nodo : nodos) {
            distancias.put(nodo.getId(), Double.MAX_VALUE);
        }
        distancias.put(origen.getId(), 0.0);
        colaPrioridad.offer(origen);
        
        while (!colaPrioridad.isEmpty()) {
            Zona actual = colaPrioridad.poll();
            
            if (actual.getId().equals(destino.getId())) {
                break;
            }
            
            for (Ruta ruta : obtenerRutasDesde(actual.getId())) {
                Zona vecino = ruta.getDestino();
                if (vecino != null) {
                    double nuevaDistancia = distancias.get(actual.getId()) + ruta.getDistancia();
                    
                    if (nuevaDistancia < distancias.get(vecino.getId())) {
                        distancias.put(vecino.getId(), nuevaDistancia);
                        predecesores.put(vecino.getId(), actual);
                        colaPrioridad.offer(vecino);
                    }
                }
            }
        }
        
        List<Zona> ruta = new ArrayList<>();
        Zona actual = destino;
        while (actual != null) {
            ruta.add(0, actual);
            actual = predecesores.get(actual.getId());
        }
        
        return ruta.get(0).getId().equals(origen.getId()) ? ruta : new ArrayList<>();
    }
    
    @Override
    public List<Ruta> calcularTodasLasRutas(Zona origen, Zona destino) {
        List<Ruta> todasLasRutas = new ArrayList<>();
        Set<String> visitados = new HashSet<>();
        List<Zona> rutaActual = new ArrayList<>();
        
        dfsTodasLasRutas(origen, destino, visitados, rutaActual, todasLasRutas);
        
        return todasLasRutas;
    }
    
    private void dfsTodasLasRutas(Zona actual, Zona destino, Set<String> visitados, List<Zona> rutaActual, List<Ruta> todasLasRutas) {
        if (actual.getId().equals(destino.getId())) {
            for (int i = 0; i < rutaActual.size() - 1; i++) {
                String idOrigen = rutaActual.get(i).getId();
                String idDestino = rutaActual.get(i + 1).getId();
                
                Ruta ruta = obtenerRutasDesde(idOrigen).stream()
                    .filter(r -> r.getDestino().getId().equals(idDestino))
                    .findFirst()
                    .orElse(null);
                
                if (ruta != null) {
                    todasLasRutas.add(ruta);
                }
            }
            return;
        }
        
        visitados.add(actual.getId());
        rutaActual.add(actual);
        
        for (Ruta ruta : obtenerRutasDesde(actual.getId())) {
            Zona vecino = ruta.getDestino();
            if (vecino != null && !visitados.contains(vecino.getId())) {
                dfsTodasLasRutas(vecino, destino, visitados, rutaActual, todasLasRutas);
            }
        }
        
        visitados.remove(actual.getId());
        rutaActual.remove(rutaActual.size() - 1);
    }
    
    @Override
    public Ruta calcularRutaMasRapida(Zona origen, Zona destino) {
        List<Ruta> rutas = calcularTodasLasRutas(origen, destino);
        return rutas.stream()
            .min(Comparator.comparingDouble(Ruta::getTiempoEstimado))
            .orElse(null);
    }
    
    @Override
    public Ruta calcularRutaMasSegura(Zona origen, Zona destino) {
        List<Ruta> rutas = calcularTodasLasRutas(origen, destino);
        return rutas.stream()
            .min(Comparator.comparingDouble(Ruta::getNivelRiesgo))
            .orElse(null);
    }
    
    @Override
    public boolean existeRuta(Zona origen, Zona destino) {
        if (origen == null || destino == null) return false;
        return existeRuta(origen.getId(), destino.getId());
    }
    
    /**
     * Simula las rutas del grafo
     */
    public void simularRutas() {
        System.out.println("=== SIMULACIÓN DE RUTAS ===");
        for (Ruta ruta : aristas) {
            System.out.println("Ruta: " + ruta.getOrigen().getNombre() + 
                            " -> " + ruta.getDestino().getNombre() + 
                            " (Distancia: " + ruta.getDistancia() + ")");
        }
    }
    
    /**
     * Obtiene estadísticas del grafo
     */
    public String generarEstadisticas() {
        StringBuilder stats = new StringBuilder();
        stats.append("=== ESTADÍSTICAS DEL GRAFO DIRIGIDO ===\n");
        stats.append("Total de nodos: ").append(nodos.size()).append("\n");
        stats.append("Total de aristas: ").append(aristas.size()).append("\n");
        
        double gradoPromedio = aristas.size() / (double) nodos.size();
        stats.append("Grado promedio: ").append(String.format("%.2f", gradoPromedio)).append("\n");
        
        long rutasActivas = aristas.stream().filter(Ruta::isActiva).count();
        stats.append("Rutas activas: ").append(rutasActivas).append("\n");
        
        return stats.toString();
    }
    
    public List<Nodo> getNodos() {
        return new ArrayList<>(nodos);
    }
    
    public List<Ruta> getAristas() {
        return new ArrayList<>(aristas);
    }
    
    public int getNumeroNodos() {
        return nodos.size();
    }
    
    public int getNumeroAristas() {
        return aristas.size();
    }
    
    @Override
    public String toString() {
        return String.format("GrafoDirigido{nodos=%d, aristas=%d}", nodos.size(), aristas.size());
    }
}

