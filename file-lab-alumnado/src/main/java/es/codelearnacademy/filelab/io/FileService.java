package es.codelearnacademy.filelab.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileService {

    public boolean existe(File file) {
        try {
            if (!file.exists()) {
                Files.createFile(file.toPath());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public boolean esArchivo(File file) {
        throw new UnsupportedOperationException("Función no implementada");
    }

    public boolean esDirectorio(File file) {
        throw new UnsupportedOperationException("Función no implementada");
    }

    public String nombre(File file) {
        throw new UnsupportedOperationException("Función no implementada");
    }

    public File padre(File file) {
        throw new UnsupportedOperationException("Función no implementada");
    }

    public Path convertirAPath(File file) {
        throw new UnsupportedOperationException("Función no implementada");
    }

    public File convertirAFile(Path path) {
        throw new UnsupportedOperationException("Función no implementada");
    }
}
