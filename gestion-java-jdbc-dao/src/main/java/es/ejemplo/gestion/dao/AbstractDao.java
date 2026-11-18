package es.ejemplo.gestion.dao;

import es.ejemplo.gestion.config.ConexionBD;
import es.ejemplo.gestion.exception.AplicationException;
import es.ejemplo.gestion.model.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO generico: hereda la conexion de ConexionBD y tiene consultar(query), que recorre el ResultSet
 * y convierte cada fila con mapperToClass, el metodo que implementa cada DAO hijo
 * @author AlejandroDonGar
 */
public abstract class AbstractDao<T> extends ConexionBD {

    public AbstractDao(String url) {
        super(url);
    }

    abstract T mapperToClass(ResultSet rs);

    public List<T> consultar(String query) {
        List<T> lista = new ArrayList<>();
        try (Connection connection = this.obtenerConexion();
             PreparedStatement sentencia = connection.prepareStatement(query)) {
            ResultSet rs = sentencia.executeQuery();
            while(rs.next()) {
                lista.add(mapperToClass(rs));
            }
        } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e.getCause());
        }
        return lista;
    }
}
