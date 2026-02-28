package parte1;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import Libreria.Archivotxt;
import Modelo.Categoria;
import Modelo.Insumo;
import Modelo.ListaCategorias;
import Modelo.ListaInsumos;

public class Practica02_a extends JFrame implements ActionListener {

    private ListaInsumos listainsumo;
    private ListaCategorias listacategorias;

    private Archivotxt archivocategorias;
    private Archivotxt archivoinsumos;

    private JList<Categoria> ListaCategoria;
    private JTextField Tid, Tinsumo;
    private JButton Bagregar, Beliminar, Bsalir;
    private JPanel panelFormulario;
    private JTable TareaProductos;

    private DefaultListModel<Categoria> modelocategoria;
    private DefaultTableModel modeloinsumos;

    public Practica02_a() {
        super("Administración de Productos");
        inicializarDatos();

        setBounds(0, 0, 390, 400);
        panelFormulario = new JPanel();
        panelFormulario.setLayout(null);
        getContentPane().add(panelFormulario, BorderLayout.CENTER);

        JLabel labelId = new JLabel("ID:");
        labelId.setBounds(10, 9, 71, 20);
        panelFormulario.add(labelId);

        Tid = new JTextField();
        Tid.setBounds(91, 9, 147, 20);
        Tid.setEditable(false);
        panelFormulario.add(Tid);

        JLabel labelInsumo = new JLabel("Insumo:");
        labelInsumo.setBounds(10, 34, 71, 20);
        panelFormulario.add(labelInsumo);

        Tinsumo = new JTextField();
        Tinsumo.setBounds(91, 35, 147, 20);
        Tinsumo.setEditable(false);
        panelFormulario.add(Tinsumo);

        JLabel labelCategoria = new JLabel("Categoria:");
        labelCategoria.setBounds(10, 61, 71, 20);
        panelFormulario.add(labelCategoria);

        JScrollPane scrollPane_jlist = new JScrollPane();
        scrollPane_jlist.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane_jlist.setBounds(91, 61, 157, 42);
        panelFormulario.add(scrollPane_jlist);

        ListaCategoria = new JList<>();
        scrollPane_jlist.setViewportView(ListaCategoria);
        ListaCategoria.setModel(this.modelocategoria);
        ListaCategoria.setEnabled(true);

        Bagregar = new JButton("Agregar");
        Bagregar.setBounds(20, 110, 111, 20);
        Bagregar.addActionListener(this);
        panelFormulario.add(Bagregar);

        Beliminar = new JButton("Eliminar");
        Beliminar.setBounds(153, 110, 111, 20);
        Beliminar.addActionListener(this);
        panelFormulario.add(Beliminar);

        Bsalir = new JButton("Salir");
        Bsalir.setBounds(274, 110, 79, 20);
        Bsalir.addActionListener(this);
        panelFormulario.add(Bsalir);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 140, 357, 179);
        panelFormulario.add(scrollPane);

        TareaProductos = new JTable();
        scrollPane.setViewportView(TareaProductos);

        actualizarTabla();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void inicializarDatos() {
        archivocategorias = new Archivotxt("categorias.txt");
        archivoinsumos = new Archivotxt("insumos.txt");

        listacategorias = new ListaCategorias();
        listainsumo = new ListaInsumos();

        if (archivocategorias.existe()) {
            listacategorias.cargarCategorias(archivocategorias.cargar());
        } else {
            listacategorias.agregarCategoria(new Categoria("01", "Materiales"));
            listacategorias.agregarCategoria(new Categoria("02", "Mano de Obra"));
            listacategorias.agregarCategoria(new Categoria("03", "Maquinaria y Equipo"));
            listacategorias.agregarCategoria(new Categoria("04", "Servicios"));
        }

        if (archivoinsumos.existe()) {
            listainsumo.cargarInsumo(archivoinsumos.cargar());
        }

        modelocategoria = listacategorias.generarModeloCategorias();
        modeloinsumos = listainsumo.getmodelo(listacategorias);
    }

    private void actualizarTabla() {
        TareaProductos.setModel(modeloinsumos);
        TareaProductos.getColumnModel().getColumn(0).setPreferredWidth(5);
        TareaProductos.getColumnModel().getColumn(1).setPreferredWidth(150);
        TareaProductos.getColumnModel().getColumn(2).setPreferredWidth(35);
    }

    private void volverAlInicio() {
        Bagregar.setText("Agregar");
        Bsalir.setText("Salir");
        Beliminar.setEnabled(true);
        Tid.setEditable(false);
        Tinsumo.setEditable(false);
        ListaCategoria.setEnabled(false);
        Tid.setText("");
        Tinsumo.setText("");
        if (modelocategoria.getSize() > 0) {
            ListaCategoria.setSelectedIndex(0);
        }
    }

    private boolean datosCompletos() {
        String id = Tid.getText().trim();
        String insumo = Tinsumo.getText().trim();
        String idcategoria = "";
        if (ListaCategoria.getSelectedIndex() >= 0) {
            idcategoria = modelocategoria.get(ListaCategoria.getSelectedIndex()).getIdcategoria();
        }
        return !id.isEmpty() && !insumo.isEmpty() && !idcategoria.isEmpty();
    }

    public void Altas() {
        if (Bagregar.getText().equals("Agregar")) {
            if (modelocategoria.getSize() > 0) {
                ListaCategoria.setSelectedIndex(0);
            }
            Bagregar.setText("Salvar");
            Bsalir.setText("Cancelar");
            Beliminar.setEnabled(false);
            Tid.setEditable(true);
            Tinsumo.setEditable(true);
            ListaCategoria.setEnabled(true);
            ListaCategoria.setFocusable(true);
        } else {
            if (datosCompletos()) {
                String id = Tid.getText().trim();
                String insumo = Tinsumo.getText().trim();
                String idcategoria = modelocategoria.get(ListaCategoria.getSelectedIndex()).getIdcategoria();
                Insumo nodo = new Insumo(id, insumo, idcategoria);
                if (listainsumo.agregarInsumo(nodo)) {
                    archivoinsumos.guardar(listainsumo.toArchivo());
                    modeloinsumos = listainsumo.getmodelo(listacategorias);
                    actualizarTabla();
                    volverAlInicio();
                } else {
                    String mensaje = "Lo siento, el id " + id + " ya existe. Tiene asignado: "
                            + listainsumo.buscarInsumo(id);
                    JOptionPane.showMessageDialog(this, mensaje);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Debe completar todos los campos");
            }
        }
    }

    public void Eliminar() {
        String[] ids = listainsumo.idinsumos();
        if (ids.length == 0) {
            JOptionPane.showMessageDialog(this, "No hay insumos para eliminar");
            return;
        }
        String seleccion = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione el ID a eliminar:",
                "Eliminar Insumo",
                JOptionPane.PLAIN_MESSAGE,
                null,
                ids,
                ids[0]);
        if (seleccion != null && !seleccion.isEmpty()) {
            if (listainsumo.eliminarInsumoPorId(seleccion)) {
                archivoinsumos.guardar(listainsumo.toArchivo());
                modeloinsumos = listainsumo.getmodelo(listacategorias);
                actualizarTabla();
                JOptionPane.showMessageDialog(this, "Insumo eliminado");
            } else {
                JOptionPane.showMessageDialog(this, "No existe ese ID");
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Bagregar) {
            Altas();
        } else if (e.getSource() == Beliminar) {
            Eliminar();
        } else if (e.getSource() == Bsalir) {
            if (Bsalir.getText().equals("Cancelar")) {
                volverAlInicio();
            } else {
                dispose();
            }
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new Practica02_a());
    }
}
