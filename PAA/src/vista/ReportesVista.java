package vista;

import controlador.ReporteControlador;
import modelo.DetalleVenta;
import modelo.Producto;
import modelo.Venta;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * VISTA - Reportes y Estadísticas
 */
public class ReportesVista extends JPanel {

    private final ReporteControlador ctrl;

    public ReportesVista(ReporteControlador ctrl) {
        this.ctrl = ctrl;
        setLayout(new BorderLayout());
        construirUI();
    }

    private void construirUI() {
        JLabel titulo = new JLabel("  Reportes y Estadísticas");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 15));
        titulo.setBorder(BorderFactory.createEmptyBorder(6, 4, 6, 4));
        add(titulo, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Ventas",        crearTabVentas());
        tabs.addTab("Top Productos", crearTabTopProductos());
        tabs.addTab("Inventario",    crearTabInventario());
        tabs.addTab("Categorías",    crearTabCategorias());
        add(tabs, BorderLayout.CENTER);
    }

    // ---- TAB: VENTAS ----
    private JPanel crearTabVentas() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        Map<String, Object> res = ctrl.obtenerResumenGeneral();
        JPanel cards = new JPanel(new GridLayout(1, 3, 10, 0));
        cards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        cards.add(crearCard("Total Ventas",     String.valueOf(res.get("totalVentas")), "transacciones"));
        cards.add(crearCard("Ingresos Totales", "$" + res.get("totalIngresos"),         "acumulado"));
        cards.add(crearCard("Valor Inventario", "$" + ctrl.calcularValorInventario(),   "costo compra"));

        String[] cols = {"Folio", "Fecha", "Artículos", "Total", "Forma Pago"};
        DefaultTableModel modelo = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        List<Venta> ventas = ctrl.obtenerVentas();
        for (int i = ventas.size() - 1; i >= 0; i--) {
            Venta v = ventas.get(i);
            modelo.addRow(new Object[]{v.getFolio(), v.getFecha().format(fmt),
                v.getDetalles().size(), "$" + v.getTotal(), v.getFormaPago()});
        }
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(22);

        // Panel detalle
        JPanel panelDet = new JPanel(new BorderLayout());
        panelDet.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Detalle de venta seleccionada",
            TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 11)));
        String[] colsDet = {"Producto", "Cantidad", "Precio Unit.", "Subtotal"};
        DefaultTableModel modeloDet = new DefaultTableModel(colsDet, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tablaDetalle = new JTable(modeloDet);
        tablaDetalle.setRowHeight(22);
        panelDet.add(new JScrollPane(tablaDetalle), BorderLayout.CENTER);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tabla.getSelectedRow();
                if (row >= 0 && row < ventas.size()) {
                    Venta v = ventas.get(ventas.size() - 1 - row);
                    modeloDet.setRowCount(0);
                    for (DetalleVenta d : v.getDetalles()) {
                        modeloDet.addRow(new Object[]{d.getProductoNombre(), d.getCantidad(),
                            "$" + d.getPrecioUnitario(), "$" + d.getSubtotal()});
                    }
                }
            }
        });

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
            new JScrollPane(tabla), panelDet);
        split.setDividerLocation(240);
        split.setResizeWeight(0.6);

        JPanel contenido = new JPanel(new BorderLayout(0, 8));
        contenido.add(cards, BorderLayout.NORTH);
        contenido.add(split, BorderLayout.CENTER);
        panel.add(contenido, BorderLayout.CENTER);
        return panel;
    }

    // ---- TAB: TOP PRODUCTOS ----
    private JPanel crearTabTopProductos() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JLabel titulo = new JLabel("Top 10 Productos Más Vendidos");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 13));

        String[] cols = {"#", "Producto", "Unidades vendidas"};
        DefaultTableModel modelo = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        List<Object[]> top = ctrl.topProductosMasVendidos();
        int pos = 1;
        for (Object[] fila : top) {
            String med = switch (pos) { case 1 -> "🥇"; case 2 -> "🥈"; case 3 -> "🥉"; default -> String.valueOf(pos); };
            modelo.addRow(new Object[]{med, fila[0], fila[1]});
            pos++;
        }
        if (top.isEmpty()) modelo.addRow(new Object[]{"—", "Sin datos de ventas aún", "—"});

        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(28);
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);
        tabla.getColumnModel().getColumn(2).setMaxWidth(160);

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return panel;
    }

    // ---- TAB: INVENTARIO ----
    private JPanel crearTabInventario() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JLabel titulo = new JLabel("Productos con Bajo Stock (≤ 10 unidades)");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        titulo.setForeground(new Color(180, 0, 0));

        String[] cols = {"Producto", "Categoría", "Stock", "Unidad", "Precio Compra", "Valor en Stock"};
        DefaultTableModel modelo = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        List<Producto> bajos = ctrl.obtenerProductosBajoStock();
        for (Producto p : bajos) {
            BigDecimal valor = p.getPrecioCompra().multiply(BigDecimal.valueOf(p.getCantidadAlmacen()));
            modelo.addRow(new Object[]{p.getNombre(), p.getCategoria(), p.getCantidadAlmacen(),
                p.getUnidad(), "$" + p.getPrecioCompra(), "$" + valor});
        }
        if (bajos.isEmpty()) modelo.addRow(new Object[]{"✅ Todos los productos tienen stock suficiente", "", "", "", "", ""});

        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(22);
        tabla.getColumnModel().getColumn(2).setMaxWidth(60);
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                if (!sel && r < bajos.size()) {
                    int stock = bajos.get(r).getCantidadAlmacen();
                    setBackground(stock <= 3 ? new Color(255, 205, 210) : new Color(255, 249, 196));
                } else if (!sel) setBackground(Color.WHITE);
                return this;
            }
        });

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return panel;
    }

    // ---- TAB: CATEGORÍAS ----
    private JPanel crearTabCategorias() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JLabel titulo = new JLabel("Ingresos por Categoría");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 13));

        Map<String, BigDecimal> ventasCat = ctrl.ventasPorCategoria();
        String[] cols = {"Categoría", "Ingresos Totales"};
        DefaultTableModel modelo = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        if (ventasCat.isEmpty()) {
            modelo.addRow(new Object[]{"Sin ventas registradas aún", "$0.00"});
        } else {
            ventasCat.forEach((cat, total) -> modelo.addRow(new Object[]{cat, "$" + total}));
        }

        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(26);

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return panel;
    }

    // ---- Helper card ----
    private JPanel crearCard(String titulo, String valor, String desc) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        JLabel lblT = new JLabel(titulo); lblT.setFont(new Font("SansSerif", Font.PLAIN, 11)); lblT.setForeground(Color.GRAY);
        JLabel lblV = new JLabel(valor);  lblV.setFont(new Font("SansSerif", Font.BOLD, 20));
        JLabel lblD = new JLabel(desc);   lblD.setFont(new Font("SansSerif", Font.PLAIN, 10)); lblD.setForeground(Color.GRAY);
        card.add(lblT); card.add(Box.createRigidArea(new Dimension(0,3)));
        card.add(lblV); card.add(Box.createRigidArea(new Dimension(0,2)));
        card.add(lblD);
        return card;
    }
}
