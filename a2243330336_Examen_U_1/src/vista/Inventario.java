package vista;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class Inventario extends JInternalFrame {

    private JTextField        cajaBuscarId;
    private JTextField        cajaBuscarNombre;
	private JComboBox<String> listaTipo;
	private JRadioButton opcionTodos;
	private JRadioButton opcionDisponible;
	private JRadioButton opcionAgotado;
	private ButtonGroup grupoEstado;
	private JButton btnBuscar;
	private JButton btnLimpiar;

	private JTable tabla;
	private DefaultTableModel modeloTabla;

	private JButton btnCrear;
	private JButton btnModificar;
	private JButton btnEliminar;

	public Inventario() {
		super("Inventario", true, true, true, true);
		setSize(860, 490);
		setMinimumSize(new Dimension(700, 390));
		setLocation(10, 10);
		armar();
	}

	private void armar() {
		JPanel raiz = new JPanel(new BorderLayout(8, 8));
		raiz.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		raiz.add(filtros(), BorderLayout.WEST);
		raiz.add(lista(), BorderLayout.CENTER);
		raiz.add(acciones(), BorderLayout.SOUTH);
		setContentPane(raiz);
	}

	private JPanel filtros() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(new TitledBorder("Filtros"));
		panel.setPreferredSize(new Dimension(200, 0));

		GridBagConstraints g = gbc();

		cajaBuscarId = new JTextField(12);
		cajaBuscarNombre = new JTextField(12);
		listaTipo = new JComboBox<>(
				new String[] { "Todos", "Granos", "Liquidos", "Lacteos", "Enlatados", "Condimentos" });

		opcionTodos = new JRadioButton("Todos");
		opcionDisponible = new JRadioButton("Disponible");
		opcionAgotado = new JRadioButton("Agotado");
		opcionDisponible.setSelected(true);
		grupoEstado = new ButtonGroup();
		grupoEstado.add(opcionTodos);
		grupoEstado.add(opcionDisponible);
		grupoEstado.add(opcionAgotado);

		btnBuscar = new JButton("Buscar");
		btnLimpiar = new JButton("Limpiar");

		JPanel estado = new JPanel(new GridLayout(3, 1, 0, 2));
		estado.setBorder(new TitledBorder("Estado"));
		estado.add(opcionTodos);
		estado.add(opcionDisponible);
		estado.add(opcionAgotado);

		g.gridy = 0;
		panel.add(new JLabel("ID:"), g);
		g.gridy = 1;
		panel.add(cajaBuscarId, g);
		g.gridy = 2;
		panel.add(new JLabel("Nombre:"), g);
		g.gridy = 3;
		panel.add(cajaBuscarNombre, g);
		g.gridy = 4;
		panel.add(new JLabel("Tipo:"), g);
		g.gridy = 5;
		panel.add(listaTipo, g);
		g.gridy = 6;
		panel.add(estado, g);

		JPanel bots = new JPanel(new GridLayout(1, 2, 4, 0));
		bots.add(btnBuscar);
		bots.add(btnLimpiar);
		g.gridy = 7;
		panel.add(bots, g);

		g.gridy = 8;
		g.weighty = 1.0;
		panel.add(Box.createVerticalGlue(), g);

		return panel;
	}

	private JPanel lista() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new TitledBorder("Productos en Inventario"));

		String[] cols = { "ID", "Nombre", "Tipo", "Cantidad", "Precio", "Estado" };
		modeloTabla = new DefaultTableModel(cols, 0) {
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};
		tabla = nuevaTabla(modeloTabla, new int[] { 55, 230, 110, 75, 80, 90 });
		panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
		return panel;
	}

	private JPanel acciones() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 6));
		panel.setBorder(new TitledBorder("Acciones"));
		btnCrear = new JButton("Crear Nuevo");
		btnModificar = new JButton("Modificar");
		btnEliminar = new JButton("Eliminar");
		panel.add(btnCrear);
		panel.add(btnModificar);
		panel.add(btnEliminar);
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

	public JTextField getCajaBuscarId() {
		return cajaBuscarId;
	}

	public JTextField getCajaBuscarNombre() {
		return cajaBuscarNombre;
	}

	public JComboBox<String> getListaTipo() {
		return listaTipo;
	}

	public DefaultTableModel getModeloTabla() {
		return modeloTabla;
	}

	public JTable getTabla() {
		return tabla;
	}

	public String getEstado() {
		if (opcionTodos.isSelected())
			return "Todos";
		if (opcionDisponible.isSelected())
			return "Disponible";
		return "Agotado";
	}

	public void limpiarFiltros() {
		cajaBuscarId.setText("");
		cajaBuscarNombre.setText("");
		listaTipo.setSelectedIndex(0);
		opcionDisponible.setSelected(true);
	}

	public void addBtnBuscar(ActionListener al) {
		btnBuscar.addActionListener(al);
	}

	public void addBtnLimpiar(ActionListener al) {
		btnLimpiar.addActionListener(al);
	}

	public void addBtnCrear(ActionListener al) {
		btnCrear.addActionListener(al);
	}

	public void addBtnModificar(ActionListener al) {
		btnModificar.addActionListener(al);
	}

	public void addBtnEliminar(ActionListener al) {
		btnEliminar.addActionListener(al);
	}
}
