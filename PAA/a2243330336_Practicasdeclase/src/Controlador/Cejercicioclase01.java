package Controlador;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

import Modelo.Lista;
import Vista.Vejercicioclase01;
public class Cejercicioclase01 implements ActionListener {

	private Vejercicioclase01 vista;
    private Lista lista;
    public Cejercicioclase01() {
        vista = new Vejercicioclase01();
        lista = new Lista();

        vista.bagregar().addActionListener(this);
        vista.bsalir().addActionListener(this);

        vista.setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vista.bagregar()) {

            String nombre = vista.getT1().trim();
            String apellido = vista.getT2().trim();

            if (nombre.isEmpty() || apellido.isEmpty()) {
                JOptionPane.showMessageDialog(null, 
                    "Todos los campos son obligatorios", 
                    "Advertencia", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            lista.insertar(nombre, apellido);

            vista.limpiarText();
            vista.setTr(lista.info());

        }

        if (e.getSource() == vista.bsalir()) {
            int opc = JOptionPane.showConfirmDialog(null,
                    "¿Desea salir del programa?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION);

            if (opc == 0) System.exit(0);
        }
    }
}
