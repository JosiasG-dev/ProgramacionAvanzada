package Modelo;

public class Insumo {
	 private int id;
	    private String insumo;
	    private String categoria;

	    public Insumo(int id, String insumo, String categoria) {
	        this.id = id;
	        this.insumo = insumo;
	        this.categoria = categoria;
	    }

	    public int getId() {
	        return id;
	    }

	    public String getInsumo() {
	        return insumo;
	    }

	    public String getCategoria() {
	        return categoria;
	    }

	    @Override
	    public String toString() {
	        return id + " - " + insumo + " (" + categoria + ")";
	    }
}
