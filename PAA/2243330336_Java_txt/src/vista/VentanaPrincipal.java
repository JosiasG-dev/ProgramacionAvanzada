
package vista;

import javax.swing.*;

public class VentanaPrincipal extends JFrame {

    JDesktopPane escritorio = new JDesktopPane();

    public VentanaPrincipal(){
        setTitle("Sistema de Ventas");
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setContentPane(escritorio);

        JMenuBar barra = new JMenuBar();

        JMenu menuClientes = new JMenu("Clientes");
        JMenuItem abrirClientes = new JMenuItem("Gestionar Clientes");

        abrirClientes.addActionListener(e -> {
            ClienteVista v = new ClienteVista();
            escritorio.add(v);
            v.setVisible(true);
        });

        menuClientes.add(abrirClientes);

        JMenu menuProductos = new JMenu("Productos");
        JMenuItem abrirProductos = new JMenuItem("Gestionar Productos");

        abrirProductos.addActionListener(e -> {
            ProductoVista v = new ProductoVista();
            escritorio.add(v);
            v.setVisible(true);
        });

        menuProductos.add(abrirProductos);

        barra.add(menuClientes);
        barra.add(menuProductos);

        setJMenuBar(barra);
    }
}
