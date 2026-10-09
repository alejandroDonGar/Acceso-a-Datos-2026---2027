package es.codelearnacademy.filelab.persona.json;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.codelearnacademy.filelab.model.Persona;
import es.codelearnacademy.filelab.model.Producto;
import es.codelearnacademy.filelab.persona.IPersonaRepository;
import es.codelearnacademy.filelab.repository.AbstractFileRepository;
import java.nio.file.*;
import java.io.*;
import java.util.*;
public class PersonaJsonRepository extends AbstractFileRepository<Persona,String> implements IPersonaRepository {

    ObjectMapper mapper;
    public PersonaJsonRepository(Path path) {
        super(path);
        this.mapper = new ObjectMapper();
    }
    @Override
    protected String getId(Persona p) {
        return p.dni();
    }
    @Override
    protected List<Persona> readAll() {
        try {
            return new ArrayList<>(mapper.readValue(path.toFile(), new TypeReference<List<Persona>>() {}));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void writeAll(List<Persona> personas) {
        try {
            Path parent = path.getParent();
            if (parent != null) Files.createDirectories(parent);
            mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), personas);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
