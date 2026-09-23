package com.docencia.condicionales;

/**
 * Interfaz IfElseService con metodos a implementar sobre condicionales
 * @author AlejandroDonGar
 */
public interface IfElseService {
    /**
     * Clasifica una persona segun si es menor de edad (menos de 18) o adulta
     * @param edad Edad a verificar
     * @return Devuelve MENOR si tiene menos de 18 anios o ADULTO si tiene 18 o mas
     * @throws IllegalArgumentException si la edad es null o negativa
     */
    String clasificarEdad(Integer edad);

    /**
     * Clasifica una nota del 0 al 10 como aprobada o suspensa
     * Si la nota es invalida (null o fuera del rango 0-10) no se lanza excepcion y se devuelve null
     * @param nota Nota a clasificar
     * @return Devuelve APROBADO si la nota es 5 o mas, SUSPENSO si es menor de 5
     */
    String evaluarNota(Integer nota);

    /**
     * Verifica si una persona puede acceder en base a si esta activa y a su edad
     * Si los datos son invalidos (null o edad negativa) no se lanza excepcion y se devuelve false
     * @param activo Estado de la persona
     * @param edad Edad de la persona
     * @return Devuelve true solo si esta activa y es mayor de edad, false en el resto de casos
     */
    Boolean puedeAcceder(Boolean activo, Integer edad);

    /**
     * Clasifica una temperatura segun si hace calor (30 grados o mas) o no
     * Si la temperatura es invalida (null) no se lanza excepcion y se devuelve null
     * @param temperatura Temperatura a clasificar
     * @return Devuelve CALOR si es 30.0 o mas, NO CALOR en caso contrario
     */
    String clasificarTemperatura(Double temperatura);

    /**
     * Compara dos numeros y dice si son iguales o distintos
     * Si algun numero es invalido (null) no se lanza excepcion y se devuelve null
     * @param primero Primer numero
     * @param segundo Segundo numero
     * @return Devuelve IGUALES si tienen el mismo valor o DIFERENTES si no
     */
    String calcularResultadoComparacion(Integer primero, Integer segundo);
}
