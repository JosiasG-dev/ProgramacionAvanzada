package parte2.Vista;import parte2.Vista.controlador.*;
import parte2.modelo.Categoria;
import parte2.modelo.Insumo;
import parte2.modelo.ListaCategorias;
import parte2.modelo.ListaInsumos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Practica03_d extends JFrame implements ActionListener{
	 private ListaInsumos listainsumo;
	    private ListaCategorias listacategorias;
	    private Archivotxt archivoCategorias;
	    private Archivotxt archivoInsumos;

	    private JComboBox<Categoria> ComboCategoria;
	    private JTextField Tid, Tinsumo;
	    private JButton Bagregar, Beliminar, Bsalir;
	    private JTextArea areaProductos;
	    private JPanel panelFormulario;

	    public Practica03_d() {
	        super("Administración de Productos con Archivo");

	        archivoCategorias = new Archivotxt("categorias.txt");
	        archivoInsumos = new Archivotxt("insumos.txt");
	        listacategorias = new ListaCategorias();
	        cargarCategoriasDesdeArchivo();
	        if (listacategorias.CategoriasArreglo().length == 0) {
	            inicializarCategoriasPorDefecto();
	        }

	        // Cargar insumos
	        listainsumo = new ListaInsumos();
	        cargarInsumosDesdeArchivo();

	        setBounds(0, 0, 390, 370);
	        panelFormulario = new JPanel();
	        panelFormulario.setLayout(null);
	        getContentPane().add(panelFormulario, BorderLayout.CENTER);

	        JLabel labelCategoria = new JLabel("Categoria:");
	        labelCategoria.setBounds(10, 66, 71, 20);
	        ComboCategoria = new JComboBox(listacategorias.CategoriasArreglo());
	        ComboCategoria.setEnabled(true);
	        ComboCategoria.setBounds(91, 66, 160, 20);
	        ComboCategoria.addActionListener(this);
	        panelFormulario.add(labelCategoria);
	        panelFormulario.add(ComboCategoria);

	        JLabel labelId = new JLabel("ID:");
	        labelId.setBounds(10, 9, 71, 20);
	        Tid = new JTextField();
	        Tid.setEditable(false);
	        Tid.setBounds(91, 9, 147, 20);
	        panelFormulario.add(labelId);
	        panelFormulario.add(Tid);

	        JLabel labelInsumo = new JLabel("Insumo:");
	        labelInsumo.setBounds(10, 34, 71, 20);
	        Tinsumo = new JTextField();
	        Tinsumo.setEditable(false);
	        Tinsumo.setBounds(91, 35, 147, 20);
	        panelFormulario.add(labelInsumo);
	        panelFormulario.add(Tinsumo);

	        Bagregar = new JButton("Agregar");
	        Bagregar.setBounds(20, 104, 111, 20);
	        Bagregar.addActionListener(this);
	        panelFormulario.add(Bagregar);

	        Beliminar = new JButton("Eliminar");
	        Beliminar.setBounds(153, 104, 111, 20);
	        Beliminar.addActionListener(this);
	        panelFormulario.add(Beliminar);

	        Bsalir = new JButton("Salir");
	        Bsalir.setBounds(274, 104, 79, 20);
	        Bsalir.addActionListener(this);
	        panelFormulario.add(Bsalir);

	        JScrollPane scrollPane = new JScrollPane();
	        scrollPane.setBounds(10, 132, 357, 179);
	        panelFormulario.add(scrollPane);
	        areaProductos = new JTextArea();
	        scrollPane.setViewportView(areaProductos);
	        areaProductos.setEditable(false);

	        actualizarArea();
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setVisible(true);
	    }

	    private void inicializarCategoriasPorDefecto() {
	        listacategorias.agregarCategoria(new Categoria("01", "Materiales"));
	        listacategorias.agregarCategoria(new Categoria("02", "Mano de Obra"));
	        listacategorias.agregarCategoria(new Categoria("03", "Maquinaria y Equipo"));
	        listacategorias.agregarCategoria(new Categoria("04", "Servicios"));
	    }

	    private void cargarCategoriasDesdeArchivo() {
	        ArrayList<String> lineas = archivoCategorias.cargar();
	        ArrayList<String[]> datos = new ArrayList<>();
	        for (String linea : lineas) {
	            String[] partes = linea.split(",");
	            if (partes.length >= 2) {
	                datos.add(partes);
	            }
	        }
	        listacategorias.cargarCategorias(datos);
	    }

	    private void cargarInsumosDesdeArchivo() {
	        ArrayList<String> lineas = archivoInsumos.cargar();
	        ArrayList<String[]> datos = new ArrayList<>();
	        for (String linea : lineas) {
	            String[] partes = linea.split(",");
	            if (partes.length >= 3) {
	                datos.add(partes);
	            }
	        }
	        listainsumo.cargarInsumos(datos);
	    }

	    private void guardarInsumosEnArchivo() {
	        archivoInsumos.guardar(listainsumo.toString());
	    }

	    private void actualizarArea() {
	        areaProductos.setText(listainsumo.toString().replace(",", "\t"));
	    }

	    private void volverAlInicio() {
	        Bagregar.setText("Agregar");
	        Bsalir.setText("Salir");
	        Beliminar.setEnabled(true);
	        Tid.setEditable(false);
	        Tinsumo.setEditable(false);
	        ComboCategoria.setEditable(false);
	        Tid.setText("");
	        Tinsumo.setText("");
	        ComboCategoria.setSelectedIndex(0);
	    }

	    private boolean datosCompletos() {
	        return !Tid.getText().trim().isEmpty() &&
	               !Tinsumo.getText().trim().isEmpty() &&
	               ComboCategoria.getSelectedItem() != null;
	    }

	    public void Altas() {
	        if (Bagregar.getText().equals("Agregar")) {
	            ComboCategoria.setSelectedIndex(0); // línea agregada
	            Bagregar.setText("Salvar");
	            Bsalir.setText("Cancelar");
	            Beliminar.setEnabled(false);
	            Tid.setEditable(true);
	            Tinsumo.setEditable(true);
	            ComboCategoria.setEditable(true);
	            ComboCategoria.setFocusable(true);
	        } else {
	            if (datosCompletos()) {
	                String id = Tid.getText().trim();
	                String insumo = Tinsumo.getText().trim();
	                String idCategoria = ((Categoria) ComboCategoria.getSelectedItem()).getIdcategoria();
	                Insumo nuevo = new Insumo(id, insumo, idCategoria);
	                if (listainsumo.agregarInsumo(nuevo)) {
	                    guardarInsumosEnArchivo();
	                    actualizarArea();
	                    volverAlInicio();
	                } else {
	                    JOptionPane.showMessageDialog(this,
	                        "El ID " + id + " ya existe. Tiene asignado: " +
	                        listainsumo.buscarInsumo(id));
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
	                "Seleccione el ID a eliminar",
	                "Eliminar Insumo",
	                JOptionPane.PLAIN_MESSAGE,
	                null,
	                ids,
	                ids[0]);
	        if (seleccion != null && !seleccion.isEmpty()) {
	            if (listainsumo.eliminarInsumoPorId(seleccion)) {
	                guardarInsumosEnArchivo();
	                actualizarArea();
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
	        SwingUtilities.invokeLater(Practica03_d::new);
	    }
}
