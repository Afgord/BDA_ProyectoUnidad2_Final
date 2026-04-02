/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.proyectounidad2_servicios;

import com.mycompany.proyectounidad2_dominio.Estudiante;
import com.mycompany.proyectounidad2_dominio.Reaccion;
import com.mycompany.proyectounidad2_dominio.TipoReaccion;
import java.util.List;

/**
 *
 * @author Afgord
 */
public interface IReaccionService {

    Reaccion registrarReaccion(Estudiante emisor, Estudiante receptor, TipoReaccion tipo);

    List<Estudiante> obtenerLikesPendientes(Long idEstudiante);

    int contarLikesPendientes(Long idEstudiante);

}
