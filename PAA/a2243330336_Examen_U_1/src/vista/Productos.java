package vista;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class Productos extends JInternalFrame {

	private JTextField cajaCodigo;
	private JTextField cajaNombre;
	private JComboBox<String> listaCategoria;
	private JTextField cajaPrecioCompra;
	private JTextField cajaPrecioVenta;
	private JTextField cajaStock;
	private JTextField cajaStockMinimo;
	private JRadioButton opcionActivo;
	private JRadioButton opcionDesactivado;
	private ButtonGroup grupoEstado;

	private JButton btnGuardar;
	private JButton btnConsultar;
	private JButton btnLimpiar;
	private JButton btnEliminar;

	private JTable tabla;
	private DefaultTableModel modeloTabla;
	private JComboBox<String> listaBuscarPor;
	private JTextField cajaBusqueda;
	private JButton btnBuscar;
	private JButton btnTodos;
	private JButton btnExportar;

	public Productos() {
		super("Productos", true, true, true, true);
		setSize(980, 580);
		setMinimumSize(new Dimension(820, 460));
		setLocation(20, 10);
		armar();
	}

	private void armar() {
		JPanel raiz = new JPanel(new BorderLayout(8, 8));
		raiz.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		raiz.add(formulario(), BorderLayout.WEST);
		raiz.add(catalogo(), BorderLayout.CENTER);
		setContentPane(raiz);
	}

	private JPanel formulario() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(new TitledBorder("Datos del Producto"));
		panel.setPreferredSize(new Dimension(290, 0));

		GridBagConstraints g = gbc();

		cajaCodigo = new JTextField(8);
		cajaCodigo.setEnabled(false);
		cajaCodigo.setBackground(UIManager.getColor("TextField.inactiveBackground"));
		cajaNombre = new JTextField(18);
		listaCategoria = new JComboBox<>(new String[] { "Granos", "Liquidos", "Lacteos", "Enlatados", "Condimentos" });
		cajaPrecioCompra = new JTextField(10);
		cajaPrecioVenta = new JTextField(10);
		cajaStock = new JTextField(8);
		cajaStockMinimo = new JTextField(8);
		opcionActivo = new JRadioButton("Activo");
		opcionDesactivado = new JRadioButton("Desactivado");
		opcionActivo.setSelected(true);
		grupoEstado = new ButtonGroup();
		grupoEstado.add(opcionActivo);
		grupoEstado.add(opcionDesactivado);

		btnGuardar = new JButton("Guardar");
		btnConsultar = new JButton("Consultar");
		btnEliminar = new JButton("Eliminar");
		btnLimpiar = new JButton("Limpiar");

		int f = 0;
		par(panel, g, "Codigo:", cajaCodigo, f++);
		uno(panel, g, new JLabel("Nombre:"), f++);
		uno(panel, g, cajaNombre, f++);
		uno(panel, g, new JLabel("Categoria:"), f++);
		uno(panel, g, listaCategoria, f++);
		par(panel, g, "Precio Compra:", cajaPrecioCompra, f++);
		par(panel, g, "Precio Venta:", cajaPrecioVenta, f++);
		par(panel, g, "Stock:", cajaStock, f++);
		par(panel, g, "Stock Minimo:", cajaStockMinimo, f++);

		JPanel estado = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 2));
		estado.setBorder(new TitledBorder("Estado"));
		estado.add(opcionActivo);
		estado.add(opcionDesactivado);
		uno(panel, g, estado, f++);

		JPanel bots = new JPanel(new GridLayout(2, 2, 4, 4));
		bots.add(btnGuardar);
		bots.add(btnConsultar);
		bots.add(btnEliminar);
		bots.add(btnLimpiar);
		g.gridx = 0;
		g.gridy = f;
		g.gridwidth = 2;
		g.weighty = 0.1;
		panel.add(bots, g);

		return panel;
	}

	private JPanel catalogo() {
		JPanel panel = new JPanel(new BorderLayout(0, 6));
		panel.setBorder(new TitledBorder("Catalogo de Productos"));

		String[] cols = { "N", "Codigo", "Nombre", "Categoria", "Stock", "Precio", "Estado" };
		modeloTabla = new DefaultTableModel(cols, 0) {
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};
		tabla = nuevaTabla(modeloTabla, new int[] { 35, 65, 200, 100, 55, 80, 70 });
		panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
		panel.add(filtroCatalogo(), BorderLayout.SOUTH);
		return panel;
	}

	private JPanel filtroCatalogo() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
		panel.setBorder(new TitledBorder("Buscar"));
		listaBuscarPor = new JComboBox<>(new String[] { "Nombre", "Categoria", "Estado" });
		cajaBusqueda = new JTextField(16);
		btnBuscar = new JButton("Buscar");
		btnTodos = new JButton("Ver Todos");
		btnExportar = new JButton("Exportar");
		panel.add(new JLabel("Por:"));
		panel.add(listaBuscarPor);
		panel.add(cajaBusqueda);
		panel.add(btnBuscar);
		panel.add(btnTodos);
		panel.add(btnExportar);
		return panel;
	}

	private GridBagConstraints gbc() {
		GridBagConstraints g = new GridBagConstraints();
		g.insets = new Insets(3, 5, 3, 5);
		g.fill = GridBagConstraints.HORIZONTAL;
		g.anchor = GridBagConstraints.WEST;
		g.weightx = 1.0;
		g.gridx = 0;
		return g;
	}

	private void par(JPanel p, GridBagConstraints g, String lbl, JComponent c, int f) {
		g.gridx = 0;
		g.gridy = f;
		g.weightx = 0;
		g.gridwidth = 1;
		p.add(new JLabel(lbl), g);
		g.gridx = 1;
		g.weightx = 1.0;
		p.add(c, g);
		g.weightx = 0;
	}

	private void uno(JPanel p, GridBagConstraints g, JComponent c, int f) {
		g.gridx = 0;
		g.gridy = f;
		g.gridwidth = 2;
		g.weightx = 1.0;
		p.add(c, g);
		g.gridwidth = 1;
		g.weightx = 0;
	}

	private JTable nuevaTabla(DefaultTableModel m, int[] anchos) {
		JTable t = new JTable(m);
		t.setRowHeight(22);
		t.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		t.getTableHeader().setReorderingAllowed(false);
		t.setFillsViewportHeight(true);
		for (int i = 0; i < anchos.length; i++)
			t.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
		return t;
	}

	public JTextField getCajaCodigo() {
		return cajaCodigo;
	}

	public JTextField getCajaNombre() {
		return cajaNombre;
	}

	public JComboBox<String> getListaCategoria() {
		return listaCategoria;
	}

	public JTextField getCajaPrecioVenta() {
		return cajaPrecioVenta;
	}

	public JTextField getCajaStock() {
		return cajaStock;
	}

	public JComboBox<String> getListaBuscarPor() {
		return listaBuscarPor;
	}

	public JTextField getCajaBusqueda() {
		return cajaBusqueda;
	}

	public DefaultTableModel getModeloTabla() {
		return modeloTabla;
	}

	public JTable getTabla() {
		return tabla;
	}

	public String getEstado() {
		return opcionActivo.isSelected() ? "Activo" : "Desactivado";
	}

	public void setEstado(String e) {
		if ("Activo".equals(e))
			opcionActivo.setSelected(true);
		else
			opcionDesactivado.setSelected(true);
	}

	public void limpiar() {
		cajaCodigo.setText("");
		cajaNombre.setText("");
		listaCategoria.setSelectedIndex(0);
		cajaPrecioCompra.setText("");
		cajaPrecioVenta.setText("");
		cajaStock.setText("");
		cajaStockMinimo.setText("");
		opcionActivo.setSelected(true);
		cajaCodigo.requestFocusInWindow();
	}

	public void llenar(String codigo, String nombre, String precio, String stock, String cat) {
		cajaCodigo.setText(codigo);
		cajaNombre.setText(nombre);
		cajaPrecioVenta.setText(precio);
		cajaStock.setText(stock);
		listaCategoria.setSelectedItem(cat);
	}

	public void addBtnGuardar(ActionListener al) {
		btnGuardar.addActionListener(al);
	}

	public void addBtnConsultar(ActionListener al) {
		btnConsultar.addActionListener(al);
	}

	public void addBtnEliminar(ActionListener al) {
		btnEliminar.addActionListener(al);
	}

	public void addBtnLimpiar(ActionListener al) {
		btnLimpiar.addActionListener(al);
	}

	public void addBtnBuscar(ActionListener al) {
		btnBuscar.addActionListener(al);
	}

	public void addBtnTodos(ActionListener al) {
		btnTodos.addActionListener(al);
	}

	public void addBtnExportar(ActionListener al) {
		btnExportar.addActionListener(al);
	}
}
