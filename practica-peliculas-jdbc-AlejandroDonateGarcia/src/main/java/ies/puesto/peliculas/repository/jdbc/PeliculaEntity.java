package ies.puesto.peliculas.repository.jdbc;

/**
 * Fila de la tabla {@code peliculas}. Sin {@code public} (visibilidad de paquete):
 * fuera de {@code repository.jdbc} no se puede usar. Sin {@code @Entity}: aquí no hay JPA.
 *
 * @author AlejandroDonGar
 */
class PeliculaEntity {
    private final String codigoFila;
    private final String tituloFila;
    private final int anioFila;
    private final int minutosFila;

    PeliculaEntity(String codigoFila, String tituloFila, int anioFila, int minutosFila) {
        this.codigoFila = codigoFila;
        this.tituloFila = tituloFila;
        this.anioFila = anioFila;
        this.minutosFila = minutosFila;
    }

    String getCodigoFila() {
        return codigoFila;
    }

    String getTituloFila() {
        return tituloFila;
    }

    int getAnioFila() {
        return anioFila;
    }

    int getMinutosFila() {
        return minutosFila;
    }
}
