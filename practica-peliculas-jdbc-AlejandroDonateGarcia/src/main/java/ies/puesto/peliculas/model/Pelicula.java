package ies.puesto.peliculas.model;

/**
 * Película del catálogo (dominio). Inmutable: sin setters, al actualizar
 * se sustituye entera por otra.
 *
 * @author AlejandroDonGar
 */
public class Pelicula {
    private final String identificador;
    private final String nombre;
    private final int anioLanzamiento;
    private final int minutosProyeccion;

    public Pelicula(String identificador, String nombre, int anioLanzamiento, int minutosProyeccion) {
        this.identificador = identificador;
        this.nombre = nombre;
        this.anioLanzamiento = anioLanzamiento;
        this.minutosProyeccion = minutosProyeccion;
    }

    public String getIdentificador() {
        return identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public int getAnioLanzamiento() {
        return anioLanzamiento;
    }

    public int getMinutosProyeccion() {
        return minutosProyeccion;
    }
}
