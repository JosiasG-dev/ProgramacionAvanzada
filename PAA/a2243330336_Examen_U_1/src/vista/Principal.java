package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Principal extends JFrame {

	private JDesktopPane escritorio;

	private JMenuItem menuInventario;
	private JMenuItem menuPuntoDeVenta;
	private JMenuItem menuProductos;
	private JMenuItem menuSalir;

	public Principal() {
		setTitle("Sistema de Presupuestos");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1024, 700);
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(800, 520));
		construirMenu();
		construirEscritorio();
	}

	private void construirMenu() {
		JMenuBar barra = new JMenuBar();

		JMenu mInventario = new JMenu("Inventario");
		JMenu mPuntoVenta = new JMenu("Punto de Venta");
		JMenu mProductos = new JMenu("Productos");
		JMenu mSalida = new JMenu("Salida");

		menuInventario = new JMenuItem("Ver Inventario");
		menuPuntoDeVenta = new JMenuItem("Abrir Caja");
		menuProductos = new JMenuItem("Gestion de Productos");
		menuSalir = new JMenuItem("Salir");

		mInventario.add(menuInventario);
		mPuntoVenta.add(menuPuntoDeVenta);
		mProductos.add(menuProductos);
		mSalida.add(menuSalir);

		barra.add(mInventario);
		barra.add(mPuntoVenta);
		barra.add(mProductos);
		barra.add(mSalida);

		setJMenuBar(barra);
	}

	private void construirEscritorio() {
		escritorio = new JDesktopPane();
		escritorio.setBackground(new Color(220, 220, 220));
		escritorio.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
		add(escritorio, BorderLayout.CENTER);
	}

	public JDesktopPane getEscritorio() {
		return escritorio;
	}

	public void addMenuInventarioListener(ActionListener al) {
		menuInventario.addActionListener(al);
	}

	public void addMenuPuntoDeVentaListener(ActionListener al) {
		menuPuntoDeVenta.addActionListener(al);
	}

	public void addMenuProductosListener(ActionListener al) {
		menuProductos.addActionListener(al);
	}

	public void addMenuSalirListener(ActionListener al) {
		menuSalir.addActionListener(al);
	}
}
