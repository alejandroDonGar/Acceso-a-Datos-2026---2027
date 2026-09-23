package com.docencia.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que implementa los metodos de la interfaz ClienteRepository
 * @author AlejandroDonGar
 */
public class ClienteSqliteRepository implements ClienteRepository {
    private final String url;

    public ClienteSqliteRepository(String databasePath) {
        // La URL de SQLite tiene la forma jdbc:sqlite:RUTA_DEL_FICHERO.
        this.url = "jdbc:sqlite:" + databasePath;
        // Al crear el repositorio nos aseguramos de que la tabla existe.
        crearTablaSiNoExiste();
    }

    private Connection getConnection() throws SQLException {
        // Abre una conexion nueva con la base de datos. Quien la pida es responsable de cerrarla (try-with-resources).
        return DriverManager.getConnection(url);
    }

    private void crearTablaSiNoExiste() {
        // IF NOT EXISTS evita el error si la tabla ya estaba creada. dni es PRIMARY KEY: no puede repetirse.
        String sql = "CREATE TABLE IF NOT EXISTS cliente (dni TEXT PRIMARY KEY, nombre TEXT NOT NULL, email TEXT NOT NULL, ciudad TEXT NOT NULL)";
        // try-with-resources: lo que se abre entre parentesis se CIERRA solo al terminar el bloque, haya o no error.
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            // Statement sirve para SQL sin parametros; execute lo lanza contra la base de datos.
            statement.execute(sql);
        } catch (SQLException exception) {
            // Si no se puede crear la tabla el repositorio no puede funcionar: relanzamos como error de ejecucion.
            throw new RuntimeException("No se pudo crear la tabla cliente", exception);
        }
    }

    @Override
    public Boolean save(Cliente cliente) {
        // Los ? son PARAMETROS: se rellenan despues con setString. Es mas seguro que pegar los textos en el SQL
        // (evita la inyeccion SQL). El orden de los ? debe coincidir con el orden de las columnas de la tabla.
        try (Connection connection = getConnection();
            PreparedStatement sentencia = connection.prepareStatement("INSERT INTO cliente VALUES (?,?,?,?)")) {
            // Los numeros de setString empiezan en 1 (no en 0): ese es el ? que se rellena.
            sentencia.setString(1, cliente.getDni());
            sentencia.setString(2, cliente.getNombre());
            sentencia.setString(3, cliente.getEmail());
            sentencia.setString(4, cliente.getCiudad());
            // executeUpdate devuelve cuantas filas se han insertado: si es mas de 0, se guardo bien.
            return sentencia.executeUpdate() > 0;
        } catch (Exception e) {
            // Si el dni ya existe (PRIMARY KEY repetida) salta una excepcion: la capturamos y devolvemos false.
            System.err.println("No se han podido insertar los datos del cliente");
            return false;
        }
    }

    @Override
    public Cliente findByDni(String dni) {
        try (Connection connection = getConnection();
            PreparedStatement sentencia = connection.prepareStatement("SELECT * FROM cliente WHERE dni=?")) {
            // Rellenamos el unico ? con el dni buscado.
            sentencia.setString(1, dni);
            // executeQuery (para SELECT) devuelve un ResultSet: las filas encontradas. Lo abrimos tambien
            // dentro del try-with-resources para que se cierre solo.
            try (ResultSet resultado = sentencia.executeQuery()) {
                // next() avanza a la siguiente fila y dice si existe. Como el dni es unico, como mucho hay UNA fila,
                // por eso usamos if y no while.
                if (resultado.next()) {
                    // Leemos cada columna por su NOMBRE EXACTO en la tabla (ojo con escribirlo bien: nombre, email, ciudad).
                    String nombre = resultado.getString("nombre");
                    String email = resultado.getString("email");
                    String ciudad = resultado.getString("ciudad");
                    // Montamos el objeto Cliente con los datos leidos y lo devolvemos.
                    return new Cliente(dni, nombre, email, ciudad);
                }
            }
        } catch (Exception e) {
            System.err.println("No se han encontrado los datos del cliente");
        }
        // Si no habia fila (o hubo error) devolvemos null: "no existe ese cliente".
        return null;
    }

    @Override
    public List<Cliente> findAll() {
        // Lista donde iremos guardando los clientes leidos.
        List<Cliente> clientesEncontrados = new ArrayList<>();
        try (Connection connection = getConnection();
            PreparedStatement sentencia = connection.prepareStatement("SELECT * FROM cliente");
            ResultSet resultado = sentencia.executeQuery()) {
            // Aqui hay MUCHAS filas posibles, asi que usamos while: se repite mientras next() encuentre otra fila.
            while (resultado.next()) {
                // Leemos las 4 columnas de la fila actual, cada una con su nombre correcto.
                String dni = resultado.getString("dni");
                String nombre = resultado.getString("nombre");
                String email = resultado.getString("email");
                String ciudad = resultado.getString("ciudad");
                // Creamos el cliente y lo añadimos a la lista.
                // OJO: NO se hace return dentro del while, si no la funcion terminaria tras leer solo la primera fila.
                clientesEncontrados.add(new Cliente(dni, nombre, email, ciudad));
            }
        } catch (Exception e) {
            System.err.println("No se han encontrado los datos de los clientes");
            // Ante un error devolvemos una lista vacia, no null.
            return new ArrayList<>();
        }
        // Ya hemos recorrido todas las filas: devolvemos la lista completa (vacia si la tabla no tenia datos).
        return clientesEncontrados;
    }

    @Override
    public Boolean update(Cliente cliente) {
        // El WHERE dni=? es imprescindible: sin el, el UPDATE cambiaria TODOS los clientes.
        // Los ? van en este orden: nombre, email, ciudad y, por ultimo, el dni del WHERE.
        try (Connection connection = getConnection();
            PreparedStatement sentencia = connection.prepareStatement("UPDATE cliente SET nombre=?,email=?,ciudad=? WHERE dni=?")) {
            sentencia.setString(1, cliente.getNombre());
            sentencia.setString(2, cliente.getEmail());
            sentencia.setString(3, cliente.getCiudad());
            sentencia.setString(4, cliente.getDni());
            // executeUpdate devuelve cuantas filas cambio: si el dni no existe devuelve 0 y por tanto false.
            return sentencia.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("No se ha podido actualizar la informacion del usuario");
            return false;
        }
    }

    @Override
    public Boolean deleteByDni(String dni) {
        // Igual que en el update, el WHERE evita borrar toda la tabla.
        try (Connection connection = getConnection();
            PreparedStatement sentencia = connection.prepareStatement("DELETE FROM cliente WHERE dni=?")) {
            sentencia.setString(1, dni);
            // Filas borradas > 0 => existia y se borro; 0 => no existia ese dni.
            return sentencia.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("No se ha podido eliminar la informacion del cliente");
            return false;
        }
    }

    @Override
    public List<Cliente> findByCiudad(String ciudad) {
        // Lista de los clientes que vivan en esa ciudad.
        List<Cliente> clientesEncontradosPorCiudad = new ArrayList<>();
        try (Connection connection = getConnection();
            PreparedStatement sentencia = connection.prepareStatement("SELECT * FROM cliente WHERE ciudad=?")) {
            // Rellenamos el ? con la ciudad buscada.
            sentencia.setString(1, ciudad);
            try (ResultSet resultado = sentencia.executeQuery()) {
                // Puede haber varios clientes en la misma ciudad: while para recorrerlos todos.
                while (resultado.next()) {
                    String dni = resultado.getString("dni");
                    String nombre = resultado.getString("nombre");
                    String email = resultado.getString("email");
                    // La ciudad ya la conocemos (es el parametro), no hace falta leerla de la fila.
                    // Sin return dentro del while para que se lean TODAS las filas, no solo la primera.
                    clientesEncontradosPorCiudad.add(new Cliente(dni, nombre, email, ciudad));
                }
            }
        } catch (Exception e) {
            System.err.println("No se han encontrado los datos de los clientes");
            return new ArrayList<>();
        }
        // Devolvemos los encontrados (lista vacia si no habia ninguno).
        return clientesEncontradosPorCiudad;
    }
}
