
package vista;

import controlador.VentaControl;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentaVista extends JFrame {

    VentaControl control = new VentaControl();
    DefaultTableModel modelo;

    public VentaVista(){

        setTitle("Venta");
        setSize(700,450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        JPanel panelListado = new JPanel(new BorderLayout());

        String columnas[] = {"ID","FECHA","DESCUENTO","ID CLIENTE","CLIENTE","TOTAL","ESTADO"};
        modelo = new DefaultTableModel(null,columnas);

        JTable tabla = new JTable(modelo);

        JPanel botones = new JPanel();

        JButton nuevo = new JButton("Nuevo");
        JButton anular = new JButton("Anular");
        JButton detalles = new JButton("Detalles");

        botones.add(nuevo);
        botones.add(anular);
        botones.add(detalles);

        panelListado.add(botones,BorderLayout.NORTH);
        panelListado.add(new JScrollPane(tabla),BorderLayout.CENTER);

        tabs.add("Listado",panelListado);

        JPanel panelMantenimiento = new JPanel();
        panelMantenimiento.add(new JLabel("Panel de mantenimiento"));

        tabs.add("Mantenimiento",panelMantenimiento);

        add(tabs);

        cargarDatos();

        nuevo.addActionListener(e -> {
            control.guardar(5,"2024-01-01",0.0,3,"ClienteNuevo",20.0,"Activo");
            cargarDatos();
        });

    }

    private void cargarDatos(){

        modelo.setRowCount(0);

        List<String> lista = control.listar();

        for(String s: lista){

            String datos[] = s.split(",");

            modelo.addRow(datos);
        }
    }
}
