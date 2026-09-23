package com.docencia.condicionales;

/**
 * Servicio para practicar switch.
 * @author AlejandroDonGar
 */
public interface SwitchService {
    /**
     * Obtiene el nombre del dia de la semana a partir de su numero (1 = LUNES ... 7 = DOMINGO)
     * @param numeroDia numero del dia, entre 1 y 7
     * @return devuelve el nombre del dia en mayusculas
     * @throws IllegalArgumentException si el numero es null o esta fuera del rango 1-7
     */
    String obtenerNombreDia(Integer numeroDia);

    /**
     * Calcula el descuento en euros que corresponde a un tipo de cliente (VIP 20%, NORMAL 0%)
     * @param tipoCliente tipo de cliente, VIP o NORMAL
     * @param importe importe sobre el que se calcula el descuento
     * @return devuelve la cantidad a descontar
     * @throws IllegalArgumentException si el tipo o el importe son null, o el tipo no es conocido
     */
    Double calcularDescuentoPorTipo(String tipoCliente, Double importe);

    /**
     * Obtiene un mensaje descriptivo a partir del estado (ACTIVO, INACTIVO o PENDIENTE)
     * @param estado estado del que se quiere el mensaje
     * @return devuelve el mensaje asociado al estado
     * @throws IllegalArgumentException si el estado es null o no es conocido
     */
    String obtenerMensajeEstado(String estado);

    /**
     * Obtiene cuantos dias tiene un mes (sin tener en cuenta los anios bisiestos)
     * @param mes numero del mes, entre 1 y 12
     * @return devuelve el numero de dias del mes
     * @throws IllegalArgumentException si el mes es null o esta fuera del rango 1-12
     */
    Integer obtenerDiasDelMes(Integer mes);

    /**
     * Obtiene la categoria de un producto segun la primera letra de su codigo (T, A o R)
     * @param codigo codigo del producto, por ejemplo T001
     * @return devuelve la categoria del producto en mayusculas
     * @throws IllegalArgumentException si el codigo es null, esta vacio o su letra no es conocida
     */
    String obtenerCategoriaProducto(String codigo);
}
