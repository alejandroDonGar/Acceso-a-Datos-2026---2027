package com.docencia.numeros;

/**
 * Clase que implementa los metodos de la interfaz IntegerService
 * @author AlejandroDonGar
 */
public class IntegerServiceImpl implements IntegerService {

    @Override
    public Boolean esPar(Integer numero) {
        // Un Integer puede ser null y no se puede operar con el: es un dato invalido.
        if (numero == null) {
            throw new IllegalArgumentException();
        }
        // % es el RESTO de la division. Al dividir entre 2, un numero par deja resto 0 y un impar deja resto 1
        // (o -1 en negativos, por eso comparamos con == 0 y no con == 1).
        return numero % 2 == 0;
    }

    @Override
    public Integer sumarDigitos(Integer numero) {
        // Sin numero no hay digitos que sumar.
        if (numero == null) {
            throw new IllegalArgumentException();
        }
        // Math.abs quita el signo: -123 se trata como 123. Usamos long para que tampoco falle con el
        // numero negativo mas grande que cabe en un int.
        long restante = Math.abs((long) numero);
        // Aqui acumularemos la suma.
        int suma = 0;
        // Mientras queden digitos por procesar (el numero sea mayor que 0).
        while (restante > 0) {
            // restante % 10 es el resto de dividir entre 10 = la ULTIMA cifra (123 % 10 = 3).
            suma = suma + (int) (restante % 10);
            // restante / 10 es la division entera = el numero SIN su ultima cifra (123 / 10 = 12).
            // Asi en la siguiente vuelta la ultima cifra sera la siguiente (2, luego 1...) hasta llegar a 0.
            restante = restante / 10;
        }
        // 123 -> 3 + 2 + 1 = 6.
        return suma;
    }

    @Override
    public Integer convertirTextoAEntero(String texto) {
        // Si no hay texto devolvemos 0 (es lo que se documento en la interfaz).
        if (texto == null || texto.isEmpty()) {
            return 0;
        }
        // trim() quita espacios sobrantes y parseInt convierte el texto en numero.
        // Si el texto no es un numero ("abc") parseInt lanza NumberFormatException,
        // que ya es una subclase de IllegalArgumentException, asi que cumple lo que pide la interfaz.
        return Integer.parseInt(texto.trim());
    }

    @Override
    public Boolean esNumeroPrimo(Integer numero) {
        // Sin numero no se puede comprobar.
        if (numero == null) {
            throw new IllegalArgumentException();
        }
        // Los menores que 2 (0, 1 y negativos) no son primos por definicion.
        if (numero < 2) {
            return false;
        }
        // Probamos divisores desde 2. Solo hace falta llegar hasta la raiz cuadrada: si un numero tiene un divisor
        // mayor que su raiz, tambien tiene otro menor, asi que "i * i <= numero" equivale a "i <= raiz de numero".
        // El (long) evita que i * i se desborde con numeros muy grandes cercanos al maximo de un int.
        for (int i = 2; (long) i * i <= numero; i++) {
            // Si la division es exacta (resto 0), encontramos un divisor: no es primo.
            if (numero % i == 0) {
                return false;
            }
        }
        // Ningun divisor encontrado: es primo.
        return true;
    }

    @Override
    public Integer calcularFactorial(Integer numero) {
        // Null o negativo: el factorial no esta definido.
        if (numero == null || numero < 0) {
            throw new IllegalArgumentException();
        }
        // 13! ya no cabe en un Integer (se desbordaria y daria un resultado negativo erroneo), asi que lo rechazamos.
        if (numero > 12) {
            throw new IllegalArgumentException();
        }
        // Empezamos en 1 porque es el elemento neutro de la multiplicacion (y asi 0! = 1 sin caso especial).
        int resultado = 1;
        // Multiplicamos 2 * 3 * ... * numero (empezar en 1 seria multiplicar por 1, que no cambia nada).
        for (int i = 2; i <= numero; i++) {
            // Acumulamos el producto en cada vuelta.
            resultado = resultado * i;
        }
        // 5! = 1 * 2 * 3 * 4 * 5 = 120.
        return resultado;
    }
}