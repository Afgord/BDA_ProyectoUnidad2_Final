package com.mycompany.proyectounidad2_persistencia;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import com.mycompany.proyectounidad2_dominio.Hobby;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 *
 * @author Afgord
 */
public class HobbyDAO implements IHobbyDAO {

    private final EntityManager em;

    public HobbyDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public Hobby guardar(Hobby hobby) {
        em.persist(hobby);
        return hobby;
    }

    @Override
    public Hobby actualizar(Hobby hobby) {
        return em.merge(hobby);
    }

    @Override
    public Hobby buscarPorId(Long id) {
        return em.find(Hobby.class, id);
    }

    @Override
    public Hobby buscarPorNombre(String nombre) {
        String jpql = """
                SELECT h
                FROM Hobby h
                WHERE h.nombre = :nombre
                """;

        TypedQuery<Hobby> query = em.createQuery(jpql, Hobby.class);
        query.setParameter("nombre", nombre);

        return query.getResultStream().findFirst().orElse(null);
    }

    @Override
    public List<Hobby> listar() {
        String jpql = "SELECT h FROM Hobby h";
        TypedQuery<Hobby> query = em.createQuery(jpql, Hobby.class);
        query.setMaxResults(100);

        return query.getResultList();
    }

    @Override
    public Hobby eliminar(Hobby hobby) {
        hobby = em.merge(hobby);
        em.remove(hobby);
        return hobby;
    }

}
