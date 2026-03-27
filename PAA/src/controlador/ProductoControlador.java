package controlador;

import modelo.DataStore;
import modelo.Producto;
import vista.ProductoVista;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductoControlador {

    private final DataStore dataStore;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public ProductoControlador() {
        this.dataStore = DataStore.getInstance();
    }

    public List<Producto> obtenerTodosLosProductos() {
        return dataStore.getProductosActivos();
    }

    public List<Producto> obtenerProductosPorCategoria(String categoria) {
        return dataStore.getProductosActivos().stream()
            .filter(p -> p.getCategoria().equalsIgnoreCase(categoria))
            .collect(java.util.stream.Collectors.toList());
    }

    public List<String> obtenerCategorias() {
        return dataStore.getCategorias();
    }

    public boolean guardarProducto(Producto producto) {
        try {
            validarProducto(producto);
            producto.calcularPrecioVenta();
            if (producto.getId() == 0) {
                dataStore.agregarProducto(producto);
            } else {
                dataStore.actualizarProducto(producto);
            }
            return true;
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),
                "Error de validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    public void eliminarProducto(int id) {
        dataStore.eliminarProducto(id);
    }

    public Producto obtenerProductoPorId(int id) {
        return dataStore.getProductoPorId(id);
    }

    public void descargarImagen(String url, String nombreArchivo, ImageDescargadaCallback callback) {
        executor.submit(() -> {
            try {
                URL imageUrl = new URL(url);
                BufferedImage img = ImageIO.read(imageUrl);
                if (img != null) {
                    File destino = new File("src/images/" + nombreArchivo + ".jpg");
                    destino.getParentFile().mkdirs();
                    ImageIO.write(img, "jpg", destino);
                    SwingUtilities.invokeLater(() -> callback.onDescargada(destino.getAbsolutePath(), img));
                } else {
                    SwingUtilities.invokeLater(() -> callback.onError("No se pudo descargar la imagen"));
                }
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> callback.onError(e.getMessage()));
            }
        });
    }

    private void validarProducto(Producto p) {
        if (p.getNombre() == null || p.getNombre().trim().isEmpty())
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        if (p.getPrecioCompra() == null || p.getPrecioCompra().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("El precio de compra debe ser mayor a cero.");
        if (p.getPorcentajeGanancia() < 0)
            throw new IllegalArgumentException("El porcentaje de ganancia no puede ser negativo.");
        if (p.getCantidadAlmacen() < 0)
            throw new IllegalArgumentException("La cantidad en almacén no puede ser negativa.");
        if (p.getUnidad() == null || p.getUnidad().trim().isEmpty())
            throw new IllegalArgumentException("Debe seleccionar una unidad de medida.");
    }

    public interface ImageDescargadaCallback {
        void onDescargada(String rutaLocal, BufferedImage imagen);
        void onError(String mensaje);
    }
}
