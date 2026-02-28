package Libreria;
import javax.swing.*;
import java.awt.*;

public class Librerias {
    public Icon EtiquetaImagen(String archivoimagen, int x, int y) {
        ImageIcon imagen = new ImageIcon(archivoimagen);
        Icon icono = new ImageIcon(imagen.getImage().getScaledInstance(x, y, Image.SCALE_DEFAULT));
        return icono;
    }

    public JMenuBar menuspadre(JMenuBar barra, boolean accion) {
        JMenuBar barraaux = barra;
        int cantmenus = barraaux.getMenuCount();
        for (int pos = 0; pos < cantmenus; pos++) {
            barraaux.getMenu(pos).setEnabled(accion);
        }
        return barraaux;
    }
}
