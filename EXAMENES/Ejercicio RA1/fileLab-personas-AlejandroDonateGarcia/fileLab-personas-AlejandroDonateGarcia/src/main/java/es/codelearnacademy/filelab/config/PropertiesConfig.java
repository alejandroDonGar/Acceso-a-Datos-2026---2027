package es.codelearnacademy.filelab.config;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/**
 * Configuración de la academia sobre un fichero .properties.
 * Lee el fichero en cada consulta y lo guarda en cada cambio, así dos instancias
 * sobre el mismo archivo siempre ven los mismos datos.
 *
 * @author AlejandroDonGar
 */
public class PropertiesConfig {
    private final Path path;
    public PropertiesConfig(Path path) {
        this.path=path;
    }

    /** Carga el fichero en un Properties (UTF-8). Si falla la E/S devuelve uno vacío. */
    private Properties cargar() {
        Properties props = new Properties();
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            props.load(reader);
        } catch (IOException e) {
            // sin fichero legible: configuración vacía
        }
        return props;
    }

    /** Guarda el Properties en disco (UTF-8). Devuelve false si falla la escritura. */
    private boolean guardar(Properties props) {
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            props.store(writer, null);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public Optional<String> get(String key) {
        return Optional.ofNullable(cargar().getProperty(key));
    }
    public String getOrDefault(String key,String defaultValue) {
        return cargar().getProperty(key, defaultValue);
    }
    public Map<String,String> findAll() {
        Properties props = cargar();
        Map<String,String> mapa = new LinkedHashMap<>();
        for (String k : props.stringPropertyNames()) mapa.put(k, props.getProperty(k));
        return mapa;
    }
    public boolean put(String key,String value) {
        Properties props = cargar();
        props.setProperty(key, value);   // añade o actualiza
        return guardar(props);
    }
    public boolean remove(String key) {
        Properties props = cargar();
        if (props.remove(key) == null) return false;   // no existía: nada que borrar
        return guardar(props);
    }
}
