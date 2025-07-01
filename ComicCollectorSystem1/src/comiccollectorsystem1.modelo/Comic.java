package comiccollector.modelo;

import java.util.Objects;

public class Comic {
    private String codigo;
    private String titulo;
    private String autor;
    private String editorial;
    private int anio;
    private double precio;

    public Comic(String codigo, String titulo, String autor, String editorial, int anio, double precio) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.anio = anio;
        this.precio = precio;
    }

    public String getCodigo() { return codigo; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getEditorial() { return editorial; }
    public int getAnio() { return anio; }
    public double getPrecio() { return precio; }

    @Override
    public String toString() {
        return codigo + " - " + titulo + " (" + autor + ", " + editorial + ", " + anio + ") - $" + precio;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Comic other = (Comic) obj;
        return codigo.equals(other.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
