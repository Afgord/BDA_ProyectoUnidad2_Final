/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectounidad2_servicios;

import com.mycompany.proyectounidad2_dominio.Estudiante;
import com.mycompany.proyectounidad2_dominio.Hobby;
import com.mycompany.proyectounidad2_exceptions.AutenticacionException;
import com.mycompany.proyectounidad2_exceptions.NegocioException;
import com.mycompany.proyectounidad2_exceptions.RecursoNoEncontradoException;
import com.mycompany.proyectounidad2_exceptions.ReglaNegocioException;
import com.mycompany.proyectounidad2_exceptions.ValidacionException;
import com.mycompany.proyectounidad2_persistencia.EstudianteDAO;
import com.mycompany.proyectounidad2_persistencia.HobbyDAO;
import com.mycompany.proyectounidad2_persistencia.IEstudianteDAO;
import com.mycompany.proyectounidad2_persistencia.IHobbyDAO;
import com.mycompany.proyectounidad2_utils.JpaUtil;
import com.mycompany.proyectounidad2_utils.PasswordUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 * Implementación de la lógica de negocio para la entidad Estudiante.
 *
 * Esta clase gestiona: - Registro de estudiantes - Autenticación (inicio de
 * sesión) - Administración de perfil - Gestión de hobbies - Exploración de
 * perfiles - Desactivación de cuentas
 *
 * Aplica validaciones de negocio, control de transacciones y coordinación con
 * la capa de persistencia.
 *
 * @author Afgord
 */
public class EstudianteService implements IEstudianteService {

