package com.docencia.colecciones;

import java.util.List;
import java.util.Set;

/**
 * Servicio para trabajar con conjuntos.
 * @author AlejandroDonGar
 */
public interface SetService {
    /**
     * Elimina los elementos repetidos de una lista convirtiendola en conjunto
     * @param elementos lista con posibles duplicados
     * @return devuelve un conjunto sin elementos repetidos
     * @throws IllegalArgumentException si la lista es null
     */
    Set<String> obtenerElementosUnicos(List<String> elementos);

    /**
     * Comprueba si un valor esta dentro de un conjunto
     * @param elementos conjunto donde buscar
     * @param valor valor a buscar
     * @return devuelve true si el valor esta en el conjunto, false si no
     * @throws IllegalArgumentException si el conjunto o el valor son null
     */
    Boolean contieneElemento(Set<String> elementos, String valor);

    /**
     * Une dos conjuntos en uno solo con todos los elementos de ambos
     * @param primero primer conjunto
     * @param segundo segundo conjunto
     * @return devuelve un conjunto nuevo con la union de los dos
     * @throws IllegalArgumentException si alguno de los conjuntos es null
     */
    Set<String> unirConjuntos(Set<String> primero, Set<String> segundo);

    /**
     * Obtiene los elementos que estan a la vez en los dos conjuntos
     * @param primero primer conjunto
     * @param segundo segundo conjunto
     * @return devuelve un conjunto nuevo con los elementos comunes
     * @throws IllegalArgumentException si alguno de los conjuntos es null
     */
    Set<String> intersectarConjuntos(Set<String> primero, Set<String> segundo);

    /**
     * Resta dos conjuntos: los elementos del primero que no estan en el segundo
     * @param primero conjunto del que se parte
     * @param segundo conjunto con los elementos a quitar
     * @return devuelve un conjunto nuevo con la diferencia
     * @throws IllegalArgumentException si alguno de los conjuntos es null
     */
    Set<String> restarConjuntos(Set<String> primero, Set<String> segundo);
}
