package ejecutor;

import controlador.ExamenControlador;
import vista.ExamenVista;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ExamenVista vista = new ExamenVista();
            new ExamenControlador(vista); 
            vista.setVisible(true);
        });
    }
}
