package es.ejemplo.taller.dao;

import es.ejemplo.taller.modelo.Mecanico;
import java.sql.*;
import java.util.*;
/**
 * @author AlejandroDonGar
 * MecanicoDao
 */
public class MecanicoDao extends AbstractDao<Mecanico>{

    public MecanicoDao(String url) {
        super(url);
    }
    
    public List<Mecanico> listar() {
        List<Mecanico> mecanicosEncontrados = new ArrayList<>();
        try (Connection connection = abrir(url);
            PreparedStatement sentencia = connection.prepareStatement("SELECT id_mecanico, nombre, especialidad, tarifa_hora FROM mecanicos")){
            ResultSet resultado = sentencia.executeQuery();
            while(resultado.next()) {
                mecanicosEncontrados.add(
                        new Mecanico(
                                resultado.getInt("id_mecanico"),
                                resultado.getString("nombre"),
                                resultado.getString("especialidad"),
                                resultado.getBigDecimal("tarifa_hora")
                        ));
            }
            return mecanicosEncontrados;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public Optional<Mecanico> buscarPorId(Integer id) {
        try (Connection connection = abrir(url);
             PreparedStatement sentencia = connection.prepareStatement("SELECT id_mecanico, nombre, especialidad, tarifa_hora FROM mecanicos WHERE id_mecanico=?")) {
            sentencia.setInt(1, id);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                return Optional.of(new Mecanico(
                    resultado.getInt("id_mecanico"),
                    resultado.getString("nombre"),
                    resultado.getString("especialidad"),
                    resultado.getBigDecimal("tarifa_hora")
                ));
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int insertar(Mecanico dato) {
        if (dato == null
            || dato.getNombre() == null || dato.getNombre().isEmpty()
            || dato.getEspecialidad() == null || dato.getEspecialidad().isEmpty()
            || dato.getTarifaHora() == null) {
            return 0;
        }
        try (Connection connection = abrir(url);
            PreparedStatement sentencia = connection.prepareStatement("INSERT INTO mecanicos (nombre, especialidad, tarifa_hora) VALUES (?,?,?)")) {
            sentencia.setString(1, dato.getNombre());
            sentencia.setString(2, dato.getEspecialidad());
            sentencia.setBigDecimal(3, dato.getTarifaHora());
            return sentencia.executeUpdate();
        } catch (SQLException e) {
            return 0;
        }
    }

    public int actualizar(Mecanico dato) {
        if (dato == null) return 0;
        try (Connection connection = abrir(url);
             PreparedStatement sentencia = connection.prepareStatement("UPDATE mecanicos SET nombre=?, especialidad=?, tarifa_hora=? WHERE id_mecanico=?")) {
            sentencia.setInt(4, dato.getIdMecanico());
            sentencia.setString(1, dato.getNombre());
            sentencia.setString(2, dato.getEspecialidad());
            sentencia.setBigDecimal(3, dato.getTarifaHora());
            return sentencia.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int eliminar(Integer id) {
        try (Connection connection = abrir(url);
            PreparedStatement sentencia = connection.prepareStatement("DELETE FROM mecanicos WHERE id_mecanico=?")) {
            sentencia.setInt(1, id);
            return sentencia.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public List<Mecanico> buscarPorEspecialidad(String especialidad) {
        List<Mecanico> mecanicosEncontradosPorEspecialidad = new ArrayList<>();
        try (Connection connection = abrir(url);
            PreparedStatement sentencia = connection.prepareStatement("SELECT id_mecanico, nombre, especialidad, tarifa_hora FROM mecanicos WHERE especialidad=?")){
            sentencia.setString(1, especialidad);
            ResultSet resultado = sentencia.executeQuery();
            while(resultado.next()) {
                mecanicosEncontradosPorEspecialidad.add(
                        new Mecanico(
                                resultado.getInt("id_mecanico"),
                                resultado.getString("nombre"),
                                resultado.getString("especialidad"),
                                resultado.getBigDecimal("tarifa_hora")
                        ));
            }
            return mecanicosEncontradosPorEspecialidad;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
