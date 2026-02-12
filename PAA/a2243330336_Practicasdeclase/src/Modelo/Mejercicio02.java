package Modelo;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

public class Mejercicio02 {
	 private DefaultComboBoxModel<String> listacombo;
	    private DefaultListModel<String> lista;

	    private String[] equipos = {
	        "America", "Chivas", "Cruz Azul", "Pumas", "Tigres", "Rayados"
	    };

	    public Mejercicio02() {
	        inicializarcombo();
	        inicializarlistmodel();
	    }

	    private void inicializarcombo() {
	        listacombo = new DefaultComboBoxModel<>();
	        for (String e : equipos) {
	            listacombo.addElement(e);
	        }
	    }

	    private void inicializarlistmodel() {
	        lista = new DefaultListModel<>();
	        for (String e : equipos) {
	            lista.addElement(e);
	        }
	    }

	    public void agregarCombo(String info) {
	        listacombo.addElement(info);
	    }

	    public void agregarLista(String info) {
	        lista.addElement(info);
	    }

	    public void eliminarCombo(int pos) {
	        listacombo.removeElementAt(pos);
	    }

	    public void eliminarLista(int pos) {
	        lista.removeElementAt(pos);
	    }

	    public DefaultComboBoxModel<String> getListacombo() {
	        return listacombo;
	    }

	    public DefaultListModel<String> getLista() {
	        return lista;
	    }	
}
