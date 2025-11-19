package co.edu.uniquindio.GestionRiesgos.Estructuras;

import co.edu.uniquindio.GestionRiesgos.Model.Recurso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Representa un árbol de distribución utilizado para organizar la asignación
 * de recursos a rutas específicas dentro del sistema de gestión.
 *
 * El árbol permite:
 * - Dividir recursos en nodos jerárquicos.
 * - Calcular cantidades distribuidas.
 * - Evaluar eficiencia.
 * - Balancear recursos.
 * - Priorizar nodos para asignación.
 *
 * Cada árbol está asociado a:
 * - Un recurso principal.
 * - Una ruta.
 * - Un nodo raíz.
 */
public class ArbolDistribucion {

    /** Identificador único del árbol. */
    private String id;

    /** Ruta a la cual se destina la distribución. */
    private Ruta ruta;

    /** Recurso principal asociado al árbol. */
    private Recurso recurso;

    /** Cantidad total asignada en el proceso de distribución. */
    private int cantidadAsignada;

    /** Cantidad disponible inicial basada en el recurso. */
    private int cantidadDisponible;

    /** Nodo raíz del árbol. */
    private NodoDistribucion nodoRaiz;

    /** Lista de todos los nodos del árbol. */
    private List<NodoDistribucion> nodos;
    
    /**
     * Clase interna que representa un nodo dentro del árbol de distribución.
     * Cada nodo puede tener hijos y un padre, formando una estructura jerárquica.
     */
    public static class NodoDistribucion {

        /** Identificador del nodo. */
        private String id;

        /** Recurso asignado al nodo. */
        private Recurso recurso;

        /** Cantidad asignada al nodo. */
        private int cantidad;

        /** Nodo padre dentro del árbol. */
        private NodoDistribucion padre;

        /** Lista de nodos hijos. */
        private List<NodoDistribucion> hijos;

        /** Prioridad del nodo para asignación. */
        private int prioridad;
        
        /**
         * Crea un nuevo nodo de distribución.
         *
         * @param id identificador del nodo
         * @param recurso recurso asignado
         * @param cantidad cantidad inicial del nodo
         */
        public NodoDistribucion(String id, Recurso recurso, int cantidad) {
            this.id = id;
            this.recurso = recurso;
            this.cantidad = cantidad;
            this.hijos = new ArrayList<>();
            this.prioridad = 1;
        }

        // ----- Getters & Setters -----

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
        
        /**
         * Agrega un nodo hijo a este nodo.
         *
         * @param hijo nodo que será agregado como hijo
         */
        public void agregarHijo(NodoDistribucion hijo) {
            if (hijo != null) {
                hijo.setPadre(this);
                this.hijos.add(hijo);
            }
        }
        
        /**
         * Determina si el nodo no tiene hijos.
         *
         * @return true si es un nodo hoja
         */
        public boolean esHoja() {
            return hijos.isEmpty();
        }
        
        /**
         * Calcula recursivamente la cantidad total acumulada en este nodo y sus descendientes.
         *
         * @return cantidad total sumada
         */
        public int calcularCantidadTotal() {
            return cantidad + hijos.stream()
                    .mapToInt(NodoDistribucion::calcularCantidadTotal)
                    .sum();
        }
    }

    // ----- Constructores -----

    /** Constructor por defecto. */
    public ArbolDistribucion() {
        this.nodos = new ArrayList<>();
        this.cantidadAsignada = 0;
        this.cantidadDisponible = 0;
    }
    
    /**
     * Crea un árbol de distribución asociado a una ruta y un recurso específico.
     *
     * @param id identificador del árbol
     * @param ruta ruta asociada
     * @param recurso recurso a distribuir
     */
    public ArbolDistribucion(String id, Ruta ruta, Recurso recurso) {
        this();
        this.id = id;
        this.ruta = ruta;
        this.recurso = recurso;
        this.cantidadDisponible = recurso.getCantidadDisponible();
    }

    // ----- Funcionalidad del árbol -----

    /**
     * Crea el nodo raíz del árbol.
     *
     * @param recurso recurso asignado al nodo raíz
     * @param cantidad cantidad inicial
     */
    public void crearNodoRaiz(Recurso recurso, int cantidad) {
        this.nodoRaiz = new NodoDistribucion("raiz", recurso, cantidad);
        this.nodos.add(nodoRaiz);
    }
    
