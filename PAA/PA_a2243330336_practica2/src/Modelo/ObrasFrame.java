package Parte2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.UUID;

public class ObrasFrame extends JInternalFrame {

    private JTextField TnombreObra;
    private JTextArea TdescripcionObra;
    private JButton Bagregar;
    private JButton Beliminar;
    private JButton Bsalir;
    private JTable Tobras;
    private DefaultTableModel modeloTabla;
    private static final String ARCHIVO = "obras.txt";
    private int contadorId = 1;

    public ObrasFrame() {
        super("Abrir una obra", true, true, true, true);
        initComponents();
        cargarDesdeArchivo();
        setSize(480, 400);
        setVisible(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Nombre de la obra:"), gbc);
        TnombreObra = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        panelFormulario.add(TnombreObra, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Descripción:"), gbc);
        TdescripcionObra = new JTextArea(4, 20);
        TdescripcionObra.setLineWrap(true);
        TdescripcionObra.setWrapStyleWord(true);
        JScrollPane spDesc = new JScrollPane(TdescripcionObra);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1;
        panelFormulario.add(spDesc, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        Bagregar = new JButton("Agregar");
        Beliminar = new JButton("Eliminar");
        Bsalir = new JButton("Salir");
        panelBotones.add(Bagregar);
        panelBotones.add(Beliminar);
        panelBotones.add(Bsalir);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panelFormulario.add(panelBotones, gbc);

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Descripción"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        Tobras = new JTable(modeloTabla);
        // Ocultar columna ID (se genera automáticamente y no se observa)
        Tobras.getColumnModel().getColumn(0).setMinWidth(0);
        Tobras.getColumnModel().getColumn(0).setMaxWidth(0);
        Tobras.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scrollPane = new JScrollPane(Tobras);

        add(panelFormulario, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        Bagregar.addActionListener(e -> agregarObra());
        Beliminar.addActionListener(e -> eliminarObra());
        Bsalir.addActionListener(e -> cerrar());

        Tobras.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = Tobras.getSelectedRow();
                if (fila >= 0) {
                    TnombreObra.setText(modeloTabla.getValueAt(fila, 1).toString());
                    TdescripcionObra.setText(modeloTabla.getValueAt(fila, 2).toString());
                }
            }
        });
    }

    private void agregarObra() {
        String nombre = TnombreObra.getText().trim();
        String descripcion = TdescripcionObra.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre de la obra.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Generar ID automáticamente
        String id = String.valueOf(contadorId++);
        modeloTabla.addRow(new Object[]{id, nombre, descripcion});
        TnombreObra.setText("");
        TdescripcionObra.setText("");
        guardarEnArchivo();
    }

    private void eliminarObra() {
        int fila = Tobras.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una obra.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int conf = JOptionPane.showConfirmDialog(this, "¿Eliminar obra seleccionada?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            modeloTabla.removeRow(fila);
            TnombreObra.setText(""); TdescripcionObra.setText("");
            guardarEnArchivo();
        }
    }

    private void cerrar() {
        try { setClosed(true); } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void guardarEnArchivo() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO))) {
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                String id = modeloTabla.getValueAt(i, 0).toString();
                String nombre = modeloTabla.getValueAt(i, 1).toString();
                String desc = modeloTabla.getValueAt(i, 2).toString().replace("\n", "\\n");
                pw.println(id + "|" + nombre + "|" + desc);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage());
        }
    }

    private void cargarDesdeArchivo() {
        File f = new File(ARCHIVO);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split("\\|", 3);
                if (p.length >= 2) {
                    String desc = p.length == 3 ? p[2].replace("\\n", "\n") : "";
                    modeloTabla.addRow(new Object[]{p[0], p[1], desc});
                    try { contadorId = Math.max(contadorId, Integer.parseInt(p[0]) + 1); } catch (NumberFormatException ignored) {}
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar: " + e.getMessage());
        }
    }
}
