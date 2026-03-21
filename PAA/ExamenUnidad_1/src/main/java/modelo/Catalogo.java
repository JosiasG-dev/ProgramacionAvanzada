package modelo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Catálogo de productos con operaciones CRUD.
 * Basado en el Catalogo.java original, extendido para manejar
 * la jerarquía de Producto abstracto.
 */
public class Catalogo {

    private ArrayList<Producto> lista;

    public Catalogo() {
        this.lista = new ArrayList<>();
    }

    // ── Getters / Setters ────────────────────────────────────────────────
    public ArrayList<Producto> getLista()                    { return lista; }
    public void                setLista(ArrayList<Producto> l){ this.lista = l; }
    public int                 total()                       { return lista.size(); }

    // ── CRUD ─────────────────────────────────────────────────────────────

    public boolean existe(String codigo) {
        for (Producto p : lista)
            if (p.getCodigo().equalsIgnoreCase(codigo.trim())) return true;
        return false;
    }

    public boolean insertar(Producto p) {
        if (existe(p.getCodigo())) return false;
        lista.add(p);
        return true;
    }

    public Producto buscar(String codigo) {
        for (Producto p : lista)
            if (p.getCodigo().equalsIgnoreCase(codigo.trim())) return p;
        return null;
    }

    public boolean actualizar(String codigo, String nombre, double precio,
                              int cantidad, String categoria) {
        Producto p = buscar(codigo);
        if (p == null) return false;
        p.setNombre(nombre);
        p.setPrecio(precio);
        p.setCantidad(cantidad);
        p.setCategoria(categoria);
        return true;
    }

    public boolean eliminar(String codigo) {
        Iterator<Producto> it = lista.iterator();
        while (it.hasNext()) {
            if (it.next().getCodigo().equalsIgnoreCase(codigo.trim())) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    // ── Filtros ───────────────────────────────────────────────────────────

    public ArrayList<Producto> porCategoria(String categoria) {
        ArrayList<Producto> res = new ArrayList<>();
        for (Producto p : lista)
            if (p.getCategoria().equalsIgnoreCase(categoria.trim()))
                res.add(p);
        return res;
    }

    /** Devuelve las categorías únicas presentes en el catálogo. */
    public ArrayList<String> categorias() {
        ArrayList<String> cats = new ArrayList<>();
        for (Producto p : lista)
            if (!cats.contains(p.getCategoria()))
                cats.add(p.getCategoria());
        java.util.Collections.sort(cats);
        return cats;
    }

    /** Devuelve los productos agrupados por categoría. */
    public Map<String, ArrayList<Producto>> agrupadosPorCategoria() {
        Map<String, ArrayList<Producto>> mapa = new LinkedHashMap<>();
        for (String cat : categorias()) {
            mapa.put(cat, porCategoria(cat));
        }
        return mapa;
    }

    /** Serializa todo el catálogo a JSON array. */
    public String toJson() {
        StringBuilder sb = new StringBuilder("[\n");
        for (int i = 0; i < lista.size(); i++) {
            sb.append("  ").append(lista.get(i).toJson());
            if (i < lista.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("]");
        return sb.toString();
    }
}
