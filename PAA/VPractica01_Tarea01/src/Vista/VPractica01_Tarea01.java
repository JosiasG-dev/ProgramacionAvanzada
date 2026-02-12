package Vista;
import javax.swing.*;
public class VPractica01_Tarea01 extends JFrame {

    private JTextField Tid, Tinsumo;
    private JComboBox<String> ComboCategoria;
    private JButton Bagregar, Beliminar, Bsalir;
    private JList<String> lista;

    public VPractica01_Tarea01() {

        setTitle("CRUD Insumos");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lid = new JLabel("ID:");
        lid.setBounds(20, 20, 80, 25);
        add(lid);

        Tid = new JTextField();
        Tid.setBounds(120, 20, 150, 25);
        add(Tid);

        JLabel lins = new JLabel("Insumo:");
        lins.setBounds(20, 60, 80, 25);
        add(lins);

        Tinsumo = new JTextField();
        Tinsumo.setBounds(120, 60, 150, 25);
        add(Tinsumo);

        JLabel lcat = new JLabel("Categoría:");
        lcat.setBounds(20, 100, 80, 25);
        add(lcat);

        ComboCategoria = new JComboBox<>();
        ComboCategoria.setBounds(120, 100, 150, 25);
        add(ComboCategoria);

        Bagregar = new JButton("Agregar");
        Bagregar.setBounds(300, 20, 120, 30);
        add(Bagregar);

        Beliminar = new JButton("Eliminar");
        Beliminar.setBounds(300, 60, 120, 30);
        add(Beliminar);

        Bsalir = new JButton("Salir");
        Bsalir.setBounds(300, 100, 120, 30);
        add(Bsalir);

        lista = new JList<>();
        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBounds(20, 150, 440, 140);
        add(scroll);
    }

    public String getId() {
        return Tid.getText();
    }

    public String getInsumo() {
        return Tinsumo.getText();
    }

    public JComboBox<String> getCategoria() {
        return ComboCategoria;
    }

    public JList<String> getLista() {
        return lista;
    }

    public JButton getBagregar() {
        return Bagregar;
    }

    public JButton getBeliminar() {
        return Beliminar;
    }

    public JButton getBsalir() {
        return Bsalir;
    }

    public void limpiar() {
        Tid.setText("");
        Tinsumo.setText("");
        ComboCategoria.setSelectedIndex(0);
    }
	
}
