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
 * Implementación del acceso a datos para la entidad Estudiante.
 *
 * Permite realizar operaciones de persistencia, consulta y actualización sobre
 * estudiantes utilizando JPA.
 *
 * Incluye consultas específicas para: - búsqueda por correo - carga de hobbies
 * - exploración de perfiles - búsqueda de hobbies en común
 *
 * @author Afgord
 */
public class EstudianteDAO implements IEstudianteDAO {

    private final EntityManager em;

    public EstudianteDAO(EntityManager em) {
        this.em = em;
    }

    /**
     * Persiste un nuevo estudiante en la base de datos.
     *
     * @param estudiante estudiante a guardar
     * @return estudiante persistido
     */
    @Override
    public Estudiante guardar(Estudiante estudiante) {
        em.persist(estudiante);
        return estudiante;
    }

    /**
     * Busca un estudiante por su identificador.
     *
     * @param id identificador del estudiante
     * @return estudiante encontrado o null si no existe
     */
    @Override
    public Estudiante buscarPorId(Long id) {
        return em.find(Estudiante.class, id);
    }

    /**
     * Busca un estudiante por su correo institucional.
     *
     * @param correoInst correo institucional del estudiante
     * @return estudiante encontrado o null si no existe
     */
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

    /**
     * Actualiza la información de un estudiante existente.
     *
     * @param estudiante estudiante con datos actualizados
     * @return estudiante actualizado
     */
    @Override
    public Estudiante actualizar(Estudiante estudiante) {
        return em.merge(estudiante);
    }

    /**
     * Busca un estudiante por su identificador y carga sus hobbies asociados.
     *
     * @param id identificador del estudiante
     * @return estudiante con hobbies cargados o null si no existe
     */
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

    /**
     * Obtiene estudiantes que comparten al menos un hobby con el estudiante
     * indicado.
     *
     * @param idEstudiante id del estudiante base
     * @return lista de estudiantes con hobbies en común
     */
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
        query.setMaxResults(100);

        return query.getResultList();
    }

    /**
     * Recupera estudiantes disponibles para exploración.
     *
     * Se excluyen: - El propio estudiante - Estudiantes inactivos - Estudiantes
     * con los que ya existe una reacción previa
     *
     * @param idEstudiante id del estudiante base
     * @return lista de estudiantes filtrados
     */
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
        query.setMaxResults(100);

        return query.getResultList();
    }

    /**
     * Obtiene la lista de estudiantes activos registrada en el sistema.
     *
     * El resultado se limita a un máximo de 100 registros.
     *
     * @return lista de estudiantes activos
     */
    @Override
    public List<Estudiante> listar() {
        String jpql = """
    SELECT e
    FROM Estudiante e
    WHERE e.activo = true
    """;

        TypedQuery<Estudiante> query = em.createQuery(jpql, Estudiante.class);
        query.setMaxResults(100);

        return query.getResultList();
    }

    /**
     * Marca la operación de eliminación del estudiante en persistencia.
     *
     * En este proyecto se utiliza para reflejar cambios sobre la entidad, por
     * ejemplo en casos de desactivación lógica.
     *
     * @param estudiante estudiante a actualizar/eliminar lógicamente
     * @return estudiante actualizado
     */
    @Override
    public Estudiante eliminar(Estudiante estudiante) {
        return em.merge(estudiante);
    }

}
