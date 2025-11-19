package co.edu.uniquindio.GestionRiesgos.Estructuras;

import co.edu.uniquindio.GestionRiesgos.Interfaces.ICalcularRuta;
import co.edu.uniquindio.GestionRiesgos.Model.Zona;
import java.util.*;

/**
 * Clase que implementa un grafo dirigido para representar las conexiones entre
 * zonas dentro del sistema de gestión de desastres.
 *
 * Este grafo permite modelar rutas, calcular caminos mínimos, generar
 * estadísticas y realizar análisis como rutas más rápidas o más seguras.
 *
 * Se maneja mediante:
 * - Una lista de nodos.
 * - Una lista de rutas (aristas dirigidas).
 * - Un mapa de nodos para acceso rápido por ID.
 * - Una lista de adyacencia para gestionar las conexiones.
 *
 * Además, implementa la interfaz {@link ICalcularRuta}, permitiendo que el
 * grafo se utilice como motor de cálculo de rutas dentro del sistema.
 */
public class GrafoDirigido implements ICalcularRuta {

    /** Lista de nodos del grafo */
    private List<Nodo> nodos;

    /** Lista de aristas o rutas entre nodos */
    private List<Ruta> aristas;

    /** Mapa para acceso rápido a nodos por su ID */
    private Map<String, Nodo> mapaNodos;

    /** Lista de adyacencia: ID del nodo → rutas salientes */
    private Map<String, List<Ruta>> listaAdyacencia;

    /** Calculador alternativo de rutas (inyección opcional) */
    private ICalcularRuta calculadorRutas;

    /**
     * Constructor principal del grafo.
     * Inicializa las estructuras de almacenamiento.
     */
    public GrafoDirigido() {
        this.nodos = new ArrayList<>();
        this.aristas = new ArrayList<>();
        this.mapaNodos = new HashMap<>();
        this.listaAdyacencia = new HashMap<>();
    }

    /**
     * Constructor que permite proporcionar un componente externo de cálculo
     * de rutas.
     *
     * @param calculadorRutas Implementación personalizada de cálculo de rutas.
     */
    public GrafoDirigido(ICalcularRuta calculadorRutas) {
        this();
        this.calculadorRutas = calculadorRutas;
    }

    /**
     * Agrega un nodo al grafo si no existe previamente.
     *
     * @param nodo Nodo a agregar.
     */
    public void agregarNodo(Nodo nodo) {
        if (nodo != null && !mapaNodos.containsKey(nodo.getId())) {
            nodos.add(nodo);
            mapaNodos.put(nodo.getId(), nodo);
            listaAdyacencia.put(nodo.getId(), new ArrayList<>());
        }
    }

    /**
     * Agrega una arista dirigida al grafo.
     * También agrega los nodos de origen y destino si aún no existen.
     *
     * @param ruta Ruta o arista a incluir en el grafo.
     */
    public void agregarArista(Ruta ruta) {
        if (ruta != null && ruta.getOrigen() != null && ruta.getDestino() != null) {

            // Clonar nodos para evitar referencias compartidas
            Nodo nodoOrigen = new Nodo();
            nodoOrigen.setId(ruta.getOrigen().getId());
            nodoOrigen.setNombre(ruta.getOrigen().getNombre());
            nodoOrigen.setCoordenadaX(ruta.getOrigen().getCoordenadaX());
            nodoOrigen.setCoordenadaY(ruta.getOrigen().getCoordenadaY());
            nodoOrigen.setTipo(Nodo.TipoNodo.CIUDAD);

            Nodo nodoDestino = new Nodo();
            nodoDestino.setId(ruta.getDestino().getId());
            nodoDestino.setNombre(ruta.getDestino().getNombre());
            nodoDestino.setCoordenadaX(ruta.getDestino().getCoordenadaX());
            nodoDestino.setCoordenadaY(ruta.getDestino().getCoordenadaY());
            nodoDestino.setTipo(Nodo.TipoNodo.CIUDAD);

            agregarNodo(nodoOrigen);
            agregarNodo(nodoDestino);

            aristas.add(ruta);
            listaAdyacencia.get(ruta.getOrigen().getId()).add(ruta);
        }
    }

    /**
     * Obtiene un nodo por su identificador.
     *
     * @param id ID del nodo.
     * @return Nodo correspondiente o null si no existe.
     */
    public Nodo obtenerNodo(String id) {
        return mapaNodos.get(id);
    }

    /**
     * Obtiene todas las rutas que salen desde un nodo origen.
     *
     * @param idOrigen ID del nodo origen.
     * @return Lista de rutas salientes.
     */
    public List<Ruta> obtenerRutasDesde(String idOrigen) {
        return listaAdyacencia.getOrDefault(idOrigen, new ArrayList<>());
    }

