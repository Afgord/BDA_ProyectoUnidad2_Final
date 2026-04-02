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
 *
 * @author Afgord
 */
public class MatchDAO implements IMatchDAO {

    private final EntityManager em;

    public MatchDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public Match guardar(Match match) {
        em.persist(match);
        return match;
    }

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

    @Override
    public Match buscarPorId(Long id) {
        return em.find(Match.class, id);
    }

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

    @Override
    public Match actualizar(Match match) {
        return em.merge(match);
    }

    @Override
    public Match eliminar(Match match) {
        match = em.merge(match);
        em.remove(match);
        return match;
    }

}
