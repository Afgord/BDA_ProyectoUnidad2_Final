/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.proyectounidad2_servicios;

import com.mycompany.proyectounidad2_dominio.Match;
import java.util.List;

/**
 *
 * @author Afgord
 */
public interface IMatchService {

    List<Match> obtenerMatchesDeEstudiante(Long idEstudiante);

}
