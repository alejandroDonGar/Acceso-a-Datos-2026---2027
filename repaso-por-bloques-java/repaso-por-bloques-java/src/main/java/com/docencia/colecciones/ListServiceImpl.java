package com.docencia.colecciones;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Clase que implementa los metodos de la interfaz ListService
 * @author AlejandroDonGar
 */
public class ListServiceImpl implements ListService {

    @Override
    public List<String> filtrarPalabrasPorLongitud(List<String> palabras, Integer longitudMinima) {
        // Validamos: sin lista, sin longitud, o con longitud negativa (imposible para una palabra) no se puede filtrar.
        if (palabras == null || longitudMinima == null || longitudMinima < 0) {
            throw new IllegalArgumentException();
        }
        // Lista nueva donde guardaremos solo las palabras que pasen el filtro.
        List<String> filtradas = new ArrayList<>();
        // Recorremos todas las palabras de la lista original.
        for (String palabra : palabras) {
            // Ignoramos las null (no tienen longitud) y nos quedamos con las que miden al menos la longitud minima.
            if (palabra != null && palabra.length() >= longitudMinima) {
                // Cumple: la copiamos a la lista resultado (se mantiene el orden original).
                filtradas.add(palabra);
            }
        }
        // Si no paso ninguna (o la lista era vacia) devolvemos una lista vacia, no null.
        return filtradas;
    }

    @Override
    public List<Integer> ordenarNumerosAscendente(List<Integer> numeros) {
        // Sin lista no hay nada que ordenar.
        if (numeros == null) {
            throw new IllegalArgumentException();
        }
        // Copia de la lista: List.of(...) es inmutable y ademas no queremos alterar la original.
        List<Integer> ordenada = new ArrayList<>(numeros);
        // Collections.sort ordena de menor a mayor por defecto (la README permite burbuja o sort).
        Collections.sort(ordenada);
        // Devolvemos la copia ya ordenada.
        return ordenada;
    }

    @Override
    public Integer sumarElementosLista(List<Integer> numeros) {
        // Sin lista no se puede sumar.
        if (numeros == null) {
            throw new IllegalArgumentException();
        }
        // Acumulador que empieza en 0 (el neutro de la suma).
        int suma = 0;
        // Recorremos todos los numeros.
        for (Integer numero : numeros) {
            // Vamos sumando cada uno al total.
            suma = suma + numero;
        }
        // Una lista vacia suma 0.
        return suma;
    }

    @Override
    public Double calcularMediaLista(List<Integer> numeros) {
        // Una lista vacia no tiene media: dividiriamos entre 0.
        if (numeros == null || numeros.isEmpty()) {
            throw new IllegalArgumentException();
        }
        // Primero la suma de todos los elementos.
        int suma = 0;
        for (Integer numero : numeros) {
            suma = suma + numero;
        }
        // Media = suma / cantidad. El (double) es IMPORTANTE: si dividimos int entre int Java descarta los decimales
        // (7 / 2 daria 3 en vez de 3.5). Convirtiendo la suma a double la division ya conserva los decimales.
        return (double) suma / numeros.size();
    }

    @Override
    public List<Integer> eliminarNumerosDuplicados(List<Integer> numeros) {
        // Sin lista no se puede trabajar.
        if (numeros == null) {
            throw new IllegalArgumentException();
        }
        // Un Set no admite repetidos; LinkedHashSet ademas RECUERDA EL ORDEN de insercion (un HashSet normal lo mezclaria).
        // Al pasarle la lista, los duplicados se descartan solos.
        LinkedHashSet<Integer> sinRepetidos = new LinkedHashSet<>(numeros);
        // Convertimos el conjunto de nuevo en lista, que es lo que pide el metodo.
        return new ArrayList<>(sinRepetidos);
    }
}
