package com.mycompany.proyectounidad2_persistencia;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
import com.mycompany.proyectounidad2_dominio.Estudiante;
import com.mycompany.proyectounidad2_dominio.Reaccion;
import java.util.List;

/**
 *
 * @author Afgord
 */
public interface IReaccionDAO {

    Reaccion guardar(Reaccion reaccion);

    Reaccion buscarPorId(Long id);

    List<Reaccion> listar();

    Reaccion actualizar(Reaccion reaccion);

    Reaccion eliminar(Reaccion reaccion);

    Reaccion buscarPorEmisorReceptor(Estudiante emisor, Estudiante receptor);

    List<Estudiante> obtenerLikesPendientes(Long idReceptor);

}
