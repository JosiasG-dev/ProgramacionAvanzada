package controlador;

import modelo.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ReporteControlador {
    private final DataStore dataStore;

    public ReporteControlador() {
        this.dataStore = DataStore.getInstance();
    }
 public Map<String, Object> obtenerResumenGeneral() {
        Map<String, Object> resumen = new LinkedHashMap<>();
        List<Producto> productos = dataStore.getProductosActivos();
        List<Venta> ventas = dataStore.getVentas();

        resumen.put("totalProductos", productos.size());
        resumen.put("totalProveedores", dataStore.getProveedores().size());
        resumen.put("totalVentas", ventas.size());

        BigDecimal totalIngresos = ventas.stream()
            .map(Venta::getTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        resumen.put("totalIngresos", totalIngresos);

        long productosBajoStock = productos.stream()
            .filter(p -> p.getCantidadAlmacen() <= 5).count();
        resumen.put("productosBajoStock", productosBajoStock);

        return resumen;
    }

    public Map<String, BigDecimal> ventasPorCategoria() {
        Map<String, BigDecimal> mapa = new LinkedHashMap<>();
        for (Venta v : dataStore.getVentas()) {
            for (DetalleVenta d : v.getDetalles()) {
                Producto p = dataStore.getProductoPorId(d.getProductoId());
                String cat = (p != null) ? p.getCategoria() : "Otros";
                mapa.merge(cat, d.getSubtotal(), BigDecimal::add);
            }
        }
        return mapa.entrySet().stream()
            .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                (e1, e2) -> e1, LinkedHashMap::new));
    }

   public List<Object[]> topProductosMasVendidos() {
        Map<String, Integer> conteo = new HashMap<>();
        for (Venta v : dataStore.getVentas()) {
            for (DetalleVenta d : v.getDetalles()) {
                conteo.merge(d.getProductoNombre(), d.getCantidad(), Integer::sum);
            }
        }
        return conteo.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(10)
            .map(e -> new Object[]{e.getKey(), e.getValue()})
            .collect(Collectors.toList());
    }

  public List<Producto> obtenerProductosBajoStock() {
        return dataStore.getProductosActivos().stream()
            .filter(p -> p.getCantidadAlmacen() <= 10)
            .sorted(Comparator.comparingInt(Producto::getCantidadAlmacen))
            .collect(Collectors.toList());
    }
  public List<Venta> obtenerVentas() {
        return dataStore.getVentas();
    }

    public List<MovimientoInventario> obtenerMovimientos() {
        return dataStore.getMovimientos();
    }

     public BigDecimal calcularValorInventario() {
        return dataStore.getProductosActivos().stream()
            .map(p -> p.getPrecioCompra().multiply(BigDecimal.valueOf(p.getCantidadAlmacen())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
