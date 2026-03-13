package Modelo;

import java.util.Objects;

public class Academia {
    private String nombre;

    public Academia(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() { return nombre; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return Objects.equals(nombre, ((Academia) obj).nombre);
    }

    @Override
    public int hashCode() { return Objects.hash(nombre); }
}
