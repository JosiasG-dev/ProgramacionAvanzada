package modelo;

/**
 * Cuidado Personal — Higiene.
 * Shampú, Jabón de tocador, Pasta dental, Desodorantes, Cremas.
 */
public class ProductoCuidadoPersonal extends Producto {

    private String tipoPiel;  // "Seco", "Graso", "Normal", "Todo tipo"

    public ProductoCuidadoPersonal(String codigo, String nombre, double precio,
                                   int cantidad, String tipoPiel,
                                   String imagenUrl, String descripcion) {
        super(codigo, nombre, precio, cantidad, "Cuidado Personal", imagenUrl, descripcion);
        this.tipoPiel = tipoPiel;
    }

    @Override public String tipoAlmacenamiento() { return "Higiene"; }

    @Override public String infoExtraJson() {
        return "\"tipoPiel\":" + jsonStr(tipoPiel);
    }

    public String getTipoPiel()          { return tipoPiel; }
    public void   setTipoPiel(String v)  { this.tipoPiel = v; }
}
