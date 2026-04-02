/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectounidad2_presentacion;

import com.mycompany.proyectounidad2_dominio.Estudiante;
import com.mycompany.proyectounidad2_dominio.Hobby;
import com.mycompany.proyectounidad2_dominio.Match;
import com.mycompany.proyectounidad2_dominio.TipoReaccion;
import com.mycompany.proyectounidad2_servicios.EstudianteService;
import com.mycompany.proyectounidad2_servicios.HobbyService;
import com.mycompany.proyectounidad2_servicios.MatchService;
import com.mycompany.proyectounidad2_servicios.ReaccionService;
import java.util.List;

/**
 *
 * @author Afgord
 */
public class main {

    private static final EstudianteService estudianteService = new EstudianteService();
    private static final HobbyService hobbyService = new HobbyService();
    private static final ReaccionService reaccionService = new ReaccionService();
    private static final MatchService matchService = new MatchService();

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println(" INICIO DE PRUEBAS FUNCIONALES");
        System.out.println("======================================");

        try {
            // 1. Registrar hobbies
            Hobby programacion = registrarHobby("Programación");
            Hobby musica = registrarHobby("Música");
            Hobby cine = registrarHobby("Cine");
            Hobby gym = registrarHobby("Gym");
            Hobby videojuegos = registrarHobby("Videojuegos");

            imprimirHobbies();

            // 2. Registrar estudiantes
            Estudiante ana = registrarEstudiante(
                    "Ana", "López", "García",
                    "ana.lopez@potros.itson.edu.mx",
                    "Ana123",
                    "Ingeniería en Software"
            );

            Estudiante carlos = registrarEstudiante(
                    "Carlos", "Ramírez", "Torres",
                    "carlos.ramirez@potros.itson.edu.mx",
                    "Carlos1",
                    "Ingeniería en Software"
            );

            Estudiante sofia = registrarEstudiante(
                    "Sofía", "Mendoza", "Ruiz",
                    "sofia.mendoza@potros.itson.edu.mx",
                    "Sofia1",
                    "Diseño Gráfico"
            );

            Estudiante diego = registrarEstudiante(
                    "Diego", "Navarro", "Pérez",
                    "diego.navarro@potros.itson.edu.mx",
                    "Diego1",
                    "Ingeniería Industrial"
            );

            // 3. Agregar hobbies
            System.out.println("\n=== AGREGAR HOBBIES ===");

            estudianteService.agregarHobby(ana.getId(), programacion.getId());
            estudianteService.agregarHobby(ana.getId(), cine.getId());

            estudianteService.agregarHobby(carlos.getId(), programacion.getId());
            estudianteService.agregarHobby(carlos.getId(), musica.getId());
            estudianteService.agregarHobby(carlos.getId(), videojuegos.getId());

            estudianteService.agregarHobby(sofia.getId(), cine.getId());
            estudianteService.agregarHobby(sofia.getId(), musica.getId());

            estudianteService.agregarHobby(diego.getId(), gym.getId());
            estudianteService.agregarHobby(diego.getId(), cine.getId());

            imprimirEstudianteConHobbies(ana.getId());
            imprimirEstudianteConHobbies(carlos.getId());
            imprimirEstudianteConHobbies(sofia.getId());
            imprimirEstudianteConHobbies(diego.getId());

            // 4. Iniciar sesión
            System.out.println("\n=== LOGIN CORRECTO ===");
            Estudiante loginAna = estudianteService.iniciarSesion(
                    "ana.lopez@potros.itson.edu.mx",
                    "Ana123"
            );
            System.out.println("Login exitoso: " + loginAna.getNombre());

            System.out.println("\n=== LOGIN INCORRECTO ===");
            try {
                estudianteService.iniciarSesion(
                        "ana.lopez@potros.itson.edu.mx",
                        "Mal123"
                );
            } catch (Exception e) {
                System.out.println("Esperado: " + e.getMessage());
            }

            // 5. Buscar hobbies en común
            System.out.println("\n=== HOBBIES EN COMÚN CON ANA ===");
            List<Estudiante> comunesAna = estudianteService.buscarConHobbiesEnComun(ana.getId());
            if (comunesAna.isEmpty()) {
                System.out.println("No se encontraron estudiantes con hobbies en común.");
            } else {
                for (Estudiante e : comunesAna) {
                    System.out.println("- " + e.getNombre() + " " + e.getApPat());
                }
            }

            // 6. Explorar perfiles
            System.out.println("\n=== EXPLORAR PERFILES DESDE ANA ===");
            List<Estudiante> perfilesAna = estudianteService.explorarPerfiles(ana.getId());
            if (perfilesAna.isEmpty()) {
                System.out.println("No hay perfiles para explorar.");
            } else {
                for (Estudiante e : perfilesAna) {
                    System.out.println("- " + e.getNombre() + " | " + e.getCarrera());
                }
            }

            // 7. Registrar reacciones
            System.out.println("\n=== REGISTRAR REACCIONES ===");
            reaccionService.registrarReaccion(ana, carlos, TipoReaccion.LIKE);
            System.out.println("Ana dio LIKE a Carlos");

            reaccionService.registrarReaccion(carlos, ana, TipoReaccion.LIKE);
            System.out.println("Carlos dio LIKE a Ana");
            System.out.println("Debe haberse generado match entre Ana y Carlos");

            reaccionService.registrarReaccion(ana, diego, TipoReaccion.NO_INTERESA);
            System.out.println("Ana dio NO_INTERESA a Diego");

            // 8. Probar likes pendientes
            System.out.println("\n=== LIKES PENDIENTES DE SOFIA ===");

            // Carlos da LIKE a Sofía
            reaccionService.registrarReaccion(carlos, sofia, TipoReaccion.LIKE);
            System.out.println("Carlos dio LIKE a Sofía");

            // Diego da LIKE a Sofía
            reaccionService.registrarReaccion(diego, sofia, TipoReaccion.LIKE);
            System.out.println("Diego dio LIKE a Sofía");

            // Sofía todavía no responde, así que debe tener 2 pendientes
            imprimirLikesPendientes(sofia.getId());

            // Sofía responde LIKE a Carlos
            reaccionService.registrarReaccion(sofia, carlos, TipoReaccion.LIKE);
            System.out.println("Sofía respondió LIKE a Carlos");

            // Ahora solo Diego debe seguir pendiente
            System.out.println("\n=== LIKES PENDIENTES DE SOFIA DESPUÉS DE RESPONDER A CARLOS ===");
            imprimirLikesPendientes(sofia.getId());

            // Sofía responde NO_INTERESA a Diego
            reaccionService.registrarReaccion(sofia, diego, TipoReaccion.NO_INTERESA);
            System.out.println("Sofía respondió NO_INTERESA a Diego");

            // Ahora ya no debe tener pendientes
            System.out.println("\n=== LIKES PENDIENTES DE SOFIA DESPUÉS DE RESPONDER A DIEGO ===");
            imprimirLikesPendientes(sofia.getId());

            // 9. Ver matches
            System.out.println("\n=== MATCHES DE ANA ===");
            imprimirMatches(ana.getId());

            System.out.println("\n=== MATCHES DE CARLOS ===");
            imprimirMatches(carlos.getId());

            System.out.println("\n=== MATCHES DE SOFIA ===");
            imprimirMatches(sofia.getId());

            // 10. Actualizar perfil
            System.out.println("\n=== ACTUALIZAR PERFIL DE ANA ===");
            estudianteService.actualizarPerfil(
                    ana.getId(),
                    "Ingeniería en Software",
                    "Me gusta programar y ver películas.",
                    "fotos/ana.jpg"
            );
            imprimirEstudianteConHobbies(ana.getId());

            // 11. Quitar hobby
            System.out.println("\n=== QUITAR HOBBY A ANA ===");
            estudianteService.quitarHobby(ana.getId(), cine.getId());
            imprimirEstudianteConHobbies(ana.getId());

            // 12. Desactivar cuenta
            System.out.println("\n=== DESACTIVAR CUENTA DE SOFIA ===");
            estudianteService.desactivarCuenta(sofia.getId());
            System.out.println("Cuenta de Sofía desactivada");

            System.out.println("\n=== INTENTO DE LOGIN DE SOFIA DESACTIVADA ===");
            try {
                estudianteService.iniciarSesion(
                        "sofia.mendoza@potros.itson.edu.mx",
                        "Sofia1"
                );
            } catch (Exception e) {
                System.out.println("Esperado: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("\nERROR GENERAL EN PRUEBAS: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n======================================");
        System.out.println(" FIN DE PRUEBAS FUNCIONALES");
        System.out.println("======================================");

    }

    private static Hobby registrarHobby(String nombre) {
        try {
            Hobby hobby = new Hobby();
            hobby.setNombre(nombre);
            hobby.setDescripcion("Hobby de prueba");

            Hobby guardado = hobbyService.registrarHobby(hobby);
            System.out.println("Hobby registrado: " + guardado.getNombre()
                    + " (id=" + guardado.getId() + ")");
            return guardado;

        } catch (Exception e) {
            System.out.println("No se pudo registrar hobby '" + nombre + "': " + e.getMessage());

            Hobby existente = hobbyService.buscarPorNombre(nombre);
            if (existente != null) {
                System.out.println("Se usará hobby existente: " + existente.getNombre()
                        + " (id=" + existente.getId() + ")");
                return existente;
            }

            throw e;
        }
    }

    private static Estudiante registrarEstudiante(
            String nombre,
            String apPat,
            String apMat,
            String correo,
            String password,
            String carrera) {

        try {
            Estudiante estudiante = new Estudiante();
            estudiante.setNombre(nombre);
            estudiante.setApPat(apPat);
            estudiante.setApMat(apMat);
            estudiante.setCorreoInst(correo);
            estudiante.setPassword(password);
            estudiante.setCarrera(carrera);
            estudiante.setDescripcion("Perfil de prueba");
            estudiante.setFotoPerfil("fotos/default.jpg");

            Estudiante guardado = estudianteService.registrarEstudiante(estudiante);
            System.out.println("Estudiante registrado: " + guardado.getNombre()
                    + " (id=" + guardado.getId() + ")");
            return guardado;

        } catch (Exception e) {
            System.out.println("No se pudo registrar estudiante '" + correo + "': " + e.getMessage());

            Estudiante existente = estudianteService.buscarPorCorreo(correo);
            if (existente != null) {
                System.out.println("Se usará estudiante existente: " + existente.getNombre()
                        + " (id=" + existente.getId() + ")");
                return existente;
            }

            throw e;
        }
    }

    private static void imprimirHobbies() {
        System.out.println("\n=== HOBBIES REGISTRADOS ===");
        List<Hobby> hobbies = hobbyService.obtenerTodos();

        if (hobbies.isEmpty()) {
            System.out.println("No hay hobbies registrados.");
            return;
        }

        for (Hobby h : hobbies) {
            System.out.println(h.getId() + " - " + h.getNombre());
        }
    }

    private static void imprimirEstudianteConHobbies(Long idEstudiante) {
        Estudiante e = estudianteService.buscarPorIdConHobbies(idEstudiante);

        System.out.println("\nEstudiante: " + e.getNombre() + " " + e.getApPat());
        System.out.println("Correo: " + e.getCorreoInst());
        System.out.println("Carrera: " + e.getCarrera());
        System.out.println("Descripción: " + e.getDescripcion());
        System.out.println("Foto: " + e.getFotoPerfil());
        System.out.println("Activo: " + e.isActivo());
        System.out.println("Hobbies:");

        if (e.getHobbies().isEmpty()) {
            System.out.println("- Sin hobbies");
        } else {
            for (Hobby h : e.getHobbies()) {
                System.out.println("- " + h.getNombre());
            }
        }
    }

    private static void imprimirMatches(Long idEstudiante) {
        List<Match> matches = matchService.obtenerMatchesDeEstudiante(idEstudiante);

        if (matches.isEmpty()) {
            System.out.println("No hay matches.");
            return;
        }

        for (Match m : matches) {
            System.out.println(
                    "Match ID: " + m.getId()
                    + " | Fecha: " + m.getFechaMatch()
                    + " | Estudiante 1: " + m.getEstudiante1().getNombre()
                    + " | Estudiante 2: " + m.getEstudiante2().getNombre()
            );
        }
    }

    private static void imprimirLikesPendientes(Long idEstudiante) {
        List<Estudiante> pendientes = reaccionService.obtenerLikesPendientes(idEstudiante);
        int cantidad = reaccionService.contarLikesPendientes(idEstudiante);

        System.out.println("Cantidad de likes pendientes: " + cantidad);

        if (pendientes.isEmpty()) {
            System.out.println("No hay likes pendientes.");
            return;
        }

        for (Estudiante e : pendientes) {
            System.out.println("- " + e.getNombre() + " " + e.getApPat()
                    + " | " + e.getCorreoInst());
        }
    }

}
