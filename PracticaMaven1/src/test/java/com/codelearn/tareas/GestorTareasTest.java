package com.codelearn.tareas;

import org.junit.jupiter.api.Test;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;

class GestorTareasTest {
    @Test
    void anadeUnaTarea() {
        var gestor = new GestorTareas();
        gestor.anadir("Aprender Maven");
        assertEquals(1, gestor.listar().size());
        assertEquals("Aprender Maven", gestor.listar().get(0).getTitulo());
        assertFalse(gestor.listar().get(0).isCompletada());
    }

    @Test
    void rechazaTituloVacio() {
        var gestor = new GestorTareas();
        assertThrows(IllegalArgumentException.class, () -> gestor.anadir(" "));
    }

    @Test
    void rechazaTituloNulo() {
        var gestor = new GestorTareas();
        assertThrows(IllegalArgumentException.class, () -> gestor.anadir(null));
    }

    @Test
    void listarNoPermiteModificarElEstadoInterno() {
        var gestor = new GestorTareas();
        gestor.anadir("Aprender Maven");
        var lista = gestor.listar();
        assertThrows(UnsupportedOperationException.class, () -> lista.add(null));
    }

    @Test
    void completarMarcaLaTareaComoCompletada() {
        var gestor = new GestorTareas();
        var tarea = gestor.anadir("Aprender Maven");
        gestor.completar(tarea.getId());
        assertTrue(gestor.buscar(tarea.getId()).isCompletada());
    }

    @Test
    void eliminarQuitaLaTareaDeLaLista() {
        var gestor = new GestorTareas();
        var tarea = gestor.anadir("Aprender Maven");
        gestor.eliminar(tarea.getId());
        assertEquals(0, gestor.listar().size());
    }

    @Test
    void completarUnIdentificadorInexistenteLanzaExcepcion() {
        var gestor = new GestorTareas();
        assertThrows(NoSuchElementException.class, () -> gestor.completar(999));
    }

    @Test
    void eliminarUnIdentificadorInexistenteLanzaExcepcion() {
        var gestor = new GestorTareas();
        assertThrows(NoSuchElementException.class, () -> gestor.eliminar(999));
    }
}
