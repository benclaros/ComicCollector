package comiccollector.modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Reserva {
    private Usuario usuario;
    private Comic comic;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    public Reserva(Usuario usuario, Comic comic, LocalDate fechaInicio, LocalDate fechaFin) {
        this.usuario = usuario;
        this.comic = comic;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Comic getComic() {
        return comic;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    @Override
    public String toString() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return "Reserva: " + comic.getTitulo() +
               " para " + usuario.getNombre() +
               " del " + fechaInicio.format(formato) +
               " al " + fechaFin.format(formato);
    }
}
