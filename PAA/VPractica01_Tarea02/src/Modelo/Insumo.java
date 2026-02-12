package Modelo;

public class Insumo {
	 private int id;
	    private String nombre;
	    private String categoria;

	    public Insumo(int id, String nombre, String categoria) {
	        this.id = id;
	        this.nombre = nombre;
	        this.categoria = categoria;
	    }

	    public int getId() {
	        return id;
	    }

	    public String getNombre() {
	        return nombre;
	    }

	    public String getCategoria() {
	        return categoria;
	    }

	    public void setId(int id) {
	        this.id = id;
	    }

	    public void setNombre(String nombre) {
	        this.nombre = nombre;
	    }

	    public void setCategoria(String categoria) {
	        this.categoria = categoria;
	    }

	    @Override
	    public String toString() {
	        return id + " - " + nombre + " - " + categoria;
	    }
}
