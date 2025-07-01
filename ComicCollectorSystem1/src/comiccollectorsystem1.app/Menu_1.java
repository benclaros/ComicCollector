package comiccollector.app;

import comiccollector.controlador.SistemaComic;
import comiccollector.excepciones.ComicNoEncontradoException;
import comiccollector.excepciones.DatosInvalidosException;
import comiccollector.modelo.Comic;
import comiccollector.modelo.Usuario;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Menu_1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SistemaComic sistema = new SistemaComic();

        try {
            sistema.cargarComicsDesdeCSV("data/comics.csv");
        } catch (IOException e) {
            System.out.println("Error al cargar los cómics: " + e.getMessage());
        }

        int opcion = -1;
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        do {
            System.out.println("\n--- MENÚ ---");
            System.out.println("1. Mostrar cómics disponibles");
            System.out.println("2. Buscar cómic por código");
            System.out.println("3. Agregar usuario");
            System.out.println("4. Comprar cómic");
            System.out.println("5. Guardar usuarios en archivo");
            System.out.println("7. Reservar cómic");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");

            String entrada = scanner.nextLine();
            try {
                opcion = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número válido.");
                opcion = -1;
                continue;
            }

            switch (opcion) {
                case 1:
                    if (sistema.getCantidadComics() == 0) {
                        System.out.println("No hay cómics cargados.");
                    } else {
                        System.out.println("\nCómics disponibles:");
                        for (Comic comic : sistema.getComicsOrdenados()) {
                            System.out.println(" - " + comic);
                        }
                    }
                    break;

                case 2:
                    System.out.print("Ingrese el código del cómic: ");
                    String codigo = scanner.nextLine();
                    try {
                        Comic encontrado = sistema.buscarComicPorCodigo(codigo);
                        System.out.println("Cómic encontrado:\n" + encontrado);
                    } catch (ComicNoEncontradoException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 3:
                    System.out.print("Ingrese el nombre del usuario: ");
                    String nombreUsuario = scanner.nextLine();
                    try {
                        sistema.agregarUsuario(nombreUsuario);
                        System.out.println("Usuario agregado.");
                    } catch (DatosInvalidosException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 4:
                    System.out.print("Ingrese el nombre del usuario: ");
                    String nombreCompra = scanner.nextLine().trim();

                    if (nombreCompra.isEmpty() || !nombreCompra.contains(" ") ||
                            !nombreCompra.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
                        System.out.println("Ingrese un nombre válido (nombre y apellido, solo letras).");
                        break;
                    }

                    boolean registrado = false;
                    for (Usuario u : sistema.getUsuariosOrdenados()) {
                        if (u.getNombre().equalsIgnoreCase(nombreCompra)) {
                            registrado = true;
                            break;
                        }
                    }

                    if (!registrado) {
                        System.out.println("El usuario no está registrado. Debe registrarse antes de comprar.");
                        break;
                    }

                    System.out.print("Ingrese el código del cómic a comprar: ");
                    String codigoCompra = scanner.nextLine();

                    try {
                        String resumen = sistema.comprarComic(nombreCompra, codigoCompra);
                        System.out.println("Compra registrada.");
                        System.out.println(resumen);
                    } catch (ComicNoEncontradoException | DatosInvalidosException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 5:
                    try {
                        sistema.guardarUsuariosEnArchivo("data/usuarios.txt");
                        System.out.println("Usuarios guardados.");
                    } catch (IOException e) {
                        System.out.println("Error al guardar usuarios: " + e.getMessage());
                    }
                    break;

                case 7:
                    System.out.print("Ingrese el nombre del usuario: ");
                    String nombreReserva = scanner.nextLine().trim();

                    if (nombreReserva.isEmpty() || !nombreReserva.contains(" ") ||
                            !nombreReserva.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
                        System.out.println("Ingrese un nombre válido (nombre y apellido, solo letras).");
                        break;
                    }

                    boolean existe = false;
                    for (Usuario u : sistema.getUsuariosOrdenados()) {
                        if (u.getNombre().equalsIgnoreCase(nombreReserva)) {
                            existe = true;
                            break;
                        }
                    }

                    if (!existe) {
                        System.out.println("Usuario no registrado. Debe registrarse antes de reservar.");
                        break;
                    }

                    System.out.print("Ingrese el código del cómic a reservar: ");
                    String codigoReserva = scanner.nextLine().trim();

                    if (codigoReserva.isEmpty()) {
                        System.out.println("El código del cómic no puede estar vacío.");
                        break;
                    }

                    System.out.print("Ingrese la fecha de inicio (DD-MM-YYYY): ");
                    String inicioStr = scanner.nextLine().trim();

                    System.out.print("Ingrese la fecha de fin (DD-MM-YYYY): ");
                    String finStr = scanner.nextLine().trim();

                    LocalDate fechaInicio;
                    LocalDate fechaFin;

                    try {
                        fechaInicio = LocalDate.parse(inicioStr, formatoFecha);
                        fechaFin = LocalDate.parse(finStr, formatoFecha);
                    } catch (DateTimeParseException e) {
                        System.out.println("Formato de fecha inválido. Use DD-MM-YYYY.");
                        break;
                    }

                    try {
                        String confirmacion = sistema.reservarComic(nombreReserva, codigoReserva, fechaInicio, fechaFin);
                        System.out.println(confirmacion);
                    } catch (ComicNoEncontradoException | DatosInvalidosException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 6:
                    System.out.println("Saliendo del sistema...");
                    break;

                default:
                    System.out.println("Opción no válida.");
            }

        } while (opcion != 6);

        scanner.close();
    }
}
