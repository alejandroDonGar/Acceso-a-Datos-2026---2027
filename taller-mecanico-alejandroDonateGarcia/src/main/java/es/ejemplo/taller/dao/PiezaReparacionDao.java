package es.ejemplo.taller.dao;

import es.ejemplo.taller.modelo.PiezaReparacion;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

/**
 * DAO de la tabla piezas_reparacion (id autogenerado) con las piezas de una reparacion
 * y el calculo de su coste
 * @author AlejandroDonGar
 */
public class PiezaReparacionDao extends AbstractDao<PiezaReparacion, Integer> {

    public PiezaReparacionDao(String url) {
        super(url, "piezas_reparacion", "id_pieza", "id_pieza, id_reparacion, nombre, cantidad, precio_unitario");
    }

    @Override
    protected PiezaReparacion mapear(ResultSet rs) throws SQLException {
        return new PiezaReparacion(rs.getInt("id_pieza"), rs.getInt("id_reparacion"), rs.getString("nombre"),
                rs.getInt("cantidad"), rs.getBigDecimal("precio_unitario"));
    }

    public int insertar(PiezaReparacion dato) {
        if (!esValido(dato)) return 0;
        return insertarDevolviendoId("INSERT INTO piezas_reparacion (id_reparacion, nombre, cantidad, precio_unitario) VALUES (?,?,?,?)",
                dato.getIdReparacion(), dato.getNombre(), dato.getCantidad(), dato.getPrecioUnitario());
    }

    public int actualizar(PiezaReparacion dato) {
        if (!esValido(dato) || dato.getIdPieza() == null) return 0;
        return ejecutar("UPDATE piezas_reparacion SET id_reparacion=?, nombre=?, cantidad=?, precio_unitario=? WHERE id_pieza=?",
                dato.getIdReparacion(), dato.getNombre(), dato.getCantidad(), dato.getPrecioUnitario(), dato.getIdPieza());
    }

    public List<PiezaReparacion> listarPorReparacion(Integer id) {
        if (id == null) return new ArrayList<>();
        return consultarLista(select + " WHERE id_reparacion = ? ORDER BY id_pieza", id);
    }

    // La suma se hace en Java con BigDecimal: en SQL, cantidad * precio se calcularia con double
    // ponytail: si falla la consulta tambien devuelve 0, igual que una reparacion sin piezas
    public BigDecimal calcularCostePiezas(Integer id) {
        BigDecimal total = BigDecimal.ZERO;
        for (PiezaReparacion p : listarPorReparacion(id)) {
            total = total.add(p.getPrecioUnitario().multiply(BigDecimal.valueOf(p.getCantidad())));
        }
        return total;
    }

    private boolean esValido(PiezaReparacion dato) {
        return dato != null && dato.getIdReparacion() != null && texto(dato.getNombre())
                && dato.getCantidad() != null && dato.getCantidad() > 0
                && dato.getPrecioUnitario() != null && dato.getPrecioUnitario().compareTo(BigDecimal.ZERO) >= 0;
    }
}
