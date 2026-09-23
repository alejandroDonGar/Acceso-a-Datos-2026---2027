package com.docencia.ficheros;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase que implementa los metodos de la interfaz CsvService
 * @author AlejandroDonGar
 */
public class CsvServiceImpl implements CsvService {

    @Override
    public List<String> leerLineasCsv(Path ruta) {
        // Sin ruta no sabemos que fichero abrir.
        if (ruta == null) {
            throw new IllegalArgumentException();
        }
        // Files.exists comprueba si el fichero esta de verdad en el disco. Lo miramos ANTES de leer para poder
        // lanzar nuestra propia excepcion clara en vez de dejar que falle dentro de readAllLines.
        if (!Files.exists(ruta)) {
            throw new IllegalArgumentException("El fichero no existe: " + ruta);
        }
        // Leer un fichero puede fallar aunque exista (permisos, disco...) y Java OBLIGA a tratar esa IOException,
        // por eso va dentro de un try/catch.
        try {
            // readAllLines abre el fichero, lo lee entero y devuelve una lista con UNA linea de texto por posicion.
            // (Ademas cierra el fichero solo, no tenemos que cerrarlo nosotros.)
            return Files.readAllLines(ruta);
        } catch (IOException e) {
            // Convertimos el error de lectura en un IllegalArgumentException (el que pide la interfaz),
            // pasando "e" como causa para no perder el motivo original.
            throw new IllegalArgumentException("No se pudo leer el fichero: " + ruta, e);
        }
    }

    @Override
    public List<String[]> leerRegistrosCsv(Path ruta) {
        // Reutilizamos leerLineasCsv: ya valida la ruta y comprueba que el fichero existe.
        List<String> lineas = leerLineasCsv(ruta);
        // Lista donde guardaremos un array de columnas por cada linea. String[] = "array de textos" (una fila del CSV).
        List<String[]> registros = new ArrayList<>();
        // Recorremos cada linea del fichero, por ejemplo "Ana,20".
        for (String linea : lineas) {
            // Una linea en blanco (por ejemplo la de despues del ultimo salto de linea) no es un registro: la saltamos.
            // continue = "termina esta vuelta y pasa a la siguiente linea".
            if (linea.trim().isEmpty()) {
                continue;
            }
            // split(",") corta el texto por cada coma: "Ana,20" -> ["Ana", "20"].
            String[] columnas = linea.split(",");
            // Guardamos las columnas de esta linea como un registro mas.
            registros.add(columnas);
        }
        // Devolvemos todos los registros, en el mismo orden en que estaban en el fichero.
        return registros;
    }

    @Override
    public void escribirLineasCsv(Path ruta, List<String> lineas) {
        // Necesitamos saber donde escribir y que escribir.
        if (ruta == null || lineas == null) {
            throw new IllegalArgumentException();
        }
        // Escribir tambien puede fallar (carpeta que no existe, sin permisos...), asi que va en try/catch.
        try {
            // Files.write guarda cada elemento de la lista como UNA linea del fichero (añade el salto de linea solo).
            // Si el fichero ya existia lo sobrescribe entero; si no existia lo crea.
            Files.write(ruta, lineas);
        } catch (IOException e) {
            // Igual que al leer: la convertimos en IllegalArgumentException conservando la causa.
            throw new IllegalArgumentException("No se pudo escribir el fichero: " + ruta, e);
        }
    }

    @Override
    public Integer contarRegistrosCsv(Path ruta) {
        // Reutilizamos leerRegistrosCsv: ya valida la ruta y descarta las lineas vacias.
        // Cada elemento de la lista es un registro, asi que contar registros = contar el tamaño de la lista.
        return leerRegistrosCsv(ruta).size();
    }

    @Override
    public List<String[]> filtrarRegistrosPorValor(Path ruta, Integer columna, String valor) {
        // Validamos los datos de entrada. Una columna negativa no existe: las posiciones empiezan en 0.
        if (columna == null || columna < 0 || valor == null) {
            throw new IllegalArgumentException();
        }
        // Leemos el fichero ya separado en columnas (esto tambien valida la ruta).
        List<String[]> registros = leerRegistrosCsv(ruta);
        // Lista donde guardaremos solo los registros que cumplan la condicion.
        List<String[]> filtrados = new ArrayList<>();
        // Recorremos cada registro (cada fila del CSV).
        for (String[] registro : registros) {
            // Si la fila tiene menos columnas de las necesarias, la columna pedida NO EXISTE en el fichero.
            // Ejemplo: filas de 2 columnas (posiciones 0 y 1) y nos piden la columna 3: se sale de rango.
            // Comprobarlo aqui evita un ArrayIndexOutOfBoundsException al hacer registro[columna].
            if (columna >= registro.length) {
                throw new IllegalArgumentException("La columna " + columna + " no existe en el fichero");
            }
            // registro[columna] es el texto de la columna que nos interesa. trim() quita espacios sobrantes
            // y equals compara el contenido exacto con el valor buscado (Strings se comparan con equals, nunca con ==).
            if (registro[columna].trim().equals(valor)) {
                // Coincide: nos quedamos con la fila ENTERA (todas sus columnas), no solo con la columna comparada.
                filtrados.add(registro);
            }
        }
        // Con "Ana,20", "Luis,30", "Ana,40" y valor "Ana" en la columna 0 devolvemos 2 registros.
        return filtrados;
    }

    @Override
    public Map<String, Integer> contarFrecuenciaColumna(Path ruta, Integer columna) {
        // Misma validacion que al filtrar: la columna debe existir y no ser negativa.
        if (columna == null || columna < 0) {
            throw new IllegalArgumentException();
        }
        // Leemos el fichero ya separado en columnas.
        List<String[]> registros = leerRegistrosCsv(ruta);
        // Mapa valor -> veces que aparece. LinkedHashMap conserva el orden en que fueron apareciendo.
        Map<String, Integer> frecuencias = new LinkedHashMap<>();
        // Recorremos cada fila del CSV.
        for (String[] registro : registros) {
            // Si la fila no llega a esa columna, la columna no existe en el fichero: dato invalido.
            if (columna >= registro.length) {
                throw new IllegalArgumentException("La columna " + columna + " no existe en el fichero");
            }
            // Cogemos el valor de la columna limpiando espacios (asi "Ana" y " Ana" cuentan como el mismo).
            String valor = registro[columna].trim();
            // CONTADOR con Map: si el valor ya lo habiamos visto...
            if (frecuencias.containsKey(valor)) {
                // ...sumamos 1 a lo que llevaba contado (put sobrescribe el valor anterior de esa clave).
                frecuencias.put(valor, frecuencias.get(valor) + 1);
            } else {
                // Es la primera vez que lo vemos: empieza en 1.
                frecuencias.put(valor, 1);
            }
        }
        // Con "Ana,20", "Luis,30", "Ana,40" en la columna 0 -> {Ana=2, Luis=1}.
        return frecuencias;
    }
}
