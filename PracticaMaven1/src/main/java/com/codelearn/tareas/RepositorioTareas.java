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
                    siguienteId = leerNumeroDespuesDeDosPuntos(lineaLimpia);
                } else if (lineaLimpia.contains("\"id\"")) {
                    int id = leerNumeroEntre(lineaLimpia, "\"id\": ", ",");
                    String titulo = leerTextoEntre(lineaLimpia, "\"titulo\": \"", "\",");
                    boolean completada = lineaLimpia.contains("\"completada\": true");
                    tareas.add(new Tarea(id, titulo, completada));
                }
            }
        } catch (RuntimeException e) {
            throw new IOException("El archivo de tareas " + archivo + " no tiene el formato esperado", e);
        }

        return new GestorTareas(tareas, siguienteId);
    }

    public void guardar(GestorTareas gestor, Path archivo) throws IOException {
        List<Tarea> tareas = gestor.listar();

        StringBuilder texto = new StringBuilder();
        texto.append("{\n");
        texto.append("  \"siguienteId\": ").append(gestor.getSiguienteId()).append(",\n");
        texto.append("  \"tareas\": [\n");
        for (int i = 0; i < tareas.size(); i++) {
            Tarea tarea = tareas.get(i);
            texto.append("    {\"id\": ").append(tarea.getId());
            texto.append(", \"titulo\": \"").append(escapar(tarea.getTitulo())).append("\"");
            texto.append(", \"completada\": ").append(tarea.isCompletada());
            texto.append("}");
            if (i < tareas.size() - 1) {
                texto.append(",");
            }
            texto.append("\n");
        }
        texto.append("  ]\n");
        texto.append("}\n");

        if (archivo.getParent() != null) {
            Files.createDirectories(archivo.getParent());
        }
        Files.writeString(archivo, texto.toString());
    }

    private String escapar(String texto) {
        return texto.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String desescapar(String texto) {
        return texto.replace("\\\"", "\"").replace("\\\\", "\\");
    }

    private int leerNumeroDespuesDeDosPuntos(String linea) {
        int posDosPuntos = linea.indexOf(":");
        String resto = linea.substring(posDosPuntos + 1).trim();
        resto = resto.replace(",", "");
        return Integer.parseInt(resto);
    }

    private int leerNumeroEntre(String linea, String inicio, String fin) {
        int desde = linea.indexOf(inicio) + inicio.length();
        int hasta = linea.indexOf(fin, desde);
        String numero = linea.substring(desde, hasta).trim();
        return Integer.parseInt(numero);
    }

    private String leerTextoEntre(String linea, String inicio, String fin) {
        int desde = linea.indexOf(inicio) + inicio.length();
        int hasta = linea.indexOf(fin, desde);
        String texto = linea.substring(desde, hasta);
        return desescapar(texto);
    }
}
