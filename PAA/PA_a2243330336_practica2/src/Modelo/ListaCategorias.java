package Modelo;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
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
        StringBuilder resultado = new StringBuilder();
        for (Categoria categoria : categorias) {
            resultado.append(categoria.getIdcategoria()).append(",").append(categoria.getCategoria()).append("\n");
        }
        return resultado.toString();
    }

    private Categoria buscarCategoriaPorId(String id) {
        for (Categoria categoria : categorias) {
            if (categoria.getIdcategoria().equals(id)) {
                return categoria;
            }
        }
        return null;
    }

    public String buscarCategoria(String id) {
        Categoria categoria = buscarCategoriaPorId(id);
        return (categoria != null) ? categoria.getCategoria() : null;
    }

    public void cargarCategorias(ArrayList<String[]> categoriasString) {
        if (categoriasString != null) {
            for (String[] partes : categoriasString) {
                if (partes.length >= 2) {
                    Categoria categoria = new Categoria(partes[0], partes[1]);
                    this.agregarCategoria(categoria);
                }
            }
        }
    }

    public DefaultListModel<Categoria> generarModeloCategorias() {
        DefaultListModel<Categoria> modelo = new DefaultListModel<>();
        for (Categoria categoria : categorias) {
            modelo.addElement(categoria);
        }
        return modelo;
    }
}
