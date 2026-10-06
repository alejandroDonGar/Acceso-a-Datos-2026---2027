package es.codelearnacademy.filelab.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.codelearnacademy.filelab.model.Producto;
import es.codelearnacademy.filelab.repository.AbstractFileRepository;
import es.codelearnacademy.filelab.repository.IProductoRepository;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ProductoJsonRepository extends AbstractFileRepository<Producto, Long> implements IProductoRepository {

    private final ObjectMapper mapper;

    public ProductoJsonRepository(Path path) {
        super(path);
        list = readAll();
        mapper = new ObjectMapper();
    }


    @Override
    protected Long getId(Producto producto) {
        throw new UnsupportedOperationException("Función no implementada");
    }

    @Override
    protected List<Producto> readAll() {
        throw new UnsupportedOperationException("Función no implementada");
    }

    @Override
    protected void writeAll(List<Producto> productos) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), productos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
