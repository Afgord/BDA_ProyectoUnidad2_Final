package com.mycompany.proyectounidad2_persistencia;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
import com.mycompany.proyectounidad2_dominio.Estudiante;
import com.mycompany.proyectounidad2_dominio.Match;
import java.util.List;

/**
 *
 * @author Afgord
 */
public interface IMatchDAO {

    Match guardar(Match match);

    Match buscarPorId(Long id);

    List<Match> listar();

    Match actualizar(Match match);

    Match eliminar(Match match);

    Match buscarMatchEntre(Estudiante estudiante1, Estudiante estudiante2);

    List<Match> buscarMatchesDeEstudiante(Long idEstudiante);

}
