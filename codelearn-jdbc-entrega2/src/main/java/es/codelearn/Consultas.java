package es.codelearn;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

/**
 * @author AlejandroDonGar
 * Consultas
 */

/** Ejercicios del alumnado: completa los 12 métodos sin cambiar sus firmas. */
public final class Consultas {
    private Consultas() { }

    /** Ejercicio: COUNT(*) de Productos; debe devolver 0 si la tabla está vacía. */
    public static int contarProductos() {
        try (Connection connection = BaseDatos.conectar();
            PreparedStatement sentencia = connection.prepareStatement("SELECT COUNT(*) FROM productos")) {
                ResultSet resultado = sentencia.executeQuery();
                while (resultado.next()) {
                    int mumeroDeProductos = resultado.getInt(1);
                    return resultado.wasNull() ? 0 : mumeroDeProductos;
                }
        } catch (SQLException e) {
            System.err.println("Error SQL al contar los productos: " + e.getMessage());
            return -1;
        }
        return -1;
    }

    /** Ejercicio: COUNT(*) de Vehiculos. */
    public static int contarVehiculos() {
        try (Connection connection = BaseDatos.conectar();
            PreparedStatement sentencia = connection.prepareStatement("SELECT COUNT(*) FROM vehiculos")) {
                ResultSet resultado = sentencia.executeQuery();
                while (resultado.next()) {
                    int mumeroDeVehiculos = resultado.getInt(1);
                    return resultado.wasNull() ? 0 : mumeroDeVehiculos;
                }
        } catch (SQLException e) {
            System.err.println("Error SQL al contar los vehículos: " + e.getMessage());
            return -1;
        }
        return -1;
    }

    /** Ejercicio: EXISTS o COUNT de Productos filtrando por id con ?. */
    public static boolean existeProducto(int id) {
        if (id < 1) {
            throw new IllegalArgumentException("El id no puede ser menor a 1");
        }
        try (Connection connection = BaseDatos.conectar();
            PreparedStatement sentencia = connection.prepareStatement("SELECT COUNT(*) FROM productos WHERE id = ?")) {
                sentencia.setInt(1, id);
                ResultSet resultado = sentencia.executeQuery();
                while (resultado.next()) {
                    return resultado.getInt(1) > 0;
                }
        } catch (SQLException e) {
            System.err.println("Error SQL al comprobar si existe el producto " + id + ": " + e.getMessage());
            return false;
        }
        return false;
    }

    /** Ejercicio: EXISTS o COUNT de Vehiculos filtrando por matrícula con ?. */
    public static boolean existeVehiculo(String matricula) {
        if (matricula == null || matricula.isBlank()) {
            throw new IllegalArgumentException("La matricula no puede ser nula");
        }
        try (Connection connection = BaseDatos.conectar();
            PreparedStatement sentencia = connection.prepareStatement("SELECT COUNT(*) FROM vehiculos WHERE matricula = ?")) {
                sentencia.setString(1, matricula);
                ResultSet resultado = sentencia.executeQuery();
                while (resultado.next()) {
                    return resultado.getBoolean(1);
                }
        } catch (SQLException e) {
            System.err.println("Error SQL al comprobar si existe el vehículo " + matricula + ": " + e.getMessage());
            return false;
        }
        return false;
    }

