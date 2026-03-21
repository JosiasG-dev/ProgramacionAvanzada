package ejecutable;

import controlador.Controlador;
import vista.Principal;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            Principal ventana = new Principal();
            new Controlador(ventana);
            ventana.setVisible(true);
        });
    }
}
