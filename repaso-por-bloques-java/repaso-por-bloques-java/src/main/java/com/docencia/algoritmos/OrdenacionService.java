package com.docencia.algoritmos;

import java.util.List;

/**
 * Interfaz con los metodos de algoritmos de ordenacion a implementar
 * @author AlejandroDonGar
 */
public interface OrdenacionService {
    /**
     * Ordena los numeros de menor a mayor con el algoritmo de la burbuja
     * @param numeros Lista de numeros a ordenar
     * @return Devuelve una lista nueva de numeros ordenada de menor a mayor
     * @throws IllegalArgumentException si la lista es null
     */
    List<Integer> ordenarBurbujaAscendente(List<Integer> numeros);

    /**
     * Ordena los numeros de mayor a menor con el algoritmo de la burbuja
     * @param numeros Lista de numeros a ordenar
     * @return Devuelve una lista nueva de numeros ordenada de mayor a menor
     * @throws IllegalArgumentException si la lista es null
     */
    List<Integer> ordenarBurbujaDescendente(List<Integer> numeros);

    /**
     * Ordena las palabras de manera alfabetica ignorando mayusculas y minusculas
     * @param palabras Lista de palabras a ordenar
     * @return Devuelve una lista nueva de palabras ordenadas alfabeticamente
     * @throws IllegalArgumentException si la lista es null
     */
    List<String> ordenarPalabrasAlfabeticamente(List<String> palabras);

    /**
     * Invierte el orden de los elementos de una lista
     * @param numeros Lista de numeros a invertir
     * @return Devuelve una lista nueva con los numeros en orden inverso
     * @throws IllegalArgumentException si la lista es null
     */
    List<Integer> invertirLista(List<Integer> numeros);

    /**
     * Comprueba si una lista esta ordenada de menor a mayor correctamente
     * @param numeros Lista de numeros a comprobar
     * @return Devuelve true si esta ordenada de menor a mayor, false si no lo esta
     * @throws IllegalArgumentException si la lista es null
     */
    Boolean estaOrdenadaAscendente(List<Integer> numeros);
}
