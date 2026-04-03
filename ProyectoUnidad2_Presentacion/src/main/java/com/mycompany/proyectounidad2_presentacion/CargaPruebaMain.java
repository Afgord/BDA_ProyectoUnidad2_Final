/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectounidad2_presentacion;

import com.mycompany.proyectounidad2_dominio.Estudiante;
import com.mycompany.proyectounidad2_dominio.Hobby;
import com.mycompany.proyectounidad2_dominio.TipoReaccion;
import com.mycompany.proyectounidad2_servicios.EstudianteService;
import com.mycompany.proyectounidad2_servicios.HobbyService;
import com.mycompany.proyectounidad2_servicios.MatchService;
import com.mycompany.proyectounidad2_servicios.ReaccionService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Afgord
 */
public class CargaPruebaMain {

    private static final EstudianteService estudianteService = new EstudianteService();
    private static final HobbyService hobbyService = new HobbyService();
    private static final ReaccionService reaccionService = new ReaccionService();
    private static final MatchService matchService = new MatchService();

    private static final Random random = new Random();

    public static void main(String[] args) {
        System.out.println("======================================");
        System.out.println(" INICIO DE CARGA DE PRUEBA");
        System.out.println("======================================");

        try {
            List<Hobby> hobbies = crearHobbiesBase();
            List<Estudiante> estudiantes = crear50Estudiantes();
            asignarHobbies(estudiantes, hobbies);
            generar20Matches(estudiantes);
            completarHasta100Reacciones(estudiantes);

            System.out.println("\n=== RESUMEN FINAL ===");
            System.out.println("Estudiantes creados: " + estudiantes.size());
            System.out.println("Matches de ejemplo del estudiante 1: "
                    + matchService.obtenerMatchesDeEstudiante(estudiantes.get(0).getId()).size());

            System.out.println("Carga de prueba finalizada correctamente.");

        } catch (Exception e) {
            System.out.println("ERROR EN CARGA DE PRUEBA: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("======================================");
        System.out.println(" FIN DE CARGA DE PRUEBA");
        System.out.println("======================================");
    }

    private static List<Hobby> crearHobbiesBase() {
        String[] nombres = {
            "Programación", "Música", "Cine", "Gym", "Videojuegos",
            "Lectura", "Fútbol", "Basquetbol", "Cocina", "Viajes"
        };

        List<Hobby> hobbies = new ArrayList<>();

        for (String nombre : nombres) {
            try {
                Hobby hobby = new Hobby();
                hobby.setNombre(nombre);
                hobby.setDescripcion("Hobby de prueba");
                hobbies.add(hobbyService.registrarHobby(hobby));
            } catch (Exception e) {
                Hobby existente = hobbyService.buscarPorNombre(nombre);
                if (existente != null) {
                    hobbies.add(existente);
                }
            }
        }

        System.out.println("Hobbies disponibles: " + hobbies.size());
        return hobbies;
    }

    private static List<Estudiante> crear50Estudiantes() {
        List<Estudiante> estudiantes = new ArrayList<>();

        for (int i = 1; i <= 50; i++) {
            try {
                Estudiante e = new Estudiante();
                e.setNombre("Alumno" + i);
                e.setApPat("ApellidoP" + i);
                e.setApMat("ApellidoM" + i);
                e.setCorreoInst("alumno" + i + "@potros.itson.edu.mx");
                e.setPassword("Alumno" + i + "A1");
                e.setCarrera(obtenerCarreraAleatoria());
                e.setDescripcion("Perfil de prueba del alumno " + i);
                e.setFotoPerfil("default.jpg");

                Estudiante guardado = estudianteService.registrarEstudiante(e);
                estudiantes.add(guardado);

            } catch (Exception ex) {
                Estudiante existente = estudianteService.buscarPorCorreo(
                        "alumno" + i + "@potros.itson.edu.mx"
                );
                if (existente != null) {
                    estudiantes.add(existente);
                }
            }
        }

        System.out.println("Estudiantes disponibles: " + estudiantes.size());
        return estudiantes;
    }

    private static void asignarHobbies(List<Estudiante> estudiantes, List<Hobby> hobbies) {
        for (Estudiante estudiante : estudiantes) {
            Collections.shuffle(hobbies, random);
            int cantidad = 2 + random.nextInt(2); // 2 o 3 hobbies

            for (int i = 0; i < cantidad; i++) {
                try {
                    estudianteService.agregarHobby(estudiante.getId(), hobbies.get(i).getId());
                } catch (Exception e) {
                    // ignorar si ya lo tiene
                }
            }
        }

        System.out.println("Hobbies asignados correctamente.");
    }

    private static void generar20Matches(List<Estudiante> estudiantes) {
        int matchesGenerados = 0;

        for (int i = 0; i < estudiantes.size() - 1 && matchesGenerados < 20; i += 2) {
            Estudiante a = estudiantes.get(i);
            Estudiante b = estudiantes.get(i + 1);

            try {
                reaccionService.registrarReaccion(a, b, TipoReaccion.LIKE);
            } catch (Exception e) {
                // ignorar
            }

            try {
                reaccionService.registrarReaccion(b, a, TipoReaccion.LIKE);
                matchesGenerados++;
            } catch (Exception e) {
                // ignorar
            }
        }

        System.out.println("Matches objetivo generados: " + matchesGenerados);
    }

    private static void completarHasta100Reacciones(List<Estudiante> estudiantes) {
        int reaccionesExtrasObjetivo = 80; // 40 ya vienen de los 20 matches
        int exitosas = 0;
        int intentos = 0;

        while (exitosas < reaccionesExtrasObjetivo && intentos < 3000) {
            intentos++;

            Estudiante emisor = estudiantes.get(random.nextInt(estudiantes.size()));
            Estudiante receptor = estudiantes.get(random.nextInt(estudiantes.size()));

            if (emisor.getId().equals(receptor.getId())) {
                continue;
            }

            TipoReaccion tipo = random.nextBoolean()
                    ? TipoReaccion.LIKE
                    : TipoReaccion.NO_INTERESA;

            try {
                reaccionService.registrarReaccion(emisor, receptor, tipo);
                exitosas++;
            } catch (Exception e) {
                // ignorar duplicados o restricciones
            }
        }

        System.out.println("Reacciones extra generadas: " + exitosas);
    }

    private static int contarReaccionesAproximadas(List<Estudiante> estudiantes) {
        int total = 0;

        for (Estudiante e : estudiantes) {
            try {
                total += reaccionService.contarLikesPendientes(e.getId());
            } catch (Exception ex) {
                // ignorar
            }
        }

        return Math.max(total, 40);
    }

    private static String obtenerCarreraAleatoria() {
        String[] carreras = {
            "Ingeniería en Software",
            "Diseño Gráfico",
            "Ingeniería Industrial",
            "Ingeniería en Sistemas",
            "Administración",
            "Contaduría"
        };

        return carreras[random.nextInt(carreras.length)];
    }

}
