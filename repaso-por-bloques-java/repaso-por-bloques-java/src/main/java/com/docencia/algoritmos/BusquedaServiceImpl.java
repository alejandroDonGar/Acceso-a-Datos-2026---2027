package com.docencia.algoritmos;

import java.util.List;

/**
 * Clase que implementa los metodos de algoritmos de BusquedaService
 * @author AlejandroDonGar
 */
public class BusquedaServiceImpl implements BusquedaService {

    @Override
    public Integer buscarIndiceElemento(List<Integer> numeros, Integer valor) {
        // Si nos dan la lista o el valor a null no hay nada que buscar: es un dato invalido.
        if (numeros == null || valor == null) {
            throw new IllegalArgumentException();
        }
        // BUSQUEDA LINEAL: miramos posicion por posicion, de la 0 hasta la ultima.
        // Usamos un for con indice (i) y no un for-each porque necesitamos devolver la POSICION, no el valor.
        for (int i = 0; i < numeros.size(); i++) {
            // Usamos equals y no == porque son objetos Integer: == compara referencias, equals compara el valor.
            if (numeros.get(i).equals(valor)) {
                // Al encontrarlo devolvemos ya la posicion (i) y salimos: nos vale la primera aparicion.
                return i;
            }
        }
        // Si el bucle termina sin haber hecho return es que el valor no esta: por convencion se devuelve -1.
        return -1;
    }

    @Override
    public Boolean existePalabra(List<String> palabras, String palabra) {
        // Con datos invalidos no hay forma de que exista la palabra, asi que devolvemos false.
        if (palabras == null || palabras.size() == 0 || palabra == null) {
            return false;
        }
        // Recorremos cada palabra de la lista.
        for (String palabraLista : palabras) {
            // equalsIgnoreCase compara sin distinguir mayusculas de minusculas ("Casa" y "casa" son la misma).
            if (palabraLista.equalsIgnoreCase(palabra)) {
                // En cuanto una coincide ya sabemos la respuesta, no hace falta seguir mirando.
                return true;
            }
        }
        // Se han mirado todas y ninguna coincide.
        return false;
    }

    @Override
    public Integer encontrarMaximo(List<Integer> numeros) {
        // Una lista vacia no tiene maximo, asi que es un dato invalido.
        if (numeros == null || numeros.size() == 0) {
            throw new IllegalArgumentException();
        }
        // Partimos suponiendo que el primero es el mayor (por eso hemos comprobado antes que hay al menos uno).
        Integer maximo = numeros.get(0);
        // Comparamos cada numero contra el mayor que llevamos hasta ahora.
        for (Integer numero : numeros) {
            // Si encontramos uno mas grande, pasa a ser el nuevo maximo.
            if (numero > maximo) {
                maximo = numero;
            }
        }
        // Al terminar, "maximo" guarda el mayor de todos.
        return maximo;
    }

    @Override
    public Integer encontrarMinimo(List<Integer> numeros) {
        // Igual que en el maximo: sin elementos no hay minimo.
        if (numeros == null || numeros.size() == 0) {
            throw new IllegalArgumentException();
        }
        // Suponemos que el primero es el menor.
        Integer minimo = numeros.get(0);
        // Recorremos toda la lista comparando.
        for (Integer numero : numeros) {
            // Si el actual es mas pequeño que el minimo que llevamos, lo sustituimos.
            if (numero < minimo) {
                minimo = numero;
            }
        }
        // Al terminar, "minimo" guarda el menor de todos.
        return minimo;
    }

    @Override
    public Integer contarApariciones(List<Integer> numeros, Integer valor) {
        // Con datos invalidos devolvemos null (es lo que se documento en la interfaz).
        if (numeros == null || numeros.size() == 0 || valor == null) {
            return null;
        }
        // Contador que empieza en 0 y sube 1 cada vez que aparece el valor.
        Integer numeroDeApariciones = 0;
        // Miramos todos los numeros, sin parar en el primero porque queremos contarlos TODOS.
        for (Integer numero : numeros) {
            // equals porque son objetos Integer (no usar == para comparar valores).
            if (numero.equals(valor)) {
                // Hemos encontrado una aparicion mas.
                numeroDeApariciones++;
            }
        }
        // Devolvemos el total contado.
        return numeroDeApariciones;
    }
}
