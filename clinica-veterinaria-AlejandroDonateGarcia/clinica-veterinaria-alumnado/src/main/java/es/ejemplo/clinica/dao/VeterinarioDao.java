package es.ejemplo.clinica.dao;

import es.ejemplo.clinica.modelo.Veterinario;
import java.sql.*;
import java.util.*;
/**
 * @author AlejandroDonGar
 * VeterinarioDao
 */
public final class VeterinarioDao extends AbstractDao implements IVeterinarioDao {
   public VeterinarioDao(String url) {
      super(url);
   }

   @Override
   public List<Veterinario> listar() {
      List<Veterinario> veterinariosEncontrados = new ArrayList<>();
      try (Connection c = this.abrir();
            PreparedStatement st = c
                  .prepareStatement("SELECT id, nombre, especialidad, tarifa_hora FROM veterinarios")) {
         try (ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
               veterinariosEncontrados.add(new Veterinario(
                     rs.getInt("id"),
                     rs.getString("nombre"),
                     rs.getString("especialidad"),
                     rs.getBigDecimal("tarifa_hora")));
            }
         }
      } catch (Exception e) {
         throw new DaoException(e.getMessage(), e.getCause());
      }
      return veterinariosEncontrados;
   }

   @Override
   public Optional<Veterinario> buscarPorId(Integer id) {
      if (id == null)
         return Optional.empty();

      try (Connection c = this.abrir();
            PreparedStatement st = c
                  .prepareStatement("SELECT id, nombre, especialidad, tarifa_hora FROM veterinarios WHERE id = ?")) {
         st.setInt(1, id);
         try (ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
               return Optional.of(new Veterinario(
                     id,
                     rs.getString("nombre"),
                     rs.getString("especialidad"),
                     rs.getBigDecimal("tarifa_hora")));
            }
         }
         return Optional.empty();
      } catch (Exception e) {
         throw new DaoException(e.getMessage(), e.getCause());
      }
   }

   @Override
   public int insertar(Veterinario entidad) {
      if (entidad == null ||
            entidad.getNombre() == null || entidad.getNombre().isEmpty() ||
            entidad.getEspecialidad() == null || entidad.getEspecialidad().isEmpty() ||
            entidad.getTarifaHora() == null) return 0;

      try (Connection c = this.abrir();
            PreparedStatement st = c.prepareStatement("INSERT INTO veterinarios (nombre, especialidad, tarifa_hora) VALUES (?,?,?)")) {
         st.setString(1, entidad.getNombre());
         st.setString(2, entidad.getEspecialidad());
         st.setBigDecimal(3, entidad.getTarifaHora());
         try (ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
               return rs.getInt("id");
            }
         }
      } catch (Exception e) {
         throw new DaoException(e.getMessage(), e.getCause());
      }
      return 0;
   }

   @Override
   public int actualizar(Veterinario entidad) {
      if (entidad == null ||
            entidad.getNombre() == null || entidad.getNombre().isEmpty() ||
            entidad.getEspecialidad() == null || entidad.getEspecialidad().isEmpty() ||
            entidad.getTarifaHora() == null) return 0;

      try (Connection c = this.abrir();
            PreparedStatement st = c.prepareStatement(
                  "UPDATE veterinarios SET nombre = ?, especialidad = ?, tarifa_hora = ? WHERE id = ?")) {
         st.setString(1, entidad.getNombre());
         st.setString(2, entidad.getEspecialidad());
         st.setBigDecimal(3, entidad.getTarifaHora());
         st.setInt(4, entidad.getId());
         return st.executeUpdate();
      } catch (Exception e) {
         throw new IllegalArgumentException(e.getMessage(), e.getCause());
      }
   }

   @Override
   public int eliminar(Integer id) {
      try (Connection c = this.abrir();
            PreparedStatement st = c.prepareStatement("DELETE FROM veterinarios WHERE id = ?")) {
         st.setInt(1, id);
         return st.executeUpdate();
      } catch (Exception e) {
         throw new NullPointerException();
      }
   }

   @Override
   public List<Veterinario> buscarPorEspecialidad(String especialidad) {
      if (especialidad == null || especialidad.isEmpty()) return new ArrayList<>();

      List<Veterinario> veterinariosEncontradosPorEspecialidad = new ArrayList<>();

      try (Connection c = this.abrir();
            PreparedStatement st = c
                  .prepareStatement("SELECT id, nombre, especialidad, tarifa_hora FROM veterinarios WHERE especialidad = ?")) {
         st.setString(1, especialidad);
         try (ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
               veterinariosEncontradosPorEspecialidad.add(new Veterinario(
                     rs.getInt("id"),
                     rs.getString("nombre"),
                     especialidad,
                     rs.getBigDecimal("tarifa_hora")));
            }
         }
      } catch (Exception e) {
         throw new DaoException(e.getMessage(), e.getCause());
      }
      return veterinariosEncontradosPorEspecialidad;
   }
}
