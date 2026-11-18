package es.codelearnacademy.filelab.repository;

import es.codelearnacademy.filelab.model.Vehiculo;

/**
 * Repositorio de vehículos: {@link IRepository} con {@code Vehiculo} e identificador
 * {@code String} (la matrícula).
 * @author AlejandroDonGar
 */
public interface IVehiculoRepository extends IRepository<Vehiculo, String> {
}
