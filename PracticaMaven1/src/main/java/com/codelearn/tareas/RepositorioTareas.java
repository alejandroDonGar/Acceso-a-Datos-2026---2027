package com.codelearn.tareas;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class RepositorioTareas {

    public GestorTareas cargar(Path archivo) throws IOException {
        if (!Files.exists(archivo)) {
            return new GestorTareas();
        }

        String contenido = Files.readString(archivo).trim();

        if (!contenido.startsWith("{") || !contenido.endsWith("}")
                || !contenido.contains("\"siguienteId\"") || !contenido.contains("\"tareas\"")) {
            throw new IOException("El archivo de tareas " + archivo + " no tiene el formato esperado");
        }

        List<Tarea> tareas = new ArrayList<>();
        int siguienteId = 1;
        String[] lineas = contenido.split("\n");

        try {
            for (String linea : lineas) {
                String lineaLimpia = linea.trim();
                if (lineaLimpia.startsWith("\"siguienteId\"")) {
                    String[] partes = lineaLimpia.split(":");
                    siguienteId = Integer.parseInt(partes[1].replace(",", "").trim());
                } else if (lineaLimpia.contains("\"id\"")) {
                    tareas.add(leerTarea(lineaLimpia));
                }
            }
        } catch (RuntimeException e) {
            throw new IOException("El archivo de tareas " + archivo + " no tiene el formato esperado", e);
        }

        return new GestorTareas(tareas, siguienteId);
    }

    private Tarea leerTarea(String linea) {
        String[] campos = linea.split(",");

        String[] campoId = campos[0].split(":");
        int id = Integer.parseInt(campoId[1].trim());

        String[] campoTitulo = campos[1].split(":", 2);
        String titulo = campoTitulo[1].trim().replace("\"", "");

        boolean completada = campos[2].contains("true");

        return new Tarea(id, titulo, completada);
    }

    public void guardar(GestorTareas gestor, Path archivo) throws IOException {
        List<Tarea> tareas = gestor.listar();

        String texto = "{\n";
        texto = texto + "  \"siguienteId\": " + gestor.getSiguienteId() + ",\n";
        texto = texto + "  \"tareas\": [\n";

        for (int i = 0; i < tareas.size(); i++) {
            Tarea tarea = tareas.get(i);
            String titulo = tarea.getTitulo().replace("\"", "");
            String linea = "    {\"id\": " + tarea.getId()
                    + ", \"titulo\": \"" + titulo + "\""
                    + ", \"completada\": " + tarea.isCompletada() + "}";
            if (i < tareas.size() - 1) {
                linea = linea + ",";
            }
            texto = texto + linea + "\n";
        }

        texto = texto + "  ]\n";
        texto = texto + "}\n";

        if (archivo.getParent() != null) {
            Files.createDirectories(archivo.getParent());
        }
        Files.writeString(archivo, texto);
    }
}
