package Vista;

import javax.swing.*;

public class Vejercicioclase01 extends JFrame {
	  private JPanel PanelPrincipal;
	    private JTextField T1;
	    private JTextField T2;
	    private JButton Bagregar;
	    private JButton Bsalir;
	    private JTextArea Tr;

	    public Vejercicioclase01() {
	        setTitle("Practica 1 - MVC");
	        setSize(450, 350);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setLocationRelativeTo(null);
	        setLayout(null);

	        PanelPrincipal = new JPanel();
	        PanelPrincipal.setLayout(null);
	        PanelPrincipal.setBounds(0, 0, 450, 350);
	        add(PanelPrincipal);

	        JLabel l1 = new JLabel("Nombre de Profesor:");
	        l1.setBounds(30, 20, 80, 25);
	        PanelPrincipal.add(l1);

	        T1 = new JTextField();
	        T1.setBounds(120, 20, 200, 25);
	        PanelPrincipal.add(T1);

	        JLabel l2 = new JLabel("Apellido:");
	        l2.setBounds(30, 60, 80, 25);
	        PanelPrincipal.add(l2);

	        T2 = new JTextField();
	        T2.setBounds(120, 60, 200, 25);
	        PanelPrincipal.add(T2);

	        Bagregar = new JButton("Agregar");
	        Bagregar.setBounds(50, 100, 120, 30);
	        PanelPrincipal.add(Bagregar);

	        Bsalir = new JButton("Salir");
	        Bsalir.setBounds(200, 100, 120, 30);
	        PanelPrincipal.add(Bsalir);

	        Tr = new JTextArea();
	        JScrollPane scroll = new JScrollPane(Tr);
	        scroll.setBounds(30, 150, 380, 130);
	        PanelPrincipal.add(scroll);
	    }

	    public String getT1() {
	        return T1.getText();
	    }

	    public String getT2() {
	        return T2.getText();
	    }

	    public void setT1(String texto) {
	        T1.setText(texto);
	    }

	    public void setT2(String texto) {
	        T2.setText(texto);
	    }

	    public void setTr(String texto) {
	        Tr.setText(texto);
	    }

	    public JButton bagregar() {
	        return Bagregar;
	    }

	    public JButton bsalir() {
	        return Bsalir;
	    }

	    public void limpiarText() {
	        T1.setText("");
	        T2.setText("");
	    }
}
