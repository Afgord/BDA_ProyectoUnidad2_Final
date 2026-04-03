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
import com.mycompany.proyectounidad2_exceptions.RecursoNoEncontradoException;
import com.mycompany.proyectounidad2_exceptions.ReglaNegocioException;
import com.mycompany.proyectounidad2_exceptions.ValidacionException;
import com.mycompany.proyectounidad2_persistencia.EstudianteDAO;
import com.mycompany.proyectounidad2_persistencia.IEstudianteDAO;
import com.mycompany.proyectounidad2_persistencia.IMatchDAO;
import com.mycompany.proyectounidad2_persistencia.IReaccionDAO;
import com.mycompany.proyectounidad2_persistencia.MatchDAO;
import com.mycompany.proyectounidad2_persistencia.ReaccionDAO;
import com.mycompany.proyectounidad2_utils.JpaUtil;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

/**
 * Implementación de la lógica de negocio para la entidad Reaccion.
 *
 * Gestiona: - Registro y actualización de reacciones (LIKE / NO_INTERESA) -
 * Validación de reglas de negocio - Generación automática de matches - Consulta
 * de likes pendientes
 *
 * Coordina la interacción entre las entidades Reaccion y Match, asegurando la
 * consistencia de las relaciones entre estudiantes.
 *
 * @author Afgord
 */
public class ReaccionService implements IReaccionService {

    /**
     * Registra una reacción entre dos estudiantes.
     *
     * Tipos de reacción: - LIKE - NO_INTERESA
     *
     * Reglas: - No se permite reaccionar a sí mismo - Si ya existe una
     * reacción, se actualiza - Si ambos estudiantes se dan LIKE mutuamente, se
     * genera un Match
     *
     * @param emisor estudiante que realiza la reacción
     * @param receptor estudiante que recibe la reacción
     * @param tipo tipo de reacción
     * @return reacción registrada o actualizada
     * @throws ValidacionException si los datos son inválidos
     */
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

    /**
     * Valida los datos necesarios para registrar una reacción.
     *
     * Verifica: - Que los objetos no sean nulos - Que ambos estudiantes tengan
     * ID válido - Que no se trate de una auto-reacción - Que ambos estudiantes
     * estén activos
     *
     * @param emisor estudiante que emite la reacción
     * @param receptor estudiante que recibe la reacción
     * @param tipo tipo de reacción
     * @throws ValidacionException si los datos son inválidos
     * @throws ReglaNegocioException si se viola una regla de negocio
     */
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

        if (!emisor.isActivo()) {
            throw new ReglaNegocioException("El emisor no tiene una cuenta activa.");
        }

        if (!receptor.isActivo()) {
            throw new ReglaNegocioException("El receptor no tiene una cuenta activa.");
        }
    }

    /**
     * Crea una nueva reacción o actualiza una existente.
     *
     * Si ya existe una reacción entre el emisor y el receptor: - Se actualiza
     * el tipo y la fecha
     *
     * Si no existe: - Se crea una nueva reacción
     *
     * @param reaccionDAO acceso a datos de reacciones
     * @param emisor estudiante emisor
     * @param receptor estudiante receptor
     * @param tipo tipo de reacción
     * @return reacción creada o actualizada
     */
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

    /**
     * Verifica si existe una reacción inversa tipo LIKE y, en caso afirmativo,
     * crea un match entre los estudiantes.
     *
     * Reglas: - Debe existir una reacción previa en sentido contrario - Ambas
     * deben ser de tipo LIKE - No debe existir ya un match entre ellos
     *
     * Para evitar duplicidad, se ordenan los estudiantes por ID.
     *
     * @param reaccionDAO acceso a datos de reacciones
     * @param matchDAO acceso a datos de matches
     * @param emisor estudiante que realizó la reacción
     * @param receptor estudiante receptor
     */
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

    /**
     * Obtiene los estudiantes que han dado LIKE al usuario actual y que aún no
     * han sido respondidos.
     *
     * Valida que el estudiante exista y esté activo.
     *
     * @param idEstudiante id del estudiante receptor
     * @return lista de estudiantes con likes pendientes
     * @throws ValidacionException si el id es nulo
     * @throws RecursoNoEncontradoException si el estudiante no existe
     * @throws ReglaNegocioException si la cuenta está desactivada
     */
    @Override
    public List<Estudiante> obtenerLikesPendientes(Long idEstudiante) {
        if (idEstudiante == null) {
            throw new ValidacionException("El id del estudiante no puede ser nulo.");
        }

        EntityManager em = JpaUtil.getEntityManager();

        try {

            IEstudianteDAO estudianteDAO = new EstudianteDAO(em);
            Estudiante estudiante = estudianteDAO.buscarPorId(idEstudiante);

            if (estudiante == null) {
                throw new RecursoNoEncontradoException("No existe un estudiante con ese id.");
            }

            if (!estudiante.isActivo()) {
                throw new ReglaNegocioException("La cuenta del estudiante está desactivada.");
            }

            IReaccionDAO reaccionDAO = new ReaccionDAO(em);
            return reaccionDAO.obtenerLikesPendientes(idEstudiante);

        } catch (NegocioException e) {
            throw e;
        } catch (Exception e) {
            throw new NegocioException("Error al obtener likes pendientes.");
        } finally {
            em.close();
        }
    }

    /**
     * Obtiene la cantidad de likes pendientes para un estudiante.
     *
     * @param idEstudiante id del estudiante
     * @return número de likes pendientes
     */
    @Override
    public int contarLikesPendientes(Long idEstudiante) {
        return obtenerLikesPendientes(idEstudiante).size();
    }
}
