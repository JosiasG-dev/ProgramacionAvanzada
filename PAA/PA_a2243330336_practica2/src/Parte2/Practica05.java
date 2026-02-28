package Parte2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Practica05 extends JFrame implements ActionListener {

    private JDesktopPane Escritorio;
    private JMenuBar BarraMenu;
    private JMenu Moperacion, Mconfiguracion, Msalir;
    private JMenuItem SMcategorias, SMinsumos, SMsalida;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    Practica05 window = new Practica05();
                    window.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Practica05() {
        setTitle("Aplicación con Escritorio");
        setBounds(100, 100, 800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Escritorio = new JDesktopPane();
        getContentPane().add(Escritorio, BorderLayout.CENTER);

        BarraMenu = new JMenuBar();
        setJMenuBar(BarraMenu);

        Moperacion = new JMenu("Operacion");
        BarraMenu.add(Moperacion);

        Mconfiguracion = new JMenu("Configuracion");
        BarraMenu.add(Mconfiguracion);

        Msalir = new JMenu("Salir");
        BarraMenu.add(Msalir);

        SMcategorias = new JMenuItem("Categorias");
        Mconfiguracion.add(SMcategorias);

        SMinsumos = new JMenuItem("Insumos");
        Mconfiguracion.add(SMinsumos);

        SMsalida = new JMenuItem("Salida");
        Msalir.add(SMsalida);

        SMcategorias.addActionListener(this);
        SMinsumos.addActionListener(this);
        SMsalida.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == SMsalida) {
            dispose();
        } else if (e.getSource() == SMcategorias) {
            JOptionPane.showMessageDialog(this, "Llamando a Categorías (pendiente de implementar)");
        } else if (e.getSource() == SMinsumos) {
            // Pasar this como padre
            Practica03_b hijo = new Practica03_b(this);
            Escritorio.add(hijo);
            hijo.setVisible(true);
        }
    }
}