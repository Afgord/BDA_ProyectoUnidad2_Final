package com.mycompany.proyectounidad2_persistencia;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import com.mycompany.proyectounidad2_dominio.Estudiante;
import com.mycompany.proyectounidad2_dominio.Reaccion;
import com.mycompany.proyectounidad2_dominio.TipoReaccion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 *
 * @author Afgord
 */
public class ReaccionDAO implements IReaccionDAO {

    private final EntityManager em;

    public ReaccionDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public Reaccion guardar(Reaccion reaccion) {
        em.persist(reaccion);
        return reaccion;
    }

    @Override
    public Reaccion actualizar(Reaccion reaccion) {
        return em.merge(reaccion);
    }

    @Override
    public Reaccion buscarPorEmisorReceptor(Estudiante emisor, Estudiante receptor) {
        String jpql = """
                SELECT r
                FROM Reaccion r
                WHERE r.emisor = :emisor
                  AND r.receptor = :receptor
                """;

        TypedQuery<Reaccion> query = em.createQuery(jpql, Reaccion.class);
        query.setParameter("emisor", emisor);
        query.setParameter("receptor", receptor);

        return query.getResultStream().findFirst().orElse(null);
    }

    @Override
    public List<Estudiante> obtenerLikesPendientes(Long idReceptor) {
        String jpql = """
        SELECT r.emisor
        FROM Reaccion r
        WHERE r.receptor.id = :idReceptor
          AND r.tipo = :tipoLike
          AND NOT EXISTS (
              SELECT r2
              FROM Reaccion r2
              WHERE r2.emisor.id = :idReceptor
                AND r2.receptor.id = r.emisor.id
          )
        """;

        TypedQuery<Estudiante> query = em.createQuery(jpql, Estudiante.class);
        query.setParameter("idReceptor", idReceptor);
        query.setParameter("tipoLike", TipoReaccion.LIKE);
        query.setMaxResults(100);

        return query.getResultList();
    }

    @Override
    public Reaccion buscarPorId(Long id) {
        return em.find(Reaccion.class, id);
    }

    @Override
    public List<Reaccion> listar() {
        String jpql = """
        SELECT r
        FROM Reaccion r
        """;

        TypedQuery<Reaccion> query = em.createQuery(jpql, Reaccion.class);
        query.setMaxResults(100);

        return query.getResultList();
    }

    @Override
    public Reaccion eliminar(Reaccion reaccion) {
        reaccion = em.merge(reaccion);
        em.remove(reaccion);
        return reaccion;
    }

}
