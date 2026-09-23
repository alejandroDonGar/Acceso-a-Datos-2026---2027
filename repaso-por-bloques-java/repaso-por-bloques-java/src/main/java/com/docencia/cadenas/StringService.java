package com.docencia.cadenas;

/**
 * Interfaz con los metodos para trabajar con cadenas de texto
 * @author AlejandroDonGar
 */
public interface StringService {
    /**
     * Normaliza un texto quitando espacios sobrantes y pasandolo a minusculas
     * @param texto texto a normalizar
     * @return devuelve el texto en minusculas, sin espacios al principio ni al final y con un solo espacio entre palabras
     * @throws IllegalArgumentException si el texto es null o esta vacio
     */
    String normalizarTexto(String texto);

    /**
     * Comprueba si un texto se lee igual de izquierda a derecha que de derecha a izquierda, ignorando espacios y mayusculas
     * @param texto texto a comprobar
     * @return devuelve true si el texto es un palindromo, false si no lo es
     * @throws IllegalArgumentException si el texto es null o esta vacio
     */
    Boolean esPalindromo(String texto);

    /**
     * Cuenta cuantas vocales (a, e, i, o, u) tiene un texto
     * @param texto texto a analizar
     * @return devuelve el numero de vocales encontradas
     * @throws IllegalArgumentException si el texto es null o esta vacio
     */
    Integer contarVocales(String texto);

    /**
     * Extrae la inicial en mayuscula de cada palabra de un nombre completo
     * @param nombreCompleto nombre completo del que sacar las iniciales
     * @return devuelve las iniciales juntas, por ejemplo JPG para Juan Perez Garcia
     * @throws IllegalArgumentException si el nombre es null o esta vacio
     */
    String extraerIniciales(String nombreCompleto);

    /**
     * Invierte el orden de los caracteres de un texto
     * @param texto texto a invertir
     * @return devuelve el texto escrito al reves
     * @throws IllegalArgumentException si el texto es null o esta vacio
     */
    String invertirTexto(String texto);

    /**
     * Comprueba que un texto contiene unicamente letras
     * Si el texto es invalido (null o vacio) no se lanza excepcion y se devuelve false
     * @param texto texto a comprobar
     * @return devuelve true si todos los caracteres son letras, false en caso contrario
     */
    Boolean contieneSoloLetras(String texto);
}
