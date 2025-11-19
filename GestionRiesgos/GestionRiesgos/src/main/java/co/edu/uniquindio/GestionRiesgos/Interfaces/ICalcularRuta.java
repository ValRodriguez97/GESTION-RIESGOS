package co.edu.uniquindio.GestionRiesgos.Interfaces;

import co.edu.uniquindio.GestionRiesgos.Interfaces.ICalcularRuta;
import co.edu.uniquindio.GestionRiesgos.Model.Zona;
import co.edu.uniquindio.GestionRiesgos.Estructuras.Ruta;

import java.util.List;

/**
 * Interfaz para calcular rutas en el sistema de gestión de desastres
 */
public interface ICalcularRuta {

    /**
     * Calcula la ruta más corta entre dos zonas
     *
     * @param origen Zona de origen
     * @param destino Zona de destino
     * @return Lista de zonas que forman la ruta más corta
     */
    List<Zona> calcularRutaMasCorta(Zona origen, Zona destino);

    /**
     * Calcula todas las rutas posibles entre dos zonas
     *
     * @param origen Zona de origen
     * @param destino Zona de destino
     * @return Lista de todas las rutas posibles
     */
    List<Ruta> calcularTodasLasRutas(Zona origen, Zona destino);

    /**
     * Calcula la ruta más rápida considerando el tiempo
     *
     * @param origen Zona de origen
     * @param destino Zona de destino
     * @return Ruta más rápida
     */
    Ruta calcularRutaMasRapida(Zona origen, Zona destino);

    /**
     * Calcula la ruta más segura considerando el nivel de riesgo
     *
     * @param origen Zona de origen
     * @param destino Zona de destino
     * @return Ruta más segura
     */
    Ruta calcularRutaMasSegura(Zona origen, Zona destino);

    /**
     * Verifica si existe una ruta entre dos zonas
     *
     * @param origen Zona de origen
     * @param destino Zona de destino
     * @return true si existe ruta, false en caso contrario
     */
    boolean existeRuta(Zona origen, Zona destino);
}
