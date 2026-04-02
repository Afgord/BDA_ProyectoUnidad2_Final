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
 *
 * @author Afgord
 */
public class EstudianteService implements IEstudianteService {

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
        if (estudiante.getNombre() == null || estudiante.getNombre().isBlank()) {
            throw new ValidacionException("El nombre no puede ser nulo o vacío.");
        }

        if (estudiante.getApPat() == null || estudiante.getApPat().isBlank()) {
            throw new ValidacionException("El apellido paterno no puede ser nulo o vacío.");
        }

        if (estudiante.getApMat() == null || estudiante.getApMat().isBlank()) {
            throw new ValidacionException("El apellido materno no puede ser nulo o vacío.");
        }

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

//    private boolean esCorreoInstitucionalValido(String correo) {
//        return correo != null
//                && correo.matches("^[A-Za-z0-9._%+-]+@potros\\.itson\\.edu\\.mx$");
//    }
    private boolean esCorreoInstitucionalValido(String correo) {
        return correo != null
                && correo.matches("^[A-Za-z0-9]+([._%+-]?[A-Za-z0-9]+)*@potros\\.itson\\.edu\\.mx$");
    }

    private boolean esPasswordRobusto(String password) {
        return password != null
                && password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$");
    }

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
