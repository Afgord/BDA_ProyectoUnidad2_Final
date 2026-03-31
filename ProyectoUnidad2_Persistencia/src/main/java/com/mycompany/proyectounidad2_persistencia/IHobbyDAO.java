package com.mycompany.proyectounidad2_persistencia;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
import com.mycompany.proyectounidad2_dominio.Hobby;
import java.util.List;

/**
 *
 * @author Afgord
 */
public interface IHobbyDAO {

    Hobby guardar(Hobby hobby);

    Hobby actualizar(Hobby hobby);

    Hobby buscarPorId(Long id);

    Hobby buscarPorNombre(String nombre);

    List<Hobby> obtenerTodos();

}
