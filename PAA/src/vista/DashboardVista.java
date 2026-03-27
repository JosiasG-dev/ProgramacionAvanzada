package vista;

import controlador.ReporteControlador;
import modelo.Producto;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Map;

/**
 * VISTA - Dashboard
 */
public class DashboardVista extends JPanel {

    private final ReporteControlador ctrl;

    public DashboardVista(ReporteControlador ctrl) {
        this.ctrl = ctrl;
        setLayout(new BorderLayout());
        construirUI();
    }

    private void construirUI() {
        // Título
        JLabel titulo = new JLabel("  Dashboard — Resumen General");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        titulo.setBorder(BorderFactory.createEmptyBorder(8, 4, 8, 4));
        add(titulo, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(crearContenido());
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel crearContenido() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        Map<String, Object> res = ctrl.obtenerResumenGeneral();

        // --- Fila de KPI ---
        JPanel filaKPI = new JPanel(new GridLayout(1, 4, 10, 0));
        filaKPI.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        filaKPI.add(crearCard("Productos",    String.valueOf(res.get("totalProductos")),    "registrados"));
        filaKPI.add(crearCard("Proveedores",  String.valueOf(res.get("totalProveedores")),  "activos"));
        filaKPI.add(crearCard("Ventas",       String.valueOf(res.get("totalVentas")),        "transacciones"));
        filaKPI.add(crearCard("Ingresos",     "$" + res.get("totalIngresos"),               "acumulado"));
        panel.add(filaKPI);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));

        // --- Segunda fila ---
        JPanel fila2 = new JPanel(new GridLayout(1, 2, 10, 0));
        fila2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        long bajo = (long) res.get("productosBajoStock");
        BigDecimal valInv = ctrl.calcularValorInventario();
        fila2.add(crearCard("Bajo Stock",         String.valueOf(bajo),           "productos con ≤5 uds."));
        fila2.add(crearCard("Valor Inventario",   "$" + valInv.toPlainString(),   "costo total de compra"));
        panel.add(fila2);
        panel.add(Box.createRigidArea(new Dimension(0, 16)));

        // --- Tabla bajo stock ---
        JLabel lbl = new JLabel("Productos con bajo inventario:");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lbl);
        panel.add(Box.createRigidArea(new Dimension(0, 6)));

        String[] cols = {"Producto", "Categoría", "Stock", "Unidad"};
        java.util.List<Producto> bajos = ctrl.obtenerProductosBajoStock();
        Object[][] data = bajos.stream().limit(10).map(p -> new Object[]{
            p.getNombre(), p.getCategoria(), p.getCantidadAlmacen(), p.getUnidad()
        }).toArray(Object[][]::new);

        JTable tabla = new JTable(data, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla.setRowHeight(24);
        JScrollPane st = new JScrollPane(tabla);
        st.setPreferredSize(new Dimension(600, 220));
        st.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        st.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(st);

        return panel;
    }

    private JPanel crearCard(String titulo, String valor, String desc) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblTitulo.setForeground(Color.GRAY);

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("SansSerif", Font.BOLD, 22));

        JLabel lblDesc = new JLabel(desc);
        lblDesc.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblDesc.setForeground(Color.GRAY);

        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.add(lblTitulo);
        inner.add(Box.createRigidArea(new Dimension(0, 4)));
        inner.add(lblValor);
        inner.add(Box.createRigidArea(new Dimension(0, 2)));
        inner.add(lblDesc);
        card.add(inner, BorderLayout.CENTER);
        return card;
    }
}
