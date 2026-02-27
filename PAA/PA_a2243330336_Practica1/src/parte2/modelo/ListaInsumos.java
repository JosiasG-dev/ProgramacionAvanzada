package parte2.modelo;
import java.util.ArrayList;
public class ListaInsumos {
	  private ArrayList<Insumo> insumos;

	    public ListaInsumos() {
	        this.insumos = new ArrayList<>();
	    }

	    public boolean agregarInsumo(Insumo insumo) {
	        if (buscarInsumoPorId(insumo.getIdProducto()) == null) {
	            insumos.add(insumo);
	            return true;
	        }
	        return false;
	    }

	    public boolean eliminarInsumoPorId(String id) {
	        Insumo insumo = buscarInsumoPorId(id);
	        if (insumo != null) {
	            insumos.remove(insumo);
	            return true;
	        }
	        return false;
	    }

	    @Override
	    public String toString() {
	        StringBuilder resultado = new StringBuilder();
	        for (Insumo insumo : insumos) {
	            resultado.append(insumo.toString()).append("\n");
	        }
	        return resultado.toString();
	    }

	    private Insumo buscarInsumoPorId(String id) {
	        for (Insumo insumo : insumos) {
	            if (insumo.getIdProducto().equals(id)) {
	                return insumo;
	            }
	        }
	        return null;
	    }

	    public String buscarInsumo(String id) {
	        Insumo insumo = buscarInsumoPorId(id);
	        return (insumo != null) ? insumo.getProducto() : null;
	    }

	    public String[] idinsumos() {
	        String[] ids = new String[insumos.size()];
	        int i = 0;
	        for (Insumo insumo : insumos) {
	            ids[i++] = insumo.getIdProducto();
	        }
	        return ids;
	    }

	    public void cargarInsumos(ArrayList<String[]> insumosString) {
	        for (String[] partes : insumosString) {
	            if (partes.length >= 3) {
	                Insumo insumo = new Insumo(partes[0], partes[1], partes[2]);
	                this.agregarInsumo(insumo);
	            }
	        }
	    }
}
