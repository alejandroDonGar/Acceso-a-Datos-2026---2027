package com.docencia.cadenas;

public class StringServiceImpl implements StringService {

    @Override
    public String normalizarTexto(String texto) {
        if(texto == null || texto.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return texto.trim().toLowerCase().replaceAll("\\s+", " ");
    }

    @Override
    public Boolean esPalindromo(String texto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'esPalindromo'");
    }

    @Override
    public Integer contarVocales(String texto) {
        if(texto == null || texto.isEmpty()) {
            throw new IllegalArgumentException();
        }
        int contador = 0;
        for (int i = 0; i < texto.length(); i++) {
            char letra = texto.charAt(i);
            if (letra == 'a' 
             || letra == 'e' 
             || letra == 'i' 
             || letra == 'o' 
             || letra == 'u') {
                contador ++;
            }
        }
        return contador;
    }

    @Override
    public String extraerIniciales(String nombreCompleto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'extraerIniciales'");
    }

    @Override
    public String invertirTexto(String texto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'invertirTexto'");
    }

    @Override
    public Boolean contieneSoloLetras(String texto) {
        if(texto == null || texto.isEmpty()) {
            return false;
        }
        return texto.matches("[a-zA-Z]+");
    }
}
