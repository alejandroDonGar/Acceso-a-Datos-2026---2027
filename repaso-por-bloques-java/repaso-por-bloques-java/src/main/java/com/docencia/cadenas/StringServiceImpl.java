package com.docencia.cadenas;

/**
 * Clase que implementa los metodos de StringService
 * @author AlejandroDonGar
 */
public class StringServiceImpl implements StringService {

    @Override
    public String normalizarTexto(String texto) {
        // Un texto null o vacio no se puede normalizar.
        if (texto == null || texto.isEmpty()) {
            throw new IllegalArgumentException();
        }
        // Encadenamos 3 pasos, uno tras otro:
        // 1) trim() quita los espacios del principio y del final.
        // 2) toLowerCase() pasa todo a minusculas.
        // 3) replaceAll("\\s+", " ") busca grupos de 1 o mas espacios seguidos (\\s+) y los cambia por UN solo espacio.
        return texto.trim().toLowerCase().replaceAll("\\s+", " ");
    }

    @Override
    public Boolean esPalindromo(String texto) {
        // Sin texto no hay nada que comprobar.
        if (texto == null || texto.isEmpty()) {
            throw new IllegalArgumentException();
        }
        // LIMPIAR: pasamos a minusculas (para que "Ana" cuente igual) y quitamos los espacios
        // (para que "anita lava la tina" se lea como una sola tira de letras).
        String limpio = texto.toLowerCase().replace(" ", "");
        // Guardamos la longitud en una variable para no llamar a length() en cada vuelta.
        int longitud = limpio.length();
        // COMPARAR los extremos hacia el centro: el 1º con el ultimo, el 2º con el penultimo...
        // Solo hace falta llegar a la mitad (longitud / 2): la segunda mitad ya se ha comparado como pareja de la primera.
        for (int i = 0; i < longitud / 2; i++) {
            // charAt(i) es la letra desde el principio; charAt(longitud - 1 - i) es su "espejo" desde el final.
            if (limpio.charAt(i) != limpio.charAt(longitud - 1 - i)) {
                // Si una pareja no coincide ya no puede ser palindromo: paramos aqui.
                return false;
            }
        }
        // Todas las parejas coincidian, asi que se lee igual en los dos sentidos.
        return true;
    }

    @Override
    public Integer contarVocales(String texto) {
        // Un texto null o vacio es un dato invalido.
        if (texto == null || texto.isEmpty()) {
            throw new IllegalArgumentException();
        }
        // Pasamos a minusculas para que "A" y "a" cuenten igual y solo tengamos que comparar con 5 letras.
        String minusculas = texto.toLowerCase();
        // Contador de vocales encontradas.
        int contador = 0;
        // Recorremos el texto letra a letra usando la posicion i.
        for (int i = 0; i < minusculas.length(); i++) {
            // charAt(i) nos da el caracter que esta en la posicion i.
            char letra = minusculas.charAt(i);
            // Si es cualquiera de las 5 vocales, sumamos uno.
            if (letra == 'a'
             || letra == 'e'
             || letra == 'i'
             || letra == 'o'
             || letra == 'u') {
                contador++;
            }
        }
        // Devolvemos cuantas vocales hemos contado.
        return contador;
    }

    @Override
    public String extraerIniciales(String nombreCompleto) {
        // Sin nombre no hay iniciales.
        if (nombreCompleto == null || nombreCompleto.isEmpty()) {
            throw new IllegalArgumentException();
        }
        // trim() quita espacios de los extremos y split("\\s+") corta el texto en palabras por cada grupo de espacios.
        // "Juan Perez Garcia" -> ["Juan", "Perez", "Garcia"].
        String[] palabras = nombreCompleto.trim().split("\\s+");
        // Aqui iremos juntando las iniciales.
        String iniciales = "";
        // Recorremos cada palabra del nombre.
        for (String palabra : palabras) {
            // Si la palabra esta vacia (pasa cuando el texto original eran solo espacios) no tiene inicial: la saltamos.
            if (!palabra.isEmpty()) {
                // charAt(0) es la primera letra; toUpperCase la pone en mayuscula; y la añadimos al final del resultado.
                iniciales = iniciales + Character.toUpperCase(palabra.charAt(0));
            }
        }
        // Devolvemos por ejemplo "JPG".
        return iniciales;
    }

    @Override
    public String invertirTexto(String texto) {
        // Sin texto no hay nada que invertir.
        if (texto == null || texto.isEmpty()) {
            throw new IllegalArgumentException();
        }
        // Aqui construiremos el texto al reves, empezando vacio.
        String invertido = "";
        // Recorremos el original DESDE EL FINAL (length - 1) HASTA EL PRINCIPIO (0), restando 1 en cada vuelta.
        for (int i = texto.length() - 1; i >= 0; i--) {
            // Añadimos cada letra al final del resultado: la ultima del original sera la primera del invertido.
            invertido = invertido + texto.charAt(i);
        }
        // "Hola" -> "aloH".
        return invertido;
    }

    @Override
    public Boolean contieneSoloLetras(String texto) {
        // Un texto null o vacio no "contiene solo letras": devolvemos false.
        if (texto == null || texto.isEmpty()) {
            return false;
        }
        // matches comprueba que TODO el texto cumple la expresion regular:
        // [a-zA-Z] = una letra (minuscula o mayuscula) y el + significa "una o mas veces".
        return texto.matches("[a-zA-Z]+");
    }
}
