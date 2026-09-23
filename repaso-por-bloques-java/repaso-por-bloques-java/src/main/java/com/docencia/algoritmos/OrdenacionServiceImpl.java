package com.docencia.algoritmos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase que implementa los metodos de algoritmos de OrdenacionService
 * @author AlejandroDonGar
 */
public class OrdenacionServiceImpl implements OrdenacionService {

    @Override
    public List<Integer> ordenarBurbujaAscendente(List<Integer> numeros) {
        // Sin lista no se puede ordenar nada.
        if (numeros == null) {
            throw new IllegalArgumentException();
        }
        // Hacemos una COPIA: asi no modificamos la lista original (y ademas List.of(...) es inmutable y daria error al hacer set).
        List<Integer> ordenada = new ArrayList<>(numeros);
        // BURBUJA: cada vuelta del bucle de fuera "hace subir" el numero mayor que quede sin colocar hasta el final.
        // Con size-1 vueltas basta: cuando quedan todos menos uno colocados, el ultimo ya esta en su sitio.
        for (int i = 0; i < ordenada.size() - 1; i++) {
            // Bucle de dentro: compara parejas de vecinos (j y j+1).
            // Restamos i porque los i ultimos elementos ya estan colocados y no hace falta volver a mirarlos.
            for (int j = 0; j < ordenada.size() - 1 - i; j++) {
                // Si el de la izquierda es mayor que el de la derecha estan al reves para un orden ascendente.
                if (ordenada.get(j) > ordenada.get(j + 1)) {
                    // INTERCAMBIO (swap): necesitamos una variable auxiliar para no perder el valor al sobrescribir.
                    Integer auxiliar = ordenada.get(j);
                    // La posicion j pasa a tener el valor pequeño...
                    ordenada.set(j, ordenada.get(j + 1));
                    // ...y la posicion j+1 recibe el valor grande que guardamos en el auxiliar.
                    ordenada.set(j + 1, auxiliar);
                }
            }
        }
        // La lista ya esta ordenada de menor a mayor.
        return ordenada;
    }

    @Override
    public List<Integer> ordenarBurbujaDescendente(List<Integer> numeros) {
        // Sin lista no se puede ordenar.
        if (numeros == null) {
            throw new IllegalArgumentException();
        }
        // Copia para respetar la lista original.
        List<Integer> ordenada = new ArrayList<>(numeros);
        // Mismo algoritmo que el ascendente: en cada vuelta se coloca un elemento mas al final.
        for (int i = 0; i < ordenada.size() - 1; i++) {
            // Comparamos parejas de vecinos, ignorando los i del final que ya estan colocados.
            for (int j = 0; j < ordenada.size() - 1 - i; j++) {
                // UNICA DIFERENCIA con el ascendente: el signo. Ahora intercambiamos si el de la izquierda es MENOR,
                // asi los grandes se van quedando a la izquierda y los pequeños al final.
                if (ordenada.get(j) < ordenada.get(j + 1)) {
                    // Intercambio con variable auxiliar.
                    Integer auxiliar = ordenada.get(j);
                    ordenada.set(j, ordenada.get(j + 1));
                    ordenada.set(j + 1, auxiliar);
                }
            }
        }
        // Lista ordenada de mayor a menor.
        return ordenada;
    }

    @Override
    public List<String> ordenarPalabrasAlfabeticamente(List<String> palabras) {
        // Sin lista no hay nada que ordenar.
        if (palabras == null) {
            throw new IllegalArgumentException();
        }
        // Copia para no tocar la lista original.
        List<String> ordenada = new ArrayList<>(palabras);
        // Collections.sort ordena la lista. CASE_INSENSITIVE_ORDER hace que "ana" y "Luis" se comparen sin importar
        // mayusculas; si no, todas las mayusculas irian antes que las minusculas ("Luis" antes que "ana").
        Collections.sort(ordenada, String.CASE_INSENSITIVE_ORDER);
        // Devolvemos la lista ya ordenada.
        return ordenada;
    }

    @Override
    public List<Integer> invertirLista(List<Integer> numeros) {
        // Sin lista no se puede invertir.
        if (numeros == null) {
            throw new IllegalArgumentException();
        }
        // Lista nueva y vacia donde iremos metiendo los numeros al reves.
        List<Integer> invertida = new ArrayList<>();
        // Recorremos la original DESDE EL FINAL hasta el principio: empezamos en la ultima posicion (size - 1)...
        for (int i = numeros.size() - 1; i >= 0; i--) {
            // ...y vamos añadiendo cada elemento a la nueva lista, asi el ultimo pasa a ser el primero.
            invertida.add(numeros.get(i));
        }
        // Devolvemos la lista invertida.
        return invertida;
    }

    @Override
    public Boolean estaOrdenadaAscendente(List<Integer> numeros) {
        // Sin lista no podemos comprobar nada.
        if (numeros == null) {
            throw new IllegalArgumentException();
        }
        // Comparamos cada elemento con el siguiente; por eso paramos en size - 1 (el ultimo no tiene "siguiente").
        for (int i = 0; i < numeros.size() - 1; i++) {
            // Si alguno es mayor que el que tiene detras, hay un desorden y ya no puede estar ordenada.
            if (numeros.get(i) > numeros.get(i + 1)) {
                // Con UN solo fallo basta para responder false, no hace falta seguir.
                return false;
            }
        }
        // Si hemos llegado aqui, ninguna pareja estaba al reves (una lista vacia o de 1 elemento tambien cuenta como ordenada).
        return true;
    }
}