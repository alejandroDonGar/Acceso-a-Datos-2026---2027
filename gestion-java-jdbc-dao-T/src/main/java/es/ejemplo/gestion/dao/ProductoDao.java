package es.ejemplo.gestion.dao;

import es.ejemplo.gestion.model.Producto;
import java.sql.*;
import java.util.*;

/**
 * DAO de la tabla Productos: cada metodo solo pone el SQL, los parametros y el mapeo;
 * ejecutar la sentencia y recorrer el ResultSet lo hace AbstractDao
 * @author AlejandroDonGar
 */
public class ProductoDao extends AbstractDao<Producto> {
    public ProductoDao(String url) { super(url); }

    /** EJEMPLO COMPLETO: transformar ResultSet a Producto sin propagar excepciones. */
    @Override
    protected Optional<Producto> mapRsToClass(ResultSet rs) {
        try {
            return Optional.of(new Producto(rs.getInt("id"), rs.getString("nombre"), rs.getDouble("precio")));
        } catch (SQLException | RuntimeException e) {
            registrar("mapRsToClass producto", e);
            return Optional.empty();
        }
    }

    /** EJEMPLO COMPLETO: listar productos mediante PreparedStatement. */
    public List<Producto> listar() {
        String sql = "SELECT id,nombre,precio FROM Productos ORDER BY id";
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            return consultar(ps);
        } catch (SQLException | RuntimeException e) {
            registrar("listar productos", e);
            return List.of();
        }
    }

    public Optional<Producto> buscarPorId(Integer id) {
        if (id == null) return Optional.empty();
        String sql = "SELECT id,nombre,precio FROM Productos WHERE id=?";
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return consultarUno(ps);
        } catch (SQLException | RuntimeException e) {
            registrar("Seleccionar producto por su id", e);
            return Optional.empty();
        }
    }

    public boolean eliminar(Integer id) {
        if (id == null) return false;
        String sql = "DELETE FROM Productos WHERE id=?";
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ejecutar(ps);
        } catch (SQLException | RuntimeException e) {
            registrar("eliminar producto", e);
            return false;
        }
    }

    public boolean insertar(Producto p) {
        if (!esValido(p)) return false;
        String sql = "INSERT INTO Productos(id,nombre,precio) VALUES (?,?,?)";
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getId());
            ps.setString(2, p.getNombre());
            ps.setDouble(3, p.getPrecio());
            // Si el id ya existe, SQLite lanza error de clave primaria: ejecutar() lo registra y devuelve false
            return ejecutar(ps);
        } catch (SQLException | RuntimeException e) {
            registrar("insertar producto", e);
            return false;
        }
    }

    public boolean actualizar(Producto p) {
        if (!esValido(p)) return false;
        String sql = "UPDATE Productos SET nombre=?, precio=? WHERE id=?";
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setDouble(2, p.getPrecio());
            ps.setInt(3, p.getId());
            return ejecutar(ps);
        } catch (SQLException | RuntimeException e) {
            registrar("actualizar producto", e);
            return false;
        }
    }

    public List<Producto> buscarPorNombre(String texto) {
        if (texto == null) return List.of();
        String sql = "SELECT id,nombre,precio FROM Productos WHERE LOWER(nombre) LIKE LOWER(?) ORDER BY nombre, id";
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            // % delante y detras: el texto puede estar en cualquier parte del nombre
            ps.setString(1, "%" + texto + "%");
            return consultar(ps);
        } catch (SQLException | RuntimeException e) {
            registrar("buscar productos por nombre", e);
            return List.of();
        }
    }

    public List<Producto> buscarPorPrecio(double minimo, double maximo) {
        // BETWEEN incluye los dos extremos; si minimo > maximo no devuelve nada
        String sql = "SELECT id,nombre,precio FROM Productos WHERE precio BETWEEN ? AND ? ORDER BY id";
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, minimo);
            ps.setDouble(2, maximo);
            return consultar(ps);
        } catch (SQLException | RuntimeException e) {
            registrar("buscar productos por precio", e);
            return List.of();
        }
    }

    public int contar() {
        String sql = "SELECT COUNT(*) FROM Productos";
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            return consultarEntero(ps);
        } catch (SQLException | RuntimeException e) {
            registrar("contar productos", e);
            return 0;
        }
    }

    public double precioMedio() {
        // AVG de una tabla vacia es NULL; COALESCE lo cambia por 0
        String sql = "SELECT COALESCE(AVG(precio),0) FROM Productos";
        try (Connection con = obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            return consultarDecimal(ps);
        } catch (SQLException | RuntimeException e) {
            registrar("precio medio", e);
            return 0;
        }
    }

    // Las mismas reglas que los CHECK de la tabla: asi no llegamos a lanzar un SQL que va a fallar
    private boolean esValido(Producto p) {
        return p != null && p.getId() > 0
                && p.getNombre() != null && !p.getNombre().isBlank()
                && p.getPrecio() >= 0;
    }
}
