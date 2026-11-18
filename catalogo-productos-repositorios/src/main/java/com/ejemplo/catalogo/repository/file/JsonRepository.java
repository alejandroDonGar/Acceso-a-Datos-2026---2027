package com.ejemplo.catalogo.repository.file;

import com.ejemplo.catalogo.model.Producto;
import com.ejemplo.catalogo.repository.IProductoRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * Repositorio de productos guardados en un fichero JSON (una lista de objetos) con Jackson
 * @author AlejandroDonGar
 */
public class JsonRepository extends AbstractFileRepository<Producto, Long> implements IProductoRepository {
    //El mapper transforma el fichero en clase. Con 2 condiciones,
    //El fichero debe de ser JSON
    //Los parametros del fichero deben de ser los mismo que nuestra clase de record
    private final ObjectMapper mapper = new ObjectMapper();

    public JsonRepository(Path path) {
        super(path);
    }

    @Override
    protected void writeAll(List<Producto> productos) {
        Path temporal = null;
        try {
            // Escribimos primero en un fichero temporal y luego lo movemos encima del bueno:
            // si algo falla a mitad, el fichero original no se queda a medias
            Path destino = getPath().toAbsolutePath();
            Path directorio = destino.getParent();
            Files.createDirectories(directorio);
            temporal = Files.createTempFile(directorio, "productos-", ".json.tmp");
            mapper.writerWithDefaultPrettyPrinter().writeValue(temporal.toFile(), productos);
            try {
                Files.move(temporal, destino, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporal, destino, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo guardar " + getPath(), e);
        } finally {
            if (temporal != null) {
                try { Files.deleteIfExists(temporal); } catch (IOException ignored) { }
            }
        }
    }

    @Override
    protected List<Producto> readAll() {
        try {
            List<Producto> leidos = mapper.readValue(getPath().toFile(), new TypeReference<List<Producto>>() {});
            if (leidos == null) {
                throw new IllegalArgumentException("El JSON debe contener una lista, no null");
            }
            return leidos;
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo cargar " + getPath(), e);
        }
    }
}
