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
 * Implementación del acceso a datos para la entidad Reaccion.
 *
 * Permite persistir, actualizar, consultar y eliminar reacciones entre
 * estudiantes, así como obtener los likes pendientes de respuesta.
 *
 * @author Afgord
 */
public class ReaccionDAO implements IReaccionDAO {

    private final EntityManager em;

    public ReaccionDAO(EntityManager em) {
        this.em = em;
    }

    /**
     * Persiste una nueva reacción en la base de datos.
     *
     * @param reaccion reacción a guardar
     * @return reacción persistida
     */
    @Override
    public Reaccion guardar(Reaccion reaccion) {
        em.persist(reaccion);
        return reaccion;
    }

    /**
     * Actualiza una reacción existente en la base de datos.
     *
     * @param reaccion reacción con datos actualizados
     * @return reacción actualizada
     */
    @Override
    public Reaccion actualizar(Reaccion reaccion) {
        return em.merge(reaccion);
    }

    /**
     * Busca una reacción específica entre un estudiante emisor y un receptor.
     *
     * @param emisor estudiante que emite la reacción
     * @param receptor estudiante que recibe la reacción
     * @return reacción encontrada o null si no existe
     */
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

    /**
     * Obtiene los estudiantes que han dado LIKE al receptor y que aún no han
     * recibido una respuesta por parte de este.
     *
     * El resultado se limita a un máximo de 100 registros.
     *
     * @param idReceptor id del estudiante receptor
     * @return lista de estudiantes emisores con likes pendientes
     */
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

    /**
     * Busca una reacción por su identificador.
     *
     * @param id identificador de la reacción
     * @return reacción encontrada o null si no existe
     */
    @Override
    public Reaccion buscarPorId(Long id) {
        return em.find(Reaccion.class, id);
    }

    /**
     * Obtiene la lista de reacciones registradas en el sistema.
     *
     * El resultado se limita a un máximo de 100 registros.
     *
     * @return lista de reacciones
     */
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

    /**
     * Elimina una reacción existente de la base de datos.
     *
     * @param reaccion reacción a eliminar
     * @return reacción eliminada
     */
    @Override
    public Reaccion eliminar(Reaccion reaccion) {
        reaccion = em.merge(reaccion);
        em.remove(reaccion);
        return reaccion;
    }

}
