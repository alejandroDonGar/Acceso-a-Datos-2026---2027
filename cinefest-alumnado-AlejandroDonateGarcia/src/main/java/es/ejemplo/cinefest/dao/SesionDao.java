package es.ejemplo.cinefest.dao;

import es.ejemplo.cinefest.modelo.Sesion;
import es.ejemplo.cinefest.orm.config.OrmFactory;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;

public class SesionDao  {

    public SesionDao(String jdbcUrl) {  }

    // Usar transacciones y rollback en operaciones de escritura.
    public List<Sesion> listar() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public Optional<Sesion> buscarPorId(int id) {
        // TODO: implementar conforme al contrato del README.md.
        return Optional.empty();
    }
    public int insertar(Sesion dato) {
        // TODO: implementar conforme al contrato del README.md.
        return 0;
    }
    public int actualizar(Sesion dato) {
        // TODO: implementar conforme al contrato del README.md.
        return 0;
    }
    public int eliminar(int id) {
        // TODO: implementar conforme al contrato del README.md.
        return 0;
    }
    public List<Object[]> sesionesConDetalles() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Object[]> sesionesPorFecha(String fecha) {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Object[]> sesionesSinVentas() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Object[]> disponibilidadSesiones() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Object[]> sesionesAgotadas() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Object[]> rankingRecaudacion() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Object[]> ingresosPorSede() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Object[]> informeDiario() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public int venderEntrada(int idSesion, int idEspectador, String fechaCompra) {
        // TODO: implementar conforme al contrato del README.md.
        return 0;
    }
   
}
