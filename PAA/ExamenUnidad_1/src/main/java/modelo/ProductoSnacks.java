package modelo;

/**
 * Snacks y Dulcería — Compra por impulso.
 * Papas, Galletas, Chocolates, Dulces, Botanas saladas.
 */
public class ProductoSnacks extends Producto {

    private String sabor;

    public ProductoSnacks(String codigo, String nombre, double precio,
                          int cantidad, String sabor,
                          String imagenUrl, String descripcion) {
        super(codigo, nombre, precio, cantidad, "Snacks y Dulcería", imagenUrl, descripcion);
        this.sabor = sabor;
    }

    @Override public String tipoAlmacenamiento() { return "Compra por impulso"; }

    @Override public String infoExtraJson() {
        return "\"sabor\":" + jsonStr(sabor);
    }

    public String getSabor()         { return sabor; }
    public void   setSabor(String v) { this.sabor = v; }
}
