package com.mycompany.proyectounidad2_persistencia;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import com.mycompany.proyectounidad2_dominio.Estudiante;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 *
 * @author Afgord
 */
public class EstudianteDAO implements IEstudianteDAO {

    private final EntityManager em;

    public EstudianteDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public Estudiante guardar(Estudiante estudiante) {
        em.persist(estudiante);
        return estudiante;
    }

    @Override
    public Estudiante buscarPorId(Long id) {
        return em.find(Estudiante.class, id);
    }

    @Override
    public Estudiante buscarPorCorreo(String correoInst) {
        String jpql = """
                SELECT e
                FROM Estudiante e
                WHERE e.correoInst = :correoInst
                """;

        TypedQuery<Estudiante> query = em.createQuery(jpql, Estudiante.class);
        query.setParameter("correoInst", correoInst);

        return query.getResultStream().findFirst().orElse(null);
    }

    @Override
    public Estudiante actualizar(Estudiante estudiante) {
        return em.merge(estudiante);
    }

    @Override
    public Estudiante buscarPorIdConHobbies(Long id) {
        String jpql = """
        SELECT e
        FROM Estudiante e
        LEFT JOIN FETCH e.hobbies
        WHERE e.id = :id
        """;

        TypedQuery<Estudiante> query = em.createQuery(jpql, Estudiante.class);
        query.setParameter("id", id);

        return query.getResultStream().findFirst().orElse(null);
    }

    @Override
    public List<Estudiante> buscarConHobbiesEnComun(Long idEstudiante) {
        String jpql = """
    SELECT DISTINCT e2
    FROM Estudiante e1
    JOIN e1.hobbies h
    JOIN h.estudiantes e2
    WHERE e1.id = :idEstudiante
      AND e2.id <> :idEstudiante
      AND e2.activo = true
    """;

        TypedQuery<Estudiante> query = em.createQuery(jpql, Estudiante.class);
        query.setParameter("idEstudiante", idEstudiante);

        return query.getResultList();
    }

    @Override
    public List<Estudiante> explorarPerfiles(Long idEstudiante) {
        String jpql = """
    SELECT e
    FROM Estudiante e
    WHERE e.id <> :idEstudiante
      AND e.activo = true
      AND e.id NOT IN (
          SELECT r.receptor.id
          FROM Reaccion r
          WHERE r.emisor.id = :idEstudiante
      )
    """;

        TypedQuery<Estudiante> query = em.createQuery(jpql, Estudiante.class);
        query.setParameter("idEstudiante", idEstudiante);

        return query.getResultList();
    }

}
