package Modelo;
import java.util.ArrayList;
public class Lista {
	 private ArrayList<Cpersona> lista;
	    private int contador = 1;

	    public Lista() {
	        lista = new ArrayList<>();
	    }

	    public void insertar(String nombre, String apellido) {
	        Cpersona p = new Cpersona(contador++, nombre, apellido);
	        lista.add(p);
	    }

	    public String info() {
	        StringBuilder sb = new StringBuilder();
	        for (Cpersona p : lista) {
	            sb.append(p.toString()).append("\n");
	        }
	        return sb.toString();
	    }
}
