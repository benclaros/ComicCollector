package comiccollector.controlador;

import comiccollector.excepciones.ComicNoEncontradoException;
import comiccollector.excepciones.DatosInvalidosException;
import comiccollector.modelo.Comic;
import comiccollector.modelo.Usuario;
import comiccollector.modelo.Reserva;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class SistemaComic {
    private List<Comic> comics = new ArrayList<>();
    private Map<String, Usuario> usuarios = new HashMap<>();
    private List<Reserva> reservas = new ArrayList<>();

    public void cargarComicsDesdeCSV(String ruta) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(ruta));
        String linea = br.readLine(); // Encabezado

        while ((linea = br.readLine()) != null) {
            String[] partes = linea.split(",");
            if (partes.length == 6) {
                String codigo = partes[0];
                String titulo = partes[1];
                String autor = partes[2];
                String editorial = partes[3];
                int anio = Integer.parseInt(partes[4]);
                double precio = Double.parseDouble(partes[5]);

                Comic comic = new Comic(codigo, titulo, autor, editorial, anio, precio);
                comics.add(comic);
            }
        }
        br.close();
    }

    public Comic buscarComicPorCodigo(String codigo) throws ComicNoEncontradoException {
        for (Comic c : comics) {
            if (c.getCodigo().equalsIgnoreCase(codigo)) {
                return c;
            }
        }
        throw new ComicNoEncontradoException("Cómic no encontrado con el código: " + codigo);
    }

    public void agregarUsuario(String nombre) throws DatosInvalidosException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatosInvalidosException("El nombre no puede estar vacío.");
        }
        if (!nombre.trim().contains(" ")) {
            throw new DatosInvalidosException("Por favor, ingrese nombre y apellido.");
        }
        if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            throw new DatosInvalidosException("El nombre debe contener solo letras y espacios.");
        }
        usuarios.putIfAbsent(nombre, new Usuario(nombre));
    }

    public String comprarComic(String nombreUsuario, String codigoComic)
            throws ComicNoEncontradoException, DatosInvalidosException {

        if (!usuarios.containsKey(nombreUsuario)) {
            throw new DatosInvalidosException("Usuario no registrado.");
        }

        Comic comic = buscarComicPorCodigo(codigoComic);
        usuarios.get(nombreUsuario).agregarCompra(comic);
        comics.remove(comic); // Quitarlo de la lista

        return "Resumen de compra:\n" +
               "Cliente: " + nombreUsuario + "\n" +
               "Título: " + comic.getTitulo() + "\n" +
               "Precio: $" + comic.getPrecio();
    }

    public String reservarComic(String nombreUsuario, String codigoComic, LocalDate inicio, LocalDate fin)
            throws ComicNoEncontradoException, DatosInvalidosException {

        if (!usuarios.containsKey(nombreUsuario)) {
            throw new DatosInvalidosException("Usuario no registrado.");
        }

        Comic comic = buscarComicPorCodigo(codigoComic);

        if (inicio.isAfter(fin)) {
            throw new DatosInvalidosException("La fecha de inicio no puede ser posterior a la de fin.");
        }

        if (inicio.isBefore(LocalDate.now())) {
            throw new DatosInvalidosException("No se puede reservar para fechas pasadas.");
        }

        Reserva reserva = new Reserva(usuarios.get(nombreUsuario), comic, inicio, fin);
        reservas.add(reserva);

        return "✅ Reserva realizada:\n" + reserva.toString();
    }

    public void guardarUsuariosEnArchivo(String ruta) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(ruta));

        for (Usuario u : usuarios.values()) {
            bw.write("Nombre: " + u.getNombre());
            bw.newLine();
            bw.write("Compras: ");
            List<Comic> compras = u.getCompras();
            for (int i = 0; i < compras.size(); i++) {
                bw.write(compras.get(i).getCodigo());
                if (i < compras.size() - 1) bw.write(", ");
            }
            bw.newLine();
            bw.write("---");
            bw.newLine();
        }

        bw.close();
    }

    public int getCantidadComics() {
        return comics.size();
    }

    public int getCantidadUsuarios() {
        return usuarios.size();
    }

    public Set<Comic> getComicsOrdenados() {
        TreeSet<Comic> ordenados = new TreeSet<>(Comparator.comparing(Comic::getTitulo));
        ordenados.addAll(comics);
        return ordenados;
    }

    public Set<Usuario> getUsuariosOrdenados() {
        TreeSet<Usuario> ordenados = new TreeSet<>(Comparator.comparing(Usuario::getNombre));
        ordenados.addAll(usuarios.values());
        return ordenados;
    }
}
