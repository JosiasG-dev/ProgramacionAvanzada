package Modelo;
import java.util.Objects;
public class Cpersona {
	 private int id;
	    private String nombre;
	    private String apellido;

	    public Cpersona(int id, String nombre, String apellido) {
	        this.id = id;
	        this.nombre = nombre;
	        this.apellido = apellido;
	    }

	    public int getId() {
	        return id;
	    }

	    public String getNombre() {
	        return nombre;
	    }

	    public String getApellido() {
	        return apellido;
	    }

	    public String toString() {
	        return id + " - " + nombre + " " + apellido;
	    }
	    public boolean equals(Object obj) {
	        if (this == obj) return true;
	        if (!(obj instanceof Cpersona)) return false;
	        Cpersona other = (Cpersona) obj;
	        return id == other.id;
	    }

	    public int hashCode() {
	        return Objects.hash(id);
	    }
}
