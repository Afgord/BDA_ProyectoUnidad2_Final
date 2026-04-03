package com.mycompany.proyectounidad2_exceptions;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 * Excepción utilizada cuando se viola una regla de negocio del sistema.
 *
 * Se lanza en situaciones donde los datos son válidos, pero no cumplen con las
 * reglas establecidas del dominio.
 *
 * Ejemplos: - Intentar registrar un correo ya existente - Asignar un hobby que
 * ya tiene el estudiante - Intentar desactivar una cuenta ya desactivada
 *
 * Forma parte de la jerarquía de excepciones de negocio, extendiendo de
 * NegocioException.
 *
 * @author Afgord
 */
public class ReglaNegocioException extends NegocioException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