    /** Ejercicio: COUNT(*) con precio estrictamente MAYOR que el parámetro. */
    public static int contarProductosCaros(double precio) {
        if (precio < 1.0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        try (Connection connection = BaseDatos.conectar();
            PreparedStatement sentencia = connection.prepareStatement("SELECT COUNT(*) FROM productos WHERE precio > ?")) {
                sentencia.setDouble(1, precio);
                ResultSet resultado = sentencia.executeQuery();
                while (resultado.next()) {
                    return resultado.getInt(1);
                }
        } catch (SQLException e) {
            System.err.println("Error SQL al contar productos con precio mayor que " + precio + ": " + e.getMessage());
            return -1;
        }
        return -1;
    }

    /** Ejercicio: COUNT(*) de la marca exacta proporcionada. */
    public static int contarVehiculosMarca(String marca) {
        if (marca == null || marca.isBlank()) {
            throw new IllegalArgumentException("El valor no puede ser nulo");
        }
        try (Connection connection = BaseDatos.conectar();
            PreparedStatement sentencia = connection.prepareStatement("SELECT COUNT(*) FROM vehiculos WHERE marca = ?")) {
                sentencia.setString(1, marca);
                ResultSet resultado = sentencia.executeQuery();
                while (resultado.next()) {
                    return resultado.getInt(1);
                }
        } catch (SQLException e) {
            System.err.println("Error SQL al contar vehículos de la marca " + marca + ": " + e.getMessage());
            return -1;
        }
        return 0;
    }

    /** Ejercicio: AVG(precio); para tabla vacía devolver 0.0. */
    public static double obtenerPrecioMedio() {
        try (Connection connection = BaseDatos.conectar();
            PreparedStatement sentencia = connection.prepareStatement("SELECT AVG(precio) FROM productos")) {
                ResultSet resultado = sentencia.executeQuery();
                while (resultado.next()) {
                    Double precioMedio = resultado.getDouble(1);
                    return resultado.wasNull() ? 0.0 : precioMedio;
                }
        } catch (SQLException e) {
            System.err.println("Error SQL al obtener el precio medio: " + e.getMessage());
            return Double.NaN;
        }
        return 0.0;
    }

    /** Ejercicio: MAX(precio); para tabla vacía devolver 0.0. */
    public static double obtenerPrecioMaximo() {
        try (Connection connection = BaseDatos.conectar();
            PreparedStatement sentencia = connection.prepareStatement("SELECT MAX(precio) FROM productos")) {
                ResultSet resultado = sentencia.executeQuery();
                while (resultado.next()) {
                    Double precioMaximo = resultado.getDouble(1);
                    return resultado.wasNull() ? 0.0 : precioMaximo;
                }
        } catch (SQLException e) {
            System.err.println("Error SQL al obtener el precio máximo: " + e.getMessage());
            return Double.NaN;
        }
        return 0.0;
    }

    /** Ejercicio: MIN(precio); para tabla vacía devolver 0.0. */
    public static double obtenerPrecioMinimo() {
        try (Connection connection = BaseDatos.conectar();
            PreparedStatement sentencia = connection.prepareStatement("SELECT MIN(precio) FROM productos")) {
                ResultSet resultado = sentencia.executeQuery();
                while (resultado.next()) {
                    Double precioMinimo = resultado.getDouble(1);
                    return resultado.wasNull() ? 0.0 : precioMinimo;
                }
        } catch (SQLException e) {
            System.err.println("Error SQL al obtener el precio mínimo: " + e.getMessage());
            return Double.NaN;
        }
        return 0.0;
    }

    /** Ejercicio: DISTINCT marca de Vehiculos ordenadas alfabéticamente ASC. */
    public static List<String> listarMarcas() {
        List<String> listaDeMarcas = new ArrayList<>();
        try (Connection connection = BaseDatos.conectar();
            PreparedStatement sentencia = connection.prepareStatement("SELECT DISTINCT marca FROM vehiculos ORDER BY marca ASC")) {
                ResultSet resultado = sentencia.executeQuery();
                while (resultado.next()) {
                    listaDeMarcas.add(resultado.getString("marca"));
                }
        }
        catch (SQLException e) {
            System.err.println("Error SQL al listar las marcas: " + e.getMessage());
            return List.of();
        }
        return listaDeMarcas;
    }

    /** Ejercicio: Nombres de Productos con precio > parámetro, ORDER BY precio DESC, nombre ASC. */
    public static List<String> listarProductosCaros(double precio) {
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        List<String> productosCaros = new ArrayList<>();
        try (Connection connection = BaseDatos.conectar();
            PreparedStatement sentencia = connection.prepareStatement("SELECT nombre FROM productos WHERE precio > ? ORDER BY precio DESC, nombre ASC")) {
                sentencia.setDouble(1, precio);
                ResultSet resultado = sentencia.executeQuery();
                while (resultado.next()) {
                    productosCaros.add(resultado.getString("nombre"));
                }
        } catch (SQLException e) {
            System.err.println("Error SQL al listar productos con precio mayor que " + precio + ": " + e.getMessage());
            return List.of();
        }
        return productosCaros;
    }

    /** Ejercicio: Marcas agrupadas con COUNT(*) >= minimo; ORDER BY cantidad DESC, marca ASC. */
    public static List<String> listarMarcasFrecuentes(int minimo) {
        if (minimo < 0) {
            throw new IllegalArgumentException("El minimo no puede ser negativo");
        }
        List<String> marcasMasFrecuentes = new ArrayList<>();
        try (Connection connection = BaseDatos.conectar();
            PreparedStatement sentencia = connection.prepareStatement("SELECT marca FROM vehiculos GROUP BY marca HAVING COUNT(*) >= ? ORDER BY COUNT(*) DESC, marca ASC")) {
                sentencia.setInt(1, minimo);
                ResultSet resultado = sentencia.executeQuery();
                while (resultado.next()) {
                    marcasMasFrecuentes.add(resultado.getString("marca"));
                }
        } catch (SQLException e) {
            System.err.println("Error SQL al listar marcas con al menos " + minimo + " vehículos: " + e.getMessage());
            return List.of();
        }
        return marcasMasFrecuentes;
    }

}