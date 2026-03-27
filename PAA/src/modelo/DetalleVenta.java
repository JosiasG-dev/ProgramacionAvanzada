package modelo;

import java.math.BigDecimal;

/**
 * MODELO - Detalle de una línea de venta
 */
public class DetalleVenta {
    private int id;
    private int productoId;
    private String productoNombre;
    private String unidad;
    private int cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    public DetalleVenta() {}

    public DetalleVenta(Producto producto, int cantidad) {
        this.productoId = producto.getId();
        this.productoNombre = producto.getNombre();
        this.unidad = producto.getUnidad();
        this.cantidad = cantidad;
        this.precioUnitario = producto.getPrecioVenta();
        calcularSubtotal();
    }

    public void calcularSubtotal() {
        if (precioUnitario != null) {
            subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        }
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProductoId() { return productoId; }
    public void setProductoId(int productoId) { this.productoId = productoId; }

    public String getProductoNombre() { return productoNombre; }
    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }

    public String getUnidad() { return unidad; }
    public void setUnidad(String unidad) { this.unidad = unidad; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        calcularSubtotal();
    }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
        calcularSubtotal();
    }

    public BigDecimal getSubtotal() { return subtotal; }
}
