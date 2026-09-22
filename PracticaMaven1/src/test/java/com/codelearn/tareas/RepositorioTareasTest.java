package com.codelearn.tareas;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de RepositorioTareas: guardar y cargar tareas desde un archivo.
 *
 * @author AlejandroDonGar
 */
class RepositorioTareasTest {

    @TempDir
    Path directorioTemporal;

    /**
     * Comprueba que las tareas guardadas se recuperan igual al cargarlas
     * (mismo título y mismo estado de completada).
     */
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

    /**
     * Comprueba que cargar un archivo que no existe devuelve un gestor
     * de tareas vacío, sin lanzar ningún error.
     */
    @Test
    void cargarUnArchivoInexistenteDevuelveUnGestorVacio() throws IOException {
        RepositorioTareas repositorio = new RepositorioTareas();
        Path archivo = directorioTemporal.resolve("no-existe.json");

        GestorTareas gestor = repositorio.cargar(archivo);

        assertEquals(0, gestor.listar().size());
    }

    /**
     * Comprueba que cargar un archivo con JSON inválido lanza una
     * excepción y no modifica el archivo original.
     */
    @Test
    void cargarUnJsonInvalidoInformaDelProblemaSinSobrescribir() throws IOException {
        RepositorioTareas repositorio = new RepositorioTareas();
        Path archivo = directorioTemporal.resolve("corrupto.json");
        Files.writeString(archivo, "{ esto no es json valido ");

        assertThrows(IOException.class, () -> repositorio.cargar(archivo));

        assertEquals("{ esto no es json valido ", Files.readString(archivo));
    }
}
