package com.codelearn.tareas;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de GestorTareas: añadir, completar, eliminar y buscar tareas.
 *
 * @author AlejandroDonGar
 */
class GestorTareasTest {

    /**
     * Comprueba que al añadir una tarea aparece en la lista, con el
     * título correcto y sin completar.
     */
    @Test
    void anadeUnaTarea() {
        GestorTareas gestor = new GestorTareas();
        gestor.anadir("Aprender Maven");
        assertEquals(1, gestor.listar().size());
        assertEquals("Aprender Maven", gestor.listar().get(0).getTitulo());
        assertFalse(gestor.listar().get(0).isCompletada());
    }

    /**
     * Comprueba que un título en blanco se rechaza.
     */
    @Test
    void rechazaTituloVacio() {
        GestorTareas gestor = new GestorTareas();
        assertThrows(IllegalArgumentException.class, () -> gestor.anadir(" "));
    }

    /**
     * Comprueba que un título nulo se rechaza.
     */
    @Test
    void rechazaTituloNulo() {
        GestorTareas gestor = new GestorTareas();
        assertThrows(IllegalArgumentException.class, () -> gestor.anadir(null));
    }

    /**
     * Comprueba que la lista devuelta por listar() no se puede modificar
     * desde fuera del gestor.
     */
    @Test
    void listarNoPermiteModificarElEstadoInterno() {
        GestorTareas gestor = new GestorTareas();
        gestor.anadir("Aprender Maven");
        List<Tarea> lista = gestor.listar();
        assertThrows(UnsupportedOperationException.class, () -> lista.add(null));
    }

    /**
     * Comprueba que completar() marca la tarea como completada.
     */
    @Test
    void completarMarcaLaTareaComoCompletada() {
        GestorTareas gestor = new GestorTareas();
        Tarea tarea = gestor.anadir("Aprender Maven");
        gestor.completar(tarea.getId());
        assertTrue(gestor.buscar(tarea.getId()).isCompletada());
    }

    /**
     * Comprueba que eliminar() quita la tarea de la lista.
     */
    @Test
    void eliminarQuitaLaTareaDeLaLista() {
        GestorTareas gestor = new GestorTareas();
        Tarea tarea = gestor.anadir("Aprender Maven");
        gestor.eliminar(tarea.getId());
        assertEquals(0, gestor.listar().size());
    }

    /**
     * Comprueba que completar() con un id que no existe lanza una excepción.
     */
    @Test
    void completarUnIdentificadorInexistenteLanzaExcepcion() {
        GestorTareas gestor = new GestorTareas();
        assertThrows(IllegalArgumentException.class, () -> gestor.completar(999));
    }

    /**
     * Comprueba que eliminar() con un id que no existe lanza una excepción.
     */
    @Test
    void eliminarUnIdentificadorInexistenteLanzaExcepcion() {
        GestorTareas gestor = new GestorTareas();
        assertThrows(IllegalArgumentException.class, () -> gestor.eliminar(999));
    }
}
