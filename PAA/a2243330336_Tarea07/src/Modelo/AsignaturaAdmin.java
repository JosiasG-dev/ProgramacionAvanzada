package Modelo;

import java.util.Objects;

public class AsignaturaAdmin {
    private String nombre;
    private String academia;

    public AsignaturaAdmin(String nombre, String academia) {
        this.nombre = nombre;
        this.academia = academia;
    }

    public String getNombre()   { return nombre; }
    public String getAcademia() { return academia; }
    public void setNombre(String nombre)     { this.nombre = nombre; }
    public void setAcademia(String academia) { this.academia = academia; }

    @Override
    public String toString() { return nombre + " [" + academia + "]"; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AsignaturaAdmin o = (AsignaturaAdmin) obj;
        return Objects.equals(nombre, o.nombre) && Objects.equals(academia, o.academia);
    }

    @Override
    public int hashCode() { return Objects.hash(nombre, academia); }
}
