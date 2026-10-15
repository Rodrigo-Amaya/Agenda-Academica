
package com.mycompany.guia_2;

import java.util.List;
import java.util.Scanner;

public class Guia_2 {
    public static void main(String[] args) {

        EstudianteDAO dao =
                new EstudianteDAOJson(
                        "src/main/java/json/estudiantes.json"
                );

        Scanner sc = new Scanner(System.in);

        int opcion;

        do {

            System.out.println();
            System.out.println("================================");
            System.out.println("       MENÚ DE ESTUDIANTES");
            System.out.println("================================");
            System.out.println("1. Insertar");
            System.out.println("2. Listar");
            System.out.println("3. Buscar");
            System.out.println("4. Actualizar");
            System.out.println("5. Eliminar");
            System.out.println("0. Salir");
            System.out.println("================================");
            System.out.print("Seleccione una opción: ");

            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {

                case 1:
                    insertarEstudiante(dao, sc);
                    break;

                case 2:
                    listarEstudiantes(dao);
                    break;

                case 3:
                    buscarEstudiante(dao, sc);
                    break;

                case 4:
                    actualizarEstudiante(dao, sc);
                    break;

                case 5:
                    eliminarEstudiante(dao, sc);
                    break;

                case 0:
                    System.out.println("Programa finalizado.");
                    break;

                default:
                    System.out.println(
                            "Opción no válida."
                    );
            }

        } while (opcion != 0);

        sc.close();
    }

    private static void insertarEstudiante(
            EstudianteDAO dao,
            Scanner sc) {

        System.out.println();
        System.out.println("--- INSERTAR ESTUDIANTE ---");

        System.out.print("Nombre: ");
        String nombre = sc.nextLine();

        System.out.print("Carrera: ");
        String carrera = sc.nextLine();

        System.out.print("Correo: ");
        String correo = sc.nextLine();

        Estudiante estudiante =
                new Estudiante(
                        0,
                        nombre,
                        carrera,
                        correo
                );

        dao.insertar(estudiante);

        System.out.println(
                "Estudiante insertado correctamente."
        );
    }

    private static void listarEstudiantes(
            EstudianteDAO dao) {

        System.out.println();
        System.out.println("--- LISTADO DE ESTUDIANTES ---");

        List<Estudiante> estudiantes =
                dao.listar();

        if (estudiantes.isEmpty()) {

            System.out.println(
                    "No hay estudiantes registrados."
            );

        } else {

            for (Estudiante e : estudiantes) {
                System.out.println(e);
            }
        }
    }

    private static void buscarEstudiante(
            EstudianteDAO dao,
            Scanner sc) {

        System.out.println();
        System.out.println("--- BUSCAR ESTUDIANTE ---");

        System.out.print("Ingrese el ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        Estudiante estudiante =
                dao.buscar(id);

        if (estudiante != null) {

            System.out.println(
                    "Estudiante encontrado:"
            );

            System.out.println(estudiante);

        } else {

            System.out.println(
                    "No se encontró el estudiante."
            );
        }
    }

    private static void actualizarEstudiante(
            EstudianteDAO dao,
            Scanner sc) {

        System.out.println();
        System.out.println("--- ACTUALIZAR ESTUDIANTE ---");

        System.out.print("Ingrese el ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        Estudiante estudiante =
                dao.buscar(id);

        if (estudiante == null) {

            System.out.println(
                    "No se encontró el estudiante."
            );

            return;
        }

        System.out.println(
                "Estudiante actual: "
                        + estudiante
        );

        System.out.print("Nuevo nombre: ");
        String nombre = sc.nextLine();

        System.out.print("Nueva carrera: ");
        String carrera = sc.nextLine();

        System.out.print("Nuevo correo: ");
        String correo = sc.nextLine();

        estudiante.setNombre(nombre);
        estudiante.setCarrera(carrera);
        estudiante.setCorreo(correo);

        if (dao.actualizar(estudiante)) {

            System.out.println(
                    "Estudiante actualizado correctamente."
            );

        } else {

            System.out.println(
                    "No se pudo actualizar."
            );
        }
    }

    private static void eliminarEstudiante(
            EstudianteDAO dao,
            Scanner sc) {

        System.out.println();
        System.out.println("--- ELIMINAR ESTUDIANTE ---");

        System.out.print("Ingrese el ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        if (dao.eliminar(id)) {

            System.out.println(
                    "Estudiante eliminado correctamente."
            );

        } else {

            System.out.println(
                    "No se encontró el estudiante."
            );
        }
    }
}

