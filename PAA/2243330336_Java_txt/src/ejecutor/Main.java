
package ejecutor;

import vista.VentaVista;

public class Main {

    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(() -> {
            new VentaVista().setVisible(true);
        });

    }
}
