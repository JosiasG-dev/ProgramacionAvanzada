package Parte2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class InsumoFrame extends JInternalFrame {

    private JTextField Tid;
    private JTextField Tnombre;
    private JTextField Tprecio;
    private JTable Tinsumos;
    private DefaultTableModel modeloTabla;
    private static final String ARCHIVO = "insumos.txt";
    private int contadorId = 1;

    public InsumoFrame() {
        super("Insumos", true, true, true, true);
        initComponents();
        cargarDesdeArchivo();
        setSize(500, 400);
        setVisible(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("ID:"), gbc);
        Tid = new JTextField(10);
        Tid.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        panelFormulario.add(Tid, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Nombre:"), gbc);
        Tnombre = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1;
        panelFormulario.add(Tnombre, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Precio:"), gbc);
        Tprecio = new JTextField(10);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1;
        panelFormulario.add(Tprecio, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton Bagregar = new JButton("Agregar");
        JButton Beliminar = new JButton("Eliminar");
        JButton Bsalir = new JButton("Salir");
        panelBotones.add(Bagregar);
        panelBotones.add(Beliminar);
        panelBotones.add(Bsalir);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panelFormulario.add(panelBotones, gbc);

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Precio"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        Tinsumos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(Tinsumos);

        add(panelFormulario, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        Bagregar.addActionListener(e -> agregarInsumo());
        Beliminar.addActionListener(e -> eliminarInsumo());
        Bsalir.addActionListener(e -> cerrar());

        Tinsumos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = Tinsumos.getSelectedRow();
                if (fila >= 0) {
                    Tid.setText(modeloTabla.getValueAt(fila, 0).toString());
                    Tnombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                    Tprecio.setText(modeloTabla.getValueAt(fila, 2).toString());
                }
            }
        });
    }

    private void agregarInsumo() {
        String nombre = Tnombre.getText().trim();
        String precio = Tprecio.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre del insumo.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String id = String.valueOf(contadorId++);
        modeloTabla.addRow(new Object[]{id, nombre, precio});
        Tnombre.setText(""); Tprecio.setText("");
        guardarEnArchivo();
    }

    private void eliminarInsumo() {
        int fila = Tinsumos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un insumo.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int conf = JOptionPane.showConfirmDialog(this, "¿Eliminar insumo seleccionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            modeloTabla.removeRow(fila);
            Tid.setText(""); Tnombre.setText(""); Tprecio.setText("");
            guardarEnArchivo();
        }
    }

    private void cerrar() {
        try { setClosed(true); } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void guardarEnArchivo() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO))) {
            for (int i = 0; i < modeloTabla.getRowCount(); i++)
                pw.println(modeloTabla.getValueAt(i, 0) + "|" + modeloTabla.getValueAt(i, 1) + "|" + modeloTabla.getValueAt(i, 2));
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
                    modeloTabla.addRow(new Object[]{p[0], p[1], p.length == 3 ? p[2] : ""});
                    try { contadorId = Math.max(contadorId, Integer.parseInt(p[0]) + 1); } catch (NumberFormatException ignored) {}
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar: " + e.getMessage());
        }
    }
}
