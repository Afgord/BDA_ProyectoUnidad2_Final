/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectounidad2_servicios;

import com.mycompany.proyectounidad2_dominio.Estudiante;
import com.mycompany.proyectounidad2_dominio.Match;
import com.mycompany.proyectounidad2_dominio.Reaccion;
import com.mycompany.proyectounidad2_dominio.TipoReaccion;
import com.mycompany.proyectounidad2_exceptions.NegocioException;
import com.mycompany.proyectounidad2_exceptions.ReglaNegocioException;
import com.mycompany.proyectounidad2_exceptions.ValidacionException;
import com.mycompany.proyectounidad2_persistencia.IMatchDAO;
import com.mycompany.proyectounidad2_persistencia.IReaccionDAO;
import com.mycompany.proyectounidad2_persistencia.MatchDAO;
import com.mycompany.proyectounidad2_persistencia.ReaccionDAO;
import com.mycompany.proyectounidad2_utils.JpaUtil;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;

/**
 *
 * @author Afgord
 */
public class ReaccionService implements IReaccionService {

    @Override
    public Reaccion registrarReaccion(Estudiante emisor, Estudiante receptor, TipoReaccion tipo) {
        validarDatos(emisor, receptor, tipo);

        EntityManager em = JpaUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            IReaccionDAO reaccionDAO = new ReaccionDAO(em);
            IMatchDAO matchDAO = new MatchDAO(em);

            Reaccion reaccion = crearOActualizarReaccion(reaccionDAO, emisor, receptor, tipo);

            if (reaccion.getTipo() == TipoReaccion.LIKE) {
                verificarYCrearMatchSiAplica(reaccionDAO, matchDAO, emisor, receptor);
            }

            em.getTransaction().commit();
            return reaccion;
        } catch (NegocioException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new NegocioException("Error al registrar la reacción.");
        } finally {
            em.close();
        }
    }

    private void validarDatos(Estudiante emisor, Estudiante receptor, TipoReaccion tipo) {
        if (emisor == null) {
            throw new ValidacionException("El emisor no puede ser nulo.");
        }

        if (receptor == null) {
            throw new ValidacionException("El receptor no puede ser nulo.");
        }

        if (tipo == null) {
            throw new ValidacionException("El tipo de reacción no puede ser nulo.");
        }

        if (emisor.getId() == null || receptor.getId() == null) {
            throw new ValidacionException("Ambos estudiantes deben tener un ID válido.");
        }

        if (emisor.getId().equals(receptor.getId())) {
            throw new ReglaNegocioException("Un estudiante no puede reaccionar a sí mismo.");
        }
    }

    private Reaccion crearOActualizarReaccion(IReaccionDAO reaccionDAO,
            Estudiante emisor, Estudiante receptor, TipoReaccion tipo) {

        Reaccion reaccionExistente = reaccionDAO.buscarPorEmisorReceptor(emisor, receptor);

        if (reaccionExistente == null) {
            Reaccion nuevaReaccion = new Reaccion(tipo, LocalDate.now(), emisor, receptor);
            return reaccionDAO.guardar(nuevaReaccion);
        }

        reaccionExistente.setTipo(tipo);
        reaccionExistente.setFecha(LocalDate.now());
        return reaccionDAO.actualizar(reaccionExistente);
    }

    private void verificarYCrearMatchSiAplica(IReaccionDAO reaccionDAO, IMatchDAO matchDAO,
            Estudiante emisor, Estudiante receptor) {

        Reaccion reaccionInversa = reaccionDAO.buscarPorEmisorReceptor(receptor, emisor);

        if (reaccionInversa != null && reaccionInversa.getTipo() == TipoReaccion.LIKE) {
            Match matchExistente = matchDAO.buscarMatchEntre(emisor, receptor);

            if (matchExistente == null) {
                Estudiante estudiante1 = emisor;
                Estudiante estudiante2 = receptor;

                if (estudiante1.getId() > estudiante2.getId()) {
                    estudiante1 = receptor;
                    estudiante2 = emisor;
                }

                Match nuevoMatch = new Match(LocalDate.now(), estudiante1, estudiante2);
                matchDAO.guardar(nuevoMatch);
            }
        }
    }
}
