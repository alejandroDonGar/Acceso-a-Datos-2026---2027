package com.docencia.sqlite;
/**
 * Clase que implementa los metodos de la interfaz ClienteDbService
 * @author AlejandroDonGar
 */
import java.util.List;
public class ClienteDbServiceImpl implements ClienteDbService {
    private final ClienteRepository repository;
    public ClienteDbServiceImpl(ClienteRepository repository) {
        this.repository = repository;
    }
    @Override
    public Boolean create(Cliente cliente) {
        return repository.save(cliente);
    }
    @Override
    public Cliente findByDni(String dni) {
        return repository.findByDni(dni);
    }
    @Override
    public List<Cliente> findAll() {
        return repository.findAll();
    }
    @Override
    public Boolean update(Cliente cliente) {
        return repository.update(cliente);
    }
    @Override
    public Boolean deleteByDni(String dni) {
        return repository.deleteByDni(dni);
    }
    @Override
    public List<Cliente> findByCiudad(String ciudad) {
        return repository.findByCiudad(ciudad);
    }
}
