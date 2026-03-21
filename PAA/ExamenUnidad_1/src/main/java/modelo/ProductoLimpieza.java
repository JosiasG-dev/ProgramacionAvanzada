package modelo;

/**
 * Limpieza del Hogar — Químicos (Aislados).
 * Detergentes, Suavizantes, Desinfectantes, Papel Higiénico, Utensilios.
 */
public class ProductoLimpieza extends Producto {

    private boolean contieneQuimicos;

    public ProductoLimpieza(String codigo, String nombre, double precio,
                            int cantidad, boolean contieneQuimicos,
                            String imagenUrl, String descripcion) {
        super(codigo, nombre, precio, cantidad, "Limpieza del Hogar", imagenUrl, descripcion);
        this.contieneQuimicos = contieneQuimicos;
    }

    @Override public String tipoAlmacenamiento() { return "Químicos (Aislados)"; }

    @Override public String infoExtraJson() {
        return "\"contieneQuimicos\":" + contieneQuimicos;
    }

    public boolean isContieneQuimicos()          { return contieneQuimicos; }
    public void    setContieneQuimicos(boolean v){ this.contieneQuimicos = v; }
}
