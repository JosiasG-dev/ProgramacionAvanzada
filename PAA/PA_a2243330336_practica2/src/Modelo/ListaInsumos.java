package Modelo;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

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

	    public String toArchivo() {
	        StringBuilder resultado = new StringBuilder();
	        for (Insumo insumo : insumos) {
	            resultado.append(insumo.toLinea()).append("\n");
	        }
	        return resultado.toString();
	    }

	    public void cargarInsumo(ArrayList<String[]> insumosString) {
	        if (insumosString != null) {
	            for (String[] partes : insumosString) {
	                if (partes.length >= 3) {
	                    Insumo insumo = new Insumo(partes[0], partes[1], partes[2]);
	                    this.agregarInsumo(insumo);
	                }
	            }
	        }
	    }
	    public DefaultTableModel getmodelo(ListaCategorias listac) {
	        DefaultTableModel modelo = new DefaultTableModel() {
	            @Override
	            public boolean isCellEditable(int row, int column) {
	                return false;
	            }
	        };
	        modelo.addColumn("Id");
	        modelo.addColumn("Insumo");
	        modelo.addColumn("Categoria");

	        for (Insumo nodo : this.insumos) {
	            String[] info = new String[3];
	            info[0] = nodo.getIdProducto().trim();
	            info[1] = nodo.getProducto().trim();
	            info[2] = listac.buscarCategoria(nodo.getIdCategoria().trim());
	            modelo.addRow(info);
	        }
	        return modelo;
	    }
}
