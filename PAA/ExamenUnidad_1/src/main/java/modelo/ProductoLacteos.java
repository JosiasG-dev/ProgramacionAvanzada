package modelo;

/**
 * Lácteos y Huevo — Cadena de Frío.
 * Leche, Huevo, Yogurt, Mantequilla, Crema, Gelatinas.
 */
public class ProductoLacteos extends Producto {

    private int diasCaducidad;

    public ProductoLacteos(String codigo, String nombre, double precio,
                           int cantidad, int diasCaducidad,
                           String imagenUrl, String descripcion) {
        super(codigo, nombre, precio, cantidad, "Lácteos y Huevo", imagenUrl, descripcion);
        this.diasCaducidad = diasCaducidad;
    }

    @Override public String tipoAlmacenamiento() { return "Cadena de Frío"; }

    @Override public String infoExtraJson() {
        return "\"diasCaducidad\":" + diasCaducidad;
    }

    public int  getDiasCaducidad()      { return diasCaducidad; }
    public void setDiasCaducidad(int v) { this.diasCaducidad = v; }
}
