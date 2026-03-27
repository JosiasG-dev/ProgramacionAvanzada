package controlador;

import modelo.*;
import javax.swing.*;
import java.math.BigDecimal;
import java.util.List;

public class VentaControlador {
    private final DataStore dataStore;
    private Venta ventaActual;

    public VentaControlador() {
        this.dataStore = DataStore.getInstance();
        this.ventaActual = new Venta();
    }

    public Venta getVentaActual() { return ventaActual; }

    public List<Producto> buscarProductos(String texto) {
        return dataStore.getProductosActivos().stream()
            .filter(p -> p.getNombre().toLowerCase().contains(texto.toLowerCase())
                      || (p.getCodigoBarras() != null && p.getCodigoBarras().contains(texto)))
            .collect(java.util.stream.Collectors.toList());
    }

    public boolean agregarProducto(Producto producto, int cantidad) {
        if (producto.getCantidadAlmacen() < cantidad) {
            JOptionPane.showMessageDialog(null,
                "Stock insuficiente. Disponible: " + producto.getCantidadAlmacen(),
                "Sin stock", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        for (DetalleVenta det : ventaActual.getDetalles()) {
            if (det.getProductoId() == producto.getId()) {
                det.setCantidad(det.getCantidad() + cantidad);
                ventaActual.calcularTotales();
                return true;
            }
        }
        ventaActual.agregarDetalle(new DetalleVenta(producto, cantidad));
        return true;
    }

    public void eliminarDetalle(int index) {
        ventaActual.eliminarDetalle(index);
    }

    public boolean procesarPago(BigDecimal efectivo, String formaPago) {
        if (ventaActual.getDetalles().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay productos en la venta.",
                "Venta vacía", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (formaPago.equals("Efectivo") && efectivo.compareTo(ventaActual.getTotal()) < 0) {
            JOptionPane.showMessageDialog(null,
                "El efectivo es insuficiente. Total: $" + ventaActual.getTotal(),
                "Pago insuficiente", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        ventaActual.setFormaPago(formaPago);
        if (formaPago.equals("Efectivo")) {
            ventaActual.setEfectivoRecibido(efectivo);
        }
        dataStore.registrarVenta(ventaActual);
        ventaActual = new Venta();
        return true;
    }

    public void cancelarVenta() {
        ventaActual = new Venta();
    }

    public List<Venta> obtenerVentas() {
        return dataStore.getVentas();
    }
}
