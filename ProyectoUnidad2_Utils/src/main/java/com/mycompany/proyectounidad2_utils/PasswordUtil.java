/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectounidad2_utils;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

/**
 * Clase utilitaria para la gestión segura de contraseñas.
 *
 * Utiliza el algoritmo Argon2 para: - Generar hashes seguros de contraseñas -
 * Verificar contraseñas contra hashes almacenados
 *
 * Argon2 es un algoritmo moderno de hashing recomendado para almacenamiento de
 * contraseñas, resistente a ataques de fuerza bruta y uso de GPU.
 *
 * @author Afgord
 */
public class PasswordUtil {

    /**
     * Codificador de contraseñas basado en Argon2.
     *
     * Parámetros: - saltLength = 16 - hashLength = 32 - parallelism = 1 -
     * memory = 65536 KB - iterations = 3
     */
    private static final Argon2PasswordEncoder encoder = new Argon2PasswordEncoder(16, 32, 1, 65536, 3);

    /**
     * Genera un hash seguro a partir de una contraseña en texto plano.
     *
     * Valida que la contraseña no sea nula ni vacía antes de procesarla.
     *
     * @param password contraseña en texto plano
     * @return hash generado con Argon2
     * @throws IllegalArgumentException si la contraseña es nula o vacía
     */
    public static String hashPassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede ser nula o vacía.");
        }

        return encoder.encode(password);
    }

    /**
     * Verifica si una contraseña ingresada coincide con el hash almacenado.
     *
     * @param passwordIngresada contraseña en texto plano
     * @param hashGuardado hash previamente almacenado
     * @return true si coinciden, false en caso contrario
     */
    public static boolean verificarPassword(String passwordIngresada, String hashGuardado) {
        if (passwordIngresada == null || hashGuardado == null) {
            return false;
        }

        return encoder.matches(passwordIngresada, hashGuardado);
    }

}
