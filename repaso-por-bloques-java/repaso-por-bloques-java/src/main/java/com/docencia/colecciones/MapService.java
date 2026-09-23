package com.docencia.colecciones;

import java.util.List;
import java.util.Map;

/**
 * Servicio para trabajar con mapas.
 * @author AlejandroDonGar
 */
public interface MapService {
    /**
     * Cuenta cuantas veces se repite cada palabra de una lista
     * @param palabras lista de palabras a contar
     * @return devuelve un mapa con cada palabra como clave y sus repeticiones como valor
     * @throws IllegalArgumentException si la lista es null
     */
    Map<String, Integer> contarFrecuenciaPalabras(List<String> palabras);

    /**
     * Obtiene el valor asociado a una clave de un mapa
     * @param mapa mapa donde buscar
     * @param clave clave a buscar
     * @return devuelve el valor de la clave, o 0 si la clave no existe
     * @throws IllegalArgumentException si el mapa es null
     */
    Integer obtenerValorPorClave(Map<String, Integer> mapa, String clave);

    /**
     * Calcula la media aritmetica de la lista de numeros de cada categoria
     * @param datos mapa con la categoria como clave y sus numeros como valor
     * @return devuelve un mapa con cada categoria y su media
     * @throws IllegalArgumentException si el mapa es null
     */
    Map<String, Double> calcularMediaPorCategoria(Map<String, List<Integer>> datos);

    /**
     * Busca la clave que tiene el valor mas alto de todo el mapa
     * @param mapa mapa a analizar
     * @return devuelve la clave con el mayor valor
     * @throws IllegalArgumentException si el mapa es null o esta vacio
     */
    String obtenerClaveConMayorValor(Map<String, Integer> mapa);

    /**
     * Filtra un mapa quedandose solo con las entradas cuyo valor alcanza un minimo
     * @param mapa mapa a filtrar
     * @param minimo valor minimo que debe tener una entrada para conservarse
     * @return devuelve un mapa nuevo solo con las entradas que cumplen el minimo
     * @throws IllegalArgumentException si el mapa o el minimo son null
     */
    Map<String, Integer> filtrarPorValorMinimo(Map<String, Integer> mapa, Integer minimo);
}
