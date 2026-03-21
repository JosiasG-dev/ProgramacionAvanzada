package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Ventana principal (JFrame) del sistema.
 * MDI con JDesktopPane para las ventanas internas.
 */
public class Principal extends JFrame {

    private JDesktopPane escritorio;

    private JMenuItem menuInventario;
    private JMenuItem menuProductos;
    private JMenuItem menuPuntoDeVenta;
    private JMenuItem menuReporteExcel;
    private JMenuItem menuSalir;

    public Principal() {
        setTitle("Supermercado – Sistema de Gestión v2.0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 720);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 560));
        construirMenu();
        construirEscritorio();
    }

    private void construirMenu() {
        JMenuBar barra = new JMenuBar();
        barra.setBackground(new Color(31, 78, 121));

        JMenu mCatalogo  = estilizarMenu(new JMenu("📦 Catálogo"));
        JMenu mVenta     = estilizarMenu(new JMenu("🛒 Punto de Venta"));
        JMenu mReportes  = estilizarMenu(new JMenu("📊 Reportes"));
        JMenu mSistema   = estilizarMenu(new JMenu("⚙ Sistema"));

        menuInventario   = new JMenuItem("Ver Inventario");
        menuProductos    = new JMenuItem("Gestión de Productos");
        menuPuntoDeVenta = new JMenuItem("Abrir Caja");
        menuReporteExcel = new JMenuItem("Generar Reportes Excel");
        menuSalir        = new JMenuItem("Salir");

        mCatalogo.add(menuInventario);
        mCatalogo.add(menuProductos);
        mVenta.add(menuPuntoDeVenta);
        mReportes.add(menuReporteExcel);
        mSistema.add(menuSalir);

        barra.add(mCatalogo);
        barra.add(mVenta);
        barra.add(mReportes);
        barra.add(Box.createHorizontalGlue());
        barra.add(mSistema);

        setJMenuBar(barra);
    }

    private JMenu estilizarMenu(JMenu m) {
        m.setForeground(Color.WHITE);
        m.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return m;
    }

    private void construirEscritorio() {
        escritorio = new JDesktopPane();
        escritorio.setBackground(new Color(236, 240, 241));
        escritorio.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

        // Panel de bienvenida
        JLabel bienvenida = new JLabel(
            "<html><center><br><br>"
            + "<font size='6' color='#1F4E79'><b>🏪 Supermercado</b></font><br><br>"
            + "<font size='4' color='#555'>Sistema de Gestión de Productos</font><br><br>"
            + "<font size='3' color='#888'>Use el menú superior para comenzar</font>"
            + "</center></html>", SwingConstants.CENTER);
        bienvenida.setBounds(200, 100, 600, 300);
        escritorio.add(bienvenida);

        add(escritorio, BorderLayout.CENTER);

        // Barra de estado
        JLabel status = new JLabel("  ✔ Sistema listo  |  11 categorías  |  63 productos iniciales");
        status.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        status.setForeground(new Color(100, 100, 100));
        status.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(3, 10, 3, 10)));
        add(status, BorderLayout.SOUTH);
    }

    public JDesktopPane getEscritorio() { return escritorio; }

    public void addMenuInventarioListener(ActionListener al)   { menuInventario.addActionListener(al); }
    public void addMenuProductosListener(ActionListener al)    { menuProductos.addActionListener(al); }
    public void addMenuPuntoDeVentaListener(ActionListener al) { menuPuntoDeVenta.addActionListener(al); }
    public void addMenuReporteExcelListener(ActionListener al) { menuReporteExcel.addActionListener(al); }
    public void addMenuSalirListener(ActionListener al)        { menuSalir.addActionListener(al); }
}
