package es.ejemplo.clinica.dao;

import es.ejemplo.clinica.modelo.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 * @author AlejandroDonGar
 * ClienteDao
 */
public final class ClienteDao extends AbstractDao implements IClienteDao {
   public ClienteDao(String url) {
      super(url);
   }

   @Override
   public List<Cliente> listar() {
      List<Cliente> clientesEncontrados = new ArrayList<>();
      try (Connection c = this.abrir();
         PreparedStatement st = c.prepareStatement("SELECT id, nombre, email, telefono FROM clientes")) {
         try (ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
               clientesEncontrados.add(new Cliente(
                  rs.getInt("id"),
                  rs.getString("nombre"),
                  rs.getString("email"),
                  rs.getString("telefono")
               ));
            }
         }
      } catch (Exception e) {
         throw new DaoException(e.getMessage(), e.getCause());
      }
      return clientesEncontrados;
   }

   @Override
   public Optional<Cliente> buscarPorId(Integer id) {
      if (id == null) return Optional.empty();

      try (Connection c = this.abrir();
         PreparedStatement st = c.prepareStatement("SELECT id, nombre, email, telefono FROM clientes WHERE id = ?")) {
         st.setInt(1, id);
         try (ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
               return Optional.of(new Cliente(
                  id,
                  rs.getString("nombre"),
                  rs.getString("email"),
                  rs.getString("telefono")
               ));
            }
         }
         return Optional.empty();
      } catch (Exception e) {
         throw new DaoException(e.getMessage(), e.getCause());
      }
   }

   @Override
   public int insertar(Cliente entidad) {
      if (entidad == null ||
         entidad.getNombre() == null || entidad.getNombre().isEmpty() ||
         entidad.getEmail() == null || entidad.getEmail().isEmpty() ||
         entidad.getTelefono() == null || entidad.getTelefono().isEmpty())  return 0;
      try (Connection c = this.abrir();
         PreparedStatement st = c.prepareStatement("INSERT INTO clientes (nombre, email, telefono) VALUES (?,?,?) RETURNING id")) {
         st.setString(1, entidad.getNombre());
         st.setString(2, entidad.getEmail());
         st.setString(3, entidad.getTelefono());
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
   public int actualizar(Cliente entidad) {
      if (entidad == null ||
         entidad.getNombre() == null || entidad.getNombre().isEmpty() ||
         entidad.getEmail() == null || entidad.getEmail().isEmpty() ||
         entidad.getTelefono() == null || entidad.getTelefono().isEmpty()) return 0;
      try (Connection c = this.abrir();
         PreparedStatement st = c.prepareStatement("UPDATE clientes SET nombre = ?, email = ?, telefono = ? WHERE id = ?")) {
         st.setString(1, entidad.getNombre());
         st.setString(2, entidad.getEmail());
         st.setString(3, entidad.getTelefono());
         st.setInt(4, entidad.getId());
         return st.executeUpdate();
      } catch (Exception e) {
         throw new IllegalArgumentException(e.getMessage(), e.getCause());
      }
   }

   @Override
   public int eliminar(Integer id) {

      try (Connection c = this.abrir();
         PreparedStatement st = c.prepareStatement("DELETE FROM clientes WHERE id = ?")) {
         st.setInt(1, id);
         return st.executeUpdate();
      } catch (Exception e) {
         throw new NullPointerException();
      }
   }
}
