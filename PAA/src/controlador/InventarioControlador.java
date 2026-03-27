package controlador;

import modelo.DataStore;
import modelo.MovimientoInventario;
import modelo.Producto;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.List;
public class InventarioControlador {
    private final DataStore dataStore;

    public InventarioControlador() {
        this.dataStore = DataStore.getInstance();
    }

    public List<MovimientoInventario> obtenerMovimientos() {
        return dataStore.getMovimientos();
    }

    public List<Producto> obtenerProductos() {
        return dataStore.getProductosActivos();
    }

    public boolean registrarEntrada(int productoId, int cantidad, BigDecimal precio,
                                    int proveedorId, String motivo) {
        try {
            Producto p = dataStore.getProductoPorId(productoId);
            if (p == null) throw new IllegalArgumentException("Producto no encontrado.");
            if (cantidad <= 0) throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
            if (precio.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("El precio debe ser mayor a cero.");

            MovimientoInventario mov = new MovimientoInventario(
                productoId, MovimientoInventario.TipoMovimiento.ENTRADA,
                cantidad, precio, motivo
            );
            mov.setProductoNombre(p.getNombre());
            mov.setProveedorId(proveedorId);
            dataStore.registrarMovimiento(mov);
            return true;
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),
                "Error de validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    public boolean registrarSalida(int productoId, int cantidad, String motivo) {
        try {
            Producto p = dataStore.getProductoPorId(productoId);
            if (p == null) throw new IllegalArgumentException("Producto no encontrado.");
            if (cantidad <= 0) throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
            if (p.getCantidadAlmacen() < cantidad)
                throw new IllegalArgumentException("Stock insuficiente. Disponible: " + p.getCantidadAlmacen());

            MovimientoInventario mov = new MovimientoInventario(
                productoId, MovimientoInventario.TipoMovimiento.SALIDA,
                cantidad, p.getPrecioVenta(), motivo
            );
            mov.setProductoNombre(p.getNombre());
            dataStore.registrarMovimiento(mov);
            return true;
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),
                "Error de validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    public boolean ajustarInventario(int productoId, int nuevaCantidad, String motivo) {
        try {
            Producto p = dataStore.getProductoPorId(productoId);
            if (p == null) throw new IllegalArgumentException("Producto no encontrado.");
            if (nuevaCantidad < 0) throw new IllegalArgumentException("La cantidad no puede ser negativa.");

            MovimientoInventario mov = new MovimientoInventario(
                productoId, MovimientoInventario.TipoMovimiento.AJUSTE,
                nuevaCantidad, p.getPrecioCompra(), motivo
            );
            mov.setProductoNombre(p.getNombre());
            dataStore.registrarMovimiento(mov);
            return true;
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),
                "Error de validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    public List<Producto> obtenerProductosBajoStock(int minimo) {
        return dataStore.getProductosActivos().stream()
            .filter(p -> p.getCantidadAlmacen() <= minimo)
            .collect(java.util.stream.Collectors.toList());
    }
}
