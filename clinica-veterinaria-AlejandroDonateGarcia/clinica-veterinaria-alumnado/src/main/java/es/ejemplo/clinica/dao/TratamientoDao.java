package es.ejemplo.clinica.dao;

import es.ejemplo.clinica.modelo.Tratamiento;
import java.sql.*;
import java.util.*;
import java.math.BigDecimal;

/**
 * @author AlejandroDonGar
 * TratamientoDao
 */
public final class TratamientoDao extends AbstractDao implements ITratamientoDao {
   public TratamientoDao(String url) {
      super(url);
   }

   @Override
   public List<Tratamiento> listar() {
      List<Tratamiento> tratamientosEncontrados = new ArrayList<>();
      try (Connection c = this.abrir();
            PreparedStatement st = c.prepareStatement(
                  "SELECT id, id_consulta, descripcion, cantidad, precio_unitario FROM tratamientos")) {
         try (ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
               tratamientosEncontrados.add(new Tratamiento(
                     rs.getInt("id"),
                     rs.getInt("id_consulta"),
                     rs.getString("descripcion"),
                     rs.getInt("cantidad"),
                     rs.getBigDecimal("precio_unitario")));
            }
         }
      } catch (Exception e) {
         throw new DaoException(e.getMessage(), e);
      }
      return tratamientosEncontrados;
   }

   @Override
   public Optional<Tratamiento> buscarPorId(Integer id) {
      if (id == null)
         return Optional.empty();

      try (Connection c = this.abrir();
            PreparedStatement st = c.prepareStatement(
                  "SELECT id, id_consulta, descripcion, cantidad, precio_unitario FROM tratamientos WHERE id = ?")) {
         st.setInt(1, id);
         try (ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
               return Optional.of(new Tratamiento(
                     id,
                     rs.getInt("id_consulta"),
                     rs.getString("descripcion"),
                     rs.getInt("cantidad"),
                     rs.getBigDecimal("precio_unitario")));
            }
         }
      } catch (Exception e) {
         throw new DaoException(e.getMessage(), e);
      }
      return Optional.empty();
   }

   @Override
   public int insertar(Tratamiento entidad) {
      if (entidad == null ||
            entidad.getIdConsulta() == null ||
            entidad.getDescripcion() == null || entidad.getDescripcion().isEmpty() ||
            entidad.getCantidad() == null || entidad.getCantidad() < 0 ||
            entidad.getPrecioUnitario() == null)
         return 0;

      try (Connection c = this.abrir();
            PreparedStatement st = c.prepareStatement(
                  "INSERT INTO tratamientos (id_consulta, descripcion, cantidad, precio_unitario) VALUES (?,?,?,?) RETURNING id")) {
         st.setInt(1, entidad.getIdConsulta());
         st.setString(2, entidad.getDescripcion());
         st.setInt(3, entidad.getCantidad());
         st.setBigDecimal(4, entidad.getPrecioUnitario());
         try (ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
               return rs.getInt("id");
            }
         }
      } catch (Exception e) {
         throw new DaoException(e.getMessage(), e);
      }
      return 0;
   }

   @Override
   public int actualizar(Tratamiento entidad) {
      if (entidad == null ||
            entidad.getId() == null || entidad.getIdConsulta() == null ||
            entidad.getDescripcion() == null || entidad.getDescripcion().isEmpty() ||
            entidad.getCantidad() == null || entidad.getCantidad() < 0 ||
            entidad.getPrecioUnitario() == null)
         return 0;

      try (Connection c = this.abrir();
            PreparedStatement st = c.prepareStatement(
                  "UPDATE tratamientos SET id_consulta=?, descripcion=?, cantidad=?, precio_unitario=? WHERE id=?")) {
         st.setInt(1, entidad.getIdConsulta());
         st.setString(2, entidad.getDescripcion());
         st.setInt(3, entidad.getCantidad());
         st.setBigDecimal(4, entidad.getPrecioUnitario());
         st.setInt(5, entidad.getId());
         return st.executeUpdate();
      } catch (Exception e) {
         throw new DaoException(e.getMessage(), e);
      }
   }

   @Override
   public int eliminar(Integer id) {
      if (id == null)
         return 0;

      try (Connection c = this.abrir();
            PreparedStatement st = c.prepareStatement("DELETE FROM tratamientos WHERE id=?")) {
         st.setInt(1, id);
         return st.executeUpdate();
      } catch (Exception e) {
         throw new DaoException(e.getMessage(), e);
      }
   }

   @Override
   public List<Tratamiento> listarPorConsulta(Integer idConsulta) {
      if (idConsulta == null)
         return new ArrayList<>();

      List<Tratamiento> tratamientosEncontradosPorIdConsulta = new ArrayList<>();
      try (Connection c = this.abrir();
            PreparedStatement st = c.prepareStatement(
                  "SELECT id, id_consulta, descripcion, cantidad, precio_unitario FROM tratamientos WHERE id_consulta=?")) {
         st.setInt(1, idConsulta);
         try (ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
               tratamientosEncontradosPorIdConsulta.add(new Tratamiento(
                     rs.getInt("id"),
                     idConsulta,
                     rs.getString("descripcion"),
                     rs.getInt("cantidad"),
                     rs.getBigDecimal("precio_unitario")));
            }
         }
      } catch (Exception e) {
         throw new DaoException(e.getMessage(), e);
      }
      return tratamientosEncontradosPorIdConsulta;
   }

   @Override
   public BigDecimal calcularCosteTratamientos(Integer idConsulta) {
      if (idConsulta == null)
         return BigDecimal.ZERO;

      try (Connection c = this.abrir();
            PreparedStatement st = c.prepareStatement("SELECT COALESCE(SUM(cantidad * precio_unitario), 0) FROM tratamientos WHERE id_consulta = ?")) {
         st.setInt(1, idConsulta);
         try (ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
               return rs.getBigDecimal(1);
            }
         }
      } catch (SQLException e) {
         throw new DaoException(e.getMessage(), e);
      }
      return BigDecimal.ZERO;
   }
}
