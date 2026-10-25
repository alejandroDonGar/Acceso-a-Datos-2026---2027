package es.ejemplo.clinica.infra;

import java.sql.*;
/**
 * @author AlejandroDonGar
 * ConexionDB
 */
public class ConexionDB {

    protected final String url;

    public ConexionDB(String url) {
        this.url =  url;
    }

    protected Connection abrir() throws SQLException {
        Connection c = DriverManager.getConnection(url);
        try (Statement st = c.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON");
        }
        return c;
    }
}
