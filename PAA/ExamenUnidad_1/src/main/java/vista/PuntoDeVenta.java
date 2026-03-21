package vista;

import modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Ventana interna — Punto de Venta.
 * Permite seleccionar productos, armar el ticket y cobrar.
 */
public class PuntoDeVenta extends JInternalFrame {

    // ── Datos cliente ─────────────────────────────────────────────────────
    private JTextField tfCliente;
    private JTextField tfNombreCliente;

    // ── Selección de producto ─────────────────────────────────────────────
    private JComboBox<Producto> cbProductos;
    private JTextField          tfCantidad;
    private JLabel              lblPrecioUnit;
    private JLabel              lblStock;

    // ── Tabla ticket ──────────────────────────────────────────────────────
    private JTable           tablaTicket;
    private DefaultTableModel modeloTicket;

    // ── Totales ───────────────────────────────────────────────────────────
    private JLabel lblSubtotal;
    private JLabel lblIva;
    private JLabel lblTotal;

    // ── Botones ───────────────────────────────────────────────────────────
    private JButton btnAgregar;
    private JButton btnQuitar;
    private JButton btnCobrar;
    private JButton btnNuevo;
    private JButton btnImagen;

    public PuntoDeVenta() {
        super("🛒 Punto de Venta", true, true, true, true);
        setSize(950, 600);
        setLocation(80, 80);
        construir();
    }

    private void construir() {
        setLayout(new BorderLayout(8, 8));
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createEmptyBorder(8, 8, 8, 8));

