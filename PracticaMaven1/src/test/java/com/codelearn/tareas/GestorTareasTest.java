package com.codelearn.tareas;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class GestorTareasTest {
    @Test
    void anadeUnaTarea() {
        GestorTareas gestor = new GestorTareas();
        gestor.anadir("Aprender Maven");
        assertEquals(1, gestor.listar().size());
        assertEquals("Aprender Maven", gestor.listar().get(0).getTitulo());
        assertFalse(gestor.listar().get(0).isCompletada());
    }

    @Test
    void rechazaTituloVacio() {
        GestorTareas gestor = new GestorTareas();
        assertThrows(IllegalArgumentException.class, () -> gestor.anadir(" "));
    }

    @Test
    void rechazaTituloNulo() {
        GestorTareas gestor = new GestorTareas();
        assertThrows(IllegalArgumentException.class, () -> gestor.anadir(null));
    }

    @Test
    void listarNoPermiteModificarElEstadoInterno() {
        GestorTareas gestor = new GestorTareas();
        gestor.anadir("Aprender Maven");
        List<Tarea> lista = gestor.listar();
        assertThrows(UnsupportedOperationException.class, () -> lista.add(null));
    }

    @Test
    void completarMarcaLaTareaComoCompletada() {
        GestorTareas gestor = new GestorTareas();
        Tarea tarea = gestor.anadir("Aprender Maven");
        gestor.completar(tarea.getId());
        assertTrue(gestor.buscar(tarea.getId()).isCompletada());
    }

    @Test
    void eliminarQuitaLaTareaDeLaLista() {
        GestorTareas gestor = new GestorTareas();
        Tarea tarea = gestor.anadir("Aprender Maven");
        gestor.eliminar(tarea.getId());
        assertEquals(0, gestor.listar().size());
    }

    @Test
    void completarUnIdentificadorInexistenteLanzaExcepcion() {
        GestorTareas gestor = new GestorTareas();
        assertThrows(IllegalArgumentException.class, () -> gestor.completar(999));
    }

    @Test
    void eliminarUnIdentificadorInexistenteLanzaExcepcion() {
        GestorTareas gestor = new GestorTareas();
        assertThrows(IllegalArgumentException.class, () -> gestor.eliminar(999));
    }
}
