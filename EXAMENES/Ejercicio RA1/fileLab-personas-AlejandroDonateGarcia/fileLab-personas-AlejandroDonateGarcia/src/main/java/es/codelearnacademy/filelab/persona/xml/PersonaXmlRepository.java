package es.codelearnacademy.filelab.persona.xml;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import es.codelearnacademy.filelab.demo.crud.XmlCrudDemo;
import es.codelearnacademy.filelab.model.Persona;
import es.codelearnacademy.filelab.persona.IPersonaRepository;
import es.codelearnacademy.filelab.repository.AbstractFileRepository;
import java.nio.file.*;
import java.io.*;
import java.util.*;
public class PersonaXmlRepository extends AbstractFileRepository<Persona,String> implements IPersonaRepository {

    XmlMapper xmlMapper;

    public PersonaXmlRepository(Path path) {
        super(path);
        xmlMapper = new XmlMapper();
    }
    @Override
    protected String getId(Persona p) {
        return p.dni();
    }
    @Override
    protected List<Persona> readAll() {
        try {
            return xmlMapper.readValue(path.toFile(), DocumentoPersonas.class).getPersonas();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void writeAll(List<Persona> personas) {
        try {
            xmlMapper.writeValue(path.toFile(), new DocumentoPersonas(personas));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
