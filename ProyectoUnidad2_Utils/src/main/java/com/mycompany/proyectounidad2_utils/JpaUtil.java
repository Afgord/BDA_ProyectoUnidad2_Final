/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectounidad2_utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Clase utilitaria para la gestión de EntityManager en JPA.
 *
 * Implementa el patrón Singleton para el EntityManagerFactory, garantizando que
 * se cree una sola instancia durante el ciclo de vida de la aplicación.
 *
 * Proporciona métodos para: - Obtener instancias de EntityManager - Cerrar la
 * fábrica de EntityManager
 *
 * Se utiliza como punto central de acceso a la persistencia en toda la
 * aplicación.
 *
 * @author Afgord
 */
public class JpaUtil {

    private static final String PERSISTENCE_UNIT = "ProyectoUnidad2PU";

    private static EntityManagerFactory emf;

    /**
     * Obtiene una nueva instancia de EntityManager.
     *
     * Si la fábrica de EntityManager no ha sido inicializada, se crea
     * utilizando la unidad de persistencia configurada.
     *
     * @return nueva instancia de EntityManager
     */
    public static EntityManager getEntityManager() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        }

        return emf.createEntityManager();
    }

    /**
     * Cierra la instancia de EntityManagerFactory si está abierta.
     *
     * Se recomienda llamar a este método al finalizar la aplicación para
     * liberar recursos.
     */
    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

}
