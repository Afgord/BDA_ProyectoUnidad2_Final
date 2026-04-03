/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectounidad2_exceptions;

/**
 * Excepción utilizada cuando los datos de entrada no cumplen con las
 * validaciones requeridas.
 *
 * Se lanza en situaciones como: - Campos obligatorios nulos o vacíos - Formatos
 * incorrectos (correo, contraseña, etc.) - Parámetros inválidos en métodos
 *
 * Forma parte de la jerarquía de excepciones de negocio, extendiendo de
 * NegocioException.
 *
 * @author Afgord
 */
public class ValidacionException extends NegocioException {

    public ValidacionException(String mensaje) {
        super(mensaje);
    }
}
