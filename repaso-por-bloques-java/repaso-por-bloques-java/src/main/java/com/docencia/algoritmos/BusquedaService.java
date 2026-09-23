package com.docencia.algoritmos;

import java.util.List;

/**
 * Interfaz con metodos de algoritmos de busqueda a implementar
 * @author AlejandroDonGar
 */
public interface BusquedaService {
    /**
     * Busca un numero concreto dentro de una lista recorriendola de forma lineal
     * @param numeros lista a analizar
     * @param valor valor a encontrar en la lista
     * @return devuelve el indice (posicion) de la primera aparicion del valor, o -1 si no esta en la lista
     * @throws IllegalArgumentException si la lista o el valor son null
     */
    Integer buscarIndiceElemento(List<Integer> numeros, Integer valor);

    /**
     * Busca si existe una palabra en una lista ignorando mayusculas o minusculas
     * Si los datos son invalidos (null o lista vacia) no se lanza excepcion y se devuelve false
     * @param palabras lista a analizar
     * @param palabra palabra a encontrar dentro de la lista
     * @return devuelve true si la palabra esta en la lista, false en caso contrario
     */
    Boolean existePalabra(List<String> palabras, String palabra);

    /**
     * Busca el valor maximo dentro de una lista comparando de forma secuencial
     * @param numeros lista a analizar
     * @return devuelve el maximo de la lista
     * @throws IllegalArgumentException si la lista es null o esta vacia
     */
    Integer encontrarMaximo(List<Integer> numeros);

    /**
     * Busca el valor minimo dentro de una lista comparando de forma secuencial
     * @param numeros lista a analizar
     * @return devuelve el minimo de la lista
     * @throws IllegalArgumentException si la lista es null o esta vacia
     */
    Integer encontrarMinimo(List<Integer> numeros);

    /**
     * Cuenta el numero de apariciones de un valor dentro de una lista
     * Si los datos son invalidos (lista null o vacia, o valor null) no se lanza excepcion y se devuelve null
     * @param numeros lista a analizar
     * @param valor valor a encontrar y contar
     * @return devuelve cuantas veces aparece el valor dentro de la lista
     */
    Integer contarApariciones(List<Integer> numeros, Integer valor);
}
