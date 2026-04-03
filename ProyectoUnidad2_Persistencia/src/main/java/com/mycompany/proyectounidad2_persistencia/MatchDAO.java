package com.mycompany.proyectounidad2_persistencia;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import com.mycompany.proyectounidad2_dominio.Estudiante;
import com.mycompany.proyectounidad2_dominio.Match;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Implementación del acceso a datos para la entidad Match.
 *
 * Permite gestionar la persistencia, consulta y eliminación de relaciones de
 * tipo match entre estudiantes.
 *
 * Un match representa una relación mutua de LIKE entre dos estudiantes.
 *
 * @author Afgord
 */
public class MatchDAO implements IMatchDAO {

    private final EntityManager em;

    public MatchDAO(EntityManager em) {
        this.em = em;
    }

    /**
     * Persiste un nuevo match en la base de datos.
     *
     * @param match match a guardar
     * @return match persistido
     */
    @Override
    public Match guardar(Match match) {
        em.persist(match);
        return match;
    }

    /**
     * Busca un match existente entre dos estudiantes.
     *
     * La relación es simétrica, por lo que se evalúan ambas combinaciones
     * (estudiante1-estudiante2 y estudiante2-estudiante1).
     *
     * @param estudiante1 primer estudiante
     * @param estudiante2 segundo estudiante
     * @return match si existe, null en caso contrario
     */
    @Override
    public Match buscarMatchEntre(Estudiante estudiante1, Estudiante estudiante2) {
        String jpql = """
                SELECT m
                FROM Match m
                WHERE (m.estudiante1 = :estudiante1 AND m.estudiante2 = :estudiante2)
                   OR (m.estudiante1 = :estudiante2 AND m.estudiante2 = :estudiante1)
                """;

        TypedQuery<Match> query = em.createQuery(jpql, Match.class);
        query.setParameter("estudiante1", estudiante1);
        query.setParameter("estudiante2", estudiante2);

        return query.getResultStream().findFirst().orElse(null);
    }

    /**
     * Obtiene todos los matches asociados a un estudiante.
     *
     * Se utiliza JOIN FETCH para cargar de manera anticipada los datos de ambos
     * estudiantes involucrados en el match, evitando problemas de carga
     * diferida (LazyInitializationException).
     *
     * El resultado se limita a un máximo de 100 registros.
     *
     * @param idEstudiante id del estudiante
     * @return lista de matches en los que participa
     */
    @Override
    public List<Match> buscarMatchesDeEstudiante(Long idEstudiante) {
        String jpql = """
        SELECT m
        FROM Match m
        JOIN FETCH m.estudiante1
        JOIN FETCH m.estudiante2
        WHERE m.estudiante1.id = :id
           OR m.estudiante2.id = :id
    """;

        TypedQuery<Match> query = em.createQuery(jpql, Match.class);
        query.setParameter("id", idEstudiante);
        query.setMaxResults(100);

        return query.getResultList();
    }

    /**
     * Busca un match por su identificador.
     *
     * @param id identificador del match
     * @return match encontrado o null si no existe
     */
    @Override
    public Match buscarPorId(Long id) {
        return em.find(Match.class, id);
    }

    /**
     * Obtiene la lista de matches registrados en el sistema.
     *
     * El resultado se limita a un máximo de 100 registros.
     *
     * @return lista de matches
     */
    @Override
    public List<Match> listar() {
        String jpql = """
        SELECT m
        FROM Match m
        """;

        TypedQuery<Match> query = em.createQuery(jpql, Match.class);
        query.setMaxResults(100);

        return query.getResultList();
    }

    /**
     * Actualiza la información de un match existente.
     *
     * @param match match con datos actualizados
     * @return match actualizado
     */
    @Override
    public Match actualizar(Match match) {
        return em.merge(match);
    }

    /**
     * Elimina un match de la base de datos.
     *
     * @param match match a eliminar
     * @return match eliminado
     */
    @Override
    public Match eliminar(Match match) {
        match = em.merge(match);
        em.remove(match);
        return match;
    }
}
