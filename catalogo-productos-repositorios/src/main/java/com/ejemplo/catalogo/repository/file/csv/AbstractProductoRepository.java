package com.ejemplo.catalogo.repository.file.csv;

import com.ejemplo.catalogo.model.Producto;

import java.nio.file.Path;

public abstract class AbstractProductoRepository extends AbstractFileRepository<Producto, Long> implements IProductoRepository {
    public AbstractProductoRepository(Path path) {
        super(path);
    }
}
