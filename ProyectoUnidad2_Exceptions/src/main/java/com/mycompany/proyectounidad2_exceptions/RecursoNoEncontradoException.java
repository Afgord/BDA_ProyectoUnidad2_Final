/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectounidad2_exceptions;

/**
 * Excepción utilizada cuando un recurso solicitado no existe en el sistema.
 *
 * Se lanza en situaciones como: - Búsqueda de un estudiante inexistente -
 * Consulta de registros que no se encuentran en la base de datos
 *
 * Forma parte de la jerarquía de excepciones de negocio, extendiendo de
 * NegocioException.
 *
 * @author Afgord
 */
public class RecursoNoEncontradoException extends NegocioException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
