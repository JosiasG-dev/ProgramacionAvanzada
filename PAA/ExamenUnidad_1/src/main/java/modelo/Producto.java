package modelo;

/**
 * Clase Abstracta Producto.
 * Define la estructura común de todos los productos del supermercado.
 * No puede ser instanciada directamente.
 */
public abstract class Producto {

    // ── Atributos comunes ────────────────────────────────────────────────
    protected String codigo;
    protected String nombre;
    protected double precio;
    protected int    cantidad;
    protected String categoria;
    protected String imagenUrl;
    protected String descripcion;

    // ── Constructor ──────────────────────────────────────────────────────
    public Producto(String codigo, String nombre, double precio,
                    int cantidad, String categoria,
                    String imagenUrl, String descripcion) {
        this.codigo      = codigo.trim();
        this.nombre      = nombre.trim();
        this.precio      = precio;
        this.cantidad    = cantidad;
        this.categoria   = categoria.trim();
        this.imagenUrl   = imagenUrl.trim();
        this.descripcion = descripcion.trim();
    }

    // ── Métodos abstractos (cada subclase DEBE implementarlos) ───────────

    /** Tipo de almacenamiento requerido por la categoría. */
    public abstract String tipoAlmacenamiento();

    /** Información extra propia de cada categoría en formato JSON. */
    public abstract String infoExtraJson();

    // ── Métodos concretos ────────────────────────────────────────────────

    /** Valor total en inventario (precio × cantidad). */
    public double totalValor() {
        return Math.round(precio * cantidad * 100.0) / 100.0;
    }

    /**
     * Serializa el producto completo a JSON (sin librerías externas).
     */
    public String toJson() {
        return "{"
            + "\"codigo\":"           + jsonStr(codigo)      + ","
            + "\"nombre\":"           + jsonStr(nombre)      + ","
            + "\"precio\":"           + precio               + ","
            + "\"cantidad\":"         + cantidad             + ","
            + "\"categoria\":"        + jsonStr(categoria)   + ","
            + "\"imagenUrl\":"        + jsonStr(imagenUrl)   + ","
            + "\"descripcion\":"      + jsonStr(descripcion) + ","
            + "\"tipoAlmacenamiento\":" + jsonStr(tipoAlmacenamiento()) + ","
            + infoExtraJson()
            + "}";
    }

    /** Escapa y envuelve en comillas dobles para JSON. */
    protected static String jsonStr(String s) {
        return "\"" + s.replace("\\", "\\\\")
                       .replace("\"", "\\\"")
                       .replace("\n", "\\n") + "\"";
    }

    // ── Getters / Setters ────────────────────────────────────────────────
    public String getCodigo()      { return codigo; }
    public String getNombre()      { return nombre; }
    public double getPrecio()      { return precio; }
    public int    getCantidad()    { return cantidad; }
    public String getCategoria()   { return categoria; }
    public String getImagenUrl()   { return imagenUrl; }
    public String getDescripcion() { return descripcion; }

    public void setCodigo(String codigo)           { this.codigo      = codigo.trim(); }
    public void setNombre(String nombre)           { this.nombre      = nombre.trim(); }
    public void setPrecio(double precio)           { this.precio      = precio; }
    public void setCantidad(int cantidad)          { this.cantidad    = cantidad; }
    public void setCategoria(String categoria)     { this.categoria   = categoria.trim(); }
    public void setImagenUrl(String imagenUrl)     { this.imagenUrl   = imagenUrl.trim(); }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion.trim(); }

    @Override
    public String toString() {
        return codigo + " - " + nombre + "  $" + String.format("%.2f", precio)
               + "  (x" + cantidad + ")  [" + categoria + "]";
    }
}
