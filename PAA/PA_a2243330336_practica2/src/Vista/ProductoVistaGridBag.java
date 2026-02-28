package Vista;

import Libreria.Librerias;
import Modelo.Categoria;
import Modelo.ListaCategorias;
import Modelo.ListaInsumos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class ProductoVistaGridBag extends JFrame {

    private JTextField Tid;
    private JTextField Tinsumo;
    private JList<Categoria> ListaCategoria;
    private JTable TareaProductos;
    private JButton Bagregar;
    private JButton Beliminar;
    private JButton Bsalir;

    private DefaultListModel<Categoria> modelocategoria;
    private DefaultTableModel modeloinsumos;

    private ListaCategorias listacategorias;
    private ListaInsumos listainsumo;

    public ProductoVistaGridBag(ListaCategorias listaCat, ListaInsumos listaIns) {
        this.listacategorias = listaCat;
        this.listainsumo = listaIns;

        setTitle("Administración de Productos (GridBagLayout)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());

        JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panel3 = new JPanel(new BorderLayout());
        JPanel panel4 = new JPanel(new FlowLayout(FlowLayout.CENTER));

        panel1.add(new JLabel("ID:"));
        Tid = new JTextField(10);
        Tid.setEditable(false);
        panel1.add(Tid);
        panel1.add(new JLabel("Insumo:"));
        Tinsumo = new JTextField(15);
        Tinsumo.setEditable(false);
        panel1.add(Tinsumo);

        panel2.add(new JLabel("Categoria:"));
        modelocategoria = listacategorias.generarModeloCategorias();
        ListaCategoria = new JList<>(modelocategoria);
        ListaCategoria.setEnabled(true);
        JScrollPane scrollList = new JScrollPane(ListaCategoria);
        scrollList.setPreferredSize(new Dimension(150, 60));
        panel2.add(scrollList);

        modeloinsumos = listainsumo.getmodelo(listacategorias);
        TareaProductos = new JTable(modeloinsumos);
        TareaProductos.setRowSelectionAllowed(true);
        JScrollPane scrollTable = new JScrollPane(TareaProductos);
        scrollTable.setPreferredSize(new Dimension(500, 150));
        panel3.add(scrollTable, BorderLayout.CENTER);

        Bagregar = new JButton("Agregar");
        Beliminar = new JButton("Eliminar");
        Bsalir = new JButton("Salir");
        panel4.add(Bagregar);
        panel4.add(Beliminar);
        panel4.add(Bsalir);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0; 
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.weighty = 0.0;
        contentPane.add(panel1, gbc);

        gbc.gridy = 1;
        contentPane.add(panel2, gbc);

        gbc.gridy = 2;
        gbc.gridheight = 3;
        gbc.weighty = 1.0; 
        contentPane.add(panel3, gbc);

        gbc.gridy = 5;
        gbc.gridheight = 1;
        gbc.weighty = 0.0;
        contentPane.add(panel4, gbc);

        panel1.setPreferredSize(new Dimension(panel1.getPreferredSize().width, 58));
        panel2.setPreferredSize(new Dimension(panel2.getPreferredSize().width, 58));
        panel4.setPreferredSize(new Dimension(panel4.getPreferredSize().width, 58));

        pack();
        setLocationRelativeTo(null);
    }

    public String getIdText() {
        return Tid.getText().trim();
    }

    public String getInsumoText() {
        return Tinsumo.getText().trim();
    }

    public Categoria getCategoriaSeleccionada() {
        int index = ListaCategoria.getSelectedIndex();
        if (index >= 0) {
            return modelocategoria.get(index);
        }
        return null;
    }

    public int getSelectedRow() {
        return TareaProductos.getSelectedRow();
    }

    public String getSelectedId() {
        int row = TareaProductos.getSelectedRow();
        if (row >= 0) {
            return TareaProductos.getValueAt(row, 0).toString();
        }
        return null;
    }

    public void actualizarTabla() {
        modeloinsumos = listainsumo.getmodelo(listacategorias);
        TareaProductos.setModel(modeloinsumos);
        TareaProductos.getColumnModel().getColumn(0).setPreferredWidth(50);
        TareaProductos.getColumnModel().getColumn(1).setPreferredWidth(200);
        TareaProductos.getColumnModel().getColumn(2).setPreferredWidth(100);
    }

    public void habilitarEdicion(boolean habilitar) {
        Tid.setEditable(habilitar);
        Tinsumo.setEditable(habilitar);
        ListaCategoria.setEnabled(habilitar);
    }

    public void limpiarCampos() {
        Tid.setText("");
        Tinsumo.setText("");
        if (modelocategoria.getSize() > 0) {
            ListaCategoria.setSelectedIndex(0);
        }
    }

    public void cambiarTextoBotonAgregar(String texto) {
        Bagregar.setText(texto);
    }

    public void cambiarTextoBotonSalir(String texto) {
        Bsalir.setText(texto);
    }

    public void habilitarBotonEliminar(boolean habilitar) {
        Beliminar.setEnabled(habilitar);
    }

    public String getTextoBotonAgregar() {
        return Bagregar.getText();
    }

    public String getTextoBotonSalir() {
        return Bsalir.getText();
    }

    public void addAgregarListener(ActionListener listener) {
        Bagregar.addActionListener(listener);
    }

    public void addEliminarListener(ActionListener listener) {
        Beliminar.addActionListener(listener);
    }

    public void addSalirListener(ActionListener listener) {
        Bsalir.addActionListener(listener);
    }

    public void addTablaSelectionListener(javax.swing.event.ListSelectionListener listener) {
        TareaProductos.getSelectionModel().addListSelectionListener(listener);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public JFrame getFrame() {
        return this;
    }

}
