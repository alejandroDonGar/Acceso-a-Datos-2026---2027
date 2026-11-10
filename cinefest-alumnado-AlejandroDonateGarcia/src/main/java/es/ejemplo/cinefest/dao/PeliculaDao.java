package es.ejemplo.cinefest.dao;

import es.ejemplo.cinefest.modelo.Pelicula;
import es.ejemplo.cinefest.orm.config.OrmFactory;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;


public class PeliculaDao {

    public PeliculaDao(String jdbcUrl) {  }

    // Usar transacciones y rollback en operaciones de escritura.
    public List<Pelicula> listar() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public Optional<Pelicula> buscarPorId(int id) {
        // TODO: implementar conforme al contrato del README.md.
        return Optional.empty();
    }
    public int insertar(Pelicula dato) {
        // TODO: implementar conforme al contrato del README.md.
        return 0;
    }
    public int actualizar(Pelicula dato) {
        // TODO: implementar conforme al contrato del README.md.
        return 0;
    }
    public int eliminar(int id) {
        // TODO: implementar conforme al contrato del README.md.
        return 0;
    }
    public List<Pelicula> buscarPorGenero(String genero) {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Pelicula> peliculasDesdeAnio(int anio) {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Pelicula> peliculasSinSesiones() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Object[]> peliculasConMediaValoraciones() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Object[]> mejorValoradas(int minimo) {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Object[]> masVendidasQueMedia() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Object[]> estadisticasGenero() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }

}
