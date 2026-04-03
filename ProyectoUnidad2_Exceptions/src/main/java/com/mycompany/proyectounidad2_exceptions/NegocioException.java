/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectounidad2_exceptions;

/**
 * Excepción base para todos los errores de negocio del sistema.
 *
 * Representa situaciones en las que una operación no puede completarse debido a
 * reglas del dominio o validaciones, y no por fallos técnicos.
 *
 * Es extendida por: - ValidacionException - ReglaNegocioException -
 * RecursoNoEncontradoException - AutenticacionException
 *
 * Extiende de RuntimeException para evitar manejo obligatorio (checked),
 * facilitando su propagación hacia capas superiores como la presentación.
 *
 * @author Afgord
 */
public class NegocioException extends RuntimeException {

    public NegocioException(String mensaje) {
        super(mensaje);
    }
}
