package es.ejemplo.taller.dao;

import es.ejemplo.taller.modelo.Vehiculo;
import java.sql.*;
import java.util.*;
/**
 * @author AlejandroDonGar
 * VehiculoDao
 */
public class VehiculoDao extends AbstractDao<Vehiculo> {

    public VehiculoDao(String url) {
        super(url);
    }

    public List<Vehiculo> listar() {
        List<Vehiculo> vehiculosEncontrados = new ArrayList<>();
        try (Connection connection = abrir(url);
             PreparedStatement sentencia = connection.prepareStatement("SELECT matricula, marca, modelo, anio, id_cliente FROM vehiculos")) {
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                vehiculosEncontrados.add(new Vehiculo(
                        resultado.getString("matricula"),
                        resultado.getString("marca"),
                        resultado.getString("modelo"),
                        resultado.getInt("anio"),
                        resultado.getInt("id_cliente")));
            }
            return vehiculosEncontrados;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public Optional<Vehiculo> buscarPorId(String id) {
        try (Connection connection = abrir(url);
             PreparedStatement sentencia = connection.prepareStatement("SELECT matricula, marca, modelo, anio, id_cliente FROM vehiculos WHERE matricula=?")) {
            sentencia.setString(1, id);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                return Optional.of(new Vehiculo(
                        resultado.getString("matricula"),
                        resultado.getString("marca"),
                        resultado.getString("modelo"),
                        resultado.getInt("anio"),
                        resultado.getInt("id_cliente")));
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int insertar(Vehiculo dato) {
        if (dato == null
                || dato.getMatricula() == null || dato.getMatricula().isEmpty()
                || dato.getMarca() == null || dato.getMarca().isEmpty()
                || dato.getModelo() == null || dato.getModelo().isEmpty()
                || dato.getAnio() == null
                || dato.getIdCliente() == null) {
            return 0;
        }
        try (Connection connection = abrir(url);
             PreparedStatement sentencia = connection.prepareStatement("INSERT INTO vehiculos (matricula, marca, modelo, anio, id_cliente) VALUES (?,?,?,?,?)")) {
            sentencia.setString(1, dato.getMatricula());
            sentencia.setString(2, dato.getMarca());
            sentencia.setString(3, dato.getModelo());
            sentencia.setInt(4, dato.getAnio());
            sentencia.setInt(5, dato.getIdCliente());
            return sentencia.executeUpdate();
        } catch (SQLException e) {
            return 0;
        }
    }

    public int actualizar(Vehiculo dato) {
        if (dato == null || dato.getMatricula() == null) return 0;
        try (Connection connection = abrir(url);
             PreparedStatement sentencia = connection.prepareStatement("UPDATE vehiculos SET marca=?, modelo=?, anio=?, id_cliente=? WHERE matricula=?")) {
            sentencia.setString(1, dato.getMarca());
            sentencia.setString(2, dato.getModelo());
            sentencia.setInt(3, dato.getAnio());
            sentencia.setInt(4, dato.getIdCliente());
            sentencia.setString(5, dato.getMatricula());
            return sentencia.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int eliminar(String id) {
        try (Connection connection = abrir(url);
             PreparedStatement sentencia = connection.prepareStatement("DELETE FROM vehiculos WHERE matricula=?")) {
            sentencia.setString(1, id);
            return sentencia.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Vehiculo> buscarPorCliente(Integer idCliente) {
        List<Vehiculo> vehiculosEncontrados = new ArrayList<>();
        try (Connection connection = abrir(url);
             PreparedStatement sentencia = connection.prepareStatement("SELECT matricula, marca, modelo, anio, id_cliente FROM vehiculos WHERE id_cliente=?")) {
            sentencia.setInt(1, idCliente);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                vehiculosEncontrados.add(new Vehiculo(
                        resultado.getString("matricula"),
                        resultado.getString("marca"),
                        resultado.getString("modelo"),
                        resultado.getInt("anio"),
                        resultado.getInt("id_cliente")));
            }
            return vehiculosEncontrados;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Vehiculo> listarPorMarca(String marca) {
        List<Vehiculo> vehiculosEncontrados = new ArrayList<>();
        try (Connection connection = abrir(url);
             PreparedStatement sentencia = connection.prepareStatement("SELECT matricula, marca, modelo, anio, id_cliente FROM vehiculos WHERE marca=?")) {
            sentencia.setString(1, marca);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                vehiculosEncontrados.add(new Vehiculo(
                        resultado.getString("matricula"),
                        resultado.getString("marca"),
                        resultado.getString("modelo"),
                        resultado.getInt("anio"),
                        resultado.getInt("id_cliente")));
            }
            return vehiculosEncontrados;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
