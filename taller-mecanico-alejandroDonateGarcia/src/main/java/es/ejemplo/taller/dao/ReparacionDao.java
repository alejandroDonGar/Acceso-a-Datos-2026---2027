package es.ejemplo.taller.dao;

import es.ejemplo.taller.modelo.Reparacion;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

/**
 * DAO de la tabla reparaciones (id autogenerado) con las busquedas por vehiculo,
 * por mecanico y las reparaciones pendientes
 * @author AlejandroDonGar
 */
public class ReparacionDao extends AbstractDao<Reparacion, Integer> {
    private static final Set<String> ESTADOS = Set.of("PENDIENTE", "EN_PROCESO", "FINALIZADA");

    public ReparacionDao(String url) {
        super(url, "reparaciones", "id_reparacion",
                "id_reparacion, matricula, id_mecanico, descripcion, fecha, horas_trabajo, estado");
    }

    @Override
    protected Reparacion mapear(ResultSet rs) throws SQLException {
        return new Reparacion(rs.getInt("id_reparacion"), rs.getString("matricula"), rs.getInt("id_mecanico"),
                rs.getString("descripcion"), rs.getString("fecha"), rs.getBigDecimal("horas_trabajo"),
                rs.getString("estado"));
    }

    public int insertar(Reparacion dato) {
        if (!esValido(dato)) return 0;
        return insertarDevolviendoId("INSERT INTO reparaciones (matricula, id_mecanico, descripcion, fecha, horas_trabajo, estado) VALUES (?,?,?,?,?,?)",
                dato.getMatricula(), dato.getIdMecanico(), dato.getDescripcion(), dato.getFecha(),
                dato.getHorasTrabajo(), dato.getEstado());
    }

    public int actualizar(Reparacion dato) {
        if (!esValido(dato) || dato.getIdReparacion() == null) return 0;
        return ejecutar("UPDATE reparaciones SET matricula=?, id_mecanico=?, descripcion=?, fecha=?, horas_trabajo=?, estado=? WHERE id_reparacion=?",
                dato.getMatricula(), dato.getIdMecanico(), dato.getDescripcion(), dato.getFecha(),
                dato.getHorasTrabajo(), dato.getEstado(), dato.getIdReparacion());
    }

    public List<Reparacion> buscarPorVehiculo(String valor) {
        if (!texto(valor)) return new ArrayList<>();
        return consultarLista(select + " WHERE matricula = ? ORDER BY id_reparacion", valor);
    }

    public List<Reparacion> buscarPorMecanico(Integer valor) {
        if (valor == null) return new ArrayList<>();
        return consultarLista(select + " WHERE id_mecanico = ? ORDER BY id_reparacion", valor);
    }

    public List<Reparacion> listarPendientes() {
        return consultarLista(select + " WHERE estado = ? ORDER BY id_reparacion", "PENDIENTE");
    }

    // La fecha se valida en Java (formato AAAA-MM-DD y que exista); el esquema solo guarda texto
    private boolean esValido(Reparacion dato) {
        return dato != null && texto(dato.getMatricula()) && dato.getIdMecanico() != null
                && texto(dato.getDescripcion()) && fecha(dato.getFecha())
                && dato.getHorasTrabajo() != null && dato.getHorasTrabajo().compareTo(BigDecimal.ZERO) >= 0
                && ESTADOS.contains(dato.getEstado());
    }
}
