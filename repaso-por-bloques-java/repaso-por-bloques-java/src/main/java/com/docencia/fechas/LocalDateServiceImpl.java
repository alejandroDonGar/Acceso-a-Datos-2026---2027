package com.docencia.fechas;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

/**
 * Clase que implementa los metodos de la interfaz LocalDateService
 * @author AlejandroDonGar
 */
public class LocalDateServiceImpl implements LocalDateService {

    @Override
    public Integer calcularEdad(LocalDate fechaNacimiento) {
        // Sin fecha no se puede calcular.
        if (fechaNacimiento == null) {
            throw new IllegalArgumentException();
        }
        // Nadie ha nacido en el futuro: isAfter comprueba si la fecha es posterior a hoy.
        if (fechaNacimiento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException();
        }
        // Period.between calcula la diferencia entre dos fechas en anios, meses y dias.
        // getYears() nos da solo los anios COMPLETOS (si aun no ha cumplido este año, no lo cuenta).
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    @Override
    public Boolean esFechaFutura(LocalDate fecha) {
        // Sin fecha no se puede comprobar.
        if (fecha == null) {
            throw new IllegalArgumentException();
        }
        // isAfter(hoy) es true solo si la fecha es estrictamente posterior a hoy (hoy mismo NO es futuro).
        return fecha.isAfter(LocalDate.now());
    }

    @Override
    public Long calcularDiasEntreFechas(LocalDate inicio, LocalDate fin) {
        // Necesitamos las dos fechas.
        if (inicio == null || fin == null) {
            throw new IllegalArgumentException();
        }
        // Si el fin es anterior al inicio el rango esta al reves: dato invalido.
        if (fin.isBefore(inicio)) {
            throw new IllegalArgumentException();
        }
        // ChronoUnit.DAYS.between cuenta los dias que hay de la primera fecha a la segunda (del 1 al 6 de enero = 5).
        return ChronoUnit.DAYS.between(inicio, fin);
    }

    @Override
    public LocalDate sumarDias(LocalDate fecha, Integer dias) {
        // Necesitamos la fecha y los dias.
        if (fecha == null || dias == null) {
            throw new IllegalArgumentException();
        }
        // plusDays suma dias y devuelve una fecha NUEVA (LocalDate es inmutable). Si dias es negativo, resta.
        return fecha.plusDays(dias);
    }

    @Override
    public Boolean esMayorDeEdad(LocalDate fechaNacimiento) {
        // Reutilizamos calcularEdad: ya valida null y fechas futuras por nosotros, y evitamos repetir codigo.
        // Es mayor de edad si tiene 18 anios cumplidos o mas.
        return calcularEdad(fechaNacimiento) >= 18;
    }
}
