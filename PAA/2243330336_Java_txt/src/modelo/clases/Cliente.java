
package modelo.clases;

public class Cliente {
    private int id;
    private String nombre;

    public Cliente(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String toString() {
        return id + "," + nombre;
    }
}
