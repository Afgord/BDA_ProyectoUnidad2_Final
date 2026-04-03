/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectounidad2_servicios;

import com.mycompany.proyectounidad2_dominio.Hobby;
import com.mycompany.proyectounidad2_exceptions.NegocioException;
import com.mycompany.proyectounidad2_exceptions.ReglaNegocioException;
import com.mycompany.proyectounidad2_exceptions.ValidacionException;
import com.mycompany.proyectounidad2_persistencia.HobbyDAO;
import com.mycompany.proyectounidad2_persistencia.IHobbyDAO;
import com.mycompany.proyectounidad2_utils.JpaUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 * Implementación de la lógica de negocio para la entidad Hobby.
 *
 * Gestiona el registro y consulta de hobbies disponibles en el sistema.
 *
 * Aplica validaciones de negocio para evitar registros nulos, nombres vacíos y
 * duplicidad de hobbies.
 *
 * @author Afgord
 */
public class HobbyService implements IHobbyService {

    /**
     * Registra un nuevo hobby en el sistema.
     *
     * Realiza las siguientes validaciones: - El objeto hobby no sea nulo - El
     * nombre del hobby no sea nulo o vacío - El nombre se normaliza (trim y
     * lowercase) - No exista previamente un hobby con el mismo nombre
     *
     * @param hobby hobby a registrar
     * @return hobby persistido en base de datos
     * @throws ValidacionException si los datos no cumplen las reglas
     * @throws ReglaNegocioException si ya existe un hobby con el mismo nombre
     */
    @Override
    public Hobby registrarHobby(Hobby hobby) {

        if (hobby == null) {
            throw new ValidacionException("El hobby no puede ser nulo.");
        }

        if (hobby.getNombre() != null) {
            hobby.setNombre(hobby.getNombre().trim().toLowerCase());
        }

        validarDatosHobby(hobby);

        EntityManager em = JpaUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            IHobbyDAO hobbyDAO = new HobbyDAO(em);

            Hobby existente = hobbyDAO.buscarPorNombre(hobby.getNombre());

            if (existente != null) {
                throw new ReglaNegocioException("Ya existe un hobby registrado con ese nombre.");
            }

            Hobby guardado = hobbyDAO.guardar(hobby);

            em.getTransaction().commit();
            return guardado;

        } catch (NegocioException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new NegocioException("Error al registrar el hobby.");
        } finally {
            em.close();
        }
    }

    /**
     * Busca un hobby por su nombre.
     *
     * El nombre es normalizado (trim y lowercase) antes de realizar la
     * consulta.
     *
     * @param nombre nombre del hobby
     * @return hobby encontrado o null si no existe
     * @throws ValidacionException si el nombre es nulo o vacío
     */
    @Override
    public Hobby buscarPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new ValidacionException("El nombre del hobby no puede ser nulo o vacío.");
        }

        nombre = nombre.trim().toLowerCase();

        EntityManager em = JpaUtil.getEntityManager();

        try {
            IHobbyDAO hobbyDAO = new HobbyDAO(em);
            return hobbyDAO.buscarPorNombre(nombre);
        } finally {
            em.close();
        }
    }

    /**
     * Obtiene todos los hobbies registrados en el sistema.
     *
     * @return lista de hobbies disponibles
     */
    @Override
    public List<Hobby> obtenerTodos() {
        EntityManager em = JpaUtil.getEntityManager();

        try {
            IHobbyDAO hobbyDAO = new HobbyDAO(em);
            return hobbyDAO.listar();
        } finally {
            em.close();
        }
    }

    /**
     * Valida los datos básicos requeridos para un hobby.
     *
     * Verifica: - Que el objeto hobby no sea nulo - Que el nombre no sea nulo o
     * vacío
     *
     * @param hobby hobby a validar
     * @throws ValidacionException si los datos son inválidos
     */
    private void validarDatosHobby(Hobby hobby) {
        if (hobby == null) {
            throw new ValidacionException("El hobby no puede ser nulo.");
        }

        if (hobby.getNombre() == null || hobby.getNombre().isBlank()) {
            throw new ValidacionException("El nombre del hobby no puede ser nulo o vacío.");
        }
    }

}
