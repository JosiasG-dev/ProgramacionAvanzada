package Controlador;
import Vista.*;
import Libreria.UtilDesktop;

import java.awt.event.*;
public class CPractica01_Tarea02 implements ActionListener {

    private VPractica01_Tarea02 vista;

    public CPractica01_Tarea02() {

        vista = new VPractica01_Tarea02();
        vista.menuInsumos.addActionListener(this);
        vista.menuObras.addActionListener(this);

        vista.setVisible(true);
        vista.maximizar();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vista.menuInsumos) {

            VGestionInsumos v = new VGestionInsumos();
            new CGestionInsumos(v);

            vista.desktop.add(v);
            UtilDesktop.centrarInternal(vista.desktop, v);
            v.setVisible(true);

            vista.menuInsumos.setEnabled(false);
        }

        if (e.getSource() == vista.menuObras) {

            VGestionObras v = new VGestionObras();
            new CGestionObras(v);

            vista.desktop.add(v);
            UtilDesktop.centrarInternal(vista.desktop, v);
            v.setVisible(true);

            vista.menuObras.setEnabled(false);
        }
    }
}
