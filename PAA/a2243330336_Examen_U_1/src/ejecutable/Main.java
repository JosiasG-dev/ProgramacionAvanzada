package ejecutable;

import controlador.Controlador;
import vista.Principal;


public class Main {

    public static void main(String[] args) {
       
            Principal ventana = new Principal();
            new Controlador(ventana);
            ventana.setVisible(true);
    }
}
