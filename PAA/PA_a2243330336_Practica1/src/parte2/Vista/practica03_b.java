package parte2.Vista;
import javax.swing.*;

import parte2.modelo.Categoria;
import parte2.modelo.ListaCategorias;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class practica03_b extends JFrame implements ActionListener{
	 private ListaCategorias listaCategorias;

	    private JTextField Tid;           
	    private JTextField Tcategoria;   
	    private JTextArea Tareacategoria;  
	    private JButton Bagregar, Beliminar, Bsalir;
	    private JPanel panelFormulario;

	    public practica03_b() {
	        super("Administración de Categorías");
	        inicializarDatosEjemplo();  
	        configurarVentana();
	        configurarComponentes();
	        actualizarArea();          
	        setVisible(true);
	    }

	    private void inicializarDatosEjemplo() {
	        listaCategorias = new ListaCategorias();
	        listaCategorias.agregarCategoria(new Categoria("01", "Materiales"));
	        listaCategorias.agregarCategoria(new Categoria("02", "Mano de Obra"));
	        listaCategorias.agregarCategoria(new Categoria("03", "Maquinaria y Equipo"));
	    }
	    private void configurarVentana() {
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setBounds(100, 100, 400, 330);
	        panelFormulario = new JPanel();
	        panelFormulario.setLayout(null);
	        getContentPane().add(panelFormulario, BorderLayout.CENTER);
	    }
	    private void configurarComponentes() {
	        JLabel labelId = new JLabel("ID:");
	        labelId.setBounds(10, 10, 80, 25);
	        panelFormulario.add(labelId);

	        Tid = new JTextField();
	        Tid.setBounds(100, 10, 150, 25);
	        Tid.setEditable(false);        
	        panelFormulario.add(Tid);
	        JLabel labelCategoria = new JLabel("Categoría:");
	        labelCategoria.setBounds(10, 40, 80, 25);
	        panelFormulario.add(labelCategoria);

	        Tcategoria = new JTextField();
	        Tcategoria.setBounds(100, 40, 150, 25);
	        Tcategoria.setEditable(false);  
	        panelFormulario.add(Tcategoria);
	        Bagregar = new JButton("Agregar");
	        Bagregar.setBounds(10, 80, 90, 25);
	        Bagregar.addActionListener(this);
	        panelFormulario.add(Bagregar);

	        Beliminar = new JButton("Eliminar");
	        Beliminar.setBounds(110, 80, 90, 25);
	        Beliminar.addActionListener(this);
	        panelFormulario.add(Beliminar);

	        Bsalir = new JButton("Salir");
	        Bsalir.setBounds(210, 80, 90, 25);
	        Bsalir.addActionListener(this);
	        panelFormulario.add(Bsalir);

	        JScrollPane scrollPane = new JScrollPane();
	        scrollPane.setBounds(10, 120, 360, 150);
	        panelFormulario.add(scrollPane);

	        Tareacategoria = new JTextArea();
	        Tareacategoria.setEditable(false);  
	        scrollPane.setViewportView(Tareacategoria);
	    }
	    private void volverAlInicio() {
	        Bagregar.setText("Agregar");
	        Bsalir.setText("Salir");
	        Beliminar.setEnabled(true);
	        Tid.setEditable(false);
	        Tcategoria.setEditable(false);
	        Tid.setText("");
	        Tcategoria.setText("");
	        Tid.requestFocusInWindow();
	    }

	    private boolean datosCompletos() {
	        return !Tid.getText().trim().isEmpty() && !Tcategoria.getText().trim().isEmpty();
	    }
	    private void actualizarArea() {
	        Tareacategoria.setText(listaCategorias.toString());
	    }

	    private void altas() {
	        if (Bagregar.getText().equals("Agregar")) {
	            Bagregar.setText("Salvar");
	            Bsalir.setText("Cancelar");
	            Beliminar.setEnabled(false);
	            Tid.setEditable(true);
	            Tcategoria.setEditable(true);
	            Tid.setText("");
	            Tcategoria.setText("");
	            Tid.requestFocusInWindow();
	        } else {
	            if (datosCompletos()) {
	                String id = Tid.getText().trim();
	                String nombre = Tcategoria.getText().trim();

	                if (listaCategorias.buscarCategoria(id) == null) {
	                    listaCategorias.agregarCategoria(new Categoria(id, nombre));
	                    actualizarArea();
	                    volverAlInicio();
	                } else {
	                    JOptionPane.showMessageDialog(this, "El ID " + id + " ya existe.");
	                }
	            } else {
	                JOptionPane.showMessageDialog(this, "Debe completar todos los campos.");
	            }
	        }
	    }
	    private void eliminar() {
	        Object[] categorias = listaCategorias.CategoriasArreglo();
	        if (categorias.length == 0) {
	            JOptionPane.showMessageDialog(this, "No hay categorías para eliminar.");
	            return;
	        }

	        Categoria seleccionada = (Categoria) JOptionPane.showInputDialog(
	                this,
	                "Seleccione la categoría a eliminar:",
	                "Eliminar Categoría",
	                JOptionPane.PLAIN_MESSAGE,
	                null,
	                categorias,
	                categorias[0]);

	        if (seleccionada != null) {
	            listaCategorias.eliminarCategoriaPorId(seleccionada.getIdcategoria());
	            actualizarArea();
	        }
	    }
	    public void actionPerformed(ActionEvent e) {
	        if (e.getSource() == Bagregar) {
	            altas();
	        } else if (e.getSource() == Beliminar) {
	            eliminar();
	        } else if (e.getSource() == Bsalir) {
	            if (Bsalir.getText().equals("Cancelar")) {
	                volverAlInicio();  
	            } else {
	                dispose();         
	            }
	        }
	    }

	    public static void main(String[] args) {
	        SwingUtilities.invokeLater(() -> new practica03_b());
	    }

}
