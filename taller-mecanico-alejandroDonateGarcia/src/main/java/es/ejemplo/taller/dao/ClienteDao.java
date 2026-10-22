package es.ejemplo.taller.dao;
import es.ejemplo.taller.infra.ConexionBD;
import es.ejemplo.taller.modelo.Cliente;
import java.sql.*;
import java.util.*;
import java.math.BigDecimal;
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
        Cliente cliente;
        try (Connection connection = abrir(url);
             PreparedStatement sentencia = connection.prepareStatement("SELECT id_cliente, nombre, email, telefono FROM clientes WHERE id_cliente=?")) {
            sentencia.setInt(1, id);
            ResultSet resultado = sentencia.executeQuery();
            cliente = new Cliente(
                    resultado.getInt("id_cliente"),
                    resultado.getString("nombre"),
                    resultado.getString("email"),
                    resultado.getString("telefono")
            );
            return Optional.of(cliente);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public int insertar(Cliente dato) {
        if(dato==null|| dato.getIdCliente() == null || dato.getIdCliente() <= 0
        || dato.getNombre() == null || dato.getNombre().isEmpty()
        || dato.getEmail() == null || dato.getEmail().isEmpty()
        ||  dato.getTelefono() == null || dato.getTelefono().isEmpty()) {
            return 0;
        }
        try (Connection connection = abrir(url);
             PreparedStatement sentencia = connection.prepareStatement("INSERT INTO clientes VALUES (?,?,?,?)")) {
            sentencia.setInt(1, dato.getIdCliente());
            sentencia.setString(2, dato.getNombre());
            sentencia.setString(3, dato.getEmail());
            sentencia.setString(4, dato.getTelefono());
            return sentencia.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public int actualizar(Cliente dato) {
        if(dato==null||dato.getIdCliente() == null || dato.getIdCliente() <= 0
                || dato.getNombre() == null || dato.getNombre().isEmpty()
                || dato.getEmail() == null || dato.getEmail().isEmpty()
                ||  dato.getTelefono() == null || dato.getTelefono().isEmpty()) {
            return 0;
        }
        try (Connection connection = abrir(url);
             PreparedStatement sentencia = connection.prepareStatement("UPDATE clientes SET nombre=?, email=?, telefono=? WHERE id_cloeinte=?")) {
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
