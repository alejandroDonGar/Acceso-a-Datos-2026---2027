package es.ejemplo.cinefest.dao;

import es.ejemplo.cinefest.modelo.Valoracion;
import es.ejemplo.cinefest.orm.config.OrmFactory;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;


public class ValoracionDao {

    public ValoracionDao(String jdbcUrl) { 

     }
   
    // Usar transacciones y rollback en operaciones de escritura.

    public List<Valoracion> listar() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public Optional<Valoracion> buscarPorId(int id) {
        // TODO: implementar conforme al contrato del README.md.
        return Optional.empty();
    }
    public int insertar(Valoracion dato) {
        // TODO: implementar conforme al contrato del README.md.
        return 0;
    }
    public int actualizar(Valoracion dato) {
        // TODO: implementar conforme al contrato del README.md.
        return 0;
    }
    public int eliminar(int id) {
        // TODO: implementar conforme al contrato del README.md.
        return 0;
    }
    public List<Object[]> valoracionesConDetalles() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Object[]> peliculasSinValoraciones() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Object[]> valoracionesPorPelicula(int idPelicula) {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Object[]> estadisticasPuntuaciones() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    
}
