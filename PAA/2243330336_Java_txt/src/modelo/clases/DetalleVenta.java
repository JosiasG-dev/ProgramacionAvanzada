
package modelo.clases;

public class DetalleVenta {
    private int id;
    private String nombre;

    public DetalleVenta(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String toString() {
        return id + "," + nombre;
    }
}
