package modelo;

/**
 * Frutas y Verduras — Perecedero (Peso variable).
 * Frutas de temporada, Verduras, Hortalizas, Legumbres frescas.
 */
public class ProductoFrutasVerduras extends Producto {

    private String unidadVenta;  // "kg", "pieza", "manojo"
    private String temporada;

    public ProductoFrutasVerduras(String codigo, String nombre, double precio,
                                  int cantidad, String unidadVenta, String temporada,
                                  String imagenUrl, String descripcion) {
        super(codigo, nombre, precio, cantidad, "Frutas y Verduras", imagenUrl, descripcion);
        this.unidadVenta = unidadVenta;
        this.temporada   = temporada;
    }

    @Override public String tipoAlmacenamiento() { return "Perecedero (Peso variable)"; }

    @Override public String infoExtraJson() {
        return "\"unidadVenta\":" + jsonStr(unidadVenta)
             + ",\"temporada\":" + jsonStr(temporada);
    }

    public String getUnidadVenta()           { return unidadVenta; }
    public String getTemporada()             { return temporada; }
    public void   setUnidadVenta(String v)   { this.unidadVenta = v; }
    public void   setTemporada(String v)     { this.temporada   = v; }
}
