package es.codelearnacademy.filelab.persona.csv;
import es.codelearnacademy.filelab.model.Persona;
import es.codelearnacademy.filelab.persona.IPersonaRepository;
import es.codelearnacademy.filelab.repository.AbstractFileRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.io.*;
import java.util.*;
public class PersonaCsvRepository extends AbstractFileRepository<Persona,String> implements IPersonaRepository {

    CSVFormat input = CSVFormat.DEFAULT.builder()
            .setHeader()
            .setSkipHeaderRecord(true)
            .build();
    CSVFormat output = CSVFormat.DEFAULT.builder()
            .setHeader("dni", "nombre", "email", "edad", "activo")
            .build();


    public PersonaCsvRepository(Path path) {
        super(path);
    }
    @Override
    protected String getId(Persona p) {
        return p.dni();
    }
    @Override
    protected List<Persona> readAll()  {
        List<Persona> result = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
             CSVParser parser = input.parse(reader)) {
            for (CSVRecord row : parser) {
                String dni  = row.get("dni");
                String nombre = row.get("nombre");
                String email = row.get("email");
                int edad = Integer.parseInt(row.get("edad"));
                boolean activo = Boolean.parseBoolean(row.get("activo"));
                result.add(new Persona(dni, nombre, email, edad, activo));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    @Override
    protected void writeAll(List<Persona> personas)  {
        Path parent = path.getParent();
        try {
            if (parent != null) Files.createDirectories(parent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
                 CSVPrinter printer = new CSVPrinter(writer, output)) {
                for (Persona p : personas) printer.printRecord(p.dni(), p.nombre(), p.email(), p.edad(), p.activo());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
