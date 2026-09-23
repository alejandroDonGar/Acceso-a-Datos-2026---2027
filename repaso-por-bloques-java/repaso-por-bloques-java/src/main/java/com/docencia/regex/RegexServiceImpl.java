package com.docencia.regex;

import java.util.regex.Pattern;

/**
 * Clase que implementa los metodos de regex de la interfaz RegexService
 * @author AlejandroDonGar
 */
public class RegexServiceImpl implements RegexService {

    // Pattern.matches(REGEX, TEXTO): el PRIMER argumento es la expresion regular y el SEGUNDO el texto a comprobar.
    // (Ojo con el orden: si se ponen al reves, se intenta usar el texto como si fuera la expresion.)
    // ^ marca el inicio y $ el final: asi TODO el texto debe cumplir el patron, no solo un trozo.

    @Override
    public Boolean validarDni(String dni) {
        // Un texto null no puede tener formato valido: false (el test lo exige asi, no lanza excepcion).
        if (dni == null) {
            return false;
        }
        // [0-9]{8} = exactamente 8 digitos. [A-Z] = una letra mayuscula.
        return Pattern.matches("^[0-9]{8}[A-Z]$", dni);
    }

    @Override
    public Boolean validarEmail(String email) {
        // Sin texto no hay email valido.
        if (email == null) {
            return false;
        }
        // [a-zA-Z0-9._%+-]+ = parte del usuario (letras, numeros y . _ % + -), una o mas veces.
        // @ = la arroba literal.
        // [a-zA-Z0-9.-]+ = el dominio (letras, numeros, puntos y guiones).
        // \\. = un punto literal (con \\ porque el . solo significaria "cualquier caracter").
        // [a-zA-Z]{2,} = la extension (com, es...) con al menos 2 letras. Por eso "usuario@mail" NO vale: le falta el .com.
        return Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", email);
    }

    @Override
    public Boolean validarTelefono(String telefono) {
        // Sin texto no hay telefono valido.
        if (telefono == null) {
            return false;
        }
        // (\\+[0-9]{1,3})? = prefijo internacional OPCIONAL (el ? del final): un + y de 1 a 3 digitos.
        //   El + va escapado (\\+) porque sin escapar significa "una o mas veces" y daria error de sintaxis.
        // [6-9] = el primer digito debe ser 6, 7, 8 o 9 (por eso "512345678" NO vale).
        // [0-9]{8} = los 8 digitos restantes, en total 9.
        return Pattern.matches("^(\\+[0-9]{1,3})?[6-9][0-9]{8}$", telefono);
    }

    @Override
    public Boolean validarNombre(String nombre) {
        // Sin texto no hay nombre valido.
        if (nombre == null) {
            return false;
        }
        // Dentro de [] van las letras permitidas: minusculas, mayusculas, tildes, enie y dieresis, mas el espacio.
        // {2,} = al menos 2 caracteres, por eso "A" NO vale pero "Al" si.
        return Pattern.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]{2,}$", nombre);
    }

    @Override
    public Boolean validarCodigoPostal(String codigoPostal) {
        // Sin texto no hay codigo postal valido.
        if (codigoPostal == null) {
            return false;
        }
        // Los dos primeros digitos son la provincia y van de 01 a 52:
        //   0[1-9] = 01 a 09 | [1-4][0-9] = 10 a 49 | 5[0-2] = 50 a 52.
        // El | significa "o". Despues, [0-9]{3} = los otros 3 digitos.
        return Pattern.matches("^(0[1-9]|[1-4][0-9]|5[0-2])[0-9]{3}$", codigoPostal);
    }

    @Override
    public Boolean validarMatricula(String matricula) {
        // Sin texto no hay matricula valida.
        if (matricula == null) {
            return false;
        }
        // [0-9]{4} = 4 numeros y [A-Z]{3} = 3 letras mayusculas: "1234ABC".
        return Pattern.matches("^[0-9]{4}[A-Z]{3}$", matricula);
    }
}
