package vista;

import controlador.ProveedorControlador;
import modelo.Proveedor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * VISTA - Gestión de Proveedores
 */
public class ProveedorVista extends JPanel {

    private final ProveedorControlador ctrl;
    private JTable            tabla;
    private DefaultTableModel modeloTabla;
    private List<Proveedor>   proveedoresVisibles;

    private JTextField txtNombre, txtContacto, txtTelefono, txtEmail, txtDireccion, txtRfc;
    private int idEditando = 0;

    public ProveedorVista(ProveedorControlador ctrl) {
        this.ctrl = ctrl;
        setLayout(new BorderLayout());
        construirUI();
        cargarProveedores();
    }

    private void construirUI() {
        JLabel titulo = new JLabel("  Gestión de Proveedores");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 15));
        titulo.setBorder(BorderFactory.createEmptyBorder(6, 4, 6, 4));
        add(titulo, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            crearPanelTabla(), crearPanelFormulario());
        split.setDividerLocation(560);
        split.setResizeWeight(0.55);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 4));

        String[] cols = {"ID", "Nombre", "Contacto", "Teléfono", "Email", "RFC"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(22);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getColumnModel().getColumn(0).setMaxWidth(40);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarEnFormulario();
        });

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        JButton btnNuevo    = new JButton("Nuevo");
        JButton btnEliminar = new JButton("Eliminar");
        btnNuevo.addActionListener(e    -> limpiarFormulario());
        btnEliminar.addActionListener(e -> eliminarSeleccionado());
        btns.add(btnNuevo);
        btns.add(btnEliminar);

        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        panel.add(btns, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        JLabel titulo = new JLabel("Datos del Proveedor");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        panel.add(titulo, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(7, 4, 7, 4);

        txtNombre    = new JTextField();
        txtContacto  = new JTextField();
        txtTelefono  = new JTextField();
        txtEmail     = new JTextField();
        txtDireccion = new JTextField();
        txtRfc       = new JTextField();

        int r = 0;
        addRow(form, gbc, r++, "Nombre *:",    txtNombre);
        addRow(form, gbc, r++, "Contacto:",    txtContacto);
        addRow(form, gbc, r++, "Teléfono *:",  txtTelefono);
        addRow(form, gbc, r++, "Email:",       txtEmail);
        addRow(form, gbc, r++, "Dirección:",   txtDireccion);
        addRow(form, gbc, r++, "RFC:",         txtRfc);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnLimpiar = new JButton("Limpiar");
        btnGuardar.addActionListener(e -> guardar());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        btns.add(btnLimpiar);
        btns.add(btnGuardar);

        panel.add(form, BorderLayout.CENTER);
        panel.add(btns, BorderLayout.SOUTH);
        return panel;
    }

    private void addRow(JPanel form, GridBagConstraints gbc, int fila, String label, JComponent campo) {
        gbc.gridx = 0; gbc.gridy = fila; gbc.weightx = 0.35;
        form.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 0.65;
        form.add(campo, gbc);
    }

    private void cargarProveedores() {
        proveedoresVisibles = ctrl.obtenerProveedores();
        modeloTabla.setRowCount(0);
        for (Proveedor p : proveedoresVisibles) {
            modeloTabla.addRow(new Object[]{p.getId(), p.getNombre(), p.getContacto(),
                p.getTelefono(), p.getEmail(), p.getRfc()});
        }
    }

    private void cargarEnFormulario() {
        int fila = tabla.getSelectedRow();
        if (fila < 0 || fila >= proveedoresVisibles.size()) return;
        Proveedor p = proveedoresVisibles.get(fila);
        idEditando = p.getId();
        txtNombre.setText(p.getNombre());
        txtContacto.setText(p.getContacto() != null ? p.getContacto() : "");
        txtTelefono.setText(p.getTelefono() != null ? p.getTelefono() : "");
        txtEmail.setText(p.getEmail() != null ? p.getEmail() : "");
        txtDireccion.setText(p.getDireccion() != null ? p.getDireccion() : "");
        txtRfc.setText(p.getRfc() != null ? p.getRfc() : "");
    }

    private void limpiarFormulario() {
        idEditando = 0;
        txtNombre.setText(""); txtContacto.setText(""); txtTelefono.setText("");
        txtEmail.setText("");  txtDireccion.setText(""); txtRfc.setText("");
        tabla.clearSelection();
    }

    private void guardar() {
        Proveedor p = new Proveedor();
        p.setId(idEditando);
        p.setNombre(txtNombre.getText().trim());
        p.setContacto(txtContacto.getText().trim());
        p.setTelefono(txtTelefono.getText().trim());
        p.setEmail(txtEmail.getText().trim());
        p.setDireccion(txtDireccion.getText().trim());
        p.setRfc(txtRfc.getText().trim());
        if (ctrl.guardarProveedor(p)) {
            cargarProveedores();
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Proveedor guardado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione un proveedor."); return; }
        Proveedor p = proveedoresVisibles.get(fila);
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar \"" + p.getNombre() + "\"?",
                "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            ctrl.eliminarProveedor(p.getId());
            cargarProveedores();
            limpiarFormulario();
        }
    }
}
