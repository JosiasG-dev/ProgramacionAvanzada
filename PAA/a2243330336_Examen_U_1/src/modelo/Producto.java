package modelo;

public class Producto {

    private String codigo;
    private String nombre;
    private double precio;
    private int    cantidad;
    private String categoria;

    public Producto(String codigo, String nombre, double precio, int cantidad, String categoria) {
        this.codigo    = codigo.trim();
        this.nombre    = nombre.trim();
        this.precio    = precio;
        this.cantidad  = cantidad;
        this.categoria = categoria.trim();
    }

    public String getCodigo()    { return codigo; }
    public String getNombre()    { return nombre; }
    public double getPrecio()    { return precio; }
    public int    getCantidad()  { return cantidad; }
    public String getCategoria() { return categoria; }

    public void setCodigo(String codigo)       { this.codigo    = codigo.trim(); }
    public void setNombre(String nombre)       { this.nombre    = nombre.trim(); }
    public void setPrecio(double precio)       { this.precio    = precio; }
    public void setCantidad(int cantidad)      { this.cantidad  = cantidad; }
    public void setCategoria(String categoria) { this.categoria = categoria.trim(); }

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
}
