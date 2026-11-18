package es.codelearn;

/**
 * Prueba por consola de algunas consultas. La aplicación no gestiona Connection ni SQLException.
 * @author AlejandroDonGar
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Productos: " + Consultas.contarProductos());
        System.out.println("Vehículos: " + Consultas.contarVehiculos());
        System.out.println("¿Existe el producto 1?: " + Consultas.existeProducto(1));
        System.out.println("Precio medio: " + Consultas.obtenerPrecioMedio());
        System.out.println("Marcas: " + Consultas.listarMarcas());    }
}
