package com.docencia.algoritmos;
/**
 * Interfaz con metodos de algortimos a implementar
 * @author AlejandroDonGar
 */
import java.util.List;
public interface BusquedaService {
    /**
     * Busca un numero concreto dentro de una lista
     * @param numeros lista a analizar
     * @param valor valor a encontrar en la lista
     * @return devuelve el valor encontrado si lo encuentra
     */
    Integer buscarIndiceElemento(List<Integer> numeros, Integer valor);
    /**
     * Busca si existe una palabra en una lista ignorando mayusculas o minusculas
     * @param palabras lista a anailzar
     * @param palabra palabra a encontrar dentro de la lista
     * @return devuelve true o false
     */
    Boolean existePalabra(List<String> palabras, String palabra);
    /**
     * Busca el valor maximo dentro de una lista
     * @param numeros lista a analizar
     * @return devuelve el maximo de la lista
     */
    Integer encontrarMaximo(List<Integer> numeros);
    /**
     * Busca el valor minimo dentro de una lista
     * @param numeros lista a analizar
     * @return devuelve el minimo de la lista
     */
    Integer encontrarMinimo(List<Integer> numeros);
    /**
     * Cuenta el numero de apariciones de un valor dentro de una lista
     * @param numeros lista a analizar
     * @param valor valor a encontrar y contar
     * @return devuelve el valor de cuantas veces aparecio el valor dentro de la lista
     */
    Integer contarApariciones(List<Integer> numeros, Integer valor);
}
