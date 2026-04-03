/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectounidad2_servicios;

import com.mycompany.proyectounidad2_dominio.Match;
import com.mycompany.proyectounidad2_exceptions.RecursoNoEncontradoException;
import com.mycompany.proyectounidad2_exceptions.ValidacionException;
import com.mycompany.proyectounidad2_persistencia.EstudianteDAO;
import com.mycompany.proyectounidad2_persistencia.IEstudianteDAO;
import com.mycompany.proyectounidad2_persistencia.IMatchDAO;
import com.mycompany.proyectounidad2_persistencia.MatchDAO;
import com.mycompany.proyectounidad2_utils.JpaUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 * Implementación de la lógica de negocio para la entidad Match.
 *
 * Gestiona la consulta de matches generados entre estudiantes cuando existe
 * interés mutuo mediante reacciones de tipo LIKE.
 *
 * Coordina validaciones básicas y delega la recuperación de datos a la capa de
 * persistencia.
 *
 * @author Afgord
 */
public class MatchService implements IMatchService {

    /**
     * Obtiene todos los matches asociados a un estudiante.
     *
     * Un match representa una relación mutua de LIKE entre dos estudiantes.
     *
     * @param idEstudiante id del estudiante
     * @return lista de matches en los que participa
     * @throws ValidacionException si el id es nulo
     * @throws RecursoNoEncontradoException si el estudiante no existe
     */
    @Override
    public List<Match> obtenerMatchesDeEstudiante(Long idEstudiante) {
        if (idEstudiante == null) {
            throw new ValidacionException("El id del estudiante no puede ser nulo.");
        }

        EntityManager em = JpaUtil.getEntityManager();

        try {
            IEstudianteDAO estudianteDAO = new EstudianteDAO(em);
            IMatchDAO matchDAO = new MatchDAO(em);

            if (estudianteDAO.buscarPorId(idEstudiante) == null) {
                throw new RecursoNoEncontradoException("No existe un estudiante con ese id.");
            }

            return matchDAO.buscarMatchesDeEstudiante(idEstudiante);
        } finally {
            em.close();
        }
    }
}
