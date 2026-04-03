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
 * Implementación del acceso a datos para la entidad Hobby.
 *
 * Permite realizar operaciones de persistencia, consulta, actualización y
 * eliminación de hobbies en el sistema.
 *
 * Los hobbies son reutilizables y pueden ser asociados a múltiples estudiantes
 * mediante una relación muchos a muchos.
 *
 * @author Afgord
 */
public class HobbyDAO implements IHobbyDAO {

    private final EntityManager em;

    public HobbyDAO(EntityManager em) {
        this.em = em;
    }

    /**
     * Persiste un nuevo hobby en la base de datos.
     *
     * @param hobby hobby a guardar
     * @return hobby persistido
     */
    @Override
    public Hobby guardar(Hobby hobby) {
        em.persist(hobby);
        return hobby;
    }

    /**
     * Actualiza la información de un hobby existente.
     *
     * @param hobby hobby con datos actualizados
     * @return hobby actualizado
     */
    @Override
    public Hobby actualizar(Hobby hobby) {
        return em.merge(hobby);
    }

    /**
     * Busca un hobby por su identificador.
     *
     * @param id identificador del hobby
     * @return hobby encontrado o null si no existe
     */
    @Override
    public Hobby buscarPorId(Long id) {
        return em.find(Hobby.class, id);
    }

    /**
     * Busca un hobby por su nombre.
     *
     * Se utiliza principalmente para evitar registros duplicados al momento de
     * crear nuevos hobbies.
     *
     * @param nombre nombre del hobby
     * @return hobby encontrado o null si no existe
     */
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

    /**
     * Obtiene la lista de hobbies registrados en el sistema.
     *
     * El resultado se limita a un máximo de 100 registros.
     *
     * @return lista de hobbies
     */
    @Override
    public List<Hobby> listar() {
        String jpql = "SELECT h FROM Hobby h";
        TypedQuery<Hobby> query = em.createQuery(jpql, Hobby.class);
        query.setMaxResults(100);

        return query.getResultList();
    }

    /**
     * Elimina un hobby de la base de datos.
     *
     * Nota: En este sistema, los hobbies normalmente no se eliminan, ya que
     * pueden estar asociados a múltiples estudiantes. Esta operación se incluye
     * por completitud del DAO.
     *
     * @param hobby hobby a eliminar
     * @return hobby eliminado
     */
    @Override
    public Hobby eliminar(Hobby hobby) {
        hobby = em.merge(hobby);
        em.remove(hobby);
        return hobby;
    }

}
