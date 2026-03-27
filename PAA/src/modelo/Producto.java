package modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * MODELO - Clase Producto
 * Representa un producto en la tienda de abarrotes
 */
public class Producto {
    private int id;
    private String nombre;
    private String categoria;
    private String subcategoria;
    private BigDecimal precioCompra;
    private double porcentajeGanancia;
    private BigDecimal precioVenta;
    private int cantidadAlmacen;
    private String unidad;         // "pieza", "kg", "litro", etc.
    private String imagenUrl;      // URL imagen de Surti-Tienda
    private String imagenLocal;    // ruta local si ya se descargó
    private String codigoBarras;
    private String descripcion;
    private boolean activo;

    public Producto() {
        this.activo = true;
        this.precioCompra = BigDecimal.ZERO;
        this.precioVenta = BigDecimal.ZERO;
    }

    public Producto(int id, String nombre, String categoria, String subcategoria,
                    BigDecimal precioCompra, double porcentajeGanancia,
                    int cantidadAlmacen, String unidad, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.subcategoria = subcategoria;
        this.precioCompra = precioCompra;
        this.porcentajeGanancia = porcentajeGanancia;
        this.cantidadAlmacen = cantidadAlmacen;
        this.unidad = unidad;
        this.imagenUrl = imagenUrl;
        this.activo = true;
        calcularPrecioVenta();
    }

    /**
     * Calcula el precio de venta a partir del precio de compra y el porcentaje de ganancia
     */
    public void calcularPrecioVenta() {
        if (precioCompra != null && porcentajeGanancia >= 0) {
            BigDecimal factor = BigDecimal.ONE.add(
                BigDecimal.valueOf(porcentajeGanancia / 100.0)
            );
            this.precioVenta = precioCompra.multiply(factor).setScale(2, RoundingMode.HALF_UP);
        }
    }

    // =========== GETTERS & SETTERS ===========
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getSubcategoria() { return subcategoria; }
    public void setSubcategoria(String subcategoria) { this.subcategoria = subcategoria; }

    public BigDecimal getPrecioCompra() { return precioCompra; }
    public void setPrecioCompra(BigDecimal precioCompra) {
        this.precioCompra = precioCompra;
        calcularPrecioVenta();
    }

    public double getPorcentajeGanancia() { return porcentajeGanancia; }
    public void setPorcentajeGanancia(double porcentajeGanancia) {
        this.porcentajeGanancia = porcentajeGanancia;
        calcularPrecioVenta();
    }

    public BigDecimal getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(BigDecimal precioVenta) { this.precioVenta = precioVenta; }

    public int getCantidadAlmacen() { return cantidadAlmacen; }
    public void setCantidadAlmacen(int cantidadAlmacen) { this.cantidadAlmacen = cantidadAlmacen; }

    public String getUnidad() { return unidad; }
    public void setUnidad(String unidad) { this.unidad = unidad; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public String getImagenLocal() { return imagenLocal; }
    public void setImagenLocal(String imagenLocal) { this.imagenLocal = imagenLocal; }

    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return nombre + " (" + unidad + ")";
    }
}
