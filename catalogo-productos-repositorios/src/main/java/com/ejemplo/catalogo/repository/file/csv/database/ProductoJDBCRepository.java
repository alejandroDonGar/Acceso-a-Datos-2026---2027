package com.ejemplo.catalogo.repository.file.csv.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoJDBCRepository extends AbtractJDBCRepositoy <Producto, Long> {
    public ProductoJDBCRepository(String url) {
        super(url);
    }
    @Override
    public List<Producto> findAll() {
        return findAll2();
    }
    @Override
    public Optional<Producto> findById(Long id) {
        Producto producto = new Producto(id, null, 0);
        return findById2(id);
    }
    @Override
    public boolean create(Producto producto) {
        return create2(producto);
    }
    @Override
    public boolean update(Producto entity) {
        int modificadas = update2(entity);
        if(modificadas > 1) {
            return false;
        }
        return true;
    }
    @Override
    public boolean delete(Long id) {
        Producto producto = new Producto(id, null, 0);
        int modificadas = update2(entity);
        if(modificadas > 1) {
            return false;
        }
        return true;
    }
}
