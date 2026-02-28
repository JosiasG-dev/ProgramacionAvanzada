package Vista;

import Libreria.Librerias;
import Modelo.Categoria;
import Modelo.ListaCategorias;
import Modelo.ListaInsumos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class ProductoVista extends JFrame {

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
    private Librerias libreria; 
    public ProductoVista(ListaCategorias categoriasModel, ListaInsumos insumosModel, Librerias libreria) {
        this.listacategorias = categoriasModel;
        this.listainsumo = insumosModel;
        this.libreria = libreria;

        setTitle("Administración de Productos (MVC)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panelCentro = new JPanel(new BorderLayout());
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.CENTER));

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

        panelNorte.add(panel1);
        panelNorte.add(panel2);
        add(panelNorte, BorderLayout.NORTH);

        modeloinsumos = listainsumo.getmodelo(listacategorias);
        TareaProductos = new JTable(modeloinsumos);
        TareaProductos.setRowSelectionAllowed(true);
        JScrollPane scrollTable = new JScrollPane(TareaProductos);
        scrollTable.setPreferredSize(new Dimension(500, 150));
        panelCentro.add(scrollTable, BorderLayout.CENTER);
        add(panelCentro, BorderLayout.CENTER);

        Bagregar = new JButton("Agregar");
        Beliminar = new JButton("Eliminar");
        Bsalir = new JButton("Salir");
        panelSur.add(Bagregar);
        panelSur.add(Beliminar);
        panelSur.add(Bsalir);
        add(panelSur, BorderLayout.SOUTH);

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
    public void actualizarImagen() {
    }
}
