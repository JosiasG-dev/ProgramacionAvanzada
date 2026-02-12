package Vista;
import javax.swing.*;
public class Vejercicioclase02 extends JFrame {

    private JPanel contentPane;
    private JComboBox<String> comboBox;
    private JList<String> listadesplegable;
    private JTextField Tdato;
    private JButton Bagregar, Beliminar, Bsalir;

    public Vejercicioclase02() {

        setTitle("Ejercicio 02 - Combo y Lista");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        contentPane = new JPanel();
        contentPane.setLayout(null);
        contentPane.setBounds(0, 0, 450, 350);
        add(contentPane);

        JLabel lbl = new JLabel("Dato:");
        lbl.setBounds(30, 20, 80, 25);
        contentPane.add(lbl);

        Tdato = new JTextField();
        Tdato.setBounds(90, 20, 200, 25);
        contentPane.add(Tdato);

        Bagregar = new JButton("Agregar");
        Bagregar.setBounds(300, 20, 100, 25);
        contentPane.add(Bagregar);

        comboBox = new JComboBox<>();
        comboBox.setBounds(30, 70, 170, 30);
        contentPane.add(comboBox);

        listadesplegable = new JList<>();
        JScrollPane scroll = new JScrollPane(listadesplegable);
        scroll.setBounds(220, 70, 180, 150);
        contentPane.add(scroll);

        Beliminar = new JButton("Eliminar");
        Beliminar.setBounds(80, 240, 120, 30);
        contentPane.add(Beliminar);

        Bsalir = new JButton("Salir");
        Bsalir.setBounds(230, 240, 120, 30);
        contentPane.add(Bsalir);
    }

    public String getTdato() {
        return Tdato.getText();
    }

    public JComboBox<String> getComboBox() {
        return comboBox;
    }

    public JList<String> getListadesplegable() {
        return listadesplegable;
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

    public void limpiarText() {
        Tdato.setText("");
    }
}
