package Modelo;
import java.util.ArrayList;
public class ListaInsumo {
	 private ArrayList<Insumo> lista;

	    public ListaInsumo() {
	        lista = new ArrayList<>();
	    }

	    public void agregar(Insumo i) {
	        lista.add(i);
	    }

	    public void eliminar(int pos) {
	        lista.remove(pos);
	    }

	    public ArrayList<Insumo> getLista() {
	        return lista;
	    }
}
