package Vista;
import javax.swing.*;
public class VGestionInsumos extends JInternalFrame {

    public JTextField Tid, Tinsumo;
    public JComboBox<String> combo;
    public JButton Bagregar, Beliminar;
    public JList<String> lista;

    public VGestionInsumos() {

        super("Gestión de Insumos", true, true, true, true);
        setSize(500, 350);
        setLayout(null);

        JLabel l1 = new JLabel("ID:");
        l1.setBounds(20, 20, 100, 25);
        add(l1);

        Tid = new JTextField();
        Tid.setBounds(120, 20, 150, 25);
        add(Tid);

        JLabel l2 = new JLabel("Insumo:");
        l2.setBounds(20, 60, 100, 25);
        add(l2);

        Tinsumo = new JTextField();
        Tinsumo.setBounds(120, 60, 150, 25);
        add(Tinsumo);

        JLabel l3 = new JLabel("Categoría:");
        l3.setBounds(20, 100, 100, 25);
        add(l3);

        combo = new JComboBox<>();
        combo.setBounds(120, 100, 150, 25);
        add(combo);

        Bagregar = new JButton("Agregar");
        Bagregar.setBounds(300, 20, 150, 30);
        add(Bagregar);

        Beliminar = new JButton("Eliminar");
        Beliminar.setBounds(300, 60, 150, 30);
        add(Beliminar);

        lista = new JList<>();
        JScrollPane sp = new JScrollPane(lista);
        sp.setBounds(20, 150, 430, 140);
        add(sp);
    }
}
