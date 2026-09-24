package com.docencia.numeros;

/**
 * Servicio para practicar Integer.
 * @author AlejandroDonGar
 */
public interface IntegerService {
    /**
     * Comprueba si un numero es par usando el resto de la division entre 2
     * @param numero numero a comprobar
     * @return devuelve true si es par, false si es impar
     * @throws IllegalArgumentException si el numero es null
     */
    Boolean esPar(Integer numero);

    /**
     * Suma todos los digitos de un numero, ignorando el signo
     * @param numero numero del que sumar los digitos
     * @return devuelve la suma de sus digitos, por ejemplo 6 para 123 o para -123
     * @throws IllegalArgumentException si el numero es null
     */
    Integer sumarDigitos(Integer numero);

    /**
     * Convierte un texto en un numero entero
     * Si el texto es null o esta vacio no se lanza excepcion y se devuelve 0
     * @param texto texto con el numero a convertir
     * @return devuelve el numero entero que representa el texto
     * @throws IllegalArgumentException si el texto no tiene formato de numero valido
     */
    Integer convertirTextoAEntero(String texto);

    /**
     * Comprueba si un numero es primo probando divisores hasta su raiz cuadrada
     * @param numero numero a comprobar
     * @return devuelve true si es primo, false si no lo es
     * @throws IllegalArgumentException si el numero es null
     */
    Boolean esNumeroPrimo(Integer numero);

    /**
     * Calcula el factorial de un numero multiplicando de forma iterativa
     * @param numero numero del que calcular el factorial, entre 0 y 12 para que quepa en un Integer
     * @return devuelve el factorial del numero
     * @throws IllegalArgumentException si el numero es null, negativo o mayor de 12
     */
    Integer calcularFactorial(Integer numero);
}