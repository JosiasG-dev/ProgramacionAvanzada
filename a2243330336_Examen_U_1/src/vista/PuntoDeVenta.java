package vista;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class PuntoDeVenta extends JInternalFrame {

	private JTextField cajaCliente;
	private JTextField cajaNombreCliente;
	private JComboBox<String> listaProductos;
	private JTextField cajaCantidad;
	private JButton btnAgregar;
	private JButton btnModificar;
	private JButton btnQuitar;

	private JTable tabla;
	private DefaultTableModel modeloTabla;

	private JTextField cajaSubtotal;
	private JTextField cajaIVA;
	private JTextField cajaTotal;
	private JButton btnVaciar;
	private JButton btnCobrar;
	private JButton btnVerTicket;

	public PuntoDeVenta() {
		super("Punto de Venta", true, true, true, true);
		setSize(860, 540);
		setMinimumSize(new Dimension(700, 440));
		setLocation(30, 15);
		armar();
	}

	private void armar() {
		JPanel raiz = new JPanel(new BorderLayout(6, 6));
		raiz.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		raiz.add(panelCliente(), BorderLayout.NORTH);
		raiz.add(panelCarrito(), BorderLayout.CENTER);
		raiz.add(panelCobro(), BorderLayout.SOUTH);
		setContentPane(raiz);
	}

	private JPanel panelCliente() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(new TitledBorder("Datos de la Venta"));

		GridBagConstraints g = new GridBagConstraints();
		g.insets = new Insets(4, 6, 4, 6);
		g.fill = GridBagConstraints.HORIZONTAL;
		g.anchor = GridBagConstraints.WEST;

		cajaCliente = new JTextField(8);
		cajaNombreCliente = new JTextField(20);
		listaProductos = new JComboBox<>();
		listaProductos.setPreferredSize(new Dimension(300, 24));
		cajaCantidad = new JTextField(6);
		btnAgregar = new JButton("Agregar");
		btnModificar = new JButton("Modificar");
		btnQuitar = new JButton("Quitar");

		g.gridx = 0;
		g.gridy = 0;
		g.weightx = 0;
		panel.add(new JLabel("ID Cliente:"), g);
		g.gridx = 1;
		g.weightx = 0.15;
		panel.add(cajaCliente, g);
		g.gridx = 2;
		g.weightx = 0;
		panel.add(new JLabel("Nombre:"), g);
		g.gridx = 3;
		g.weightx = 0.5;
		panel.add(cajaNombreCliente, g);
		g.gridx = 4;
		g.weightx = 0;
		panel.add(new JLabel("Cajero: Sistema"), g);

		g.gridx = 0;
		g.gridy = 1;
		g.weightx = 0;
		panel.add(new JLabel("Producto:"), g);
		g.gridx = 1;
		g.weightx = 0.5;
		g.gridwidth = 2;
		panel.add(listaProductos, g);
		g.gridwidth = 1;
		g.gridx = 3;
		g.weightx = 0;
		panel.add(new JLabel("Cantidad:"), g);
		g.gridx = 4;
		g.weightx = 0.2;
		panel.add(cajaCantidad, g);

		JPanel bots = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
		bots.add(btnAgregar);
		bots.add(btnModificar);
		bots.add(btnQuitar);
		g.gridx = 0;
		g.gridy = 2;
		g.gridwidth = 5;
		g.weightx = 1;
		panel.add(bots, g);

		return panel;
	}

	private JPanel panelCarrito() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new TitledBorder("Carrito"));

		String[] cols = { "Codigo", "Descripcion", "Cantidad", "Precio Unit.", "Total" };
		modeloTabla = new DefaultTableModel(cols, 0) {
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};
		tabla = new JTable(modeloTabla);
		tabla.setRowHeight(22);
		tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tabla.getTableHeader().setReorderingAllowed(false);
		tabla.setFillsViewportHeight(true);
		int[] anchos = { 70, 290, 70, 110, 100 };
		for (int i = 0; i < anchos.length; i++)
			tabla.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);

		panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
		return panel;
	}

	private JPanel panelCobro() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		GridBagConstraints g = new GridBagConstraints();
		g.insets = new Insets(3, 6, 3, 6);
		g.fill = GridBagConstraints.HORIZONTAL;
		g.anchor = GridBagConstraints.WEST;

		cajaSubtotal = resumen();
		cajaIVA = resumen();
		cajaTotal = resumen();

		btnVaciar = new JButton("Vaciar");
		btnCobrar = new JButton("Cobrar");
		btnVerTicket = new JButton("Ver Ticket");

		g.gridx = 0;
		g.gridy = 0;
		g.weightx = 0;
		panel.add(new JLabel("Subtotal:"), g);
		g.gridx = 1;
		g.weightx = 0.2;
		panel.add(cajaSubtotal, g);
		g.gridx = 0;
		g.gridy = 1;
		g.weightx = 0;
		panel.add(new JLabel("IVA (16%):"), g);
		g.gridx = 1;
		g.weightx = 0.2;
		panel.add(cajaIVA, g);
		g.gridx = 0;
		g.gridy = 2;
		g.weightx = 0;
		panel.add(new JLabel("Total:"), g);
		g.gridx = 1;
		g.weightx = 0.2;
		panel.add(cajaTotal, g);

		JPanel bots = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
		bots.add(btnVaciar);
		bots.add(btnCobrar);
		bots.add(btnVerTicket);
		g.gridx = 2;
		g.gridy = 0;
		g.gridheight = 3;
		g.weightx = 0.8;
		panel.add(bots, g);

		return panel;
	}

	private JTextField resumen() {
		JTextField tf = new JTextField(12);
		tf.setEditable(false);
		tf.setHorizontalAlignment(JTextField.RIGHT);
		return tf;
	}

	public JTextField getCajaCliente() {
		return cajaCliente;
	}

	public JTextField getCajaNombreCliente() {
		return cajaNombreCliente;
	}

	public JComboBox<String> getListaProductos() {
		return listaProductos;
	}

	public JTextField getCajaCantidad() {
		return cajaCantidad;
	}

	public DefaultTableModel getModeloTabla() {
		return modeloTabla;
	}

	public JTable getTabla() {
		return tabla;
	}

	public void setSubtotal(String v) {
		cajaSubtotal.setText(v);
	}

	public void setIVA(String v) {
		cajaIVA.setText(v);
	}

	public void setTotal(String v) {
		cajaTotal.setText(v);
	}

	public String getTotal() {
		return cajaTotal.getText();
	}

	public void vaciar() {
		modeloTabla.setRowCount(0);
		cajaSubtotal.setText("");
		cajaIVA.setText("");
		cajaTotal.setText("");
		cajaCantidad.setText("");
	}

	public void addBtnAgregar(ActionListener al) {
		btnAgregar.addActionListener(al);
	}

	public void addBtnModificar(ActionListener al) {
		btnModificar.addActionListener(al);
	}

	public void addBtnQuitar(ActionListener al) {
		btnQuitar.addActionListener(al);
	}

	public void addBtnVaciar(ActionListener al) {
		btnVaciar.addActionListener(al);
	}

	public void addBtnCobrar(ActionListener al) {
		btnCobrar.addActionListener(al);
	}

	public void addBtnVerTicket(ActionListener al) {
		btnVerTicket.addActionListener(al);
	}
}
