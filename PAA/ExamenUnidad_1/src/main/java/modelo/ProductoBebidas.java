package modelo;

/**
 * Bebidas — Líquidos / Pesado.
 * Agua, Refrescos, Jugos, Bebidas Energéticas, Café, Té, Cervezas.
 */
public class ProductoBebidas extends Producto {

    private int     volumenMl;
    private boolean alcoholica;

    public ProductoBebidas(String codigo, String nombre, double precio,
                           int cantidad, int volumenMl, boolean alcoholica,
                           String imagenUrl, String descripcion) {
        super(codigo, nombre, precio, cantidad, "Bebidas", imagenUrl, descripcion);
        this.volumenMl  = volumenMl;
        this.alcoholica = alcoholica;
    }

    @Override public String tipoAlmacenamiento() { return "Líquidos / Pesado"; }

    @Override public String infoExtraJson() {
        return "\"volumenMl\":" + volumenMl + ",\"alcoholica\":" + alcoholica;
    }

    public int     getVolumenMl()          { return volumenMl; }
    public boolean isAlcoholica()          { return alcoholica; }
    public void    setVolumenMl(int v)     { this.volumenMl  = v; }
    public void    setAlcoholica(boolean v){ this.alcoholica = v; }
}
