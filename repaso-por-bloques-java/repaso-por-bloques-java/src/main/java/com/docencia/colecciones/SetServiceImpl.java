package com.docencia.colecciones;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Clase que implementa los metodos de la interfaz SetService
 * @author AlejandroDonGar
 */
public class SetServiceImpl implements SetService {

    @Override
    public Set<String> obtenerElementosUnicos(List<String> elementos) {
        // Sin lista no se puede convertir.
        if (elementos == null) {
            throw new IllegalArgumentException();
        }
        // Un Set no permite elementos repetidos, asi que al construirlo a partir de la lista los duplicados desaparecen.
        // Usamos LinkedHashSet para conservar el orden en que aparecieron.
        return new LinkedHashSet<>(elementos);
    }

    @Override
    public Boolean contieneElemento(Set<String> elementos, String valor) {
        // Validamos ambos: un Set inmutable (Set.of) incluso lanza error si le preguntas por null.
        if (elementos == null || valor == null) {
            throw new IllegalArgumentException();
        }
        // contains responde true/false segun si el valor esta dentro del conjunto.
        return elementos.contains(valor);
    }

    @Override
    public Set<String> unirConjuntos(Set<String> primero, Set<String> segundo) {
        // Necesitamos los dos conjuntos.
        if (primero == null || segundo == null) {
            throw new IllegalArgumentException();
        }
        // Empezamos con una COPIA del primero para no modificar el original (y porque Set.of es inmutable).
        Set<String> union = new LinkedHashSet<>(primero);
        // addAll añade todos los del segundo; los que ya estaban no se repiten porque es un Set.
        union.addAll(segundo);
        // Devolvemos la union.
        return union;
    }

    @Override
    public Set<String> intersectarConjuntos(Set<String> primero, Set<String> segundo) {
        // Necesitamos los dos conjuntos.
        if (primero == null || segundo == null) {
            throw new IllegalArgumentException();
        }
        // Copia del primero para no tocar el original.
        Set<String> interseccion = new LinkedHashSet<>(primero);
        // retainAll = "retener": conserva SOLO los elementos que tambien estan en el segundo y elimina el resto.
        interseccion.retainAll(segundo);
        // Devolvemos los elementos comunes.
        return interseccion;
    }

    @Override
    public Set<String> restarConjuntos(Set<String> primero, Set<String> segundo) {
        // Necesitamos los dos conjuntos.
        if (primero == null || segundo == null) {
            throw new IllegalArgumentException();
        }
        // Copia del primero para no tocar el original.
        Set<String> diferencia = new LinkedHashSet<>(primero);
        // removeAll elimina de la copia todo lo que aparezca en el segundo: nos quedamos con lo que solo tiene el primero.
        diferencia.removeAll(segundo);
        // Devolvemos la diferencia.
        return diferencia;
    }
}
