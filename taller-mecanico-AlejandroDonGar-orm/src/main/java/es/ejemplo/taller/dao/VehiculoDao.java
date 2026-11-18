package es.ejemplo.taller.dao;
import es.ejemplo.taller.modelo.Vehiculo;
import es.ejemplo.taller.orm.entity.VehiculoEntity;
import es.ejemplo.taller.orm.mapper.VehiculoMapper;
import java.util.*;

/**
 * DAO de vehiculos con JPA. La clave es la matricula (String) y la aporta el objeto,
 * por eso insertar devuelve 1 y no un id generado
 * @author AlejandroDonGar
 */
public class VehiculoDao extends AbstractDao<Vehiculo, VehiculoEntity, String> {

    public VehiculoDao(String url) {
        super(url, VehiculoEntity.class, VehiculoMapper::toDomain,
                "select v from VehiculoEntity v order by v.marca");
    }

    public int insertar(Vehiculo vehiculo) {
        if (!esValido(vehiculo)) return 0;
        return escribir("insertar", em -> {
            em.persist(VehiculoMapper.toEntity(vehiculo));
            em.flush(); // Si la matricula ya existe, el error salta aqui y escribir() hace rollback
            return 1;
        });
    }

    public int actualizar(Vehiculo vehiculo) {
        if (!esValido(vehiculo)) return 0;
        return escribir("actualizar", em -> {
            VehiculoEntity v = em.find(VehiculoEntity.class, vehiculo.getMatricula());
            if (v == null) return 0;
            v.setMarca(vehiculo.getMarca());
            v.setModelo(vehiculo.getModelo());
            v.setAnio(vehiculo.getAnio());
            // Tambien puede cambiar de propietario: se copia la FK plana id_cliente
            v.setIdCliente(vehiculo.getIdCliente());
            return 1;
        });
    }

    public List<Vehiculo> buscarPorCliente(Integer idCliente) {
        if (idCliente == null) return new ArrayList<>();
        return consultar("select v from VehiculoEntity v where v.idCliente = :idCliente", Map.of("idCliente", idCliente));
    }

    // Coincidencia exacta, sin comodines ni pasar a minusculas
    public List<Vehiculo> listarPorMarca(String marca) {
        if (!texto(marca)) return new ArrayList<>();
        return consultar("select v from VehiculoEntity v where v.marca = :marca", Map.of("marca", marca));
    }

    private boolean esValido(Vehiculo v) {
        return v != null && texto(v.getMatricula()) && texto(v.getMarca()) && texto(v.getModelo())
                && v.getAnio() != null && v.getIdCliente() != null;
    }
}
