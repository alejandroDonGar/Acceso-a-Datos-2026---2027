package com.docencia.fechas;

import java.time.LocalDate;

/**
 * Servicio para practicar LocalDate.
 * @author AlejandroDonGar
 */
public interface LocalDateService {
    /**
     * Calcula la edad en anios completos a partir de la fecha de nacimiento
     * @param fechaNacimiento fecha de nacimiento de la persona
     * @return devuelve los anios cumplidos hasta hoy
     * @throws IllegalArgumentException si la fecha es null o es una fecha futura
     */
    Integer calcularEdad(LocalDate fechaNacimiento);

    /**
     * Comprueba si una fecha es posterior al dia de hoy
     * @param fecha fecha a comprobar
     * @return devuelve true si la fecha es futura, false si es hoy o pasada
     * @throws IllegalArgumentException si la fecha es null
     */
    Boolean esFechaFutura(LocalDate fecha);

    /**
     * Calcula los dias que hay entre dos fechas
     * @param inicio fecha de inicio
     * @param fin fecha de fin
     * @return devuelve el numero de dias entre el inicio y el fin
     * @throws IllegalArgumentException si alguna fecha es null o el fin es anterior al inicio
     */
    Long calcularDiasEntreFechas(LocalDate inicio, LocalDate fin);

    /**
     * Suma (o resta si son negativos) una cantidad de dias a una fecha
     * @param fecha fecha de partida
     * @param dias dias a sumar, un numero negativo resta dias
     * @return devuelve la nueva fecha
     * @throws IllegalArgumentException si la fecha o los dias son null
     */
    LocalDate sumarDias(LocalDate fecha, Integer dias);

    /**
     * Comprueba si una persona tiene 18 anios o mas a partir de su fecha de nacimiento
     * @param fechaNacimiento fecha de nacimiento de la persona
     * @return devuelve true si es mayor de edad, false si es menor
     * @throws IllegalArgumentException si la fecha es null o es una fecha futura
     */
    Boolean esMayorDeEdad(LocalDate fechaNacimiento);
}
