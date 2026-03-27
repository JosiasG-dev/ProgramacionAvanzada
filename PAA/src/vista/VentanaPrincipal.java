package vista;

import controlador.*;
import javax.swing.*;
import java.awt.*;

/**
 * VISTA - Ventana Principal
 * Menú superior simple + Panel de contenido. Patrón MVC.
 */
public class VentanaPrincipal extends JFrame {

    private JPanel panelContenido;

    // Controladores (instanciados una sola vez)
    private final ProductoControlador   prodCtrl  = new ProductoControlador();
    private final InventarioControlador invCtrl   = new InventarioControlador();
    private final ProveedorControlador  provCtrl  = new ProveedorControlador();
    private final VentaControlador      ventaCtrl = new VentaControlador();
    private final UnidadControlador     unidCtrl  = new UnidadControlador();
    private final ReporteControlador    repCtrl   = new ReporteControlador();

    public VentanaPrincipal() {
        configurarVentana();
        construirUI();
        navegarA("dashboard");
    }

    private void configurarVentana() {
        setTitle("Sistema de Abarrotes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setMinimumSize(new Dimension(850, 550));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void construirUI() {
        // ---- Barra de menú ----
        JMenuBar menuBar = new JMenuBar();

        // Menú: Gestión de Presupuesto (Productos/Inventario)
        JMenu menuGestionPres = new JMenu("Gestión de Presupuesto");
        JMenuItem miProductos   = new JMenuItem("Productos");
        JMenuItem miInventario  = new JMenuItem("Inventario");
        JMenuItem miUnidades    = new JMenuItem("Unidades de Medida");
        miProductos.addActionListener(e  -> navegarA("productos"));
        miInventario.addActionListener(e -> navegarA("inventario"));
        miUnidades.addActionListener(e   -> navegarA("unidades"));
        menuGestionPres.add(miProductos);
        menuGestionPres.add(miInventario);
        menuGestionPres.add(miUnidades);

        // Menú: Gestión de Obras (Proveedores/Venta)
        JMenu menuGestionObras = new JMenu("Gestión de Punto de Venta");
        JMenuItem miProveedores = new JMenuItem("Proveedores");
        JMenuItem miVenta       = new JMenuItem("Punto de Venta");
        miProveedores.addActionListener(e -> navegarA("proveedores"));
        miVenta.addActionListener(e       -> navegarA("venta"));
        menuGestionObras.add(miProveedores);
        menuGestionObras.add(miVenta);

        // Menú: Reportes
        JMenu menuReportes = new JMenu("Reportes");
        JMenuItem miDashboard = new JMenuItem("Dashboard");
        JMenuItem miReportes  = new JMenuItem("Estadísticas");
        miDashboard.addActionListener(e -> navegarA("dashboard"));
        miReportes.addActionListener(e  -> navegarA("reportes"));
        menuReportes.add(miDashboard);
        menuReportes.add(miReportes);

        // Menú: Salida
        JMenu menuSalida = new JMenu("Salida");
        JMenuItem miSalir = new JMenuItem("Salir");
        miSalir.addActionListener(e -> System.exit(0));
        menuSalida.add(miSalir);

        menuBar.add(menuGestionPres);
        menuBar.add(menuGestionObras);
        menuBar.add(menuReportes);
        menuBar.add(menuSalida);
        setJMenuBar(menuBar);

        // ---- Panel de contenido ----
        panelContenido = new JPanel(new BorderLayout());
        add(panelContenido, BorderLayout.CENTER);
    }

    private void navegarA(String destino) {
        panelContenido.removeAll();
        JPanel panel = switch (destino) {
            case "dashboard"   -> new DashboardVista(repCtrl);
            case "productos"   -> new ProductoVista(prodCtrl, unidCtrl);
            case "inventario"  -> new InventarioVista(invCtrl, provCtrl);
            case "proveedores" -> new ProveedorVista(provCtrl);
            case "venta"       -> new PuntoVentaVista(ventaCtrl, prodCtrl);
            case "reportes"    -> new ReportesVista(repCtrl);
            case "unidades"    -> new UnidadesVista(unidCtrl);
            default            -> new DashboardVista(repCtrl);
        };
        panelContenido.add(panel, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }
}
