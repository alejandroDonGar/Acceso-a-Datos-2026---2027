package es.codelearnacademy.filelab.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Infraestructura CRUD reutilizable para repositorios basados en ficheros.
 * No guarda los datos en memoria: cada operación lee el fichero en el momento,
 * así siempre trabaja con lo que hay realmente en disco.
 *
 * @author AlejandroDonGar
 */
public abstract class AbstractFileRepository<T, ID> implements IRepository<T, ID> {

    protected final Path path;

    public AbstractFileRepository(Path path) {
        this.path = path;
        // OJO: aquí NO se llama a findAll(). El constructor del padre se ejecuta antes
        // que el del hijo, y el hijo aún no ha creado su mapper/formato (valen null).
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract ID getId(T entity);
    protected abstract List<T> readAll() throws IOException;
    protected abstract void writeAll(List<T> entities) throws IOException;

    public Path getPath() {
        return path;
    }

    /** Lee todo el fichero; si falla la E/S devuelve lista vacía (no propaga excepciones). */
    @Override
    public List<T> findAll() {
        try {
            return readAll();
        } catch (Exception e) {
            return List.of();
        }
    }

    @Override
    public Optional<T> findById(ID id) {
        return findAll().stream()
                .filter(e -> getId(e).equals(id))
                .findFirst();
    }

    @Override
    public boolean create(T entity) {
        // Copia modificable: findAll() puede devolver List.of(), que es inmutable.
        List<T> lista = new ArrayList<>(findAll());
        if (lista.stream().anyMatch(e -> getId(e).equals(getId(entity)))) return false; // DNI repetido
        lista.add(entity);
        return guardar(lista);
    }

    @Override
    public boolean update(T entity) {
        List<T> lista = new ArrayList<>(findAll());
        for (int i = 0; i < lista.size(); i++) {          // necesitamos la posición para set()
            if (getId(lista.get(i)).equals(getId(entity))) {
                lista.set(i, entity);
                return guardar(lista);
            }
        }
        return false;
    }

    @Override
    public boolean delete(ID id) {
        List<T> lista = new ArrayList<>(findAll());
        // removeIf borra sin ConcurrentModificationException y dice si borró algo
        return lista.removeIf(e -> getId(e).equals(id)) && guardar(lista);
    }

    /** Escribe y traduce la IOException a false, como pide el contrato público. */
    private boolean guardar(List<T> lista) {
        try {
            writeAll(lista);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
