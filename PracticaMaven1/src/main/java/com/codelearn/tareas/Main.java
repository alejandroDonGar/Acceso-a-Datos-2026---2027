package com.codelearn.tareas;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Path archivoDatos = args.length > 0 ? Path.of(args[0]) : Path.of("tareas.json");
        RepositorioTareas repositorio = new RepositorioTareas();

        GestorTareas gestor;
        try {
            gestor = repositorio.cargar(archivoDatos);
        } catch (IOException e) {
            System.out.println("No se pudo cargar " + archivoDatos + ": " + e.getMessage());
            return;
        }

        Scanner scanner = new Scanner(System.in);
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
                if (comando.equals("añadir") || comando.equals("anadir")) {
                    Tarea tarea = gestor.anadir(partes.length > 1 ? partes[1] : "");
                    repositorio.guardar(gestor, archivoDatos);
                    System.out.println("Tarea " + tarea.getId() + " creada");
                } else if (comando.equals("completar")) {
                    int id = Integer.parseInt(partes[1].trim());
                    gestor.completar(id);
                    repositorio.guardar(gestor, archivoDatos);
                    System.out.println("Tarea " + id + " completada");
                } else if (comando.equals("eliminar")) {
                    int id = Integer.parseInt(partes[1].trim());
                    gestor.eliminar(id);
                    repositorio.guardar(gestor, archivoDatos);
                    System.out.println("Tarea " + id + " eliminada");
                } else if (comando.equals("listar")) {
                    List<Tarea> tareas = gestor.listar();
                    for (Tarea tarea : tareas) {
                        System.out.println(tarea);
                    }
                } else if (comando.equals("salir")) {
                    return;
                } else {
                    System.out.println("Comando no reconocido: " + comando);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Error: falta el argumento del comando " + comando);
            } catch (NumberFormatException e) {
                System.out.println("Error: el identificador debe ser un número");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
