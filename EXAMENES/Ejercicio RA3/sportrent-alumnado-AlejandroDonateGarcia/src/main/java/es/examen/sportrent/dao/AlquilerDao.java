package es.examen.sportrent.dao;
import es.examen.sportrent.entity.ClienteEntity;
import es.examen.sportrent.infra.OrmFactory;
import es.examen.sportrent.mapper.AlquilerMapper;
import es.examen.sportrent.mapper.ClienteMapper;
import es.examen.sportrent.model.Alquiler;
import es.examen.sportrent.model.EquipoAlquileres;
import es.examen.sportrent.model.EquipoIngresos;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class AlquilerDao {
    private final EntityManagerFactory managerFactory;

    public AlquilerDao() {
        String url = "jdbc:sqlite:datos/sportrent.db";
        managerFactory = OrmFactory.crear(url);
    }

    public List<Alquiler> listarTodos() {
        throw new UnsupportedOperationException("No implementado");
    }


    public Alquiler buscarPorId(Long id) {
        throw new UnsupportedOperationException("No implementado");

    }

    public boolean insertar(Alquiler alquiler) {
        throw new UnsupportedOperationException("No implementado");

    }

    public boolean actualizar(Alquiler alquiler) {
        throw new UnsupportedOperationException("No implementado");

    }

    public boolean eliminar(Long id) {
        throw new UnsupportedOperationException("No implementado");

    }
   
    public List<Alquiler> pendientesDevolucion() {
        throw new UnsupportedOperationException("No implementado");
    }

    public List<EquipoAlquileres> equiposAlquiladosDosVeces() {
        throw new UnsupportedOperationException("No implementado");
    }

    public List<EquipoIngresos> tresEquiposMasIngresos() {
        throw new UnsupportedOperationException("No implementado");
    }
}
