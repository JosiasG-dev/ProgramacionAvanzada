package vista;

import controlador.ProductoControlador;
import controlador.VentaControlador;
import modelo.DetalleVenta;
import modelo.Producto;
import modelo.Venta;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * VISTA - Punto de Venta
 */
public class PuntoVentaVista extends JPanel {

    private final VentaControlador      ctrl;
    private final ProductoControlador   prodCtrl;

    private JTextField          txtBuscar;
    private JList<String>       listaSugerencias;
    private DefaultListModel<String> modeloSugerencias;
    private JSpinner            spnCantidad;

    private JTable              tablaCarrito;
    private DefaultTableModel   modeloCarrito;

    private JLabel              lblSubtotal, lblTotal, lblCambio;
    private JTextField          txtEfectivo;
    private JComboBox<String>   cmbFormaPago;

    private List<Producto> resultadosBusqueda;

    public PuntoVentaVista(VentaControlador ctrl, ProductoControlador prodCtrl) {
        this.ctrl     = ctrl;
        this.prodCtrl = prodCtrl;
        setLayout(new BorderLayout());
        construirUI();
    }

    private void construirUI() {
        JLabel titulo = new JLabel("  Punto de Venta");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 15));
        titulo.setBorder(BorderFactory.createEmptyBorder(6, 4, 6, 4));
        add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridLayout(1, 2, 8, 0));
        centro.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        centro.add(crearPanelBusqueda());
        centro.add(crearPanelCarrito());
        add(centro, BorderLayout.CENTER);
    }

    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        JLabel lbl = new JLabel("Buscar Producto");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        txtBuscar = new JTextField();
        txtBuscar.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtBuscar.setPreferredSize(new Dimension(0, 34));
        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { buscar(); }
        });

        modeloSugerencias = new DefaultListModel<>();
        listaSugerencias  = new JList<>(modeloSugerencias);
        listaSugerencias.setFixedCellHeight(28);
        listaSugerencias.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) agregarSeleccionado();
            }
        });

        JPanel panelCant = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        panelCant.add(new JLabel("Cantidad:"));
        spnCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));
        spnCantidad.setPreferredSize(new Dimension(70, 28));
        panelCant.add(spnCantidad);

        JButton btnAgregar = new JButton("Agregar al carrito");
        btnAgregar.addActionListener(e -> agregarSeleccionado());

        JPanel inferior = new JPanel(new BorderLayout(0, 4));
        inferior.add(panelCant, BorderLayout.NORTH);
        inferior.add(btnAgregar, BorderLayout.SOUTH);

        JPanel top = new JPanel(new BorderLayout(0, 4));
        top.add(lbl, BorderLayout.NORTH);
        top.add(txtBuscar, BorderLayout.CENTER);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(listaSugerencias), BorderLayout.CENTER);
        panel.add(inferior, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelCarrito() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        JLabel lbl = new JLabel("Carrito de Venta");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));

        String[] cols = {"Producto", "Cantidad", "Precio U.", "Subtotal", "✖"};
        modeloCarrito = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 4; }
            @Override public Class<?> getColumnClass(int c) { return c == 4 ? JButton.class : String.class; }
        };
        tablaCarrito = new JTable(modeloCarrito);
        tablaCarrito.setRowHeight(24);
        tablaCarrito.getColumnModel().getColumn(1).setMaxWidth(65);
        tablaCarrito.getColumnModel().getColumn(2).setMaxWidth(80);
        tablaCarrito.getColumnModel().getColumn(3).setMaxWidth(90);
        tablaCarrito.getColumnModel().getColumn(4).setMaxWidth(38);
        tablaCarrito.getColumn("✖").setCellRenderer(new ButtonRenderer());
        tablaCarrito.getColumn("✖").setCellEditor(new ButtonEditor(new JCheckBox(), tablaCarrito));

        // Panel totales
        JPanel panelTotales = new JPanel(new GridBagLayout());
        panelTotales.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 4, 3, 4);

        lblSubtotal = new JLabel("$ 0.00"); lblSubtotal.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblTotal    = new JLabel("$ 0.00"); lblTotal.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblCambio   = new JLabel("$ 0.00"); lblCambio.setFont(new Font("SansSerif", Font.BOLD, 16));
        txtEfectivo = new JTextField();
        txtEfectivo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtEfectivo.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) { calcularCambio(); }
        });
        cmbFormaPago = new JComboBox<>(new String[]{"Efectivo", "Tarjeta"});
        cmbFormaPago.addActionListener(e -> txtEfectivo.setEnabled("Efectivo".equals(cmbFormaPago.getSelectedItem())));

        int r = 0;
        addTotalRow(panelTotales, gbc, r++, "Subtotal:",          lblSubtotal);
        addTotalRow(panelTotales, gbc, r++, "TOTAL:",             lblTotal);
        addTotalRow(panelTotales, gbc, r++, "Forma de pago:",     cmbFormaPago);
        addTotalRow(panelTotales, gbc, r++, "Efectivo recibido:", txtEfectivo);
        addTotalRow(panelTotales, gbc, r++, "Cambio:",            lblCambio);

        JButton btnCobrar   = new JButton("COBRAR");
        btnCobrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        JButton btnCancelar = new JButton("Cancelar");
        btnCobrar.addActionListener(e   -> cobrar());
        btnCancelar.addActionListener(e -> cancelar());

        JPanel btnsCobro = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 4));
        btnsCobro.add(btnCancelar);
        btnsCobro.add(btnCobrar);

        JPanel abajo = new JPanel(new BorderLayout(0, 4));
        abajo.add(panelTotales, BorderLayout.CENTER);
        abajo.add(btnsCobro, BorderLayout.SOUTH);

        panel.add(lbl, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);
        panel.add(abajo, BorderLayout.SOUTH);
        return panel;
    }

    private void addTotalRow(JPanel p, GridBagConstraints gbc, int fila, String lbl, JComponent campo) {
        gbc.gridx = 0; gbc.gridy = fila; gbc.weightx = 0.4;
        p.add(new JLabel(lbl), gbc);
        gbc.gridx = 1; gbc.weightx = 0.6;
        p.add(campo, gbc);
    }

    private void buscar() {
        String texto = txtBuscar.getText().trim();
        modeloSugerencias.clear();
        if (texto.isBlank()) { resultadosBusqueda = null; return; }
        resultadosBusqueda = ctrl.buscarProductos(texto);
        for (Producto p : resultadosBusqueda) {
            modeloSugerencias.addElement(p.getNombre() + "  [$" + p.getPrecioVenta() + " / " + p.getUnidad() + "]  Stock:" + p.getCantidadAlmacen());
        }
    }

    private void agregarSeleccionado() {
        int idx = listaSugerencias.getSelectedIndex();
        if (idx < 0 || resultadosBusqueda == null || idx >= resultadosBusqueda.size()) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la lista.");
            return;
        }
        Producto p = resultadosBusqueda.get(idx);
        if (ctrl.agregarProducto(p, (int) spnCantidad.getValue())) {
            refrescarCarrito();
            txtBuscar.setText("");
            modeloSugerencias.clear();
            spnCantidad.setValue(1);
        }
    }

    private void refrescarCarrito() {
        Venta venta = ctrl.getVentaActual();
        modeloCarrito.setRowCount(0);
        for (DetalleVenta d : venta.getDetalles()) {
            modeloCarrito.addRow(new Object[]{
                d.getProductoNombre(), d.getCantidad(),
                "$" + d.getPrecioUnitario(), "$" + d.getSubtotal(), "✖"
            });
        }
        lblSubtotal.setText("$ " + venta.getSubtotal());
        lblTotal.setText("$ " + venta.getTotal());
        calcularCambio();
    }

    private void calcularCambio() {
        try {
            BigDecimal efectivo = new BigDecimal(txtEfectivo.getText().trim());
            BigDecimal cambio   = efectivo.subtract(ctrl.getVentaActual().getTotal());
            lblCambio.setText("$ " + (cambio.compareTo(BigDecimal.ZERO) >= 0 ? cambio : "—"));
        } catch (Exception ignored) { lblCambio.setText("$ —"); }
    }

    private void cobrar() {
        String formaPago = (String) cmbFormaPago.getSelectedItem();
        BigDecimal efectivo = BigDecimal.ZERO;
        if ("Efectivo".equals(formaPago)) {
            try { efectivo = new BigDecimal(txtEfectivo.getText().trim()); }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ingrese el monto de efectivo.", "Campo requerido", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        if (ctrl.procesarPago(efectivo, formaPago)) {
            String msg = "Venta registrada.\n";
            if ("Efectivo".equals(formaPago)) msg += "Cambio: " + lblCambio.getText();
            JOptionPane.showMessageDialog(this, msg, "Venta completada", JOptionPane.INFORMATION_MESSAGE);
            refrescarCarrito();
            txtEfectivo.setText("");
            lblCambio.setText("$ 0.00");
        }
    }

    private void cancelar() {
        if (JOptionPane.showConfirmDialog(this, "¿Cancelar la venta actual?",
                "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            ctrl.cancelarVenta();
            refrescarCarrito();
            txtEfectivo.setText("");
        }
    }

    // ---- Renderer / Editor botón en tabla ----
    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() { setOpaque(true); }
        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
            setText("✖"); return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private final JButton boton;
        private int filaActual;
        public ButtonEditor(JCheckBox chk, JTable tabla) {
            super(chk);
            boton = new JButton("✖");
            boton.setOpaque(true);
            boton.addActionListener(e -> {
                fireEditingStopped();
                ctrl.eliminarDetalle(filaActual);
                refrescarCarrito();
            });
        }
        @Override
        public Component getTableCellEditorComponent(JTable t, Object v, boolean sel, int row, int col) {
            filaActual = row; return boton;
        }
        @Override public Object getCellEditorValue() { return "✖"; }
    }
}
