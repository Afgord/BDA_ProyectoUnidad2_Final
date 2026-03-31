/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.proyectounidad2_servicios;

import com.mycompany.proyectounidad2_dominio.Hobby;
import java.util.List;

/**
 *
 * @author Afgord
 */
public interface IHobbyService {

    Hobby registrarHobby(Hobby hobby);

    Hobby buscarPorNombre(String nombre);

    List<Hobby> obtenerTodos();

}
