package Parte2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Practica03_c extends JInternalFrame {

    private JTextField Tid;
    private JTextField Tcategoria;
    private JButton Bagregar;
    private JButton Beliminar;
    private JButton Bsalir;
    private JTable Tcategorias;
    private DefaultTableModel modeloTabla;
    private static final String ARCHIVO = "categorias.txt";

    public Practica03_c() {
        super("Categorías", true, true, true, true);
        initComponents();
        cargarDesdeArchivo();
        setSize(480, 380);
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
        panelFormulario.add(new JLabel("ID:"), gbc);
        Tid = new JTextField(15);
        Tid.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        panelFormulario.add(Tid, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Categoría:"), gbc);
        Tcategoria = new JTextField(15);
        Tcategoria.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1;
        panelFormulario.add(Tcategoria, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        Bagregar = new JButton("Agregar");
        Beliminar = new JButton("Eliminar");
        Bsalir = new JButton("Salir");
        panelBotones.add(Bagregar);
        panelBotones.add(Beliminar);
        panelBotones.add(Bsalir);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panelFormulario.add(panelBotones, gbc);

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Categoría"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        Tcategorias = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(Tcategorias);

        add(panelFormulario, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        Bagregar.addActionListener(e -> agregarCategoria());
        Beliminar.addActionListener(e -> eliminarCategoria());
        Bsalir.addActionListener(e -> cerrar());

        Tcategorias.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = Tcategorias.getSelectedRow();
                if (fila >= 0) {
                    Tid.setText(modeloTabla.getValueAt(fila, 0).toString());
                    Tcategoria.setText(modeloTabla.getValueAt(fila, 1).toString());
                }
            }
        });
    }

    private void agregarCategoria() {
        String id = JOptionPane.showInputDialog(this, "Ingrese el ID:");
        if (id == null || id.trim().isEmpty()) return;
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            if (modeloTabla.getValueAt(i, 0).toString().equals(id.trim())) {
                JOptionPane.showMessageDialog(this, "El ID ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        String cat = JOptionPane.showInputDialog(this, "Ingrese la Categoría:");
        if (cat == null || cat.trim().isEmpty()) return;
        modeloTabla.addRow(new Object[]{id.trim(), cat.trim()});
        guardarEnArchivo();
    }

    private void eliminarCategoria() {
        int fila = Tcategorias.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una categoría.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int conf = JOptionPane.showConfirmDialog(this, "¿Eliminar categoría seleccionada?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            modeloTabla.removeRow(fila);
            Tid.setText(""); Tcategoria.setText("");
            guardarEnArchivo();
        }
    }

    private void cerrar() {
        try { setClosed(true); } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void guardarEnArchivo() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO))) {
            for (int i = 0; i < modeloTabla.getRowCount(); i++)
                pw.println(modeloTabla.getValueAt(i, 0) + "," + modeloTabla.getValueAt(i, 1));
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
                String[] p = linea.split(",", 2);
                if (p.length == 2) modeloTabla.addRow(new Object[]{p[0], p[1]});
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar: " + e.getMessage());
        }
    }
}
