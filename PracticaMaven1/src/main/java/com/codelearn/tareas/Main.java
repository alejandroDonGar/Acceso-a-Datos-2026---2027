package com.codelearn.tareas;

import java.io.IOException;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Path archivoDatos = args.length > 0 ? Path.of(args[0]) : Path.of("tareas.json");
        var repositorio = new RepositorioTareas();

        GestorTareas gestor;
        try {
            gestor = repositorio.cargar(archivoDatos);
        } catch (IOException e) {
            System.out.println("No se pudo cargar " + archivoDatos + ": " + e.getMessage());
            return;
        }

        var scanner = new Scanner(System.in);
        System.out.println("Gestor de tareas. Comandos: añadir <titulo>, completar <id>, eliminar <id>, listar, salir");

        while (true) {
            System.out.print("> ");
            if (!scanner.hasNextLine()) {
                break;
            }
            String linea = scanner.nextLine().trim();
            if (linea.isEmpty()) {
                continue;
            }
            String[] partes = linea.split(" ", 2);
            String comando = partes[0];

            try {
                switch (comando) {
                    case "añadir", "anadir" -> {
                        var tarea = gestor.anadir(partes.length > 1 ? partes[1] : "");
                        repositorio.guardar(gestor, archivoDatos);
                        System.out.println("Tarea " + tarea.getId() + " creada");
                    }
                    case "completar" -> {
                        int id = Integer.parseInt(partes[1].trim());
                        gestor.completar(id);
                        repositorio.guardar(gestor, archivoDatos);
                        System.out.println("Tarea " + id + " completada");
                    }
                    case "eliminar" -> {
                        int id = Integer.parseInt(partes[1].trim());
                        gestor.eliminar(id);
                        repositorio.guardar(gestor, archivoDatos);
                        System.out.println("Tarea " + id + " eliminada");
                    }
                    case "listar" -> {
                        for (var tarea : gestor.listar()) {
                            System.out.println(tarea);
                        }
                    }
                    case "salir" -> {
                        return;
                    }
                    default -> System.out.println("Comando no reconocido: " + comando);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Error: falta el argumento del comando " + comando);
            } catch (NumberFormatException e) {
                System.out.println("Error: el identificador debe ser un número");
            } catch (IllegalArgumentException | NoSuchElementException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
