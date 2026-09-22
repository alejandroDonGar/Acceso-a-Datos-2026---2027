package com.docencia.sqlite;
/**
 * Clase que implementa los metodos de la interfaz ClienteRepository
 * @author AlejandroDonGar
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ClienteSqliteRepository implements ClienteRepository {
    private final String url;
    public ClienteSqliteRepository(String databasePath) {
        this.url = "jdbc:sqlite:" + databasePath;
        crearTablaSiNoExiste();
    }
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }
    private void crearTablaSiNoExiste() {
        String sql = "CREATE TABLE IF NOT EXISTS cliente (dni TEXT PRIMARY KEY, nombre TEXT NOT NULL, email TEXT NOT NULL, ciudad TEXT NOT NULL)";
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException exception) {
            throw new RuntimeException("No se pudo crear la tabla cliente", exception);
        }
    }
    @Override
    public Boolean save(Cliente cliente) {
        try (Connection connection = getConnection();
            PreparedStatement sentencia = connection.prepareStatement("INSERT INTO cliente VALUES (?,?,?,?)")){
            sentencia.setString(1, cliente.getDni());
            sentencia.setString(2, cliente.getNombre());
            sentencia.setString(3, cliente.getEmail());
            sentencia.setString(4, cliente.getCiudad());
            return sentencia.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("No se han podido insertar los datos del cliente");
            return false;
        }
    }
    @Override
    public Cliente findByDni(String dni) {
        Cliente cliente = null;
        try (Connection connection = getConnection();
            PreparedStatement sentencia = connection.prepareStatement("SELECT * FROM cliente WHERE dni=?")){
            sentencia.setString(1, dni);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                String nombre = resultado.getString("nombre");
                String correo = resultado.getString("correco");
                String ciudad = resultado.getString("ciudad");

                cliente = new Cliente(dni, nombre, correo, ciudad);
                return cliente;
                }
        } catch (Exception e) {
            System.err.println("No se han encontrado los datos del cliente");
            return null;
        }
        return cliente;
    }
    @Override
    public List<Cliente> findAll() {
        List<Cliente> clientesEncontrados = new ArrayList<Cliente>();
        try (Connection connection = getConnection();
            PreparedStatement sentencia = connection.prepareStatement("SELECT * FROM cliente")){
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                String dni = resultado.getString("dni");
                String nombre = resultado.getString("nombre");
                String email = resultado.getString("email");
                String ciudad = resultado.getString("email");
                Cliente cliente = new Cliente(dni, nombre, email, ciudad);
                clientesEncontrados.add(cliente);
                return clientesEncontrados;
            }
        } catch (Exception e) {
            System.err.println("No se han encoontrado los datos de los clientes");
            return new ArrayList<Cliente>();
        }
        return clientesEncontrados;
    }
    @Override
    public Boolean update(Cliente cliente) {
        try (Connection connection = getConnection();
            PreparedStatement sentencia = connection.prepareStatement("UPDATE cliente SET nombre=?,email=?,ciudad=? WHERE dni=?")){
            sentencia.setString(1, cliente.getNombre());
            sentencia.setString(2, cliente.getEmail());
            sentencia.setString(3, cliente.getCiudad());
            sentencia.setString(4, cliente.getDni());
            return sentencia.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("No se ha podido actualizar la informacion del usuario");
            return false;
        }
    }
    @Override
    public Boolean deleteByDni(String dni) {
        try (Connection connection = getConnection();
            PreparedStatement sentencia = connection.prepareStatement("DELETE FROM cliente WHERE dni=?")){
            sentencia.setString(1, dni);
            return sentencia.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("No se ha podido eliminar la informacion del cliente");
            return false;
        }
    }
    @Override
    public List<Cliente> findByCiudad(String ciudad) {
        List<Cliente> clientesEncontradosPorCiudad = new ArrayList<Cliente>();
        try (Connection connection = getConnection();
            PreparedStatement sentencia = connection.prepareStatement("SELECT * FROM cliente WHERE ciudad=?")){
            sentencia.setString(1, ciudad);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                String dni = resultado.getString("dni");
                String nombre = resultado.getString("nombre");
                String email = resultado.getString("email");
                Cliente cliente = new Cliente(dni, nombre, email, ciudad);
                clientesEncontradosPorCiudad.add(cliente);
                return clientesEncontradosPorCiudad;
            }
        } catch (Exception e) {
            System.err.println("No se han encontrado los datos de los clientes");
            return new ArrayList<Cliente>();
        }
        return clientesEncontradosPorCiudad;
    }
}
