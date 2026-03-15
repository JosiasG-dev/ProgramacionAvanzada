
package vista;

import controlador.ProductoControl;
import javax.swing.*;
import java.awt.*;
import java.awt.List;
import java.util.*;
import java.util.*;

public class ProductoVista extends JInternalFrame {

    ProductoControl control = new ProductoControl();
    JTextField txtId = new JTextField();
    JTextField txtNombre = new JTextField();
    JTextArea area = new JTextArea();

    public ProductoVista(){
        setTitle("Productos");
        setSize(400,300);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(3,2));

        panel.add(new JLabel("ID"));
        panel.add(txtId);

        panel.add(new JLabel("Nombre"));
        panel.add(txtNombre);

        JButton guardar = new JButton("Guardar");
        JButton listar = new JButton("Listar");

        panel.add(guardar);
        panel.add(listar);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(area), BorderLayout.CENTER);

        guardar.addActionListener(e -> {
            int id = Integer.parseInt(txtId.getText());
            String nombre = txtNombre.getText();
            control.guardar(id,nombre);
            JOptionPane.showMessageDialog(this,"Guardado");
        });

        listar.addActionListener(e -> {
            area.setText("");
           
        });
    }
}
