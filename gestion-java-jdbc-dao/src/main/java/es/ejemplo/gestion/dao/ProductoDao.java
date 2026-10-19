package es.ejemplo.gestion.dao;

import es.ejemplo.gestion.config.ConexionBD;
import es.ejemplo.gestion.exception.AplicationException;
import es.ejemplo.gestion.model.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author AlejandroDonGar
 * ProductoDao
 */
public class ProductoDao extends AbstractDao<Producto> {

    public ProductoDao(String url) {
        super(url);
    }

    @Override
    Producto mapperToClass(ResultSet rs) {
        try {
            return new Producto(rs.getInt(1), rs.getString(2), rs.getDouble(3));
        } catch (SQLException e) {
            throw new AplicationException(e.getMessage(), e.getCause());
        }
    }


    public List<Producto> listar() {
        String query = "SELECT id, nombre, precio FROM Productos";

        return this.consultar(query);
    }

    public Optional<Producto> buscarPorId(int id) {
    try (Connection connection = this.obtenerConexion();
        PreparedStatement sentencia = connection.prepareStatement("SELECT id, nombre, precio FROM Productos WHERE id=?")) {
        sentencia.setInt(1, id);
        ResultSet resultado = sentencia.executeQuery();
        return resultado.next() ? Optional.of(productoMapper(resultado)) : Optional.empty();
    } catch (Exception e) {
        throw new AplicationException(e.getMessage(), e);
    }
}

    public boolean insertar(Producto producto) {
        try (Connection connection = this.obtenerConexion();
            PreparedStatement sentencia = connection.prepareStatement("INSERT INTO productos VALUES (?,?,?)")) {
            sentencia.setInt(1, producto.getId());
            sentencia.setString(2, producto.getNombre());
            sentencia.setDouble(3, producto.getPrecio());
            return sentencia.executeUpdate() > 0;
        } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e.getCause());
        }
    }

    public boolean actualizar(Producto producto) {
        try (Connection connection = this.obtenerConexion();
            PreparedStatement sentencia = connection.prepareStatement("UPDATE productos SET nombre=?, precio=? WHERE id=?")) {
            sentencia.setDouble(1, producto.getPrecio());    
            sentencia.setString(2, producto.getNombre());
            sentencia.setInt(3, producto.getId());
            return sentencia.executeUpdate() > 0;
        } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e.getCause());
        }
    }

    public boolean eliminar(int id) {
        try (Connection connection = this.obtenerConexion();
            PreparedStatement sentencia = connection.prepareStatement("DELETE FROM productos WHERE id=?")) { 
            sentencia.setInt(1, id);
            return sentencia.executeUpdate() > 0;
        } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e.getCause());
        }
    }

    public List<Producto> buscarPorNombre(String texto) {
       List<Producto> listaDeNombres = new ArrayList<>();
        try (Connection connection = this.obtenerConexion();
            PreparedStatement sentencia = connection.prepareStatement("SELECT id, nombre, precio FROM Productos WHERE nombre=?")) {
            sentencia.setString(1, texto);
            ResultSet resultado = sentencia.executeQuery();
            while(resultado.next()) {
                listaDeNombres.add(productoMapper(resultado));
            }
        } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e.getCause());
        }
        return listaDeNombres;
    }

    public List<Producto> buscarPorPrecio(double minimo, double maximo) {
        List<Producto> listaDePrecios = new ArrayList<>();
        try (Connection connection = this.obtenerConexion();
            PreparedStatement sentencia = connection.prepareStatement("SELECT id, nombre, precio FROM Productos WHERE precio BETWEEN ? AND ?")) {
            sentencia.setDouble(1, minimo);
            sentencia.setDouble(2, maximo);
            ResultSet resultado = sentencia.executeQuery();
            while(resultado.next()) {
                listaDePrecios.add(productoMapper(resultado));
            }
        } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e.getCause());
        }
        return listaDePrecios;
    }

    public int contar() {
       try (Connection connection = this.obtenerConexion();
            PreparedStatement sentencia = connection.prepareStatement("SELECT COUNT(*) FROM productos")) {
           ResultSet resultado = sentencia.executeQuery();
           return resultado.next() ? resultado.getInt(1) : 0;
       } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e.getCause());
       }
    }

    public double precioMedio() {
        try (Connection connection = this.obtenerConexion();
            PreparedStatement sentencia = connection.prepareStatement("SELECT AVG(precio) FROM productos")) {
            return sentencia.executeUpdate();
       } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e.getCause());
       }
    }

    private Producto productoMapper(ResultSet rs) {
        return null;
    }
}
