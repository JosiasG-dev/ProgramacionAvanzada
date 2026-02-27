package parte2.modelo;
import java.util.ArrayList;
import javax.swing.JComboBox;
public class ListaCategorias {
	 private ArrayList<Categoria> categorias;

	    public ListaCategorias() {
	        this.categorias = new ArrayList<>();
	    }

	    public void agregarCategoria(Categoria categoria) {
	        if (buscarCategoriaPorId(categoria.getIdcategoria()) == null) {
	            categorias.add(categoria);
	        }
	    }

	    public void eliminarCategoriaPorId(String id) {
	        Categoria categoria = buscarCategoriaPorId(id);
	        if (categoria != null) {
	            categorias.remove(categoria);
	        }
	    }

	    @Override
	    public String toString() {
	        StringBuilder sb = new StringBuilder();
	        for (Categoria c : categorias) {
	            sb.append(c.getIdcategoria()).append(",").append(c.getCategoria()).append("\n");
	        }
	        return sb.toString();
	    }

	    private Categoria buscarCategoriaPorId(String id) {
	        for (Categoria c : categorias) {
	            if (c.getIdcategoria().equals(id)) {
	                return c;
	            }
	        }
	        return null;
	    }

	    public String buscarCategoria(String id) {
	        Categoria c = buscarCategoriaPorId(id);
	        return (c != null) ? c.getCategoria() : null;
	    }

	    public JComboBox<Categoria> agregarCategoriasAComboBox(JComboBox<Categoria> comboBox) {
	        comboBox.removeAllItems();
	        for (Categoria c : categorias) {
	            comboBox.addItem(c);
	        }
	        return comboBox;
	    }

	    public Object[] CategoriasArreglo() {
	        return categorias.toArray();
	    }

	    public void cargarCategorias(ArrayList<String[]> categoriasString) {
	        for (String[] partes : categoriasString) {
	            if (partes.length >= 2) {
	                this.agregarCategoria(new Categoria(partes[0], partes[1]));
	            }
	        }
	    }
}
