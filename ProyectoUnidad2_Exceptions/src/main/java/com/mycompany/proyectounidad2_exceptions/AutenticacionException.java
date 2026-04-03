/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectounidad2_exceptions;

/**
 * Excepción utilizada para errores de autenticación en el sistema.
 *
 * Se lanza cuando ocurre un problema al intentar iniciar sesión, por ejemplo: -
 * Contraseña incorrecta - Credenciales inválidas
 *
 * Forma parte de la jerarquía de excepciones de negocio, extendiendo de
 * NegocioException.
 *
 * @author Afgord
 */
public class AutenticacionException extends NegocioException {

    public AutenticacionException(String mensaje) {
        super(mensaje);
    }
}
