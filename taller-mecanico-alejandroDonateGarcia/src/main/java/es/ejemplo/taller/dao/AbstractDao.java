package es.ejemplo.taller.dao;

import es.ejemplo.taller.infra.ConexionBD;

public class AbstractDao<T> extends ConexionBD{

    public AbstractDao(String url) {
        super(url);
    }
}
