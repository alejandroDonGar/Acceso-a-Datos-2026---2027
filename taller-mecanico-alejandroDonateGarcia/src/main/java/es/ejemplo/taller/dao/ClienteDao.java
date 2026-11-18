package es.ejemplo.taller.dao;

import es.ejemplo.taller.modelo.Cliente;
import java.sql.*;

/**
 * DAO de la tabla clientes (id autogenerado). listar, buscarPorId y eliminar vienen de AbstractDao
 * @author AlejandroDonGar
 */
public class ClienteDao extends AbstractDao<Cliente, Integer> {

    public ClienteDao(String url) {
        super(url, "clientes", "id_cliente", "id_cliente, nombre, email, telefono");
    }

    @Override
    protected Cliente mapear(ResultSet rs) throws SQLException {
        return new Cliente(rs.getInt("id_cliente"), rs.getString("nombre"),
                rs.getString("email"), rs.getString("telefono"));
    }

    public int insertar(Cliente dato) {
        if (!esValido(dato)) return 0;
        return insertarDevolviendoId("INSERT INTO clientes (nombre, email, telefono) VALUES (?,?,?)",
                dato.getNombre(), dato.getEmail(), dato.getTelefono());
    }

    public int actualizar(Cliente dato) {
        if (!esValido(dato) || dato.getIdCliente() == null) return 0;
        return ejecutar("UPDATE clientes SET nombre=?, email=?, telefono=? WHERE id_cliente=?",
                dato.getNombre(), dato.getEmail(), dato.getTelefono(), dato.getIdCliente());
    }

    // Nombre y email obligatorios; el telefono puede ser null
    private boolean esValido(Cliente dato) {
        return dato != null && texto(dato.getNombre()) && texto(dato.getEmail());
    }
}
