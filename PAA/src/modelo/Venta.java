package modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * MODELO - Venta (ticket del punto de venta)
 */
public class Venta {
    private int id;
    private LocalDateTime fecha;
    private List<DetalleVenta> detalles;
    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal total;
    private BigDecimal efectivoRecibido;
    private BigDecimal cambio;
    private String formaPago; // "Efectivo", "Tarjeta"
    private String folio;

    public Venta() {
        this.fecha = LocalDateTime.now();
        this.detalles = new ArrayList<>();
        this.subtotal = BigDecimal.ZERO;
        this.descuento = BigDecimal.ZERO;
        this.total = BigDecimal.ZERO;
        this.efectivoRecibido = BigDecimal.ZERO;
        this.cambio = BigDecimal.ZERO;
    }

    public void agregarDetalle(DetalleVenta detalle) {
        detalles.add(detalle);
        calcularTotales();
    }

    public void eliminarDetalle(int index) {
        if (index >= 0 && index < detalles.size()) {
            detalles.remove(index);
            calcularTotales();
        }
    }

    public void calcularTotales() {
        subtotal = detalles.stream()
            .map(DetalleVenta::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        total = subtotal.subtract(descuento == null ? BigDecimal.ZERO : descuento);
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public List<DetalleVenta> getDetalles() { return detalles; }

    public BigDecimal getSubtotal() { return subtotal; }
    public BigDecimal getDescuento() { return descuento; }
    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
        calcularTotales();
    }
    public BigDecimal getTotal() { return total; }

    public BigDecimal getEfectivoRecibido() { return efectivoRecibido; }
    public void setEfectivoRecibido(BigDecimal efectivoRecibido) {
        this.efectivoRecibido = efectivoRecibido;
        this.cambio = efectivoRecibido.subtract(total);
    }

    public BigDecimal getCambio() { return cambio; }

    public String getFormaPago() { return formaPago; }
    public void setFormaPago(String formaPago) { this.formaPago = formaPago; }

    public String getFolio() { return folio; }
    public void setFolio(String folio) { this.folio = folio; }
}
