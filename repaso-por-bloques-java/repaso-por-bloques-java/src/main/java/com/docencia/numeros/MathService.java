package com.docencia.numeros;

/**
 * Servicio para practicar Math.
 * @author AlejandroDonGar
 */
public interface MathService {
    /**
     * Calcula el area de un circulo con la formula PI por radio al cuadrado
     * @param radio radio del circulo
     * @return devuelve el area del circulo
     * @throws IllegalArgumentException si el radio es null o negativo
     */
    Double calcularAreaCirculo(Double radio);

    /**
     * Calcula la potencia de un numero, es decir, la base multiplicada por si misma tantas veces como indique el exponente
     * @param base numero que se multiplica
     * @param exponente numero de veces que se multiplica, no puede ser negativo
     * @return devuelve el resultado de la potencia
     * @throws IllegalArgumentException si algun dato es null o el exponente es negativo
     */
    Integer calcularPotencia(Integer base, Integer exponente);

    /**
     * Redondea un numero decimal hacia arriba, al entero superior
     * @param numero numero decimal a redondear
     * @return devuelve el entero superior o igual al numero
     * @throws IllegalArgumentException si el numero es null
     */
    Integer redondearHaciaArriba(Double numero);

    /**
     * Calcula la raiz cuadrada de un numero
     * @param numero numero del que calcular la raiz
     * @return devuelve la raiz cuadrada
     * @throws IllegalArgumentException si el numero es null o negativo
     */
    Double calcularRaizCuadrada(Double numero);

    /**
     * Obtiene el valor absoluto de un numero, es decir, sin signo negativo
     * @param numero numero del que obtener el valor absoluto
     * @return devuelve el numero en positivo
     * @throws IllegalArgumentException si el numero es null
     */
    Integer obtenerValorAbsoluto(Integer numero);
}
