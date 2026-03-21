package modelo;

/**
 * Panadería y Tortillería — Alta rotación diaria.
 * Pan de caja, Pan dulce, Bolillo, Tortilla de maíz/harina.
 */
public class ProductoPanaderia extends Producto {

    private int piezasPorPaquete;

    public ProductoPanaderia(String codigo, String nombre, double precio,
                             int cantidad, int piezasPorPaquete,
                             String imagenUrl, String descripcion) {
        super(codigo, nombre, precio, cantidad, "Panadería y Tortillería", imagenUrl, descripcion);
        this.piezasPorPaquete = piezasPorPaquete;
    }

    @Override public String tipoAlmacenamiento() { return "Alta rotación diaria"; }

    @Override public String infoExtraJson() {
        return "\"piezasPorPaquete\":" + piezasPorPaquete;
    }

    public int  getPiezasPorPaquete()      { return piezasPorPaquete; }
    public void setPiezasPorPaquete(int v) { this.piezasPorPaquete = v; }
}
