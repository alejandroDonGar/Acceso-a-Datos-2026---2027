package com.docencia.colecciones;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase que implementa los metodos de la interfaz MapService
 * @author AlejandroDonGar
 */
public class MapServiceImpl implements MapService {

    @Override
    public Map<String, Integer> contarFrecuenciaPalabras(List<String> palabras) {
        // Sin lista no se puede contar.
        if (palabras == null) {
            throw new IllegalArgumentException();
        }
        // Mapa palabra -> veces que aparece. LinkedHashMap conserva el orden de primera aparicion.
        Map<String, Integer> frecuencias = new LinkedHashMap<>();
        // Recorremos cada palabra de la lista.
        for (String palabra : palabras) {
            // Si la palabra YA esta como clave, la hemos visto antes...
            if (frecuencias.containsKey(palabra)) {
                // ...asi que leemos su contador actual y guardamos ese valor + 1 (put sobrescribe el anterior).
                frecuencias.put(palabra, frecuencias.get(palabra) + 1);
            } else {
                // Si es la primera vez que la vemos, la metemos con contador 1.
                frecuencias.put(palabra, 1);
            }
        }
        // "a","b","a" -> {a=2, b=1}.
        return frecuencias;
    }

    @Override
    public Integer obtenerValorPorClave(Map<String, Integer> mapa, String clave) {
        // Sin mapa no se puede buscar.
        if (mapa == null) {
            throw new IllegalArgumentException();
        }
        // Una clave null no puede estar en el mapa (y con Map.of preguntar por null da error), asi que devolvemos 0 directamente.
        if (clave == null) {
            return 0;
        }
        // containsKey nos dice si la clave existe.
        if (mapa.containsKey(clave)) {
            // Existe: devolvemos su valor.
            return mapa.get(clave);
        }
        // No existe: devolvemos 0 (get devolveria null y eso daria problemas al usar el Integer).
        return 0;
    }

    @Override
    public Map<String, Double> calcularMediaPorCategoria(Map<String, List<Integer>> datos) {
        // Sin mapa no se puede calcular.
        if (datos == null) {
            throw new IllegalArgumentException();
        }
        // Mapa resultado: categoria -> media.
        Map<String, Double> medias = new LinkedHashMap<>();
        // Recorremos cada categoria (cada clave del mapa).
        for (String categoria : datos.keySet()) {
            // La lista de numeros de esa categoria.
            List<Integer> numeros = datos.get(categoria);
            // Si la categoria no tiene numeros no se puede dividir entre 0: su media la dejamos en 0.0.
            if (numeros == null || numeros.isEmpty()) {
                medias.put(categoria, 0.0);
            } else {
                // Sumamos todos los numeros de la categoria.
                int suma = 0;
                for (Integer numero : numeros) {
                    suma = suma + numero;
                }
                // Media = suma / cantidad, con (double) para no perder los decimales en la division.
                medias.put(categoria, (double) suma / numeros.size());
            }
        }
        // Devolvemos todas las medias.
        return medias;
    }

    @Override
    public String obtenerClaveConMayorValor(Map<String, Integer> mapa) {
        // Un mapa vacio no tiene "mayor valor".
        if (mapa == null || mapa.isEmpty()) {
            throw new IllegalArgumentException();
        }
        // Guardaremos la mejor clave encontrada y su valor. Al principio aun no hay ninguna.
        String claveMayor = null;
        int valorMayor = 0;
        // Recorremos todas las claves.
        for (String clave : mapa.keySet()) {
            // Valor asociado a esta clave.
            int valor = mapa.get(clave);
            // Es la mejor hasta ahora si todavia no habia ninguna (primera vuelta) o si supera al mayor guardado.
            if (claveMayor == null || valor > valorMayor) {
                claveMayor = clave;
                valorMayor = valor;
            }
        }
        // Al terminar, claveMayor es la clave del valor mas alto.
        return claveMayor;
    }

    @Override
    public Map<String, Integer> filtrarPorValorMinimo(Map<String, Integer> mapa, Integer minimo) {
        // Necesitamos el mapa y el minimo.
        if (mapa == null || minimo == null) {
            throw new IllegalArgumentException();
        }
        // Mapa nuevo donde guardaremos solo las entradas que cumplan.
        Map<String, Integer> filtrado = new LinkedHashMap<>();
        // Recorremos cada clave del mapa original.
        for (String clave : mapa.keySet()) {
            // Valor de esa clave.
            Integer valor = mapa.get(clave);
            // Si alcanza el minimo (mayor o igual) la copiamos al resultado.
            if (valor >= minimo) {
                filtrado.put(clave, valor);
            }
        }
        // {a=1, b=5} con minimo 3 -> {b=5}.
        return filtrado;
    }
}
