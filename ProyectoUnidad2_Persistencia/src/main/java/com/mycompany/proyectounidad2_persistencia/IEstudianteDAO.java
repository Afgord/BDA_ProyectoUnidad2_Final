package com.mycompany.proyectounidad2_persistencia;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
import com.mycompany.proyectounidad2_dominio.Estudiante;
import java.util.List;

/**
 *
 * @author Afgord
 */
public interface IEstudianteDAO {

    Estudiante guardar(Estudiante estudiante);

    Estudiante buscarPorId(Long id);

    Estudiante buscarPorIdConHobbies(Long id);

    List<Estudiante> buscarConHobbiesEnComun(Long idEstudiante);

    List<Estudiante> explorarPerfiles(Long idEstudiante);

    List<Estudiante> listar();

    Estudiante buscarPorCorreo(String correoInst);

    Estudiante actualizar(Estudiante estudiante);

    Estudiante eliminar(Estudiante estudiante);

}
