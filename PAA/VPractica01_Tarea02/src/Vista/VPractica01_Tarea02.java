package Vista;
import javax.swing.*;
public class VPractica01_Tarea02 extends JFrame {

    public JDesktopPane desktop;
    public JMenuItem menuInsumos, menuObras;

    public VPractica01_Tarea02() {

        setTitle("Sistema Gestión - Evidencia 02");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        desktop = new JDesktopPane();
        setContentPane(desktop);

        JMenuBar barra = new JMenuBar();
        JMenu menu = new JMenu("Gestiones");

        menuInsumos = new JMenuItem("Gestión de Insumos");
        menuObras = new JMenuItem("Gestión de Obras");

        menu.add(menuInsumos);
        menu.add(menuObras);
        barra.add(menu);

        setJMenuBar(barra);
    }

    public void maximizar() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
}
