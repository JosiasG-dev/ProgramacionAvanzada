package modelo;

/**
 * Carnes y Pescados — Congelados / Frescos.
 * Pollo, Res, Cerdo, Pescados, Mariscos.
 */
public class ProductoCarnesPescados extends Producto {

    private String  tipoCorte;
    private boolean congelado;

    public ProductoCarnesPescados(String codigo, String nombre, double precio,
                                  int cantidad, String tipoCorte, boolean congelado,
                                  String imagenUrl, String descripcion) {
        super(codigo, nombre, precio, cantidad, "Carnes y Pescados", imagenUrl, descripcion);
        this.tipoCorte = tipoCorte;
        this.congelado = congelado;
    }

    @Override public String tipoAlmacenamiento() { return "Congelados / Frescos"; }

    @Override public String infoExtraJson() {
        return "\"tipoCorte\":" + jsonStr(tipoCorte)
             + ",\"congelado\":" + congelado;
    }

    public String  getTipoCorte()         { return tipoCorte; }
    public boolean isCongelado()          { return congelado; }
    public void    setTipoCorte(String v) { this.tipoCorte = v; }
    public void    setCongelado(boolean v){ this.congelado  = v; }
}
