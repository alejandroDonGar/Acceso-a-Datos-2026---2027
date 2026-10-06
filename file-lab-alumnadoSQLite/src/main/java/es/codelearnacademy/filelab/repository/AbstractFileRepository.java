package es.codelearnacademy.filelab.repository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public abstract class AbstractFileRepository<T extends Identifiable<ID>, ID> implements IRepository<T, ID> {

    public Path path;
    protected List<T> list;

    public AbstractFileRepository(Path path) {
        if(path == null) {
            throw new IllegalArgumentException("El path no puede ser nulo");
        }
        this.path = path;
        if(Files.notExists(path)) {
            try {
                Files.createFile(path);
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<T> findAll() {
        return list;
    }

    @Override
    public Optional<T> findById(ID id) {
        return list.stream().filter(t -> t.equals(id)).findFirst();
    }

    @Override
    public boolean create(T entity) {
        if(entity == null || entity.id() == null) {
            throw new IllegalArgumentException("El entity no puede ser nulo");
        }
        if (list.stream().anyMatch(t -> t.equals(entity))) {
            throw new IllegalArgumentException("El entity existente");
        }
        return list.add(entity);
    }

    @Override
    public boolean update(T entity) {
        if(entity == null || entity.id() == null) {
            throw new IllegalArgumentException("El entity no puede ser nulo");
        }
        for(int i=0; i < list.size(); i++) {
            if(list.get(i).equals(entity.id())) {
                list.set(i, entity); writeAll(list);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(ID id) {
        boolean removed = list.removeIf(p -> p.id() == id);
        if (removed) {
            writeAll(list);
        }
        return removed;

    }

    protected abstract ID getId(T entity);
    protected abstract List<T> readAll();
    protected abstract void writeAll(List<T> entities);
}
