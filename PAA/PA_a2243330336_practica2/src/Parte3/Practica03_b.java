package Parte3;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class Practica03_b extends JFrame {

    private JTextField Tid;
    private JTextField Tcategoria;
    private JButton Bagregar;
    private JButton Beliminar;
    private JButton Bsalir;
    private JTable Tcategorias;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollPane;
    private static final String ARCHIVO = "categorias.txt";

    public Practica03_b() {
        initComponents();
        cargarDesdeArchivo();
    }

    private void initComponents() {
        setTitle("Categorías");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel labelId = new JLabel("ID:");
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panelFormulario.add(labelId, gbc);

        Tid = new JTextField(15);
        Tid.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1;
        panelFormulario.add(Tid, gbc);

        JLabel labelCategoria = new JLabel("Categoría:");
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelFormulario.add(labelCategoria, gbc);

        Tcategoria = new JTextField(15);
        Tcategoria.setEditable(false);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1;
        panelFormulario.add(Tcategoria, gbc);

        // Panel botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        Bagregar = new JButton("Agregar");
        Beliminar = new JButton("Eliminar");
        Bsalir = new JButton("Salir");
        panelBotones.add(Bagregar);
        panelBotones.add(Beliminar);
        panelBotones.add(Bsalir);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panelFormulario.add(panelBotones, gbc);

        // Tabla
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Categoría"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        Tcategorias = new JTable(modeloTabla);
        scrollPane = new JScrollPane(Tcategorias);

        add(panelFormulario, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Eventos
        Bagregar.addActionListener(e -> agregarCategoria());
        Beliminar.addActionListener(e -> eliminarCategoria());
        Bsalir.addActionListener(e -> System.exit(0));

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

        // Verificar si el ID ya existe
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            if (modeloTabla.getValueAt(i, 0).toString().equals(id.trim())) {
                JOptionPane.showMessageDialog(this, "El ID ya existe. No se puede agregar.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String categoria = JOptionPane.showInputDialog(this, "Ingrese la Categoría:");
        if (categoria == null || categoria.trim().isEmpty()) return;

        modeloTabla.addRow(new Object[]{id.trim(), categoria.trim()});
        guardarEnArchivo();
    }

    private void eliminarCategoria() {
        int fila = Tcategorias.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una categoría para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "¿Desea eliminar la categoría seleccionada?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            modeloTabla.removeRow(fila);
            Tid.setText("");
            Tcategoria.setText("");
            guardarEnArchivo();
        }
    }

    private void guardarEnArchivo() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO))) {
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                pw.println(modeloTabla.getValueAt(i, 0) + "," + modeloTabla.getValueAt(i, 1));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDesdeArchivo() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",", 2);
                if (partes.length == 2) {
                    modeloTabla.addRow(new Object[]{partes[0], partes[1]});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Practica03_b().setVisible(true));
    }
}
