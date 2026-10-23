package es.ejemplo.taller.dao;

import es.ejemplo.taller.modelo.Cliente;
import java.sql.*;
import java.util.*;
/**
 * @author AlejandroDonGar
 * ClienteDao
 */
public class ClienteDao extends AbstractDao<Cliente> {

    public ClienteDao(String url) {
        super(url);
    }
    
    public List<Cliente> listar() {
        List<Cliente> clientesEncontrados = new ArrayList<>();
        try (Connection connection = abrir(url);
            PreparedStatement sentencia = connection.prepareStatement("SELECT id_cliente, nombre, email, telefono FROM clientes")){
            ResultSet resultado = sentencia.executeQuery();
            while(resultado.next()) {
                clientesEncontrados.add(
                        new Cliente(
                                resultado.getInt("id_cliente"),
                                resultado.getString("nombre"),
                                resultado.getString("email"),
                                resultado.getString("telefono")));
            }
            return clientesEncontrados;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public Optional<Cliente> buscarPorId(Integer id) {
        try (Connection connection = abrir(url);
             PreparedStatement sentencia = connection.prepareStatement("SELECT id_cliente, nombre, email, telefono FROM clientes WHERE id_cliente=?")) {
            sentencia.setInt(1, id);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                return Optional.of(new Cliente(
                    resultado.getInt("id_cliente"),
                    resultado.getString("nombre"),
                    resultado.getString("email"),
                    resultado.getString("telefono")
                ));
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int insertar(Cliente dato) {
        if (dato == null
            || dato.getNombre() == null || dato.getNombre().isEmpty()
            || dato.getEmail() == null || dato.getEmail().isEmpty()
            || dato.getTelefono() == null || dato.getTelefono().isEmpty()) {
            return 0;
        }
        try (Connection connection = abrir(url);
            PreparedStatement sentencia = connection.prepareStatement("INSERT INTO clientes (nombre, email, telefono) VALUES (?,?,?)")) {
            sentencia.setString(1, dato.getNombre());
            sentencia.setString(2, dato.getEmail());
            sentencia.setString(3, dato.getTelefono());
            return sentencia.executeUpdate();
        } catch (SQLException e) {
            return 0;
        }
    }

    public int actualizar(Cliente dato) {
        try (Connection connection = abrir(url);
             PreparedStatement sentencia = connection.prepareStatement("UPDATE clientes SET nombre=?, email=?, telefono=? WHERE id_cliente=?")) {
            sentencia.setInt(4, dato.getIdCliente());
            sentencia.setString(1, dato.getNombre());
            sentencia.setString(2, dato.getEmail());
            sentencia.setString(3, dato.getTelefono());
            return sentencia.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int eliminar(Integer id) {
        try (Connection connection = abrir(url);
            PreparedStatement sentencia = connection.prepareStatement("DELETE FROM clientes WHERE id_cliente = ?")) {
            sentencia.setInt(1, id);
            return sentencia.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
