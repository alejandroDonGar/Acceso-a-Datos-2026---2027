package com.docencia.algoritmos;
/**
 * Clase que implementa los metodos de algoritmos de BusquedaServiceImpl
 * @author AlejandroDonGar
 */
import java.util.List;
public interface OrdenacionService {
    /**
     * Ordena los numeros de menor a mayor
     * @param numeros Lista de numeros a ordenar
     * @return Devuelve una lista de numero ordenada
     */
    List<Integer> ordenarBurbujaAscendente(List<Integer> numeros);
    /**
     * Ordena los numeros de mayor a menor
     * @param numeros Lista de numeros a ordenar
     * @return Devuelve una lista de numeros ordenado
     */
    List<Integer> ordenarBurbujaDescendente(List<Integer> numeros);
    /**
     * Ordena las palabras de manera alfabética
     * @param palabras Lista de palabras a ordenar
     * @return Devuelve una lista de palabras ordenadas
     */
    List<String> ordenarPalabrasAlfabeticamente(List<String> palabras);
    /**
     * Invierte una lista
     * @param numeros Lista de numeros a invertir
     * @return Devuelve una lista de numero invertida
     */
    List<Integer> invertirLista(List<Integer> numeros);
    /**
     * Comprueba si una lista esta ordenada de menor a mayor correctamente
     * @param numeros Lista de numero a comprobar
     * @return Devuelve true o false
     */
    Boolean estaOrdenadaAscendente(List<Integer> numeros);
}
