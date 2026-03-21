package modelo;

/**
 * Salchichonería — Refrigerados.
 * Jamón, Salchichas, Tocino, Quesos por kilo, Chorizo.
 */
public class ProductoSalchichoneria extends Producto {

    private boolean ventaPorKilo;

    public ProductoSalchichoneria(String codigo, String nombre, double precio,
                                  int cantidad, boolean ventaPorKilo,
                                  String imagenUrl, String descripcion) {
        super(codigo, nombre, precio, cantidad, "Salchichonería", imagenUrl, descripcion);
        this.ventaPorKilo = ventaPorKilo;
    }

    @Override public String tipoAlmacenamiento() { return "Refrigerados"; }

    @Override public String infoExtraJson() {
        return "\"ventaPorKilo\":" + ventaPorKilo;
    }

    public boolean isVentaPorKilo()          { return ventaPorKilo; }
    public void    setVentaPorKilo(boolean v){ this.ventaPorKilo = v; }
}
