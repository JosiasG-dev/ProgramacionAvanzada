package parte2.Vista;
import javax.swing.*;

import parte2.modelo.Categoria;
import parte2.modelo.Insumo;
import parte2.modelo.ListaCategorias;
import parte2.modelo.ListaInsumos;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class practica03_a extends JFrame implements ActionListener {
	private ListaInsumos listainsumo;
    private ListaCategorias listacategorias;

    private JComboBox<Categoria> ComboCategoria;
    private JTextField Tid, Tinsumo;
    private JButton Bagregar, Beliminar, Bsalir;
    private JTextArea areaProductos;
    private JPanel panelFormulario;

    public practica03_a() {
        super("Administración de Productos");
        inicializarCategorias();
        this.listainsumo = new ListaInsumos();

        setBounds(0, 0, 390, 370);
        panelFormulario = new JPanel();
        panelFormulario.setLayout(null);
        getContentPane().add(panelFormulario, BorderLayout.CENTER);

        JLabel labelCategoria = new JLabel("Categoria:");
        labelCategoria.setBounds(10, 66, 71, 20);
        ComboCategoria = new JComboBox(this.listacategorias.CategoriasArreglo());
        ComboCategoria.setEditable(false);
        ComboCategoria.setBounds(91, 66, 160, 20);
        ComboCategoria.addActionListener(this);
        panelFormulario.add(labelCategoria);
        panelFormulario.add(ComboCategoria);

        JLabel labelId = new JLabel("ID:");
        labelId.setBounds(10, 9, 71, 20);
        this.Tid = new JTextField(10);
        this.Tid.setEditable(false);
        this.Tid.setBounds(91, 9, 147, 20);
        panelFormulario.add(labelId);
        panelFormulario.add(Tid);

        JLabel labelInsumo = new JLabel("Insumo:");
        labelInsumo.setBounds(10, 34, 71, 20);
        this.Tinsumo = new JTextField(20);
        this.Tinsumo.setEditable(false);
        this.Tinsumo.setBounds(91, 35, 147, 20);
        panelFormulario.add(labelInsumo);
        panelFormulario.add(Tinsumo);

        this.Bagregar = new JButton("Agregar");
        this.Bagregar.setBounds(20, 104, 111, 20);
        this.Bagregar.addActionListener(this);
        panelFormulario.add(Bagregar);

        this.Beliminar = new JButton("Eliminar");
        this.Beliminar.setBounds(153, 104, 111, 20);
        this.Beliminar.addActionListener(this);
        panelFormulario.add(Beliminar);

        this.Bsalir = new JButton("Salir");
        this.Bsalir.setBounds(274, 104, 79, 20);
        this.Bsalir.addActionListener(this);
        panelFormulario.add(Bsalir);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 132, 357, 179);
        panelFormulario.add(scrollPane);
        this.areaProductos = new JTextArea(10, 40);
        scrollPane.setViewportView(areaProductos);
        this.areaProductos.setEditable(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    public void inicializarCategorias() {
        this.listacategorias = new ListaCategorias();
        Categoria nodo1 = new Categoria("01", "Materiales");
        Categoria nodo2 = new Categoria("02", "Mano de Obra");
        Categoria nodo3 = new Categoria("03", "Maquinaria y Equipo");
        Categoria nodo4 = new Categoria("04", "Servicios");
        this.listacategorias.agregarCategoria(nodo1);
        this.listacategorias.agregarCategoria(nodo2);
        this.listacategorias.agregarCategoria(nodo3);
        this.listacategorias.agregarCategoria(nodo4);
    }
    public void VolveralInicio() {
        this.Bagregar.setText("Agregar");
        this.Bsalir.setText("Salir");
        this.Beliminar.setEnabled(true);
        this.Tid.setEditable(false);
        this.Tinsumo.setEditable(false);
        this.ComboCategoria.setEditable(false);
        this.Tid.setText("");
        this.Tinsumo.setText("");
        this.ComboCategoria.setSelectedIndex(0);
    }

    private boolean esdatoscompletos() {
        return !Tid.getText().trim().isEmpty() &&
               !Tinsumo.getText().trim().isEmpty() &&
               ComboCategoria.getSelectedItem() != null;
    }

    public void Altas() {
        if (this.Bagregar.getText().compareTo("Agregar") == 0) {
            this.Bagregar.setText("Salvar");
            this.Bsalir.setText("Cancelar");
            this.Beliminar.setEnabled(false);
            this.Tid.setEditable(true);
            this.Tinsumo.setEditable(true);
            this.ComboCategoria.setEditable(true);
            this.ComboCategoria.setFocusable(true);
        } else {
            if (esdatoscompletos()) {
                String id = this.Tid.getText().trim();
                String insumo = this.Tinsumo.getText().trim();
                String idcategoria = ((Categoria) this.ComboCategoria.getSelectedItem()).getIdcategoria().trim();
                Insumo nodo = new Insumo(id, insumo, idcategoria);
                if (!this.listainsumo.agregarInsumo(nodo)) {
                    JOptionPane.showMessageDialog(this,
                            "Lo siento, el id " + id + " ya existe. Tiene asignado: " +
                            this.listainsumo.buscarInsumo(id));
                } else {
                    this.VolveralInicio();
                    this.areaProductos.setText(this.listainsumo.toString());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Debe completar todos los campos");
            }
        }
    }

    public void Eliminar() {
        String[] ids = this.listainsumo.idinsumos();
        if (ids.length == 0) {
            JOptionPane.showMessageDialog(this, "No hay insumos para eliminar");
            return;
        }
        String idSeleccionado = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el ID a eliminar",
                "Eliminación de Insumos",
                JOptionPane.PLAIN_MESSAGE,
                null,
                ids,
                ids[0]);
        if (idSeleccionado != null && !idSeleccionado.isEmpty()) {
            if (this.listainsumo.eliminarInsumoPorId(idSeleccionado)) {
                this.areaProductos.setText(this.listainsumo.toString());
                JOptionPane.showMessageDialog(this, "Insumo eliminado");
            } else {
                JOptionPane.showMessageDialog(this, "No existe ese id");
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.Bagregar) {
            this.Altas();
        } else if (e.getSource() == this.Beliminar) {
            this.Eliminar();
        } else if (e.getSource() == Bsalir) {
            if (this.Bsalir.getText().compareTo("Cancelar") == 0) {
                this.VolveralInicio();
            } else {
                this.dispose();
            }
        }
    }

    public static void main(String[] args) {
        new practica03_a();
    }
}
