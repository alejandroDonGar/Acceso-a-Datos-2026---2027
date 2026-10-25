package es.ejemplo.clinica.dao;

import es.ejemplo.clinica.modelo.Mascota;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
/**
 * @author AlejandroDonGar
 * MascotaDao
 */
public final class MascotaDao extends AbstractDao implements IMascotaDao {
   public MascotaDao(String url) {
      super(url);
   }

   @Override
   public List<Mascota> listar() {
      List<Mascota> mascotasEncontradas = new ArrayList<>();
      try (Connection c = this.abrir();
            PreparedStatement st = c
                  .prepareStatement("SELECT id, nombre, especie, raza, fecha_nacimiento, id_cliente FROM mascotas")) {
         try (ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
               mascotasEncontradas.add(new Mascota(
                     rs.getInt("id"),
                     rs.getString("nombre"),
                     rs.getString("especie"),
                     rs.getString("raza"),
                     rs.getString("fecha_nacimiento"),
                     rs.getInt("id_cliente")));
            }
         }
      } catch (Exception e) {
         throw new DaoException(e.getMessage(), e.getCause());
      }
      return mascotasEncontradas;
   }

   @Override
   public Optional<Mascota> buscarPorId(Integer id) {
      if (id == null)
         return Optional.empty();

      try (Connection c = this.abrir();
            PreparedStatement st = c.prepareStatement(
                  "SELECT id, nombre, especie, raza, fecha_nacimiento, id_cliente FROM mascotas WHERE id = ?")) {
         st.setInt(1, id);
         try (ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
               return Optional.of(new Mascota(
                     id,
                     rs.getString("nombre"),
                     rs.getString("especie"),
                     rs.getString("raza"),
                     rs.getString("fecha_nacimiento"),
                     rs.getInt("id_cliente")));
            }
         }
         return Optional.empty();
      } catch (Exception e) {
         throw new DaoException(e.getMessage(), e.getCause());
      }
   }

   @Override
   public int insertar(Mascota entidad) {
      if (entidad == null ||
            entidad.getNombre() == null || entidad.getNombre().isEmpty() ||
            entidad.getEspecie() == null || entidad.getEspecie().isEmpty() ||
            entidad.getFechaNacimiento() == null || entidad.getFechaNacimiento().isEmpty() ||
            entidad.getIdCliente() == null) return 0;

      try (Connection c = this.abrir();
            PreparedStatement st = c.prepareStatement(
                  "INSERT INTO mascotas (nombre, especie, raza, fecha_nacimiento, id_cliente) VALUES (?,?,?,?,?) RETURNING id")) {
         st.setString(1, entidad.getNombre());
         st.setString(2, entidad.getEspecie());
         st.setString(3, entidad.getRaza());
         st.setString(4, entidad.getFechaNacimiento());
         st.setInt(5, entidad.getIdCliente());
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
   public int actualizar(Mascota entidad) {
      if (entidad == null || entidad.getId() == null ||
            entidad.getNombre() == null || entidad.getNombre().isEmpty() ||
            entidad.getEspecie() == null || entidad.getEspecie().isEmpty() ||
            entidad.getFechaNacimiento() == null || entidad.getFechaNacimiento().isEmpty() ||
            entidad.getIdCliente() == null) return 0;
      try (Connection c = this.abrir();
         PreparedStatement st = c.prepareStatement("UPDATE mascotas SET nombre=?, especie=?, raza=?, fecha_nacimiento=?, id_cliente=? WHERE id=?")) {
         st.setString(1, entidad.getNombre());
         st.setString(2, entidad.getEspecie());
         st.setString(3, entidad.getRaza());
         st.setString(4, entidad.getFechaNacimiento());
         st.setInt(5, entidad.getIdCliente());
         st.setInt(6, entidad.getId());
         return st.executeUpdate();
      } catch (Exception e) {
         throw new DaoException(e.getMessage(), e.getCause());
      }
   }

   @Override
   public int eliminar(Integer id) {
      if (id == null || id < 0) return 0;

      try (Connection c = this.abrir();
         PreparedStatement st = c.prepareStatement("DELETE FROM mascotas WHERE id=?")) {
         st.setInt(1, id);
         return st.executeUpdate();
      } catch (Exception e) {
         throw new DaoException(e.getMessage(), e.getCause());
      }
   }

   @Override
   public List<Mascota> buscarPorCliente(Integer idCliente) {
      if(idCliente == null || idCliente < 0) return new ArrayList<>();

      List<Mascota> mascotasEncontradasPorCliente = new ArrayList<>();
      try (Connection c = this.abrir();
         PreparedStatement st = c.prepareStatement("SELECT id, nombre, especie, raza, fecha_nacimiento, id_cliente FROM mascotas WHERE id_cliente=?")) {
         st.setInt(1, idCliente);
         try (ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
               mascotasEncontradasPorCliente.add(new Mascota(
                     rs.getInt("id"),
                     rs.getString("nombre"),
                     rs.getString("especie"),
                     rs.getString("raza"),
                     rs.getString("fecha_nacimiento"),
                     idCliente
               ));
            }
         }
      } catch (Exception e) {
         throw new DaoException(e.getMessage(), e.getCause());
      }
      return mascotasEncontradasPorCliente;
   }

   @Override
   public List<Mascota> listarPorEspecie(String especie) {
      if(especie == null || especie.isEmpty()) return new ArrayList<>();

      List<Mascota> mascotasEncontradasPorEspecie = new ArrayList<>();
      try (Connection c = this.abrir();
         PreparedStatement st = c.prepareStatement("SELECT id, nombre, especie, raza, fecha_nacimiento, id_cliente FROM mascotas WHERE especie=?")) {
         st.setString(1, especie);
         try (ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
               mascotasEncontradasPorEspecie.add(new Mascota(
                     rs.getInt("id"),
                     rs.getString("nombre"),
                     especie,
                     rs.getString("raza"),
                     rs.getString("fecha_nacimiento"),
                     rs.getInt("id_cliente")));
            }
         }
      } catch (Exception e) {
         throw new DaoException(e.getMessage(), e.getCause());
      }
      return mascotasEncontradasPorEspecie;
   }
}
