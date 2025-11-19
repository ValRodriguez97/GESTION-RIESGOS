package co.edu.uniquindio.GestionRiesgos.Estructuras;

import co.edu.uniquindio.GestionRiesgos.Model.Recurso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Clase que representa un árbol de distribución para organizar
 * la asignación de recursos a rutas específicas
 */
public class ArbolDistribucion {
    private String id;
    private Ruta ruta;
    private Recurso recurso;
    private int cantidadAsignada;
    private int cantidadDisponible;
    private NodoDistribucion nodoRaiz;
    private List<NodoDistribucion> nodos;
    
    /**
     * Clase interna que representa un nodo en el árbol de distribución
     */
    public static class NodoDistribucion {
        private String id;
        private Recurso recurso;
        private int cantidad;
        private NodoDistribucion padre;
        private List<NodoDistribucion> hijos;
        private int prioridad;
        
        public NodoDistribucion(String id, Recurso recurso, int cantidad) {
            this.id = id;
            this.recurso = recurso;
            this.cantidad = cantidad;
            this.hijos = new ArrayList<>();
            this.prioridad = 1;
        }
        
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        
        public Recurso getRecurso() { return recurso; }
        public void setRecurso(Recurso recurso) { this.recurso = recurso; }
        
        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }
        
        public NodoDistribucion getPadre() { return padre; }
        public void setPadre(NodoDistribucion padre) { this.padre = padre; }
        
        public List<NodoDistribucion> getHijos() { return new ArrayList<>(hijos); }
        public void setHijos(List<NodoDistribucion> hijos) { this.hijos = new ArrayList<>(hijos); }
        
        public int getPrioridad() { return prioridad; }
        public void setPrioridad(int prioridad) { this.prioridad = prioridad; }
        
        public void agregarHijo(NodoDistribucion hijo) {
            if (hijo != null) {
                hijo.setPadre(this);
                this.hijos.add(hijo);
            }
        }
        
        public boolean esHoja() {
            return hijos.isEmpty();
        }
        
