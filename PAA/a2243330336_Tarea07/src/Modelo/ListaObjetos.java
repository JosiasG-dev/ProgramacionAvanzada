package Modelo;

import java.util.ArrayList;

public class ListaObjetos {
    private ArrayList lista;

	public ListaObjetos() {
		lista = new ArrayList();
	}

	public boolean Insertar(Object nodo) {
		if (this.buscar(nodo) == -1) {
			this.lista.add(nodo);
			return true;
		}
		return false;
	}

	public boolean Eliminar(Object nodo) {
		int pos = this.buscar(nodo);
		if (pos > -1) {
			this.lista.remove(pos);
			return true;
		}
		return false;
	}

	public int buscar(Object nodoid) {
		int pos = -1, enc = -1;
		for (Object nodo : this.lista) {
			pos++;
			if (nodo.equals(nodoid))
				enc = pos;
		}
		return enc;
	}

	public ArrayList getLista() {
		return this.lista;
	}

	public Object getPos(int pos) {
		return this.lista.get(pos);
	}
}
