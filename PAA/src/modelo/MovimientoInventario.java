package modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * MODELO - Movimiento de Inventario (entrada/salida de stock)
 */
public class MovimientoInventario {
    public enum TipoMovimiento { ENTRADA, SALIDA, AJUSTE }

    private int id;
    private int productoId;
    private String productoNombre;
    private TipoMovimiento tipo;
    private int cantidad;
    private BigDecimal precioUnitario;
    private LocalDateTime fecha;
    private String motivo;
    private int proveedorId;
    private String proveedorNombre;

    public MovimientoInventario() {
        this.fecha = LocalDateTime.now();
    }

    public MovimientoInventario(int productoId, TipoMovimiento tipo,
                                 int cantidad, BigDecimal precioUnitario, String motivo) {
        this.productoId = productoId;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.motivo = motivo;
        this.fecha = LocalDateTime.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProductoId() { return productoId; }
    public void setProductoId(int productoId) { this.productoId = productoId; }

    public String getProductoNombre() { return productoNombre; }
    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }

    public TipoMovimiento getTipo() { return tipo; }
    public void setTipo(TipoMovimiento tipo) { this.tipo = tipo; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public int getProveedorId() { return proveedorId; }
    public void setProveedorId(int proveedorId) { this.proveedorId = proveedorId; }

    public String getProveedorNombre() { return proveedorNombre; }
    public void setProveedorNombre(String proveedorNombre) { this.proveedorNombre = proveedorNombre; }
}
