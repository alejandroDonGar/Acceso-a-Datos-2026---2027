package com.codelearn.tareas;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class RepositorioTareas {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static class Datos {
        List<Tarea> tareas = new ArrayList<>();
        int siguienteId = 1;
    }

    public GestorTareas cargar(Path archivo) throws IOException {
        if (!Files.exists(archivo)) {
            return new GestorTareas();
        }
        String contenido = Files.readString(archivo);
        Datos datos;
        try {
            datos = GSON.fromJson(contenido, Datos.class);
        } catch (JsonSyntaxException e) {
            throw new IOException("El archivo de tareas " + archivo + " contiene JSON inválido: " + e.getMessage(), e);
        }
        if (datos == null) {
            throw new IOException("El archivo de tareas " + archivo + " está vacío o no tiene el formato esperado");
        }
        return new GestorTareas(datos.tareas, datos.siguienteId);
    }

    public void guardar(GestorTareas gestor, Path archivo) throws IOException {
        Datos datos = new Datos();
        datos.tareas = gestor.listar();
        datos.siguienteId = gestor.getSiguienteId();
        if (archivo.getParent() != null) {
            Files.createDirectories(archivo.getParent());
        }
        Files.writeString(archivo, GSON.toJson(datos));
    }
}
