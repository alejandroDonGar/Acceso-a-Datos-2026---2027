package es.ejemplo.taller.dao;

import es.ejemplo.taller.modelo.Reparacion;
import java.sql.*;
import java.util.*;
/**
 * @author AlejandroDonGar
 * ReparacionDao
 */
public class ReparacionDao extends AbstractDao<Reparacion> {

    public ReparacionDao(String url) {
        super(url);
    }

    public List<Reparacion> listar() {
        List<Reparacion> reparacionesEncontradas = new ArrayList<>();
        try (Connection connection = abrir(url);
             PreparedStatement sentencia = connection.prepareStatement("SELECT id_reparacion, matricula, id_mecanico, descripcion, fecha, horas_trabajo, estado FROM reparaciones")) {
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                reparacionesEncontradas.add(new Reparacion(
                        resultado.getInt("id_reparacion"),
                        resultado.getString("matricula"),
                        resultado.getInt("id_mecanico"),
                        resultado.getString("descripcion"),
                        resultado.getString("fecha"),
                        resultado.getBigDecimal("horas_trabajo"),
                        resultado.getString("estado")));
            }
            return reparacionesEncontradas;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Reparacion> buscarPorId(Integer id) {
        try (Connection connection = abrir(url);
             PreparedStatement sentencia = connection.prepareStatement("SELECT id_reparacion, matricula, id_mecanico, descripcion, fecha, horas_trabajo, estado FROM reparaciones WHERE id_reparacion=?")) {
            sentencia.setInt(1, id);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                return Optional.of(new Reparacion(
                        resultado.getInt("id_reparacion"),
                        resultado.getString("matricula"),
                        resultado.getInt("id_mecanico"),
                        resultado.getString("descripcion"),
                        resultado.getString("fecha"),
                        resultado.getBigDecimal("horas_trabajo"),
                        resultado.getString("estado")));
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int insertar(Reparacion dato) {
        if (dato == null
                || dato.getMatricula() == null || dato.getMatricula().isEmpty()
                || dato.getIdMecanico() == null
                || dato.getDescripcion() == null || dato.getDescripcion().isEmpty()
                || dato.getFecha() == null || dato.getFecha().isEmpty()
                || dato.getHorasTrabajo() == null
                || dato.getEstado() == null) {
            return 0;
        }
        try (Connection connection = abrir(url);
             PreparedStatement sentencia = connection.prepareStatement("INSERT INTO reparaciones (matricula, id_mecanico, descripcion, fecha, horas_trabajo, estado) VALUES (?,?,?,?,?,?)")) {
            sentencia.setString(1, dato.getMatricula());
            sentencia.setInt(2, dato.getIdMecanico());
            sentencia.setString(3, dato.getDescripcion());
            sentencia.setString(4, dato.getFecha());
            sentencia.setBigDecimal(5, dato.getHorasTrabajo());
            sentencia.setString(6, dato.getEstado());
            return sentencia.executeUpdate();
        } catch (SQLException e) {
            return 0;
        }
    }

    public int actualizar(Reparacion dato) {
        if (dato == null || dato.getIdReparacion() == null) return 0;
        try (Connection connection = abrir(url);
             PreparedStatement sentencia = connection.prepareStatement("UPDATE reparaciones SET matricula=?, id_mecanico=?, descripcion=?, fecha=?, horas_trabajo=?, estado=? WHERE id_reparacion=?")) {
            sentencia.setString(1, dato.getMatricula());
            sentencia.setInt(2, dato.getIdMecanico());
            sentencia.setString(3, dato.getDescripcion());
            sentencia.setString(4, dato.getFecha());
            sentencia.setBigDecimal(5, dato.getHorasTrabajo());
            sentencia.setString(6, dato.getEstado());
            sentencia.setInt(7, dato.getIdReparacion());
            return sentencia.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int eliminar(Integer id) {
        try (Connection connection = abrir(url);
             PreparedStatement sentencia = connection.prepareStatement("DELETE FROM reparaciones WHERE id_reparacion=?")) {
            sentencia.setInt(1, id);
            return sentencia.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Reparacion> buscarPorVehiculo(String valor) {
        List<Reparacion> reparacionesEncontradas = new ArrayList<>();
        try (Connection connection = abrir(url);
             PreparedStatement sentencia = connection.prepareStatement("SELECT id_reparacion, matricula, id_mecanico, descripcion, fecha, horas_trabajo, estado FROM reparaciones WHERE matricula=?")) {
            sentencia.setString(1, valor);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                reparacionesEncontradas.add(new Reparacion(
                        resultado.getInt("id_reparacion"),
                        resultado.getString("matricula"),
                        resultado.getInt("id_mecanico"),
                        resultado.getString("descripcion"),
                        resultado.getString("fecha"),
                        resultado.getBigDecimal("horas_trabajo"),
                        resultado.getString("estado")));
            }
            return reparacionesEncontradas;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Reparacion> buscarPorMecanico(Integer valor) {
        List<Reparacion> reparacionesEncontradas = new ArrayList<>();
        try (Connection connection = abrir(url);
             PreparedStatement sentencia = connection.prepareStatement("SELECT id_reparacion, matricula, id_mecanico, descripcion, fecha, horas_trabajo, estado FROM reparaciones WHERE id_mecanico=?")) {
            sentencia.setInt(1, valor);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                reparacionesEncontradas.add(new Reparacion(
                        resultado.getInt("id_reparacion"),
                        resultado.getString("matricula"),
                        resultado.getInt("id_mecanico"),
                        resultado.getString("descripcion"),
                        resultado.getString("fecha"),
                        resultado.getBigDecimal("horas_trabajo"),
                        resultado.getString("estado")));
            }
            return reparacionesEncontradas;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Reparacion> listarPendientes() {
        List<Reparacion> reparacionesEncontradas = new ArrayList<>();
        try (Connection connection = abrir(url);
             PreparedStatement sentencia = connection.prepareStatement("SELECT id_reparacion, matricula, id_mecanico, descripcion, fecha, horas_trabajo, estado FROM reparaciones WHERE estado='PENDIENTE'")) {
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                reparacionesEncontradas.add(new Reparacion(
                        resultado.getInt("id_reparacion"),
                        resultado.getString("matricula"),
                        resultado.getInt("id_mecanico"),
                        resultado.getString("descripcion"),
                        resultado.getString("fecha"),
                        resultado.getBigDecimal("horas_trabajo"),
                        resultado.getString("estado")));
            }
            return reparacionesEncontradas;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
