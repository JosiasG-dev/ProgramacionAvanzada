package Vista;
import javax.swing.*;
public class VGestionObras extends JInternalFrame {

	    public JTextField Tid, Tobra;
	    public JButton Bagregar;
	    public JList<String> lista;

	    public VGestionObras() {

	        super("Gestion de Obras", true, true, true, true);
	        setSize(500, 300);
	        setLayout(null);

	        JLabel l1 = new JLabel("ID:");
	        l1.setBounds(20, 20, 100, 25);
	        add(l1);

	        Tid = new JTextField();
	        Tid.setBounds(120, 20, 150, 25);
	        add(Tid);

	        JLabel l2 = new JLabel("Obra:");
	        l2.setBounds(20, 60, 100, 25);
	        add(l2);

	        Tobra = new JTextField();
	        Tobra.setBounds(120, 60, 150, 25);
	        add(Tobra);

	        Bagregar = new JButton("Agregar");
	        Bagregar.setBounds(300, 20, 150, 30);
	        add(Bagregar);

	        lista = new JList<>();
	        JScrollPane sp = new JScrollPane(lista);
	        sp.setBounds(20, 120, 430, 130);
	        add(sp);
	    }
}
