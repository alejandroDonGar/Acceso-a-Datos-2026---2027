package com.docencia.numeros;

/**
 * Clase que implementa los metodos de la interfaz MathService
 * @author AlejandroDonGar
 */
public class MathServiceImpl implements MathService {

    @Override
    public Double calcularAreaCirculo(Double radio) {
        // Un radio null o negativo no tiene sentido geometrico.
        if (radio == null || radio < 0) {
            throw new IllegalArgumentException();
        }
        // Formula del area: PI * radio al cuadrado. Math.PI es la constante de PI ya definida en Java.
        return Math.PI * radio * radio;
    }

    @Override
    public Integer calcularPotencia(Integer base, Integer exponente) {
        // Sin datos no se puede calcular; y con exponente negativo el resultado seria un decimal (no cabe en Integer).
        if (base == null || exponente == null || exponente < 0) {
            throw new IllegalArgumentException();
        }
        // Empezamos en 1: cualquier numero elevado a 0 es 1, y ademas es el neutro de la multiplicacion.
        int resultado = 1;
        // Multiplicamos la base por si misma tantas veces como diga el exponente (2^3 = 2 * 2 * 2).
        for (int i = 0; i < exponente; i++) {
            // En cada vuelta el resultado crece una multiplicacion mas.
            resultado = resultado * base;
        }
        // Con exponente 0 el bucle no se ejecuta y se devuelve 1, que es lo correcto.
        return resultado;
    }

    @Override
    public Integer redondearHaciaArriba(Double numero) {
        // Sin numero no se puede redondear.
        if (numero == null) {
            throw new IllegalArgumentException();
        }
        // Math.ceil devuelve el entero superior (2.1 -> 3.0) pero como double, asi que lo convertimos a int con (int).
        return (int) Math.ceil(numero);
    }

    @Override
    public Double calcularRaizCuadrada(Double numero) {
        // No existe raiz cuadrada real de un numero negativo.
        if (numero == null || numero < 0) {
            throw new IllegalArgumentException();
        }
        // Math.sqrt calcula la raiz cuadrada (sqrt = square root).
        return Math.sqrt(numero);
    }

    @Override
    public Integer obtenerValorAbsoluto(Integer numero) {
        // Sin numero no hay valor absoluto.
        if (numero == null) {
            throw new IllegalArgumentException();
        }
        // Math.abs devuelve el numero sin signo: -5 -> 5 y 5 -> 5.
        return Math.abs(numero);
    }
}
