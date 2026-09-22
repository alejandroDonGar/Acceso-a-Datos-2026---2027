package com.codelearn.tareas;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class RepositorioTareasTest {

    @TempDir
    Path directorioTemporal;

    @Test
    void guardarYCargarDevuelveLasMismasTareas() throws IOException {
        RepositorioTareas repositorio = new RepositorioTareas();
        Path archivo = directorioTemporal.resolve("tareas.json");

        GestorTareas gestor = new GestorTareas();
        gestor.anadir("Aprender Maven");
        gestor.anadir("Escribir el README");
        gestor.completar(1);

        repositorio.guardar(gestor, archivo);
        GestorTareas gestorCargado = repositorio.cargar(archivo);

        assertEquals(2, gestorCargado.listar().size());
        assertEquals("Aprender Maven", gestorCargado.listar().get(0).getTitulo());
        assertTrue(gestorCargado.listar().get(0).isCompletada());
        assertFalse(gestorCargado.listar().get(1).isCompletada());
    }

    @Test
    void cargarUnArchivoInexistenteDevuelveUnGestorVacio() throws IOException {
        RepositorioTareas repositorio = new RepositorioTareas();
        Path archivo = directorioTemporal.resolve("no-existe.json");

        GestorTareas gestor = repositorio.cargar(archivo);

        assertEquals(0, gestor.listar().size());
    }

    @Test
    void cargarUnJsonInvalidoInformaDelProblemaSinSobrescribir() throws IOException {
        RepositorioTareas repositorio = new RepositorioTareas();
        Path archivo = directorioTemporal.resolve("corrupto.json");
        Files.writeString(archivo, "{ esto no es json valido ");

        assertThrows(IOException.class, () -> repositorio.cargar(archivo));

        assertEquals("{ esto no es json valido ", Files.readString(archivo));
    }
}
