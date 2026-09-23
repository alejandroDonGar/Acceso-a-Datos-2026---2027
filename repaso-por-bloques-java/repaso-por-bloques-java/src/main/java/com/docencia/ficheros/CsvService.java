package com.docencia.ficheros;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Servicio para trabajar con ficheros CSV.
 * @author AlejandroDonGar
 */
public interface CsvService {
    /**
     * Lee un fichero CSV y devuelve cada linea como un texto
     * @param ruta ruta del fichero a leer
     * @return devuelve la lista de lineas del fichero
     * @throws IllegalArgumentException si la ruta es null, el fichero no existe o no se puede leer
     */
    List<String> leerLineasCsv(Path ruta);

    /**
     * Lee un fichero CSV y separa cada linea en sus columnas usando la coma
     * @param ruta ruta del fichero a leer
     * @return devuelve una lista con un array de columnas por cada linea no vacia
     * @throws IllegalArgumentException si la ruta es null, el fichero no existe o no se puede leer
     */
    List<String[]> leerRegistrosCsv(Path ruta);

    /**
     * Escribe una lista de lineas en un fichero CSV, una linea por registro
     * @param ruta ruta del fichero a escribir
     * @param lineas lineas de texto a guardar
     * @throws IllegalArgumentException si la ruta o las lineas son null, o no se puede escribir
     */
    void escribirLineasCsv(Path ruta, List<String> lineas);

    /**
     * Cuenta cuantos registros (lineas no vacias) tiene un fichero CSV
     * @param ruta ruta del fichero a leer
     * @return devuelve el numero de registros
     * @throws IllegalArgumentException si la ruta es null, el fichero no existe o no se puede leer
     */
    Integer contarRegistrosCsv(Path ruta);

    /**
     * Filtra los registros de un CSV cuya columna indicada vale exactamente el valor buscado
     * @param ruta ruta del fichero a leer
     * @param columna posicion de la columna a comparar, empezando en 0
     * @param valor valor que debe tener la columna
     * @return devuelve los registros que cumplen la condicion
     * @throws IllegalArgumentException si algun dato es null, la columna es negativa o no existe en el fichero
     */
    List<String[]> filtrarRegistrosPorValor(Path ruta, Integer columna, String valor);

    /**
     * Cuenta cuantas veces se repite cada valor de una columna de un CSV
     * @param ruta ruta del fichero a leer
     * @param columna posicion de la columna a contar, empezando en 0
     * @return devuelve un mapa con cada valor de la columna y sus repeticiones
     * @throws IllegalArgumentException si algun dato es null, la columna es negativa o no existe en el fichero
     */
    Map<String, Integer> contarFrecuenciaColumna(Path ruta, Integer columna);
}
