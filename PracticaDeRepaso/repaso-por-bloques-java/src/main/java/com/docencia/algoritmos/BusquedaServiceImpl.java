package com.docencia.algoritmos;
/**
 * Clase que implementa los metodos de algoritmos de BusquedaServiceImpl
 * @author AlejandroDonGar
 */
import java.util.List;
public class BusquedaServiceImpl implements BusquedaService {
    @Override
    public Integer buscarIndiceElemento(List<Integer> numeros, Integer valor) {
        if(numeros==null || valor==null) {
            throw new IllegalArgumentException();
        }
        for (Integer numero : numeros) {
            if(numero.equals(valor)) {
                return numero;
            }
        }
        return -1;
    }
    @Override
    public Boolean existePalabra(List<String> palabras, String palabra) {
        if(palabras == null || palabras.size() == 0 || palabra == null) {
            return false;
        }
        for (String palabraLista : palabras) {
            if(palabraLista.equalsIgnoreCase(palabra)) {
                return true;
            }
        }
        return false;
    }
    @Override
    public Integer encontrarMaximo(List<Integer> numeros) {
        if(numeros == null || numeros.size() == 0) {
            throw new IllegalArgumentException();
        }
        Integer maximo = numeros.get(0);;
        for (Integer numero : numeros) {
            if(numero > maximo) {
                maximo = numero;
            }
        }
        return maximo;
    }
    @Override
    public Integer encontrarMinimo(List<Integer> numeros) {
        if(numeros == null || numeros.size() == 0) {
            throw new IllegalArgumentException();
        }
        Integer minimo = numeros.get(0);
        for (Integer numero : numeros) {
            if(numero < minimo) {
                minimo = numero;
            }
        }
        return minimo;
    }
    @Override
    public Integer contarApariciones(List<Integer> numeros, Integer valor) {
        if(numeros == null || numeros.size() == 0 || valor == null) {
            return null;
        }
        Integer numeroDeApariciones = 0;
        for (Integer numero : numeros) {
            if(numero.equals(valor)) {
                numeroDeApariciones ++;
            }
        }
        return numeroDeApariciones;
    }
}
