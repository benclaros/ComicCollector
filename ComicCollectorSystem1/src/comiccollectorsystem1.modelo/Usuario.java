package comiccollector.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Usuario {
    private String nombre;
    private List<Comic> compras;

    public Usuario(String nombre) {
        this.nombre = nombre;
        this.compras = new ArrayList<>();
    }

    public String getNombre() { return nombre; }

    public List<Comic> getCompras() { return compras; }

    public void agregarCompra(Comic comic) {
        compras.add(comic);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Nombre: " + nombre + "\nCompras: ");
        for (Comic c : compras) {
            sb.append(c.getCodigo()).append(", ");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuario other = (Usuario) obj;
        return nombre.equals(other.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
}
