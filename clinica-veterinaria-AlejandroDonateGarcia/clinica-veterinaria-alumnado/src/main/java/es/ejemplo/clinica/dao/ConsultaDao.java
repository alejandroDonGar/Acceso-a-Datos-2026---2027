package es.ejemplo.clinica.dao;

import es.ejemplo.clinica.modelo.Consulta;
import java.sql.*;
import java.util.*;
/**
 * @author AlejandroDonGar
 * ConsultaDao
 */
public final class ConsultaDao extends AbstractDao implements IConsultaDao {
   public ConsultaDao(String url) {
      super(url);
   }

   @Override
   public List<Consulta> listar() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'listar'");
   }

   @Override
   public Optional<Consulta> buscarPorId(Integer id) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'buscarPorId'");
   }

 @Override
 public int insertar(Consulta entidad) {
   if (entidad == null ||
      entidad.getIdMascota() == null || entidad.getIdVeterinario() == null ||
      entidad.getFecha() == null || entidad.getFecha().isEmpty() ||
      entidad.getHoras() == null || entidad.getEstado() == null || entidad.getEstado().isEmpty()) return 0;
      
   try (Connection c = this.abrir();
      PreparedStatement st = c.prepareStatement("INSERTAR INTO consulta VALUES (?,?,?,?,?)")) {
         st.setInt(1, entidad.getIdMascota());
         st.setInt(2, entidad.getIdVeterinario());
         st.setString(3,entidad.getFecha());
         st.setBigDecimal(4, entidad.getHoras());
         st.setString(5, entidad.getEstado());
         return st.executeUpdate();
      
   } catch (Exception e) {
      throw new IllegalArgumentException("No se ha podido insertar la consulta", e);
   }
 }

   @Override
   public int actualizar(Consulta entidad) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'actualizar'");
   }

   @Override
   public int eliminar(Integer id) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'eliminar'");
   }

   @Override
   public List<Consulta> buscarPorMascota(Integer idMascota) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'buscarPorMascota'");
   }

   @Override
   public List<Consulta> buscarPorVeterinario(Integer idVeterinario) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'buscarPorVeterinario'");
   }

   @Override
   public List<Consulta> listarProgramadas() {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'listarProgramadas'");
   }

}
