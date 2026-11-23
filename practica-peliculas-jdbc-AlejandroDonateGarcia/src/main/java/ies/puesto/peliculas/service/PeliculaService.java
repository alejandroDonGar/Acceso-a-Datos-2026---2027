package ies.puesto.peliculas.service;

import ies.puesto.peliculas.model.Pelicula;
import ies.puesto.peliculas.repository.PeliculaRepository;
import ies.puesto.peliculas.service.mapper.PeliculaDominioMapper;
import ies.puesto.peliculas.service.modelo.DatosCreacionPelicula;
import ies.puesto.peliculas.service.modelo.DatosEdicionPelicula;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Casos de uso del catálogo. No lanza excepciones: el resultado se indica con
 * {@code Optional} (vacío = no existe o código repetido) o {@code boolean}.
 * No conoce nada de la web (ni {@code ResponseEntity} ni {@code controller.api}).
 *
 * @author AlejandroDonGar
 */
@Service
public class PeliculaService {

    private final PeliculaRepository repository;
    private final PeliculaDominioMapper mapper;

    public PeliculaService(PeliculaRepository repository, PeliculaDominioMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<Pelicula> listar() {
        return repository.buscarTodas();
    }

    public Optional<Pelicula> obtener(String codigo) {
        return repository.buscarPorCodigo(codigo);
    }

    public Optional<Pelicula> crear(DatosCreacionPelicula datos) {
        Pelicula pelicula = mapper.aPelicula(datos);
        if (repository.insertarSiAusente(pelicula)) {
            return Optional.of(pelicula);
        }
        return Optional.empty(); // el código ya existía
    }

    public Optional<Pelicula> actualizar(String codigo, DatosEdicionPelicula datos) {
        Pelicula pelicula = mapper.aPelicula(codigo, datos);
        if (repository.actualizarSiExiste(pelicula)) {
            return Optional.of(pelicula);
        }
        return Optional.empty(); // no existe
    }

    public boolean eliminar(String codigo) {
        return repository.eliminar(codigo);
    }
}
