package es.ejemplo.cinefest.app;

import es.ejemplo.cinefest.dao.*;
import es.ejemplo.cinefest.modelo.*;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        try (var dao = new SesionDao("jdbc:sqlite:datos/cinefest.sqlite3?foreign_keys=on")) {

            // List<Object[]> -> un for y Arrays.toString para ver cada fila
            for (Object[] fila : dao.informeDiario()) {
                System.out.println(Arrays.toString(fila));
            }

            // Con parametro -> el valor de "Parametros" del README va dentro del parentesis del metodo
            // for (Object[] fila : dao.sesionesPorFecha("2026-11-13")) {
            //     System.out.println(Arrays.toString(fila));
            // }

            // List<Pelicula> con parametro -> los campos de "Salida" van en el println
            // (necesita new PeliculaDao en lugar de new SesionDao)
            // for (Pelicula p : dao.buscarPorGenero("Drama")) {
            //     System.out.println(p.getTitulo() + " | " + p.getPais() + " | " + p.getAnio());
            // }

            // List<Pelicula> -> el for usa la clase del modelo y se imprimen sus getters
            // for (Pelicula p : dao.listar()) {
            //     System.out.println(p.getTitulo() + " | " + p.getGenero() + " | " + p.getAnio());
            // }

            // Optional<Pelicula> -> sin for; si no existe muestra el texto del orElse
            // System.out.println(dao.buscarPorId(1).map(Pelicula::getTitulo).orElse("no existe"));

            // int o long -> sin for, se imprime el numero directamente
            // System.out.println(dao.venderEntrada(1, 8, "2026-11-10"));
        }
    }
}
