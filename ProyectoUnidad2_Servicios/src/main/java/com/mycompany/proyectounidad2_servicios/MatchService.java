/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectounidad2_servicios;

import com.mycompany.proyectounidad2_dominio.Match;
import com.mycompany.proyectounidad2_exceptions.RecursoNoEncontradoException;
import com.mycompany.proyectounidad2_exceptions.ValidacionException;
import com.mycompany.proyectounidad2_persistencia.EstudianteDAO;
import com.mycompany.proyectounidad2_persistencia.IMatchDAO;
import com.mycompany.proyectounidad2_persistencia.MatchDAO;
import com.mycompany.proyectounidad2_utils.JpaUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 *
 * @author Afgord
 */
public class MatchService implements IMatchService {

    @Override
    public List<Match> obtenerMatchesDeEstudiante(Long idEstudiante) {
        if (idEstudiante == null) {
            throw new ValidacionException("El id del estudiante no puede ser nulo.");
        }

        EntityManager em = JpaUtil.getEntityManager();

        try {
            EstudianteDAO estudianteDAO = new EstudianteDAO(em);
            if (estudianteDAO.buscarPorId(idEstudiante) == null) {
                throw new RecursoNoEncontradoException("No existe un estudiante con ese id.");
            }

            IMatchDAO matchDAO = new MatchDAO(em);
            return matchDAO.buscarMatchesDeEstudiante(idEstudiante);
        } finally {
            em.close();
        }
    }
}