    /**
     * Registra un nuevo estudiante en el sistema.
     *
     * Realiza las siguientes validaciones: - El objeto estudiante no sea nulo -
     * Normaliza el correo institucional (trim y lowercase) - Valida datos
     * obligatorios y formato de correo institucional - Verifica que no exista
     * otro estudiante con el mismo correo
     *
     * La contraseña es encriptada antes de persistirse.
     *
     * @param estudiante objeto con los datos del estudiante a registrar
     * @return estudiante persistido en base de datos
     * @throws ValidacionException si los datos no cumplen las reglas
     * @throws ReglaNegocioException si el correo ya está registrado
     */
    @Override
    public Estudiante registrarEstudiante(Estudiante estudiante) {

        if (estudiante == null) {
            throw new ValidacionException("El estudiante no puede ser nulo.");
        }

        String correo = estudiante.getCorreoInst();

        if (correo != null) {
            correo = correo.trim().toLowerCase();
            estudiante.setCorreoInst(correo);
        }

        validarDatosEstudiante(estudiante);

        EntityManager em = JpaUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            IEstudianteDAO estudianteDAO = new EstudianteDAO(em);

            Estudiante existente = estudianteDAO.buscarPorCorreo(estudiante.getCorreoInst());

            if (existente != null) {
                throw new ReglaNegocioException("Ya existe un estudiante registrado con ese correo institucional.");
            }

            String hash = PasswordUtil.hashPassword(estudiante.getPassword());
            estudiante.setPassword(hash);

            Estudiante guardado = estudianteDAO.guardar(estudiante);

            em.getTransaction().commit();
            return guardado;

        } catch (NegocioException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new NegocioException("Error al registrar el estudiante.");
        } finally {
            em.close();
        }
    }

    /**
     * Busca un estudiante por su correo institucional.
     *
     * El correo es normalizado (trim y lowercase) antes de la consulta.
     *
     * @param correoInst correo institucional
     * @return estudiante encontrado o null si no existe
     * @throws ValidacionException si el correo es nulo o vacío
     */
    @Override
    public Estudiante buscarPorCorreo(String correoInst) {
        if (correoInst == null || correoInst.isBlank()) {
            throw new ValidacionException("El correo institucional no puede ser nulo o vacío.");
        }

        correoInst = correoInst.trim().toLowerCase();

        EntityManager em = JpaUtil.getEntityManager();

        try {
            IEstudianteDAO estudianteDAO = new EstudianteDAO(em);
            return estudianteDAO.buscarPorCorreo(correoInst);
        } finally {
            em.close();
        }
    }

    private void validarDatosEstudiante(Estudiante estudiante) {

        if (estudiante == null) {
            throw new ValidacionException("El estudiante no puede ser nulo.");
        }

        if (estudiante.getNombre() == null || estudiante.getNombre().isBlank()) {
            throw new ValidacionException("El nombre no puede ser nulo o vacío.");
        }
        String nombre = estudiante.getNombre().trim().replaceAll("\\s+", " ");
        if (!nombre.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) {
            throw new ValidacionException("El nombre solo puede contener letras y espacios.");
        }
        estudiante.setNombre(nombre);

        if (estudiante.getApPat() == null || estudiante.getApPat().isBlank()) {
            throw new ValidacionException("El apellido paterno no puede ser nulo o vacío.");
        }
        String apPat = estudiante.getApPat().trim().replaceAll("\\s+", " ");
        if (!apPat.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) {
            throw new ValidacionException("El apellido paterno solo puede contener letras y espacios.");
        }
        estudiante.setApPat(apPat);

        if (estudiante.getApMat() == null || estudiante.getApMat().isBlank()) {
            throw new ValidacionException("El apellido materno no puede ser nulo o vacío.");
        }
        String apMat = estudiante.getApMat().trim().replaceAll("\\s+", " ");
        if (!apMat.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) {
            throw new ValidacionException("El apellido materno solo puede contener letras y espacios.");
        }
        estudiante.setApMat(apMat);

        if (estudiante.getCorreoInst() == null || estudiante.getCorreoInst().isBlank()) {
            throw new ValidacionException("El correo institucional no puede ser nulo o vacío.");
        }

        if (!esCorreoInstitucionalValido(estudiante.getCorreoInst())) {
            throw new ValidacionException("El correo institucional no tiene un formato válido. Debe ser usuario@potros.itson.edu.mx");
        }

        if (estudiante.getPassword() == null || estudiante.getPassword().isBlank()) {
            throw new ValidacionException("La contraseña no puede ser nula o vacía.");
        }

        if (!esPasswordRobusto(estudiante.getPassword())) {
            throw new ValidacionException(
                    "La contraseña debe tener al menos 6 caracteres, una mayúscula, una minúscula y un número."
            );
        }

        if (estudiante.getCarrera() == null || estudiante.getCarrera().isBlank()) {
            throw new ValidacionException("La carrera no puede ser nula o vacía.");
        }
    }

    /**
     * Permite a un estudiante iniciar sesión en el sistema.
     *
     * Valida: - Que el correo y contraseña no sean nulos o vacíos - Que el
     * estudiante exista - Que la cuenta esté activa - Que la contraseña
     * coincida (usando hash)
     *
     * @param correoInst correo institucional del estudiante
     * @param password contraseña en texto plano
     * @return estudiante autenticado
     * @throws ValidacionException si los datos son inválidos
     * @throws RecursoNoEncontradoException si el estudiante no existe
     * @throws ReglaNegocioException si la cuenta está desactivada
     * @throws AutenticacionException si la contraseña es incorrecta
     */
    @Override
    public Estudiante iniciarSesion(String correoInst, String password) {
        if (correoInst == null || correoInst.isBlank()) {
            throw new ValidacionException("El correo institucional no puede ser nulo o vacío.");
        }

        correoInst = correoInst.trim().toLowerCase();

        if (password == null || password.isBlank()) {
            throw new ValidacionException("La contraseña no puede ser nula o vacía.");
        }

        EntityManager em = JpaUtil.getEntityManager();

        try {
            IEstudianteDAO estudianteDAO = new EstudianteDAO(em);

            Estudiante estudiante = estudianteDAO.buscarPorCorreo(correoInst);

            if (estudiante == null) {
                throw new RecursoNoEncontradoException("No existe un estudiante con ese correo institucional.");
            }

            if (!estudiante.isActivo()) {
                throw new ReglaNegocioException("La cuenta del estudiante está desactivada.");
            }

            boolean passwordCorrecto = PasswordUtil.verificarPassword(password, estudiante.getPassword());

            if (!passwordCorrecto) {
                throw new AutenticacionException("Contraseña incorrecta.");
            }

            return estudiante;

        } finally {
            em.close();
        }
    }

    /**
     * Asigna un hobby a un estudiante.
     *
     * Valida: - Existencia del estudiante - Existencia del hobby - Que el hobby
     * no esté ya asignado al estudiante
     *
     * @param idEstudiante id del estudiante
     * @param idHobby id del hobby a asignar
     * @return estudiante actualizado
     * @throws ValidacionException si los ids son inválidos
     * @throws ReglaNegocioException si el hobby ya está asignado
     */
    @Override
    public Estudiante agregarHobby(Long idEstudiante, Long idHobby) {
        if (idEstudiante == null) {
            throw new ValidacionException("El id del estudiante no puede ser nulo.");
        }

        if (idHobby == null) {
            throw new ValidacionException("El id del hobby no puede ser nulo.");
        }

        EntityManager em = JpaUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            IEstudianteDAO estudianteDAO = new EstudianteDAO(em);
            IHobbyDAO hobbyDAO = new HobbyDAO(em);

            Estudiante estudiante = obtenerEstudianteActivoPorId(estudianteDAO, idEstudiante);

            Hobby hobby = hobbyDAO.buscarPorId(idHobby);
            if (hobby == null) {
                throw new RecursoNoEncontradoException("No existe un hobby con ese id.");
            }

            if (estudiante.getHobbies().contains(hobby)) {
                throw new ReglaNegocioException("El estudiante ya tiene asignado ese hobby.");
            }

            estudiante.getHobbies().add(hobby);

            Estudiante actualizado = estudianteDAO.actualizar(estudiante);

            em.getTransaction().commit();
            return actualizado;

        } catch (NegocioException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new NegocioException("Error al agregar hobby al estudiante.");
        } finally {
            em.close();
        }
    }

    /**
     * Busca un estudiante por su identificador.
     *
     * @param id identificador del estudiante
     * @return estudiante encontrado
     * @throws ValidacionException si el id es nulo
     * @throws RecursoNoEncontradoException si no existe el estudiante
     */
    @Override
    public Estudiante buscarPorId(Long id) {
        if (id == null) {
            throw new ValidacionException("El id del estudiante no puede ser nulo.");
        }

        EntityManager em = JpaUtil.getEntityManager();

        try {
            IEstudianteDAO estudianteDAO = new EstudianteDAO(em);
            Estudiante estudiante = estudianteDAO.buscarPorId(id);

            if (estudiante == null) {
                throw new RecursoNoEncontradoException("No existe un estudiante con ese id.");
            }

            return estudiante;
        } finally {
            em.close();
        }
    }

    /**
     * Busca un estudiante por su identificador cargando sus hobbies.
     *
     * Valida que el estudiante exista y esté activo.
     *
     * @param id identificador del estudiante
     * @return estudiante con hobbies cargados
     * @throws ValidacionException si el id es nulo
     * @throws RecursoNoEncontradoException si no existe
     * @throws ReglaNegocioException si la cuenta está desactivada
     */
    @Override
    public Estudiante buscarPorIdConHobbies(Long id) {
        if (id == null) {
            throw new ValidacionException("El id no puede ser nulo.");
        }

        EntityManager em = JpaUtil.getEntityManager();

        try {
            IEstudianteDAO estudianteDAO = new EstudianteDAO(em);

            Estudiante estudiante = estudianteDAO.buscarPorIdConHobbies(id);

            if (estudiante == null) {
                throw new RecursoNoEncontradoException("No existe un estudiante con ese id.");
            }

            if (!estudiante.isActivo()) {
                throw new ReglaNegocioException("La cuenta del estudiante está desactivada.");
            }

            return estudiante;

        } finally {
            em.close();
        }
    }

    /**
     * Obtiene estudiantes que comparten al menos un hobby con el estudiante
     * indicado.
     *
     * @param idEstudiante id del estudiante base
     * @return lista de estudiantes con hobbies en común
     * @throws ValidacionException si el id es nulo
     * @throws RecursoNoEncontradoException si el estudiante no existe o está
     * inactivo
     */
    @Override
    public List<Estudiante> buscarConHobbiesEnComun(Long idEstudiante) {
        if (idEstudiante == null) {
            throw new ValidacionException("El id del estudiante no puede ser nulo.");
        }

        EntityManager em = JpaUtil.getEntityManager();

        try {
            IEstudianteDAO estudianteDAO = new EstudianteDAO(em);

            obtenerEstudianteActivoPorId(estudianteDAO, idEstudiante);

            return estudianteDAO.buscarConHobbiesEnComun(idEstudiante);

        } finally {
            em.close();
        }
    }

    /**
     * Obtiene una lista de perfiles disponibles para explorar.
     *
     * Se excluyen: - El propio estudiante - Estudiantes inactivos - Estudiantes
     * a los que ya se les ha dado reacción (LIKE o NO_INTERESA)
     *
     * @param idEstudiante identificador del estudiante actual
     * @return lista de estudiantes disponibles para explorar
     * @throws ValidacionException si el id es nulo
     * @throws RecursoNoEncontradoException si el estudiante no existe o está
     * inactivo
     */
    @Override
    public List<Estudiante> explorarPerfiles(Long idEstudiante) {
        if (idEstudiante == null) {
            throw new ValidacionException("El id del estudiante no puede ser nulo.");
        }

        EntityManager em = JpaUtil.getEntityManager();

        try {
            IEstudianteDAO estudianteDAO = new EstudianteDAO(em);

            obtenerEstudianteActivoPorId(estudianteDAO, idEstudiante);

            return estudianteDAO.explorarPerfiles(idEstudiante);

        } finally {
            em.close();
        }
    }

    /**
     * Elimina un hobby del listado de un estudiante.
     *
     * El hobby no se elimina del sistema, solo se remueve la relación.
     *
     * @param idEstudiante id del estudiante
     * @param idHobby id del hobby a remover
     * @return estudiante actualizado
     * @throws ReglaNegocioException si el estudiante no tiene ese hobby
     */
    @Override
    public Estudiante quitarHobby(Long idEstudiante, Long idHobby) {
        if (idEstudiante == null) {
            throw new ValidacionException("El id del estudiante no puede ser nulo.");
        }

        if (idHobby == null) {
            throw new ValidacionException("El id del hobby no puede ser nulo.");
        }

        EntityManager em = JpaUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            IEstudianteDAO estudianteDAO = new EstudianteDAO(em);
            IHobbyDAO hobbyDAO = new HobbyDAO(em);

            Estudiante estudiante = estudianteDAO.buscarPorIdConHobbies(idEstudiante);
            if (estudiante == null) {
                throw new RecursoNoEncontradoException("No existe un estudiante con ese id.");
            }

            if (!estudiante.isActivo()) {
                throw new ReglaNegocioException("La cuenta del estudiante está desactivada.");
            }

            Hobby hobby = hobbyDAO.buscarPorId(idHobby);
            if (hobby == null) {
                throw new RecursoNoEncontradoException("No existe un hobby con ese id.");
            }

            if (!estudiante.getHobbies().contains(hobby)) {
                throw new ReglaNegocioException("El estudiante no tiene asignado ese hobby.");
            }

            estudiante.getHobbies().remove(hobby);

            Estudiante actualizado = estudianteDAO.actualizar(estudiante);

            em.getTransaction().commit();
            return actualizado;

        } catch (NegocioException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new NegocioException("Error al quitar hobby del estudiante.");
        } finally {
            em.close();
        }
    }

    /**
     * Actualiza la información del perfil de un estudiante.
     *
     * Permite modificar: - Carrera - Descripción (máx. 500 caracteres) - Foto
     * de perfil
     *
     * Solo se actualizan los campos que no son nulos o vacíos.
     *
     * @param idEstudiante id del estudiante
     * @param carrera nueva carrera
     * @param descripcion nueva descripción
     * @param fotoPerfil nueva ruta de imagen
     * @return estudiante actualizado
     * @throws ValidacionException si los datos son inválidos
     */
    @Override
    public Estudiante actualizarPerfil(Long idEstudiante, String carrera, String descripcion, String fotoPerfil) {
        if (idEstudiante == null) {
            throw new ValidacionException("El id del estudiante no puede ser nulo.");
        }

        EntityManager em = JpaUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            IEstudianteDAO estudianteDAO = new EstudianteDAO(em);

            Estudiante estudiante = obtenerEstudianteActivoPorId(estudianteDAO, idEstudiante);

            if (carrera != null && !carrera.isBlank()) {
                estudiante.setCarrera(carrera.trim());
            }

            if (descripcion != null) {
                descripcion = descripcion.trim();
                if (descripcion.length() > 500) {
                    throw new ValidacionException("La descripción no puede exceder 500 caracteres.");
                }
                estudiante.setDescripcion(descripcion);
            }

            if (fotoPerfil != null && !fotoPerfil.isBlank()) {
                estudiante.setFotoPerfil(fotoPerfil.trim());
            }

            Estudiante actualizado = estudianteDAO.actualizar(estudiante);

            em.getTransaction().commit();
            return actualizado;

        } catch (NegocioException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new NegocioException("Error al actualizar el perfil del estudiante.");
        } finally {
            em.close();
        }
    }

    /**
     * Desactiva la cuenta de un estudiante.
     *
     * Se realiza una eliminación lógica cambiando el atributo "activo" a false.
     *
     * @param idEstudiante id del estudiante
     * @throws ValidacionException si el id es nulo
     * @throws RecursoNoEncontradoException si no existe el estudiante
     * @throws ReglaNegocioException si la cuenta ya está desactivada
     */
    @Override
    public void desactivarCuenta(Long idEstudiante) {
        if (idEstudiante == null) {
            throw new ValidacionException("El id del estudiante no puede ser nulo.");
        }

        EntityManager em = JpaUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            IEstudianteDAO estudianteDAO = new EstudianteDAO(em);

            Estudiante estudiante = estudianteDAO.buscarPorId(idEstudiante);
            if (estudiante == null) {
                throw new RecursoNoEncontradoException("No existe un estudiante con ese id.");
            }

            if (!estudiante.isActivo()) {
                throw new ReglaNegocioException("La cuenta del estudiante ya está desactivada.");
            }

            estudiante.setActivo(false);
            estudianteDAO.actualizar(estudiante);

            em.getTransaction().commit();

        } catch (NegocioException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new NegocioException("Error al desactivar la cuenta del estudiante.");
        } finally {
            em.close();
        }
    }

    /**
     * Valida que el correo cumpla con el formato institucional.
     *
     * @param correo correo a validar
     * @return true si es válido, false en caso contrario
     */
    private boolean esCorreoInstitucionalValido(String correo) {
        return correo != null
                && correo.matches("^[A-Za-z0-9]+([._%+-]?[A-Za-z0-9]+)*@potros\\.itson\\.edu\\.mx$");
    }

    /**
     * Valida que la contraseña cumpla con los requisitos de seguridad: - mínimo
     * 6 caracteres - al menos una mayúscula - al menos una minúscula - al menos
     * un número
     *
     * @param password contraseña a validar
     * @return true si es válida, false en caso contrario
     */
    private boolean esPasswordRobusto(String password) {
        return password != null
                && password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$");
    }

    /**
     * Obtiene un estudiante por su id y valida que esté activo.
     *
     * Método reutilizable para centralizar la validación de existencia y estado
     * de la cuenta.
     *
     * @param estudianteDAO acceso a datos de estudiantes
     * @param idEstudiante id del estudiante
     * @return estudiante activo
     * @throws RecursoNoEncontradoException si no existe
     * @throws ReglaNegocioException si está desactivado
     */
    private Estudiante obtenerEstudianteActivoPorId(IEstudianteDAO estudianteDAO, Long idEstudiante) {
        Estudiante estudiante = estudianteDAO.buscarPorId(idEstudiante);

        if (estudiante == null) {
            throw new RecursoNoEncontradoException("No existe un estudiante con ese id.");
        }

        if (!estudiante.isActivo()) {
            throw new ReglaNegocioException("La cuenta del estudiante está desactivada.");
        }

        return estudiante;
    }
}
