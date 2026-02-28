package Partefinal2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * Actividad 5 - Sistema con soporte de archivos TXT o XML
 * El administrador puede configurar qué tipo de base de datos usar.
 */
public class Practica05XML extends JFrame {

    private JDesktopPane desktopPane;
    private JMenu menuConfiguracion;
    private JMenu menuOperacion;
    private String tipoBaseDatos = "TXT"; // "TXT" o "XML"

    public Practica05XML() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Sistema de Gestión (TXT/XML)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 580);
        setLocationRelativeTo(null);
        setLayout(new CardLayout());

        desktopPane = new JDesktopPane();
        desktopPane.setBackground(new Color(180, 200, 220));
        add(desktopPane);

        JMenuBar menuBar = new JMenuBar();

        menuOperacion = new JMenu("Operacion");
        menuConfiguracion = new JMenu("Configuracion");
        JMenu menuSalir = new JMenu("Salir");

        JMenuItem miCategorias = new JMenuItem("Categorias");
        JMenuItem miInsumos = new JMenuItem("Insumos");
        JMenuItem miObras = new JMenuItem("Obras");
        JMenuItem miTipoDB = new JMenuItem("Tipo de Base de Datos");
        JMenuItem miSalir = new JMenuItem("Salir");

        menuConfiguracion.add(miCategorias);
        menuConfiguracion.add(miInsumos);
        menuConfiguracion.add(miObras);
        menuConfiguracion.addSeparator();
        menuConfiguracion.add(miTipoDB);
        menuSalir.add(miSalir);

        menuBar.add(menuOperacion);
        menuBar.add(menuConfiguracion);
        menuBar.add(menuSalir);
        setJMenuBar(menuBar);

        miCategorias.addActionListener(e -> abrirCategorias());
        miInsumos.addActionListener(e -> abrirInsumos());
        miObras.addActionListener(e -> abrirObras());
        miTipoDB.addActionListener(e -> configurarTipoDB());
        miSalir.addActionListener(e -> System.exit(0));
    }

    private void configurarTipoDB() {
        String[] opciones = {"TXT", "XML"};
        String seleccion = (String) JOptionPane.showInputDialog(
            this, "Seleccione el tipo de base de datos:",
            "Configuración", JOptionPane.QUESTION_MESSAGE,
            null, opciones, tipoBaseDatos
        );
        if (seleccion != null) {
            tipoBaseDatos = seleccion;
            JOptionPane.showMessageDialog(this, "Tipo de base de datos configurado: " + tipoBaseDatos);
        }
    }

    private void abrirCategorias() {
        GenericInternalFrame frame = new GenericInternalFrame("Categorías", "categorias", tipoBaseDatos,
            new String[]{"ID", "Categoría"});
        desktopPane.add(frame);
        desktopPane.setSelectedFrame(frame);
        frame.setLocation(20, 20);
        desactivarMenu();
        frame.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent e) { activarMenu(); }
        });
    }

    private void abrirInsumos() {
        GenericInternalFrame frame = new GenericInternalFrame("Insumos", "insumos", tipoBaseDatos,
            new String[]{"ID", "Nombre", "Precio"});
        desktopPane.add(frame);
        desktopPane.setSelectedFrame(frame);
        frame.setLocation(30, 30);
        desactivarMenu();
        frame.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent e) { activarMenu(); }
        });
    }

    private void abrirObras() {
        GenericInternalFrame frame = new GenericInternalFrame("Obras", "obras", tipoBaseDatos,
            new String[]{"ID", "Nombre", "Descripción"});
        desktopPane.add(frame);
        desktopPane.setSelectedFrame(frame);
        frame.setLocation(40, 40);
        desactivarMenu();
        frame.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent e) { activarMenu(); }
        });
    }

    private void desactivarMenu() {
        menuConfiguracion.setEnabled(false);
        menuOperacion.setEnabled(false);
    }

    private void activarMenu() {
        if (desktopPane.getAllFrames().length == 0) {
            menuConfiguracion.setEnabled(true);
            menuOperacion.setEnabled(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Practica05XML().setVisible(true));
    }

    // =====================================================================
    // Clase interna genérica para los JInternalFrames de cada entidad
    // =====================================================================
    static class GenericInternalFrame extends JInternalFrame {
        private DefaultTableModel modeloTabla;
        private String entidad;
        private String tipoDB;
        private String[] columnas;
        private int contadorId = 1;
        private JTextField[] campos;

        public GenericInternalFrame(String titulo, String entidad, String tipoDB, String[] columnas) {
            super(titulo, true, true, true, true);
            this.entidad = entidad;
            this.tipoDB = tipoDB;
            this.columnas = columnas;
            initComponents();
            cargarDatos();
            setSize(500, 400);
            setVisible(true);
        }

        private void initComponents() {
            setLayout(new BorderLayout());
            JPanel panelForm = new JPanel(new GridBagLayout());
            panelForm.setBorder(BorderFactory.createEmptyBorder(8, 8, 5, 8));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(3, 3, 3, 3);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Mostrar campos para columnas excepto ID (autoincremental, oculto)
            campos = new JTextField[columnas.length];
            for (int i = 1; i < columnas.length; i++) {
                gbc.gridx = 0; gbc.gridy = i - 1; gbc.weightx = 0;
                panelForm.add(new JLabel(columnas[i] + ":"), gbc);
                campos[i] = new JTextField(15);
                gbc.gridx = 1; gbc.gridy = i - 1; gbc.weightx = 1;
                panelForm.add(campos[i], gbc);
            }

            JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
            JButton btnAgregar = new JButton("Agregar");
            JButton btnEliminar = new JButton("Eliminar");
            JButton btnSalir = new JButton("Salir");
            panelBotones.add(btnAgregar);
            panelBotones.add(btnEliminar);
            panelBotones.add(btnSalir);
            gbc.gridx = 0; gbc.gridy = columnas.length; gbc.gridwidth = 2;
            panelForm.add(panelBotones, gbc);

            modeloTabla = new DefaultTableModel(columnas, 0) {
                @Override
                public boolean isCellEditable(int row, int col) { return false; }
            };
            JTable tabla = new JTable(modeloTabla);
            // Ocultar columna ID
            tabla.getColumnModel().getColumn(0).setMinWidth(0);
            tabla.getColumnModel().getColumn(0).setMaxWidth(0);
            tabla.getColumnModel().getColumn(0).setWidth(0);

            tabla.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int fila = tabla.getSelectedRow();
                    if (fila >= 0) {
                        for (int i = 1; i < columnas.length; i++)
                            campos[i].setText(modeloTabla.getValueAt(fila, i).toString());
                    }
                }
            });

            JScrollPane sp = new JScrollPane(tabla);
            add(panelForm, BorderLayout.NORTH);
            add(sp, BorderLayout.CENTER);

            btnAgregar.addActionListener(e -> agregar());
            btnEliminar.addActionListener(e -> eliminar(tabla));
            btnSalir.addActionListener(e -> { try { setClosed(true); } catch (Exception ex) { ex.printStackTrace(); } });
        }

        private void agregar() {
            for (int i = 1; i < columnas.length; i++) {
                if (campos[i].getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Complete todos los campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            String id = String.valueOf(contadorId++);
            Object[] fila = new Object[columnas.length];
            fila[0] = id;
            for (int i = 1; i < columnas.length; i++) fila[i] = campos[i].getText().trim();
            modeloTabla.addRow(fila);
            for (int i = 1; i < columnas.length; i++) campos[i].setText("");
            guardarDatos();
        }

        private void eliminar(JTable tabla) {
            int fila = tabla.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un registro.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int conf = JOptionPane.showConfirmDialog(this, "¿Eliminar registro seleccionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                modeloTabla.removeRow(fila);
                for (int i = 1; i < columnas.length; i++) campos[i].setText("");
                guardarDatos();
            }
        }

        private String getArchivoTXT() { return entidad + ".txt"; }
        private String getArchivoXML() { return entidad + ".xml"; }

        private void guardarDatos() {
            if ("XML".equals(tipoDB)) guardarXML();
            else guardarTXT();
        }

        private void cargarDatos() {
            if ("XML".equals(tipoDB)) cargarXML();
            else cargarTXT();
        }

        private void guardarTXT() {
            try (PrintWriter pw = new PrintWriter(new FileWriter(getArchivoTXT()))) {
                for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                    StringBuilder sb = new StringBuilder();
                    for (int j = 0; j < columnas.length; j++) {
                        if (j > 0) sb.append("|");
                        sb.append(modeloTabla.getValueAt(i, j).toString().replace("\n", "\\n"));
                    }
                    pw.println(sb.toString());
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al guardar TXT: " + e.getMessage());
            }
        }

        private void cargarTXT() {
            File f = new File(getArchivoTXT());
            if (!f.exists()) return;
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] p = linea.split("\\|", columnas.length);
                    if (p.length == columnas.length) {
                        for (int i = 0; i < p.length; i++) p[i] = p[i].replace("\\n", "\n");
                        modeloTabla.addRow(p);
                        try { contadorId = Math.max(contadorId, Integer.parseInt(p[0]) + 1); } catch (NumberFormatException ignored) {}
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar TXT: " + e.getMessage());
            }
        }

        private void guardarXML() {
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.newDocument();
                Element root = doc.createElement(entidad);
                doc.appendChild(root);
                for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                    Element registro = doc.createElement("registro");
                    for (int j = 0; j < columnas.length; j++) {
                        Element campo = doc.createElement(columnas[j].replace(" ", "_").replace("ó", "o").replace("ó", "o"));
                        campo.setTextContent(modeloTabla.getValueAt(i, j).toString());
                        registro.appendChild(campo);
                    }
                    root.appendChild(registro);
                }
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer t = tf.newTransformer();
                t.setOutputProperty(OutputKeys.INDENT, "yes");
                t.transform(new DOMSource(doc), new StreamResult(new File(getArchivoXML())));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al guardar XML: " + e.getMessage());
            }
        }

        private void cargarXML() {
            File f = new File(getArchivoXML());
            if (!f.exists()) return;
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(f);
                NodeList registros = doc.getElementsByTagName("registro");
                for (int i = 0; i < registros.getLength(); i++) {
                    Node nodo = registros.item(i);
                    if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                        Element el = (Element) nodo;
                        Object[] fila = new Object[columnas.length];
                        for (int j = 0; j < columnas.length; j++) {
                            String tagName = columnas[j].replace(" ", "_").replace("ó", "o").replace("ó", "o");
                            NodeList nl = el.getElementsByTagName(tagName);
                            fila[j] = nl.getLength() > 0 ? nl.item(0).getTextContent() : "";
                        }
                        modeloTabla.addRow(fila);
                        try { contadorId = Math.max(contadorId, Integer.parseInt(fila[0].toString()) + 1); } catch (NumberFormatException ignored) {}
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al cargar XML: " + e.getMessage());
            }
        }
    }
}
