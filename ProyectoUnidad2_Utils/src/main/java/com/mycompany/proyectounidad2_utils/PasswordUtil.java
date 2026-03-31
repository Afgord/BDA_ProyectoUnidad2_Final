/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectounidad2_utils;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

/**
 *
 * @author Afgord
 */
public class PasswordUtil {

    private static final Argon2PasswordEncoder encoder = new Argon2PasswordEncoder(16, 32, 1, 65536, 3);

    /**
     * Genera hash seguro de una contraseña.
     */
    public static String hashPassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede ser nula o vacía.");
        }

        return encoder.encode(password);
    }

    /**
     * Verifica contraseña ingresada contra hash almacenado.
     */
    public static boolean verificarPassword(String passwordIngresada, String hashGuardado) {
        if (passwordIngresada == null || hashGuardado == null) {
            return false;
        }

        return encoder.matches(passwordIngresada, hashGuardado);
    }

}
