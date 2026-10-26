package es.ejemplo.clinica.dao;

import es.ejemplo.clinica.modelo.Consulta;
import java.sql.*;
import java.util.*;
/**
 * @author AlejandroDonGar
 *         ConsultaDao
 */
public final class ConsultaDao extends AbstractDao implements IConsultaDao {
   public ConsultaDao(String url) {
      super(url);
   }

   @Override
   public List<Consulta> listar() {
      List<Consulta> consultasEncontradas = new ArrayList<>();
      try (Connection c = this.abrir();
         PreparedStatement st = c.prepareStatement("SELECT id, id_mascota, id_veterinario, fecha, horas, estado FROM consultas")) {
            try (ResultSet rs = st.executeQuery()) {
               while (rs.next()) {
                  consultasEncontradas.add(new Consulta(
                     rs.getInt("id"),
                     rs.getInt("id_mascota"),
                     rs.getInt("id_veterinario"),
                     rs.getString("fecha"),
                     rs.getBigDecimal("horas"),
                     rs.getString("estado")
                  ));
               }
            }
      } catch (Exception e) {
         throw new DaoException(e.getMessage(), e);
      }
      return consultasEncontradas;
   }

   @Override
   public Optional<Consulta> buscarPorId(Integer id) {
      if (id == null) return Optional.empty();
      
      try (Connection c = this.abrir();
         PreparedStatement st = c.prepareStatement("SELECT id, id_mascota, id_veterinario, fecha, horas, estado FROM consultas WHERE id = ?")) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
               while (rs.next()) {
                  return Optional.of(new Consulta(
                     rs.getInt("id"),
                     rs.getInt("id_mascota"),
                     rs.getInt("id_veterinario"),
                     rs.getString("fecha"),
                     rs.getBigDecimal("horas"),
                     rs.getString("estado")
                  ));
               }
            }
      } catch (Exception e) {
         throw new DaoException(e.getMessage(), e);
      }
      return Optional.empty();
   }

   @Override
   public int insertar(Consulta entidad) {
      if (entidad == null ||
            entidad.getIdMascota() == null || entidad.getIdVeterinario() == null ||
            entidad.getFecha() == null || entidad.getFecha().isEmpty() ||
            entidad.getHoras() == null || entidad.getEstado() == null || entidad.getEstado().isEmpty()) return 0;

      try (Connection c = this.abrir();
            PreparedStatement st = c.prepareStatement("INSERT INTO consultas (id_mascota, id_veterinario, fecha, horas, estado) VALUES (?,?,?,?,?) RETURNING id")) {
         st.setInt(1, entidad.getIdMascota());
         st.setInt(2, entidad.getIdVeterinario());
         st.setString(3, entidad.getFecha());
         st.setBigDecimal(4, entidad.getHoras());
         st.setString(5, entidad.getEstado());
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
   public int actualizar(Consulta entidad) {
      if (entidad == null || entidad.getId() == null ||
            entidad.getIdMascota() == null || entidad.getIdVeterinario() == null ||
            entidad.getFecha() == null || entidad.getFecha().isEmpty() ||
            entidad.getHoras() == null || entidad.getEstado() == null || entidad.getEstado().isEmpty()) return 0;

      try (Connection c = this.abrir();
            PreparedStatement st = c.prepareStatement("UPDATE consultas SET id_mascota=?, id_veterinario=?, fecha=?, horas=?, estado=? WHERE id=?")) {
         st.setInt(1, entidad.getIdMascota());
         st.setInt(2, entidad.getIdVeterinario());
         st.setString(3, entidad.getFecha());
         st.setBigDecimal(4, entidad.getHoras());
         st.setString(5, entidad.getEstado());
         st.setInt(6, entidad.getId());
         return st.executeUpdate();
      } catch (Exception e) {
         throw new DaoException(e.getMessage(), e);
      }
   }

   @Override
   public int eliminar(Integer id) {
      if (id == null) return 0;

      try (Connection c = this.abrir();
            PreparedStatement st = c.prepareStatement("DELETE FROM consultas WHERE id=?")) {
         st.setInt(1, id);
         return st.executeUpdate();
      } catch (Exception e) {
         throw new DaoException(e.getMessage(), e);
      }
   }

   @Override
   public List<Consulta> buscarPorMascota(Integer idMascota) {
      if (idMascota == null) return new ArrayList<>();
      
      List<Consulta> consultasEncotradasPorIdMascota = new ArrayList<>();
      try (Connection c = this.abrir();
         PreparedStatement st = c.prepareStatement("SELECT id, id_mascota, id_veterinario, fecha, horas, estado FROM consultas WHERE id_mascota = ?")) {
            st.setInt(1, idMascota);
            try (ResultSet rs = st.executeQuery()) {
               while (rs.next()) {
                  consultasEncotradasPorIdMascota.add(new Consulta(
                     rs.getInt("id"),
                     idMascota,
                     rs.getInt("id_veterinario"),
                     rs.getString("fecha"),
                     rs.getBigDecimal("horas"),
                     rs.getString("estado")
                  ));
               }
            }
      } catch (Exception e) {
         throw new DaoException(e.getMessage(), e);
      }
      return consultasEncotradasPorIdMascota;
   }

   @Override
   public List<Consulta> buscarPorVeterinario(Integer idVeterinario) {
      if (idVeterinario == null) return new ArrayList<>();
      
      List<Consulta> consultasEncotradasPorIdVeterinario = new ArrayList<>();
      try (Connection c = this.abrir();
         PreparedStatement st = c.prepareStatement("SELECT id, id_mascota, id_veterinario, fecha, horas, estado FROM consultas WHERE id_veterinario = ?")) {
            st.setInt(1, idVeterinario);
            try (ResultSet rs = st.executeQuery()) {
               while (rs.next()) {
                  consultasEncotradasPorIdVeterinario.add(new Consulta(
                     rs.getInt("id"),
                     rs.getInt("id_mascota"),
                     idVeterinario,
                     rs.getString("fecha"),
                     rs.getBigDecimal("horas"),
                     rs.getString("estado")
                  ));
               }
            }
      } catch (Exception e) {
         throw new DaoException(e.getMessage(), e);
      }
      return consultasEncotradasPorIdVeterinario;
   }

   @Override
   public List<Consulta> listarProgramadas() {
      List<Consulta> consultasEncotradasPorEstadoProgramadas = new ArrayList<>();
      try (Connection c = this.abrir();
         PreparedStatement st = c.prepareStatement("SELECT id, id_mascota, id_veterinario, fecha, horas, estado FROM consultas WHERE estado = 'PROGRAMADA'")) {
            try (ResultSet rs = st.executeQuery()) {
               while (rs.next()) {
                  consultasEncotradasPorEstadoProgramadas.add(new Consulta(
                     rs.getInt("id"),
                     rs.getInt("id_mascota"),
                     rs.getInt("id_veterinario"),
                     rs.getString("fecha"),
                     rs.getBigDecimal("horas"),
                     rs.getString("estado")
                  ));
               }
            }
      } catch (Exception e) {
         throw new DaoException(e.getMessage(), e);
      }
      return consultasEncotradasPorEstadoProgramadas;
   }
}
