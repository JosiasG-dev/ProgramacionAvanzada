package vista;

import controlador.InventarioControlador;
import controlador.ProveedorControlador;
import modelo.MovimientoInventario;
import modelo.Producto;
import modelo.Proveedor;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * VISTA - Inventario (entradas, salidas, ajustes)
 */
public class InventarioVista extends JPanel {

    private final InventarioControlador ctrl;
    private final ProveedorControlador  provCtrl;

    private JTable           tablaMovimientos;
    private DefaultTableModel modeloTabla;
    private JTable           tablaBajoStock;
    private DefaultTableModel modeloBajoStock;

    private JComboBox<String> cmbProducto, cmbTipo, cmbProveedor;
    private JTextField        txtCantidad, txtPrecio, txtMotivo;
    private JLabel            lblStockActual;

    private List<Producto>  productosLista;
    private List<Proveedor> proveedoresLista;

    public InventarioVista(InventarioControlador ctrl, ProveedorControlador provCtrl) {
        this.ctrl     = ctrl;
        this.provCtrl = provCtrl;
        setLayout(new BorderLayout());
        construirUI();
        cargarDatos();
    }

    private void construirUI() {
        JLabel titulo = new JLabel("  Control de Inventario");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 15));
        titulo.setBorder(BorderFactory.createEmptyBorder(6, 4, 6, 4));
        add(titulo, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            crearPanelMovimientos(), crearPanelFormulario());
        split.setDividerLocation(620);
        split.setResizeWeight(0.60);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);
    }

    private JPanel crearPanelMovimientos() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 4));

        JLabel lbl = new JLabel("Historial de Movimientos");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        panel.add(lbl, BorderLayout.NORTH);

        String[] cols = {"#", "Fecha", "Producto", "Tipo", "Cantidad", "Precio U.", "Motivo"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaMovimientos = new JTable(modeloTabla);
        tablaMovimientos.setRowHeight(22);
        tablaMovimientos.getColumnModel().getColumn(0).setMaxWidth(40);
        tablaMovimientos.getColumnModel().getColumn(3).setMaxWidth(70);
        tablaMovimientos.getColumnModel().getColumn(4).setMaxWidth(70);

        // Colorear columna Tipo
        tablaMovimientos.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                if (!sel) {
                    String tipo = v != null ? v.toString() : "";
                    setBackground(switch (tipo) {
                        case "ENTRADA" -> new Color(200, 230, 201);
                        case "SALIDA"  -> new Color(255, 205, 210);
                        case "AJUSTE"  -> new Color(255, 249, 196);
                        default        -> Color.WHITE;
                    });
                }
                setHorizontalAlignment(CENTER);
                return this;
            }
        });

        // Tabla bajo stock
        JLabel lblBS = new JLabel("Productos con bajo inventario (≤ 10):");
        lblBS.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblBS.setForeground(new Color(180, 0, 0));

        String[] colsBS = {"Producto", "Categoría", "Stock", "Unidad"};
        modeloBajoStock = new DefaultTableModel(colsBS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaBajoStock = new JTable(modeloBajoStock);
        tablaBajoStock.setRowHeight(20);
        tablaBajoStock.setPreferredScrollableViewportSize(new Dimension(400, 90));

        JPanel panelBS = new JPanel(new BorderLayout(0, 2));
        panelBS.add(lblBS, BorderLayout.NORTH);
        panelBS.add(new JScrollPane(tablaBajoStock), BorderLayout.CENTER);
        panelBS.setPreferredSize(new Dimension(0, 140));

        panel.add(new JScrollPane(tablaMovimientos), BorderLayout.CENTER);
        panel.add(panelBS, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        JLabel titulo = new JLabel("Registrar Movimiento");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        panel.add(titulo, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 4, 6, 4);

        cmbTipo      = new JComboBox<>(new String[]{"ENTRADA", "SALIDA", "AJUSTE"});
        cmbProducto  = new JComboBox<>();
        cmbProveedor = new JComboBox<>();
        txtCantidad  = new JTextField();
        txtPrecio    = new JTextField();
        txtMotivo    = new JTextField();
        lblStockActual = new JLabel("—");
        lblStockActual.setFont(new Font("SansSerif", Font.BOLD, 13));

        cmbProducto.addActionListener(e -> actualizarStockActual());
        cmbTipo.addActionListener(e -> ajustarPorTipo());

        int r = 0;
        addRow(form, gbc, r++, "Tipo:", cmbTipo);
        addRow(form, gbc, r++, "Producto:", cmbProducto);
        addRow(form, gbc, r++, "Stock actual:", lblStockActual);
        addRow(form, gbc, r++, "Cantidad:", txtCantidad);
        addRow(form, gbc, r++, "Precio unitario $:", txtPrecio);
        addRow(form, gbc, r++, "Proveedor (entrada):", cmbProveedor);
        addRow(form, gbc, r++, "Motivo / Nota:", txtMotivo);

        // Info label
        JLabel lblInfo = new JLabel("<html><b>ENTRADA</b>: ingreso · <b>SALIDA</b>: merma/devolución · <b>AJUSTE</b>: corrección</html>");
        lblInfo.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblInfo.setForeground(Color.DARK_GRAY);
        gbc.gridx = 0; gbc.gridy = r; gbc.gridwidth = 2;
        form.add(lblInfo, gbc);

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnLimpiar   = new JButton("Limpiar");
        btnRegistrar.addActionListener(e -> registrar());
        btnLimpiar.addActionListener(e   -> limpiar());
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        btns.add(btnLimpiar);
        btns.add(btnRegistrar);

        panel.add(new JScrollPane(form) {{ setBorder(null); }}, BorderLayout.CENTER);
        panel.add(btns, BorderLayout.SOUTH);
        return panel;
    }

    private void addRow(JPanel form, GridBagConstraints gbc, int fila, String label, JComponent campo) {
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 1; gbc.weightx = 0.35;
        form.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 0.65;
        form.add(campo, gbc);
    }

    private void cargarDatos() {
        productosLista   = ctrl.obtenerProductos();
        proveedoresLista = provCtrl.obtenerProveedores();

        cmbProducto.removeAllItems();
        for (Producto p : productosLista) cmbProducto.addItem(p.getNombre());

        cmbProveedor.removeAllItems();
        cmbProveedor.addItem("— Sin proveedor —");
        for (Proveedor p : proveedoresLista) cmbProveedor.addItem(p.getNombre());

        recargarTablaMovimientos();
        recargarBajoStock();
        actualizarStockActual();
    }

    private void recargarTablaMovimientos() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
        modeloTabla.setRowCount(0);
        List<MovimientoInventario> movs = ctrl.obtenerMovimientos();
        for (int i = movs.size() - 1; i >= 0; i--) {
            MovimientoInventario m = movs.get(i);
            modeloTabla.addRow(new Object[]{
                m.getId(), m.getFecha().format(fmt),
                m.getProductoNombre(), m.getTipo().name(),
                m.getCantidad(),
                m.getPrecioUnitario() != null ? "$" + m.getPrecioUnitario() : "—",
                m.getMotivo()
            });
        }
    }

    private void recargarBajoStock() {
        modeloBajoStock.setRowCount(0);
        for (Producto p : ctrl.obtenerProductosBajoStock(10)) {
            modeloBajoStock.addRow(new Object[]{p.getNombre(), p.getCategoria(), p.getCantidadAlmacen(), p.getUnidad()});
        }
    }

    private void actualizarStockActual() {
        int idx = cmbProducto.getSelectedIndex();
        if (idx >= 0 && idx < productosLista.size()) {
            Producto p = productosLista.get(idx);
            lblStockActual.setText(p.getCantidadAlmacen() + " " + p.getUnidad());
            txtPrecio.setText(p.getPrecioCompra().toPlainString());
        }
    }

    private void ajustarPorTipo() {
        String tipo = (String) cmbTipo.getSelectedItem();
        cmbProveedor.setEnabled("ENTRADA".equals(tipo));
        txtPrecio.setEnabled(!"SALIDA".equals(tipo));
    }

    private void registrar() {
        int idxProd = cmbProducto.getSelectedIndex();
        if (idxProd < 0) return;
        Producto p = productosLista.get(idxProd);
        String tipo = (String) cmbTipo.getSelectedItem();
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            BigDecimal precio = txtPrecio.getText().trim().isBlank()
                ? BigDecimal.ZERO : new BigDecimal(txtPrecio.getText().trim());
            String motivo = txtMotivo.getText().trim();

            boolean ok = switch (tipo) {
                case "ENTRADA" -> {
                    int idxProv = cmbProveedor.getSelectedIndex() - 1;
                    int idProv  = idxProv >= 0 ? proveedoresLista.get(idxProv).getId() : 0;
                    yield ctrl.registrarEntrada(p.getId(), cantidad, precio, idProv, motivo);
                }
                case "SALIDA"  -> ctrl.registrarSalida(p.getId(), cantidad, motivo);
                case "AJUSTE"  -> ctrl.ajustarInventario(p.getId(), cantidad, motivo);
                default -> false;
            };
            if (ok) {
                cargarDatos();
                limpiar();
                JOptionPane.showMessageDialog(this, "Movimiento registrado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese valores numéricos válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiar() {
        txtCantidad.setText("");
        txtMotivo.setText("");
        cmbTipo.setSelectedIndex(0);
        if (cmbProducto.getItemCount() > 0) cmbProducto.setSelectedIndex(0);
        actualizarStockActual();
    }
}
