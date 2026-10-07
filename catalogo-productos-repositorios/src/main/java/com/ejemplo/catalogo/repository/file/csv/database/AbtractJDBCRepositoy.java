package com.ejemplo.catalogo.repository.file.csv.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbtractJDBCRepositoy extends DatabaseInitializer implements IRepository<T, ID>{

    public AbtractJDBCRepositoy(String url) {
        super(url);
    }

    @Override
    public List<Producto> findAll2() {
        List<Producto> productos = new ArrayList<>();

        try (Connection coneccion = DriverManager.getConnection(url);
             PreparedStatement sentencia = coneccion.prepareStatement("SELECT id, nombre, precio FROM producto ORDER BY id");
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {
                productos.add(new Producto(
                        resultado.getLong("id"),
                        resultado.getString("nombre"),
                        resultado.getDouble("precio")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return productos;
    }
    @Override
    public Optional<Producto> findById2(Long id) {
        List<Producto> productos = new ArrayList<>();

        try (Connection coneccion = DriverManager.getConnection(url);
             PreparedStatement sentencia = coneccion.prepareStatement("SELECT id, nombre, precio FROM producto WHERE id = ?")) {

            sentencia.setLong(1, producto.id());
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                productos.add(new Producto(
                        resultado.getLong("id"),
                        resultado.getString("nombre"),
                        resultado.getDouble("precio")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return productos.stream().findFirst();
    }
    @Override
    public boolean create2(Producto producto) {

        try (Connection coneccion = DriverManager.getConnection(url);
             PreparedStatement sentencia = connection.prepareStatement("INSERT INTO producto(id, nombre, precio) VALUES (?, ?, ?)")) {
            sentencia.setLong(1, producto.id());
            sentencia.setString(2, producto.nombre());
            sentencia.setDouble(3, producto.precio());
            return sentencia.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public boolean update2(Producto producto) {

        try (Connection coneccion = DriverManager.getConnection(url);
             PreparedStatement sentencia = coneccion.prepareStatement("UPDATE producto SET nombre = ?, precio = ? WHERE id = ?")) {
            sentencia.setString(1, producto.nombre());
            sentencia.setDouble(2, producto.precio());
            sentencia.setLong(3, producto.id());
            return sentencia.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public boolean delete2(Producto producto) {

        try (Connection coneccion = DriverManager.getConnection(url);
             PreparedStatement sentencia = coneccion.prepareStatement("DELETE FROM producto WHERE id = ?")) {
            sentencia.setLong(1, producto.id());
            return sentencia.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Optional<T> findById3(String sentencia) {
        List<T> list = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement sentencia2 = connection.prepareStatement(sentencia)) {

            ResultSet resultado = sentencia2.executeQuery();

            while (resultado.next()) {
                list.add(new Producto(
                        resultado.getLong("id"),
                        resultado.getString("nombre"),
                        resultado.getDouble("precio")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return productos.stream().findFirst();
    }
}
