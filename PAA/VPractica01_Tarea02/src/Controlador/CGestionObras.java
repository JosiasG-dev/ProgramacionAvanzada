package Controlador;
import Vista.*;

import javax.swing.*;
import java.awt.event.*;
public class CGestionObras implements ActionListener {

    private VGestionObras vista;
    private DefaultListModel<String> modelo;

    public CGestionObras(VGestionObras v) {

        vista = v;
        modelo = new DefaultListModel<>();

        vista.lista.setModel(modelo);
        vista.Bagregar.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {

        int id = Integer.parseInt(vista.Tid.getText());
        String obra = vista.Tobra.getText();

        modelo.addElement(id + " - " + obra);
    }
}
