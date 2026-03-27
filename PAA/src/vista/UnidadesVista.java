package vista;

import controlador.UnidadControlador;
import modelo.UnidadMedida;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * VISTA - Unidades de Medida
 */
public class UnidadesVista extends JPanel {

    private final UnidadControlador ctrl;
    private JTable            tabla;
    private DefaultTableModel modeloTabla;
    private List<UnidadMedida> unidadesVisibles;

    private JTextField        txtNombre, txtAbreviatura;
    private JComboBox<String> cmbTipo;
    private int idEditando = 0;

    public UnidadesVista(UnidadControlador ctrl) {
        this.ctrl = ctrl;
        setLayout(new BorderLayout());
        construirUI();
        cargarUnidades();
    }

    private void construirUI() {
        JLabel titulo = new JLabel("  Configuración de Unidades de Medida");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 15));
        titulo.setBorder(BorderFactory.createEmptyBorder(6, 4, 6, 4));
        add(titulo, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            crearPanelTabla(), crearPanelFormulario());
        split.setDividerLocation(520);
        split.setResizeWeight(0.55);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 4));

        String[] cols = {"ID", "Nombre", "Abreviatura", "Tipo"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(22);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getColumnModel().getColumn(0).setMaxWidth(40);
        tabla.getColumnModel().getColumn(2).setMaxWidth(100);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarEnFormulario();
        });

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        JButton btnNuevo    = new JButton("Nueva");
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

        JLabel titulo = new JLabel("Datos de la Unidad");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 13));
        panel.add(titulo, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 4, 8, 4);

        txtNombre      = new JTextField();
        txtAbreviatura = new JTextField();
        cmbTipo = new JComboBox<>(new String[]{"Unidad", "Peso", "Volumen", "Longitud", "Área", "Otro"});

        int r = 0;
        addRow(form, gbc, r++, "Nombre *:",      txtNombre);
        addRow(form, gbc, r++, "Abreviatura *:", txtAbreviatura);
        addRow(form, gbc, r++, "Tipo:",          cmbTipo);

        JLabel lblEj = new JLabel("<html><b>Unidades comunes:</b><br>" +
            "Peso: kg, g &nbsp;&nbsp; Volumen: L, mL<br>" +
            "Unidad: pza, caja, bolsa, paq, doc<br>" +
            "Longitud: m</html>");
        lblEj.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblEj.setForeground(Color.DARK_GRAY);

        JButton btnGuardar = new JButton("Guardar");
        JButton btnLimpiar = new JButton("Limpiar");
        btnGuardar.addActionListener(e -> guardar());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        btns.add(btnLimpiar);
        btns.add(btnGuardar);

        JPanel centro = new JPanel(new BorderLayout(0, 12));
        centro.add(form, BorderLayout.NORTH);
        centro.add(lblEj, BorderLayout.CENTER);

        panel.add(centro, BorderLayout.CENTER);
        panel.add(btns, BorderLayout.SOUTH);
        return panel;
    }

    private void addRow(JPanel form, GridBagConstraints gbc, int fila, String label, JComponent campo) {
        gbc.gridx = 0; gbc.gridy = fila; gbc.weightx = 0.4;
        form.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 0.6;
        form.add(campo, gbc);
    }

    private void cargarUnidades() {
        unidadesVisibles = ctrl.obtenerUnidades();
        modeloTabla.setRowCount(0);
        for (UnidadMedida u : unidadesVisibles) {
            modeloTabla.addRow(new Object[]{u.getId(), u.getNombre(), u.getAbreviatura(), u.getTipo()});
        }
    }

    private void cargarEnFormulario() {
        int fila = tabla.getSelectedRow();
        if (fila < 0 || fila >= unidadesVisibles.size()) return;
        UnidadMedida u = unidadesVisibles.get(fila);
        idEditando = u.getId();
        txtNombre.setText(u.getNombre());
        txtAbreviatura.setText(u.getAbreviatura());
        for (int i = 0; i < cmbTipo.getItemCount(); i++) {
            if (cmbTipo.getItemAt(i).equals(u.getTipo())) { cmbTipo.setSelectedIndex(i); break; }
        }
    }

    private void limpiarFormulario() {
        idEditando = 0;
        txtNombre.setText(""); txtAbreviatura.setText(""); cmbTipo.setSelectedIndex(0);
        tabla.clearSelection();
    }

    private void guardar() {
        UnidadMedida u = new UnidadMedida();
        u.setId(idEditando);
        u.setNombre(txtNombre.getText().trim());
        u.setAbreviatura(txtAbreviatura.getText().trim());
        u.setTipo((String) cmbTipo.getSelectedItem());
        if (ctrl.guardarUnidad(u)) {
            cargarUnidades();
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Unidad guardada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione una unidad."); return; }
        UnidadMedida u = unidadesVisibles.get(fila);
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar \"" + u.getNombre() + "\"?\nVerifique que no esté en uso.",
                "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
            ctrl.eliminarUnidad(u.getId());
            cargarUnidades();
            limpiarFormulario();
        }
    }
}
