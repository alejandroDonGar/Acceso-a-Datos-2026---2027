package es.ejemplo.taller.dao;

import es.ejemplo.taller.modelo.Mecanico;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

/**
 * DAO de la tabla mecanicos (id autogenerado) con la busqueda por especialidad
 * @author AlejandroDonGar
 */
public class MecanicoDao extends AbstractDao<Mecanico, Integer> {

    public MecanicoDao(String url) {
        super(url, "mecanicos", "id_mecanico", "id_mecanico, nombre, especialidad, tarifa_hora");
    }

    @Override
    protected Mecanico mapear(ResultSet rs) throws SQLException {
        return new Mecanico(rs.getInt("id_mecanico"), rs.getString("nombre"),
                rs.getString("especialidad"), rs.getBigDecimal("tarifa_hora"));
    }

    public int insertar(Mecanico dato) {
        if (!esValido(dato)) return 0;
        return insertarDevolviendoId("INSERT INTO mecanicos (nombre, especialidad, tarifa_hora) VALUES (?,?,?)",
                dato.getNombre(), dato.getEspecialidad(), dato.getTarifaHora());
    }

    public int actualizar(Mecanico dato) {
        if (!esValido(dato) || dato.getIdMecanico() == null) return 0;
        return ejecutar("UPDATE mecanicos SET nombre=?, especialidad=?, tarifa_hora=? WHERE id_mecanico=?",
                dato.getNombre(), dato.getEspecialidad(), dato.getTarifaHora(), dato.getIdMecanico());
    }

    // Coincidencia exacta, sin comodines ni pasar a minusculas
    public List<Mecanico> buscarPorEspecialidad(String especialidad) {
        if (!texto(especialidad)) return new ArrayList<>();
        return consultarLista(select + " WHERE especialidad = ? ORDER BY id_mecanico", especialidad);
    }

    private boolean esValido(Mecanico dato) {
        return dato != null && texto(dato.getNombre()) && texto(dato.getEspecialidad())
                && dato.getTarifaHora() != null && dato.getTarifaHora().compareTo(BigDecimal.ZERO) >= 0;
    }
}