        add(panelSuperior(), BorderLayout.NORTH);
        add(panelCentral(),  BorderLayout.CENTER);
        add(panelTotales(),  BorderLayout.SOUTH);
    }

    // ── Panel superior: cliente + selección producto ──────────────────────
    private JPanel panelSuperior() {
        JPanel p = new JPanel(new GridLayout(1, 2, 10, 0));

        // Sub-panel cliente
        JPanel pCliente = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        pCliente.setBorder(BorderFactory.createTitledBorder("Cliente"));
        pCliente.add(new JLabel("ID:"));
        tfCliente = tf(100);
        pCliente.add(tfCliente);
        pCliente.add(new JLabel("Nombre:"));
        tfNombreCliente = tf(180);
        pCliente.add(tfNombreCliente);

        // Sub-panel producto
        JPanel pProd = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        pProd.setBorder(BorderFactory.createTitledBorder("Agregar Producto"));

        cbProductos = new JComboBox<>();
        cbProductos.setPreferredSize(new Dimension(280, 26));
        cbProductos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cbProductos.addActionListener(e -> actualizarInfoProducto());

        pProd.add(new JLabel("Producto:"));
        pProd.add(cbProductos);
        pProd.add(new JLabel("Cant:"));
        tfCantidad = tf(50);
        tfCantidad.setText("1");
        pProd.add(tfCantidad);

        lblPrecioUnit = new JLabel("$0.00");
        lblPrecioUnit.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStock = new JLabel("Stock: 0");
        lblStock.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblStock.setForeground(Color.GRAY);

        pProd.add(new JLabel("Precio:"));
        pProd.add(lblPrecioUnit);
        pProd.add(lblStock);

        btnAgregar = btn("➕ Agregar", new Color(39, 174, 96));
        btnImagen  = btn("🖼 Imagen",  new Color(142, 68, 173));
        pProd.add(btnAgregar);
        pProd.add(btnImagen);

        p.add(pCliente);
        p.add(pProd);
        return p;
    }

    // ── Panel central: tabla del ticket ───────────────────────────────────
    private JPanel panelCentral() {
        String[] cols = {"Código","Descripción","Cantidad","Precio Unit.","Importe"};
        modeloTicket = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaTicket = new JTable(modeloTicket);
        tablaTicket.setRowHeight(22);
        tablaTicket.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaTicket.getTableHeader().setBackground(new Color(31, 78, 121));
        tablaTicket.getTableHeader().setForeground(Color.WHITE);
        tablaTicket.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaTicket.setGridColor(new Color(220, 220, 220));
        tablaTicket.setSelectionBackground(new Color(210, 230, 255));

        int[] anchos = {80, 280, 80, 110, 110};
        for (int i = 0; i < anchos.length; i++)
            tablaTicket.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);

        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setBorder(BorderFactory.createTitledBorder("Detalle del Ticket"));
        p.add(new JScrollPane(tablaTicket), BorderLayout.CENTER);

        JPanel pBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        btnQuitar = btn("➖ Quitar línea", new Color(192, 57, 43));
        btnNuevo  = btn("🗑 Nuevo ticket", new Color(100, 100, 100));
        pBtns.add(btnQuitar);
        pBtns.add(btnNuevo);
        p.add(pBtns, BorderLayout.SOUTH);
        return p;
    }

    // ── Panel totales ─────────────────────────────────────────────────────
    private JPanel panelTotales() {
        JPanel p = new JPanel(new BorderLayout());

        JPanel pTotales = new JPanel(new GridLayout(3, 2, 4, 2));
        pTotales.setBorder(BorderFactory.createTitledBorder("Totales"));

        pTotales.add(etiqueta("Subtotal (sin IVA):"));
        lblSubtotal = valor("$0.00");
        pTotales.add(lblSubtotal);

        pTotales.add(etiqueta("IVA (16%):"));
        lblIva = valor("$0.00");
        pTotales.add(lblIva);

        pTotales.add(etiqueta("TOTAL A PAGAR:"));
        lblTotal = new JLabel("$0.00", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotal.setForeground(new Color(31, 78, 121));
        pTotales.add(lblTotal);

        btnCobrar = new JButton("💳 COBRAR");
        btnCobrar.setBackground(new Color(31, 78, 121));
        btnCobrar.setForeground(Color.WHITE);
        btnCobrar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnCobrar.setFocusPainted(false);
        btnCobrar.setPreferredSize(new Dimension(180, 60));

        p.add(pTotales,   BorderLayout.CENTER);
        p.add(btnCobrar,  BorderLayout.EAST);
        return p;
    }

    // ── Helpers visuales ──────────────────────────────────────────────────
    private JTextField tf(int ancho) {
        JTextField tf = new JTextField();
        tf.setPreferredSize(new Dimension(ancho, 26));
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return tf;
    }

    private JButton btn(String texto, Color bg) {
        JButton b = new JButton(texto);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return b;
    }

    private JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto, SwingConstants.RIGHT);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return l;
    }

    private JLabel valor(String texto) {
        JLabel l = new JLabel(texto, SwingConstants.RIGHT);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return l;
    }

    // ── Lógica interna ────────────────────────────────────────────────────
    private void actualizarInfoProducto() {
        Producto p = getProductoSeleccionado();
        if (p == null) return;
        lblPrecioUnit.setText(String.format("$%.2f", p.getPrecio()));
        lblStock.setText("Stock: " + p.getCantidad());
        lblStock.setForeground(p.getCantidad() < 5 ? Color.RED : Color.GRAY);
    }

    // ── API pública ───────────────────────────────────────────────────────
    public void cargarProductos(List<Producto> lista) {
        cbProductos.removeAllItems();
        for (Producto p : lista) cbProductos.addItem(p);
        actualizarInfoProducto();
    }

    public Producto getProductoSeleccionado() {
        Object sel = cbProductos.getSelectedItem();
        return (sel instanceof Producto) ? (Producto) sel : null;
    }

    public void agregarFilaTicket(String cod, String nom, double cant,
                                  double precioUnit, double importe) {
        modeloTicket.addRow(new Object[]{
            cod, nom,
            String.valueOf((int)cant),
            String.format("%.2f", precioUnit),
            String.format("%.2f", importe)
        });
    }

    public void quitarFila(int fila) {
        if (fila >= 0 && fila < modeloTicket.getRowCount())
            modeloTicket.removeRow(fila);
    }

    public void actualizarTotales(double subtotal, double iva, double total) {
        lblSubtotal.setText(String.format("$%.2f", subtotal));
        lblIva.setText(String.format("$%.2f", iva));
        lblTotal.setText(String.format("$%.2f", total));
    }

    public void limpiarTicket() {
        modeloTicket.setRowCount(0);
        tfCliente.setText("");
        tfNombreCliente.setText("");
        tfCantidad.setText("1");
        lblSubtotal.setText("$0.00");
        lblIva.setText("$0.00");
        lblTotal.setText("$0.00");
    }

    public String          getCantidadStr()       { return tfCantidad.getText(); }
    public String          getCliente()           { return tfCliente.getText().trim(); }
    public String          getNombreCliente()     { return tfNombreCliente.getText().trim(); }
    public JTable          getTablaTicket()       { return tablaTicket; }
    public DefaultTableModel getModeloTicket()    { return modeloTicket; }

    public void addBtnAgregar(ActionListener al)  { btnAgregar.addActionListener(al); }
    public void addBtnQuitar(ActionListener al)   { btnQuitar.addActionListener(al); }
    public void addBtnCobrar(ActionListener al)   { btnCobrar.addActionListener(al); }
    public void addBtnNuevo(ActionListener al)    { btnNuevo.addActionListener(al); }
    public void addBtnImagen(ActionListener al)   { btnImagen.addActionListener(al); }
}
