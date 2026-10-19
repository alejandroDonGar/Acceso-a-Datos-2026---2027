package es.ejemplo.gestion.dao;

import es.ejemplo.gestion.config.ConexionBD;
import es.ejemplo.gestion.exception.AplicationException;
import es.ejemplo.gestion.model.Vehiculo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author AlejandroDonGar
 * VehiculoDao: CRUD y consultas sobre la tabla Vehiculos.
 * A diferencia de ProductoDao, no hereda de ConexionBD: la guarda en un atributo (composición),
 * que es como venía la plantilla. Las dos formas funcionan igual.
 */
public class VehiculoDao extends AbstractDao<Vehiculo>{

    public VehiculoDao(String url) {
        super(url);
    }

    @Override
    Vehiculo mapperToClass(ResultSet rs) {
        try {
            return new Vehiculo(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4));
        } catch (SQLException e) {
            throw new AplicationException(e.getMessage(), e);
        }
    }

    public List<Vehiculo> listar() {
        String query = "SELECT matricula, marca, modelo, anio FROM Vehiculos ORDER BY matricula";
        return consultar(query);
    }

    public Optional<Vehiculo> buscarPorMatricula(String matricula) {
        try (Connection connection = this.obtenerConexion();
            PreparedStatement sentencia = connection.prepareStatement("SELECT matricula, marca, modelo, anio FROM Vehiculos WHERE matricula=?")) {
            sentencia.setString(1, matricula);
            ResultSet resultado = sentencia.executeQuery();
            return resultado.next() ? Optional.of(vehiculoMapper(resultado)) : Optional.empty();
        } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e);
        }
    }

    public boolean insertar(Vehiculo vehiculo) {
        try (Connection connection = this.obtenerConexion();
            PreparedStatement sentencia = connection.prepareStatement("INSERT INTO Vehiculos (matricula, marca, modelo, anio) VALUES (?,?,?,?)")) {
            sentencia.setString(1, vehiculo.getMatricula());
            sentencia.setString(2, vehiculo.getMarca());
            sentencia.setString(3, vehiculo.getModelo());
            sentencia.setInt(4, vehiculo.getAnio());
            return sentencia.executeUpdate() > 0;
        } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e);
        }
    }

    public boolean actualizar(Vehiculo vehiculo) {
        try (Connection connection = this.obtenerConexion();
            PreparedStatement sentencia = connection.prepareStatement("UPDATE Vehiculos SET marca=?, modelo=?, anio=? WHERE matricula=?")) {
            sentencia.setString(1, vehiculo.getMarca());
            sentencia.setString(2, vehiculo.getModelo());
            sentencia.setInt(3, vehiculo.getAnio());
            sentencia.setString(4, vehiculo.getMatricula());
            return sentencia.executeUpdate() > 0;
        } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e);
        }
    }

    public boolean eliminar(String matricula) {
        try (Connection connection = this.obtenerConexion();
            PreparedStatement sentencia = connection.prepareStatement("DELETE FROM Vehiculos WHERE matricula=?")) {
            sentencia.setString(1, matricula);
            return sentencia.executeUpdate() > 0;
        } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e);
        }
    }

    public List<Vehiculo> buscarPorMarca(String texto) {
        List<Vehiculo> lista = new ArrayList<>();
        try (Connection connection = this.obtenerConexion();
            PreparedStatement sentencia = connection.prepareStatement("SELECT matricula, marca, modelo, anio FROM Vehiculos WHERE LOWER(marca) = LOWER(?) ORDER BY matricula")) {
            sentencia.setString(1, texto);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                lista.add(vehiculoMapper(resultado));
            }
        } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e);
        }
        return lista;
    }

    public List<Vehiculo> buscarPorAnio(int desde, int hasta) {
        List<Vehiculo> lista = new ArrayList<>();
        try (Connection connection = this.obtenerConexion();
            PreparedStatement sentencia = connection.prepareStatement("SELECT matricula, marca, modelo, anio FROM Vehiculos WHERE anio BETWEEN ? AND ? ORDER BY anio, matricula")) {
            sentencia.setInt(1, desde);
            sentencia.setInt(2, hasta);
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                lista.add(vehiculoMapper(resultado));
            }
        } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e);
        }
        return lista;
    }

    public int contar() {
        try (Connection connection = this.obtenerConexion();
            PreparedStatement sentencia = connection.prepareStatement("SELECT COUNT(*) FROM Vehiculos")) {
            ResultSet resultado = sentencia.executeQuery();
            return resultado.next() ? resultado.getInt(1) : 0;
        } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e);
        }
    }

    public Map<String, Integer> contarPorMarca() {
        Map<String, Integer> conteo = new LinkedHashMap<>();
        try (Connection connection = this.obtenerConexion();
            PreparedStatement sentencia = connection.prepareStatement("SELECT marca, COUNT(*) FROM Vehiculos GROUP BY marca ORDER BY marca")) {
            ResultSet resultado = sentencia.executeQuery();
            while (resultado.next()) {
                conteo.put(resultado.getString(1), resultado.getInt(2));
            }
        } catch (Exception e) {
            throw new AplicationException(e.getMessage(), e);
        }
        return conteo;
    }

    private Vehiculo vehiculoMapper(ResultSet rs) {
        return null;
    }
}
