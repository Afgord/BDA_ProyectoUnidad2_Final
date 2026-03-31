package com.mycompany.proyectounidad2_persistencia;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import com.mycompany.proyectounidad2_dominio.Estudiante;
import com.mycompany.proyectounidad2_dominio.Reaccion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

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

}
