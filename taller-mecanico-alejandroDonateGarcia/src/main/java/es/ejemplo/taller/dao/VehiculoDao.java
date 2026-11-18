package es.ejemplo.taller.dao;

import es.ejemplo.taller.modelo.Vehiculo;
import java.sql.*;
import java.util.*;

/**
 * DAO de la tabla vehiculos. Su clave es la matricula (String) y la aporta el objeto,
 * por eso insertar devuelve 1 y no un id generado
 * @author AlejandroDonGar
 */
public class VehiculoDao extends AbstractDao<Vehiculo, String> {

    public VehiculoDao(String url) {
        super(url, "vehiculos", "matricula", "matricula, marca, modelo, anio, id_cliente");
    }

    @Override
    protected Vehiculo mapear(ResultSet rs) throws SQLException {
        return new Vehiculo(rs.getString("matricula"), rs.getString("marca"), rs.getString("modelo"),
                rs.getInt("anio"), rs.getInt("id_cliente"));
    }

    public int insertar(Vehiculo dato) {
        if (!esValido(dato)) return 0;
        return ejecutar("INSERT INTO vehiculos (matricula, marca, modelo, anio, id_cliente) VALUES (?,?,?,?,?)",
                dato.getMatricula(), dato.getMarca(), dato.getModelo(), dato.getAnio(), dato.getIdCliente());
    }

    public int actualizar(Vehiculo dato) {
        if (!esValido(dato)) return 0;
        return ejecutar("UPDATE vehiculos SET marca=?, modelo=?, anio=?, id_cliente=? WHERE matricula=?",
                dato.getMarca(), dato.getModelo(), dato.getAnio(), dato.getIdCliente(), dato.getMatricula());
    }

    public List<Vehiculo> buscarPorCliente(Integer idCliente) {
        if (idCliente == null) return new ArrayList<>();
        return consultarLista(select + " WHERE id_cliente = ? ORDER BY matricula", idCliente);
    }

    // Coincidencia exacta, sin comodines ni pasar a minusculas
    public List<Vehiculo> listarPorMarca(String marca) {
        if (!texto(marca)) return new ArrayList<>();
        return consultarLista(select + " WHERE marca = ? ORDER BY matricula", marca);
    }

    private boolean esValido(Vehiculo dato) {
        return dato != null && texto(dato.getMatricula()) && texto(dato.getMarca()) && texto(dato.getModelo())
                && dato.getAnio() != null && dato.getIdCliente() != null;
    }
}
