package es.codelearnacademy.filelab.model;

/**
 * Modelo de un producto usado en los ejemplos de CRUD
 * @author AlejandroDonGar
 */
public record Producto(long id, String nombre, double precio) {

    public Producto {
        if (id <= 0) {
            throw new IllegalArgumentException("El id debe ser positivo");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (!Double.isFinite(precio) || precio < 0) {
            throw new IllegalArgumentException("El precio no es válido");
        }
    }
}
