package es.ejemplo.taller.dao;

import es.ejemplo.taller.modelo.PiezaReparacion;
import java.sql.*;
import java.util.*;
import java.math.BigDecimal;
/**
 * @author AlejandroDonGar
 * PiezaReparacionDao
 */
public class PiezaReparacionDao extends AbstractDao<PiezaReparacion> {

    public PiezaReparacionDao(String url) {
        super(url);
    }

    public List<PiezaReparacion> listar() {
        List<PiezaReparacion> piezasEncontradas = new ArrayList<>();
        try (Connection connection = abrir(url);
             PreparedStatement sentencia = connection.prepareStatement("SELECT id_pieza, id_reparacion, nombre, cantidad, precio_unitario FROM piezas_reparacion")) {
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                piezasEncontradas.add(new PiezaReparacion(
                        resultado.getInt("id_pieza"),
                        resultado.getInt("id_reparacion"),
                        resultado.getString("nombre"),
                        resultado.getInt("cantidad"),
                        resultado.getBigDecimal("precio_unitario")));
            }
            return piezasEncontradas;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public Optional<PiezaReparacion> buscarPorId(Integer id) {
        try (Connection connection = abrir(url);
             PreparedStatement sentencia = connection.prepareStatement("SELECT id_pieza, id_reparacion, nombre, cantidad, precio_unitario FROM piezas_reparacion WHERE id_pieza=?")) {
            sentencia.setInt(1, id);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                return Optional.of(new PiezaReparacion(
                        resultado.getInt("id_pieza"),
                        resultado.getInt("id_reparacion"),
                        resultado.getString("nombre"),
                        resultado.getInt("cantidad"),
                        resultado.getBigDecimal("precio_unitario")));
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int insertar(PiezaReparacion dato) {
        if (dato == null
                || dato.getIdReparacion() == null
                || dato.getNombre() == null || dato.getNombre().isEmpty()
                || dato.getCantidad() == null || dato.getCantidad() <= 0
                || dato.getPrecioUnitario() == null) {
            return 0;
        }
        try (Connection connection = abrir(url);
             PreparedStatement sentencia = connection.prepareStatement("INSERT INTO piezas_reparacion (id_reparacion, nombre, cantidad, precio_unitario) VALUES (?,?,?,?)")) {
            sentencia.setInt(1, dato.getIdReparacion());
            sentencia.setString(2, dato.getNombre());
            sentencia.setInt(3, dato.getCantidad());
            sentencia.setBigDecimal(4, dato.getPrecioUnitario());
            return sentencia.executeUpdate();
        } catch (SQLException e) {
            return 0;
        }
    }

    public int actualizar(PiezaReparacion dato) {
        if (dato == null || dato.getIdPieza() == null) return 0;
        try (Connection connection = abrir(url);
             PreparedStatement sentencia = connection.prepareStatement("UPDATE piezas_reparacion SET id_reparacion=?, nombre=?, cantidad=?, precio_unitario=? WHERE id_pieza=?")) {
            sentencia.setInt(1, dato.getIdReparacion());
            sentencia.setString(2, dato.getNombre());
            sentencia.setInt(3, dato.getCantidad());
            sentencia.setBigDecimal(4, dato.getPrecioUnitario());
            sentencia.setInt(5, dato.getIdPieza());
            return sentencia.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int eliminar(Integer id) {
        try (Connection connection = abrir(url);
             PreparedStatement sentencia = connection.prepareStatement("DELETE FROM piezas_reparacion WHERE id_pieza=?")) {
            sentencia.setInt(1, id);
            return sentencia.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<PiezaReparacion> listarPorReparacion(Integer id) {
        List<PiezaReparacion> piezasEncontradas = new ArrayList<>();
        try (Connection connection = abrir(url);
             PreparedStatement sentencia = connection.prepareStatement("SELECT id_pieza, id_reparacion, nombre, cantidad, precio_unitario FROM piezas_reparacion WHERE id_reparacion=?")) {
            sentencia.setInt(1, id);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                piezasEncontradas.add(new PiezaReparacion(
                        resultado.getInt("id_pieza"),
                        resultado.getInt("id_reparacion"),
                        resultado.getString("nombre"),
                        resultado.getInt("cantidad"),
                        resultado.getBigDecimal("precio_unitario")));
            }
            return piezasEncontradas;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public BigDecimal calcularCostePiezas(Integer id) {
        try (Connection connection = abrir(url);
             PreparedStatement sentencia = connection.prepareStatement("SELECT SUM(cantidad * precio_unitario) AS total FROM piezas_reparacion WHERE id_reparacion=?")) {
            sentencia.setInt(1, id);
            ResultSet resultado = sentencia.executeQuery();
            // SUM sobre cero filas devuelve NULL
            BigDecimal total = resultado.next() ? resultado.getBigDecimal("total") : null;
            return total == null ? BigDecimal.ZERO : total;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
