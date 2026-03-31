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
 *
 * @author Afgord
 */
public class HobbyService implements IHobbyService {

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

    @Override
    public List<Hobby> obtenerTodos() {
        EntityManager em = JpaUtil.getEntityManager();

        try {
            IHobbyDAO hobbyDAO = new HobbyDAO(em);
            return hobbyDAO.obtenerTodos();
        } finally {
            em.close();
        }
    }

    private void validarDatosHobby(Hobby hobby) {
        if (hobby == null) {
            throw new ValidacionException("El hobby no puede ser nulo.");
        }

        if (hobby.getNombre() == null || hobby.getNombre().isBlank()) {
            throw new ValidacionException("El nombre del hobby no puede ser nulo o vacío.");
        }
    }

}
