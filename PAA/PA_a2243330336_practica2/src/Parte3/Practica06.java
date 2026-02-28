package Parte3;

import Libreria.Archivotxt;
import Libreria.Librerias;
import Modelo.Categoria;
import Modelo.Insumo;
import Modelo.ListaCategorias;
import Modelo.ListaInsumos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Practica06 extends JFrame implements ActionListener {

    private ListaInsumos listainsumo;
    private ListaCategorias listacategorias;
    private Archivotxt archivocategorias;
    private Archivotxt archivoinsumos;
    private Librerias libreria;

    private JList<Categoria> ListaCategoria;
    private JTextField Tid, Tinsumo;
    private JButton Bagregar, Beliminar, Bsalir;
    private JTable TareaProductos;
    private DefaultListModel<Categoria> modelocategoria;
    private DefaultTableModel modeloinsumos;

    private JPanel panel1, panel2, panel3, panel4;

    public Practica06() {
        super("Administración de Productos (FlowLayout)");
        inicializarDatos();

        setLayout(new FlowLayout());

        panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel4 = new JPanel(new FlowLayout(FlowLayout.CENTER));

        panel1.add(new JLabel("ID:"));
        Tid = new JTextField(10);
        Tid.setEditable(false);
        panel1.add(Tid);
        panel1.add(new JLabel("Insumo:"));
        Tinsumo = new JTextField(15);
        Tinsumo.setEditable(false);
        panel1.add(Tinsumo);

        panel2.add(new JLabel("Categoria:"));
        ListaCategoria = new JList<>();
        ListaCategoria.setModel(modelocategoria);
        ListaCategoria.setEnabled(true);
        JScrollPane scrollList = new JScrollPane(ListaCategoria);
        scrollList.setPreferredSize(new Dimension(150, 60));
        panel2.add(scrollList);

        TareaProductos = new JTable();
        TareaProductos.setRowSelectionAllowed(true);
        TareaProductos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) actualizarimagen();
        });
        JScrollPane scrollTable = new JScrollPane(TareaProductos);
        scrollTable.setPreferredSize(new Dimension(500, 150));
        panel3.add(scrollTable);

        Bagregar = new JButton("Agregar");
        Bagregar.addActionListener(this);
        panel4.add(Bagregar);

        Beliminar = new JButton("Eliminar");
        Beliminar.addActionListener(this);
        panel4.add(Beliminar);

        Bsalir = new JButton("Salir");
        Bsalir.addActionListener(this);
        panel4.add(Bsalir);

        add(panel1);
        add(panel2);
        add(panel3);
        add(panel4);

        actualizartabla();
        actualizarimagen();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void inicializarDatos() {
        archivocategorias = new Archivotxt("categorias.txt");
        archivoinsumos = new Archivotxt("insumos.txt");
        libreria = new Librerias();

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

    private void actualizartabla() {
        TareaProductos.setModel(modeloinsumos);
        TareaProductos.getColumnModel().getColumn(0).setPreferredWidth(50);
        TareaProductos.getColumnModel().getColumn(1).setPreferredWidth(200);
        TareaProductos.getColumnModel().getColumn(2).setPreferredWidth(100);
    }

    private void actualizarimagen() {
        int fila = TareaProductos.getSelectedRow();
        if (fila < 0) return;
        String id = TareaProductos.getValueAt(fila, 0).toString();
        String ruta = System.getProperty("user.dir") + "/Imagenes/" + id + ".png";
        File f = new File(ruta);
       
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
        } else {
            if (datosCompletos()) {
                String id = Tid.getText().trim();
                String insumo = Tinsumo.getText().trim();
                String idcategoria = modelocategoria.get(ListaCategoria.getSelectedIndex()).getIdcategoria();
                Insumo nodo = new Insumo(id, insumo, idcategoria);
                if (listainsumo.agregarInsumo(nodo)) {
                    archivoinsumos.guardar(listainsumo.toArchivo());
                    modeloinsumos = listainsumo.getmodelo(listacategorias);
                    actualizartabla();
                    volverAlInicio();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "El id " + id + " ya existe. Tiene asignado: " + listainsumo.buscarInsumo(id));
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
                actualizartabla();
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
        SwingUtilities.invokeLater(Practica06::new);
    }
}