        public int calcularCantidadTotal() {
            return cantidad + hijos.stream()
                .mapToInt(NodoDistribucion::calcularCantidadTotal)
                .sum();
        }
    }
    
    public ArbolDistribucion() {
        this.nodos = new ArrayList<>();
        this.cantidadAsignada = 0;
        this.cantidadDisponible = 0;
    }
    
    public ArbolDistribucion(String id, Ruta ruta, Recurso recurso) {
        this();
        this.id = id;
        this.ruta = ruta;
        this.recurso = recurso;
        this.cantidadDisponible = recurso.getCantidadDisponible();
    }
    
    /**
     * Crea el nodo raíz del árbol
     */
    public void crearNodoRaiz(Recurso recurso, int cantidad) {
        this.nodoRaiz = new NodoDistribucion("raiz", recurso, cantidad);
        this.nodos.add(nodoRaiz);
    }
    
    /**
     * Agrega un nodo hijo al árbol
     */
    public void agregarNodo(String id, Recurso recurso, int cantidad, String idPadre) {
        NodoDistribucion nodo = new NodoDistribucion(id, recurso, cantidad);
        nodos.add(nodo);
        
        if (nodoRaiz == null) {
            nodoRaiz = nodo;
        } else {
            NodoDistribucion padre = buscarNodo(idPadre);
            if (padre != null) {
                padre.agregarHijo(nodo);
            }
        }
    }
    
    /**
     * Busca un nodo por ID
     */
    public NodoDistribucion buscarNodo(String id) {
        return nodos.stream()
            .filter(nodo -> nodo.getId().equals(id))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Calcula la cantidad total de recursos en el árbol
     */
    public int calcularCantidadTotal() {
        if (nodoRaiz == null) return 0;
        return nodoRaiz.calcularCantidadTotal();
    }
    
    /**
     * Verifica si hay recursos suficientes para la distribución
     */
    public boolean tieneRecursosSuficientes(int cantidadRequerida) {
        return calcularCantidadTotal() >= cantidadRequerida;
    }
    
    /**
     * Distribuye recursos según la prioridad de los nodos
     */
    public List<NodoDistribucion> distribuirRecursos(int cantidadTotal) {
        List<NodoDistribucion> distribucion = new ArrayList<>();
        
        if (!tieneRecursosSuficientes(cantidadTotal)) {
            return distribucion;
        }
        
        List<NodoDistribucion> nodosOrdenados = new ArrayList<>(nodos);
        nodosOrdenados.sort((n1, n2) -> Integer.compare(n2.getPrioridad(), n1.getPrioridad()));
        
        int cantidadRestante = cantidadTotal;
        
        for (NodoDistribucion nodo : nodosOrdenados) {
            if (cantidadRestante <= 0) break;
            
            int cantidadAsignar = Math.min(nodo.getCantidad(), cantidadRestante);
            if (cantidadAsignar > 0) {
                nodo.setCantidad(cantidadAsignar);
                distribucion.add(nodo);
                cantidadRestante -= cantidadAsignar;
            }
        }
        
        return distribucion;
    }
    
    /**
     * Obtiene los nodos hoja del árbol
     */
    public List<NodoDistribucion> obtenerNodosHoja() {
        return nodos.stream()
            .filter(NodoDistribucion::esHoja)
            .toList();
    }
    
    /**
     * Calcula la eficiencia de la distribución
     */
    public double calcularEficienciaDistribucion() {
        if (nodos.isEmpty()) return 0.0;
        
        int cantidadTotal = calcularCantidadTotal();
        if (cantidadTotal == 0) return 0.0;
        
        int cantidadAsignada = nodos.stream()
            .mapToInt(NodoDistribucion::getCantidad)
            .sum();
        
        return (double) cantidadAsignada / cantidadTotal;
    }
    
    /**
     * Balancea el árbol redistribuyendo recursos
     */
    public void balancearArbol() {
        if (nodoRaiz == null) return;
        
        int cantidadTotal = calcularCantidadTotal();
        int cantidadPorNodo = cantidadTotal / nodos.size();
        
        for (NodoDistribucion nodo : nodos) {
            nodo.setCantidad(cantidadPorNodo);
        }
    }
    
    /**
     * Obtiene estadísticas del árbol
     */
    public String generarEstadisticas() {
        StringBuilder stats = new StringBuilder();
        stats.append("=== ESTADÍSTICAS DEL ÁRBOL DE DISTRIBUCIÓN ===\n");
        stats.append("ID: ").append(id).append("\n");
        stats.append("Ruta: ").append(ruta != null ? ruta.getId() : "No asignada").append("\n");
        stats.append("Recurso: ").append(recurso != null ? recurso.getNombre() : "No asignado").append("\n");
        stats.append("Total de nodos: ").append(nodos.size()).append("\n");
        stats.append("Cantidad total: ").append(calcularCantidadTotal()).append("\n");
        stats.append("Eficiencia: ").append(String.format("%.1f%%", calcularEficienciaDistribucion() * 100)).append("\n");
        stats.append("Nodos hoja: ").append(obtenerNodosHoja().size()).append("\n");
        
        return stats.toString();
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public Ruta getRuta() {
        return ruta;
    }
    
    public void setRuta(Ruta ruta) {
        this.ruta = ruta;
    }
    
    public Recurso getRecurso() {
        return recurso;
    }
    
    public void setRecurso(Recurso recurso) {
        this.recurso = recurso;
    }
    
    public int getCantidadAsignada() {
        return cantidadAsignada;
    }
    
    public void setCantidadAsignada(int cantidadAsignada) {
        this.cantidadAsignada = cantidadAsignada;
    }
    
    public int getCantidadDisponible() {
        return cantidadDisponible;
    }
    
    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }
    
    public NodoDistribucion getNodoRaiz() {
        return nodoRaiz;
    }
    
    public void setNodoRaiz(NodoDistribucion nodoRaiz) {
        this.nodoRaiz = nodoRaiz;
    }
    
    public List<NodoDistribucion> getNodos() {
        return new ArrayList<>(nodos);
    }
    
    public void setNodos(List<NodoDistribucion> nodos) {
        this.nodos = new ArrayList<>(nodos);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArbolDistribucion that = (ArbolDistribucion) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return String.format("ArbolDistribucion{id='%s', ruta=%s, recurso=%s, nodos=%d}", id, ruta != null ? ruta.getId() : "null", recurso != null ? recurso.getNombre() : "null", nodos.size());
    }
}

