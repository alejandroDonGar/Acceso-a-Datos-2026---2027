package com.ejemplo.catalogo.repository.file;

import com.ejemplo.catalogo.model.Identifiable;
import com.ejemplo.catalogo.repository.IRepository;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio generico sobre un fichero: tiene la logica CRUD comun en memoria
 * y deja a cada hija (CSV, JSON, XML) solo leer y escribir el fichero con readAll()/writeAll()
 * @param <T> tipo de la entidad, que debe tener id (Identifiable)
 * @param <ID> tipo del identificador
 * @author AlejandroDonGar
 */
public abstract class AbstractFileRepository <T extends Identifiable<ID>, ID> implements IRepository<T, ID> {
    private final Path path;
    private List<T> list;

    protected abstract List<T> readAll();
    protected abstract void writeAll(List<T> elements);

    public AbstractFileRepository(Path path) {
        if (path == null) {
            throw new IllegalArgumentException("El path es null");
        }
        this.path = path;
        if (Files.notExists(path)) {
            try {
                // Si la carpeta tampoco existe la creamos antes que el fichero
                if (path.toAbsolutePath().getParent() != null) {
                    Files.createDirectories(path.toAbsolutePath().getParent());
                }
                Files.createFile(path);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    public Path getPath() {
        return path;
    }

    // La lista se carga la primera vez que se usa y no en el constructor:
    // cuando se ejecuta el constructor de esta clase, los atributos de la hija
    // (el CSVFormat, el mapper...) todavia no existen y readAll() fallaria
    private List<T> lista() {
        if (list == null) {
            try {
                // Un fichero recien creado esta vacio: no hay nada que leer
                list = Files.size(path) == 0 ? new ArrayList<>() : new ArrayList<>(readAll());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        return list;
    }

    @Override
    public List<T> findAll() {
        // Devolvemos una copia para que nadie cambie nuestra lista desde fuera
        return new ArrayList<>(lista());
    }

    @Override
    public Optional<T> findById(ID id)  {
        if (id == null) {
            return Optional.empty();
        }
        // equals y no == : con Long, == compara objetos y falla a partir de 127
        return lista().stream().filter(p -> p.id().equals(id)).findFirst();
    }

    @Override
    public boolean create(T element) {
        if (element == null || element.id() == null || findById(element.id()).isPresent()) {
            return false;
        }
        lista().add(element);
        writeAll(lista());
        return true;
    }

    @Override
    public boolean update(T element) {
        if (element == null || element.id() == null) {
            return false;
        }
        for (int i = 0; i < lista().size(); i++) {
            if (lista().get(i).id().equals(element.id())) {
                lista().set(i, element);
                writeAll(lista());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(ID id) {
        if (id == null) {
            return false;
        }
        boolean removed = lista().removeIf(p -> p.id().equals(id));
        if (removed) {
            writeAll(lista());
        }
        return removed;
    }

}
