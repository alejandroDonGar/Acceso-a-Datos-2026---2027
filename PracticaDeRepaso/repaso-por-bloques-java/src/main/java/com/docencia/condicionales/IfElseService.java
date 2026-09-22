package com.docencia.condicionales;
/**
 * Interfaz IfElseService con metodos a implementar sobre condicionales
 * @author AlejandroDonGar
 */
public interface IfElseService {
    /**
     * Clasifica una persona en base a si es menor o mayor de edad
     * @param edad Edad a verificar
     * @return Devuelve si es menor o menor de edad
     */
    String clasificarEdad(Integer edad);
    /**
     * Clasifica las notas en base a su digito
     * @param nota Nota a clasificar
     * @return Devuelve las clasificaciones en base a la nota
     */
    String evaluarNota(Integer nota);
    /**
     * Verifica si una persona puede acceder en base a si esta activo y su edad
     * @param activo Estado de la persona
     * @param edad Edad de la persona
     * @return Devuelve true o false en base a si puede o no entrar
     */
    Boolean puedeAcceder(Boolean activo, Integer edad);
    /**
     * Clasifica las temperatura en base a si hacer calor o no
     * @param temperatura Temperatura a clasificar
     * @return Devuelve si hace calor o no
     */
    String clasificarTemperatura(Double temperatura);
    /**
     * Calcula el resultado de la comparacion de dos numeros
     * @param primero Primer numero
     * @param segundo Segundo numero
     * @return Devuelve el resultado de la operacion
     */
    String calcularResultadoComparacion(Integer primero, Integer segundo);
}
