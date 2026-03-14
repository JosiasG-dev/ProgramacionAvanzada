package modelo;

import java.util.ArrayList;
import java.util.Iterator;

public class Catalogo {

    private ArrayList<Producto> lista;

    public Catalogo() {
        lista = new ArrayList<>();
    }

    public ArrayList<Producto> getLista() { return lista; }
    public void setLista(ArrayList<Producto> lista) { this.lista = lista; }

    public boolean existe(String codigo) {
        Iterator<Producto> it = lista.iterator();
        while (it.hasNext()) {
            if (it.next().getCodigo().equalsIgnoreCase(codigo.trim())) return true;
        }
        return false;
    }

    public boolean insertar(Producto p) {
        if (existe(p.getCodigo())) return false;
        lista.add(p);
        return true;
    }

    public Producto buscar(String codigo) {
        Iterator<Producto> it = lista.iterator();
        while (it.hasNext()) {
            Producto p = it.next();
            if (p.getCodigo().equalsIgnoreCase(codigo.trim())) return p;
        }
        return null;
    }

    public boolean actualizar(String codigo, String nombre, double precio, int cantidad, String categoria) {
        Iterator<Producto> it = lista.iterator();
        while (it.hasNext()) {
            Producto p = it.next();
            if (p.getCodigo().equalsIgnoreCase(codigo.trim())) {
                p.setNombre(nombre);
                p.setPrecio(precio);
                p.setCantidad(cantidad);
                p.setCategoria(categoria);
                return true;
            }
        }
        return false;
    }

    public boolean eliminar(String codigo) {
        Iterator<Producto> it = lista.iterator();
        while (it.hasNext()) {
            Producto p = it.next();
            if (p.getCodigo().equalsIgnoreCase(codigo.trim())) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public int total() { return lista.size(); }
}
