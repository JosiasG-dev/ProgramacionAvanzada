package Controlador;
import Vista.Vejercicioclase02;
import Modelo.Mejercicio02;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class Cejercicioclase02  implements ActionListener {

    private Vejercicioclase02 vista;
    private Mejercicio02 modelo;

    public Cejercicioclase02() {

        vista = new Vejercicioclase02();
        modelo = new Mejercicio02();

        vista.getComboBox().setModel(modelo.getListacombo());
        vista.getListadesplegable().setModel(modelo.getLista());

        vista.getBagregar().addActionListener(this);
        vista.getBeliminar().addActionListener(this);
        vista.getBsalir().addActionListener(this);

        vista.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vista.getBagregar()) {

            String dato = vista.getTdato().trim();

            if (dato.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Debe ingresar un dato",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            modelo.agregarCombo(dato);
            modelo.agregarLista(dato);

            vista.limpiarText();
        }

        if (e.getSource() == vista.getBeliminar()) {

            int s1 = vista.getComboBox().getSelectedIndex();
            int s2 = vista.getListadesplegable().getSelectedIndex();

            if (s1 < 0 && s2 < 0) {
                JOptionPane.showMessageDialog(null,
                        "Debe seleccionar un elemento",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (s1 >= 0) modelo.eliminarCombo(s1);
            if (s2 >= 0) modelo.eliminarLista(s2);
        }

        if (e.getSource() == vista.getBsalir()) {

            int opc = JOptionPane.showConfirmDialog(null,
                    "¿Desea salir del programa?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION);

            if (opc == 0) vista.dispose();
        }
    }
}
