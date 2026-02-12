package Modelo;
import javax.swing.*;

public class ModeloInsumo {
	 private DefaultComboBoxModel<String> modeloCategoria;
	    private DefaultListModel<String> modeloLista;

	    public ModeloInsumo() {

	        modeloCategoria = new DefaultComboBoxModel<>();
	        modeloLista = new DefaultListModel<>();

	        modeloCategoria.addElement("Materiales");
	        modeloCategoria.addElement("Mano de Obra");
	        modeloCategoria.addElement("Herramienta");
	        modeloCategoria.addElement("Servicios");
	    }

	    public DefaultComboBoxModel<String> getModeloCategoria() {
	        return modeloCategoria;
	    }

	    public DefaultListModel<String> getModeloLista() {
	        return modeloLista;
	    }

	    public void agregarLista(String texto) {
	        modeloLista.addElement(texto);
	    }

	    public void eliminarLista(int pos) {
	        modeloLista.remove(pos);
	    }
}
