package Parte2;

import javax.swing.*;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Practica04_1 implements ActionListener{
	 private JFrame VentanaPrincipal;
	    private JMenuBar BarraMenu;
	    private JMenu Moperacion, Mconfiguracion, Msalir;
	    private JMenuItem SMcategorias, SMinsumos, SMsalida;

	    public static void main(String[] args) {
	        EventQueue.invokeLater(new Runnable() {
	            public void run() {
	                try {
	                    Practica04_1 window = new Practica04_1();
	                    window.VentanaPrincipal.setVisible(true);
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
	        });
	    }

	    public Practica04_1() {
	        VentanaPrincipal = new JFrame();
	        VentanaPrincipal.setBounds(100, 100, 622, 395);
	        VentanaPrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	        BarraMenu = new JMenuBar();
	        VentanaPrincipal.setJMenuBar(BarraMenu);

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
	            VentanaPrincipal.dispose();
	        } else if (e.getSource() == SMcategorias) {
	            JOptionPane.showMessageDialog(VentanaPrincipal, "Llamando a Categorías");
	        } else if (e.getSource() == SMinsumos) {
	            Practica04 hijo = new Practica04();
	            hijo.setVisible(true);
	        }
	    }
	}