package modelo;

/**
 * Abarrotes (Despensa) — No perecedero (Anaquel).
 * Aceites, Arroz, Frijol, Pastas, Enlatados, Especias, Harinas, Azúcar.
 */
public class ProductoAbarrotes extends Producto {

    private double pesoKg;

    public ProductoAbarrotes(String codigo, String nombre, double precio,
                             int cantidad, double pesoKg,
                             String imagenUrl, String descripcion) {
        super(codigo, nombre, precio, cantidad, "Abarrotes", imagenUrl, descripcion);
        this.pesoKg = pesoKg;
    }

    @Override public String tipoAlmacenamiento() { return "No perecedero (Anaquel)"; }

    @Override public String infoExtraJson() {
        return "\"pesoKg\":" + pesoKg;
    }

    public double getPesoKg()           { return pesoKg; }
    public void   setPesoKg(double v)   { this.pesoKg = v; }
}