    /**
     * Obtiene todas las rutas que llegan a un nodo destino.
     *
     * @param idDestino ID del nodo destino.
     * @return Lista de rutas entrantes.
     */
    public List<Ruta> obtenerRutasHacia(String idDestino) {
        return aristas.stream()
                .filter(ruta -> ruta.getDestino().getId().equals(idDestino))
                .toList();
    }

    /**
     * Verifica si existe una ruta directa entre dos nodos.
     *
     * @param idOrigen ID del nodo origen.
     * @param idDestino ID del nodo destino.
     * @return true si existe, false en caso contrario.
     */
    public boolean existeRuta(String idOrigen, String idDestino) {
        return listaAdyacencia.getOrDefault(idOrigen, new ArrayList<>())
                .stream()
                .anyMatch(ruta -> ruta.getDestino().getId().equals(idDestino));
    }

    /**
     * Calcula la ruta más corta entre dos zonas utilizando el algoritmo de Dijkstra.
     *
     * @param origen Zona origen.
     * @param destino Zona destino.
     * @return Lista de zonas que conforman el camino mínimo.
     */
    @Override
    public List<Zona> calcularRutaMasCorta(Zona origen, Zona destino) {
        if (origen == null || destino == null) return new ArrayList<>();

        Map<String, Double> distancias = new HashMap<>();
        Map<String, Zona> predecesores = new HashMap<>();

        PriorityQueue<Zona> colaPrioridad = new PriorityQueue<>(
                Comparator.comparingDouble(z -> distancias.getOrDefault(z.getId(), Double.MAX_VALUE))
        );

        for (Nodo nodo : nodos) {
            distancias.put(nodo.getId(), Double.MAX_VALUE);
        }

        distancias.put(origen.getId(), 0.0);
        colaPrioridad.offer(origen);

        while (!colaPrioridad.isEmpty()) {
            Zona actual = colaPrioridad.poll();

            if (actual.getId().equals(destino.getId())) break;

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

    /**
     * Calcula todas las rutas posibles entre dos zonas usando búsqueda DFS.
     *
     * @param origen Zona origen.
     * @param destino Zona destino.
     * @return Lista de rutas que forman todos los caminos posibles.
     */
    @Override
    public List<Ruta> calcularTodasLasRutas(Zona origen, Zona destino) {
        List<Ruta> todasLasRutas = new ArrayList<>();
        Set<String> visitados = new HashSet<>();
        List<Zona> rutaActual = new ArrayList<>();

        dfsTodasLasRutas(origen, destino, visitados, rutaActual, todasLasRutas);
        return todasLasRutas;
    }

    /**
     * Búsqueda en profundidad para generar todas las rutas posibles.
     */
    private void dfsTodasLasRutas(
            Zona actual,
            Zona destino,
            Set<String> visitados,
            List<Zona> rutaActual,
            List<Ruta> todasLasRutas) {

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

    /**
     * Obtiene la ruta más rápida según el tiempo estimado.
     */
    @Override
    public Ruta calcularRutaMasRapida(Zona origen, Zona destino) {
        List<Ruta> rutas = calcularTodasLasRutas(origen, destino);
        return rutas.stream()
                .min(Comparator.comparingDouble(Ruta::getTiempoEstimado))
                .orElse(null);
    }

    /**
     * Obtiene la ruta más segura (menor nivel de riesgo).
     */
    @Override
    public Ruta calcularRutaMasSegura(Zona origen, Zona destino) {
        List<Ruta> rutas = calcularTodasLasRutas(origen, destino);
        return rutas.stream()
                .min(Comparator.comparingDouble(Ruta::getNivelRiesgo))
                .orElse(null);
    }

    /**
     * Verifica si existe una ruta entre dos zonas.
     */
    @Override
    public boolean existeRuta(Zona origen, Zona destino) {
        if (origen == null || destino == null) return false;
        return existeRuta(origen.getId(), destino.getId());
    }

    /**
     * Imprime en consola una simulación simple de las rutas del grafo.
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
     * Genera estadísticas generales del grafo.
     *
     * @return Cadena detallada con información del grafo.
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

    /** @return Lista de nodos del grafo. */
    public List<Nodo> getNodos() {
        return new ArrayList<>(nodos);
    }

    /** @return Lista de aristas del grafo. */
    public List<Ruta> getAristas() {
        return new ArrayList<>(aristas);
    }

    /** @return Cantidad de nodos del grafo. */
    public int getNumeroNodos() {
        return nodos.size();
    }

    /** @return Cantidad de aristas del grafo. */
    public int getNumeroAristas() {
        return aristas.size();
    }

    @Override
    public String toString() {
        return String.format("GrafoDirigido{nodos=%d, aristas=%d}", nodos.size(), aristas.size());
    }
}