    /**
     * Agrega un nuevo nodo al árbol, asignándolo como hijo de un nodo padre.
     *
     * @param id identificador del nuevo nodo
     * @param recurso recurso asignado
     * @param cantidad cantidad inicial
     * @param idPadre id del nodo padre
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
     * Busca un nodo en el árbol por su ID.
     *
     * @param id identificador del nodo a buscar
     * @return el nodo encontrado o null si no existe
     */
    public NodoDistribucion buscarNodo(String id) {
        return nodos.stream()
                .filter(n -> n.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Calcula la cantidad total de recursos representada en todos los nodos del árbol.
     *
     * @return cantidad total acumulada
     */
    public int calcularCantidadTotal() {
        if (nodoRaiz == null) return 0;
        return nodoRaiz.calcularCantidadTotal();
    }
    
    /**
     * Verifica si hay suficientes recursos para distribuir.
     *
     * @param cantidadRequerida cantidad solicitada
     * @return true si la cantidad total disponible es suficiente
     */
    public boolean tieneRecursosSuficientes(int cantidadRequerida) {
        return calcularCantidadTotal() >= cantidadRequerida;
    }
    
    /**
     * Distribuye recursos a los nodos según su prioridad.
     *
     * @param cantidadTotal cantidad total a distribuir
     * @return lista de nodos que recibieron asignación
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
     * Obtiene los nodos hoja del árbol.
     *
     * @return lista de nodos sin hijos
     */
    public List<NodoDistribucion> obtenerNodosHoja() {
        return nodos.stream()
                .filter(NodoDistribucion::esHoja)
                .toList();
    }
    
    /**
     * Calcula qué tan eficiente ha sido la distribución de recursos.
     * 
     * @return porcentaje de eficiencia entre 0 y 1
     */
    public double calcularEficienciaDistribucion() {
        if (nodos.isEmpty()) return 0.0;
        
        int total = calcularCantidadTotal();
        if (total == 0) return 0.0;
        
        int asignada = nodos.stream()
                .mapToInt(NodoDistribucion::getCantidad)
                .sum();
        
        return (double) asignada / total;
    }
    
    /**
     * Balancea el árbol distribuyendo la misma cantidad de recurso a cada nodo.
     */
    public void balancearArbol() {
        if (nodoRaiz == null) return;
        
        int total = calcularCantidadTotal();
        int cantidadPorNodo = total / nodos.size();
        
        for (NodoDistribucion nodo : nodos) {
            nodo.setCantidad(cantidadPorNodo);
        }
    }
    
    /**
     * Genera un reporte con estadísticas generales del árbol.
     *
     * @return cadena con estadísticas detalladas
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

    // ----- Getters, setters, equals, hashCode, toString -----

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Ruta getRuta() { return ruta; }
    public void setRuta(Ruta ruta) { this.ruta = ruta; }

    public Recurso getRecurso() { return recurso; }
    public void setRecurso(Recurso recurso) { this.recurso = recurso; }

    public int getCantidadAsignada() { return cantidadAsignada; }
    public void setCantidadAsignada(int cantidadAsignada) { this.cantidadAsignada = cantidadAsignada; }

    public int getCantidadDisponible() { return cantidadDisponible; }
    public void setCantidadDisponible(int cantidadDisponible) { this.cantidadDisponible = cantidadDisponible; }

    public NodoDistribucion getNodoRaiz() { return nodoRaiz; }
    public void setNodoRaiz(NodoDistribucion nodoRaiz) { this.nodoRaiz = nodoRaiz; }

    public List<NodoDistribucion> getNodos() { return new ArrayList<>(nodos); }
    public void setNodos(List<NodoDistribucion> nodos) { this.nodos = new ArrayList<>(nodos); }
    
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
        return String.format(
                "ArbolDistribucion{id='%s', ruta=%s, recurso=%s, nodos=%d}",
                id,
                ruta != null ? ruta.getId() : "null",
                recurso != null ? recurso.getNombre() : "null",
                nodos.size()
        );
    }
}


