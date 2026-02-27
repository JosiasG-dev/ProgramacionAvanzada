package Parte1.Vista;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

public class practica02_c extends JFrame implements ActionListener {
	String[] opciones = {"Opción 1", "Opción 2", "Opción 3"};
    JComboBox<String> comboBox;
    JPanel panel;
    JButton Bsalir;
    JTextField campoTexto;
    JPasswordField campoPassword;
    JTextArea areaTexto;
    JFormattedTextField campoFormateado;
    JSpinner spinner;
    JSlider slider;

    public practica02_c() {
        setTitle("Ejemplo de componentes de entrada de texto");
        setSize(442, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setBounds(0, 0, 403, 365);

        JLabel etiqueta1 = new JLabel("JButton:");
        etiqueta1.setBounds(0, 3, 178, 36);
        Bsalir = new JButton("Haz clic para Salir");
        Bsalir.setBounds(188, 3, 172, 36);
        Bsalir.addActionListener(this);
        panel.setLayout(null);
        panel.add(etiqueta1);
        panel.add(Bsalir);

        JLabel etiqueta2 = new JLabel("TextField");
        etiqueta2.setBounds(0, 49, 148, 36);
        campoTexto = new JTextField();
        campoTexto.setBounds(188, 50, 172, 36);
        panel.add(etiqueta2);
        panel.add(campoTexto);

        JLabel etiqueta3 = new JLabel("PasswordField");
        etiqueta3.setBounds(0, 95, 148, 36);
        campoPassword = new JPasswordField();
        campoPassword.setBounds(188, 97, 172, 36);
        panel.add(etiqueta3);
        panel.add(campoPassword);

        JLabel etiqueta4 = new JLabel("JTextArea");
        etiqueta4.setBounds(0, 141, 148, 36);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(188, 141, 172, 36);
        panel.add(etiqueta4);
        panel.add(scrollPane);
        areaTexto = new JTextArea();
        scrollPane.setViewportView(areaTexto);

        JLabel etiqueta5 = new JLabel("JFormattedTextField:");
        etiqueta5.setBounds(0, 187, 141, 36);
        campoFormateado = new JFormattedTextField();
        campoFormateado.setBounds(188, 187, 172, 36);
        campoFormateado.setValue(12345.67);
        panel.add(etiqueta5);
        panel.add(campoFormateado);

        JLabel etiqueta6 = new JLabel("JSpinner:");
        etiqueta6.setBounds(0, 233, 141, 36);
        SpinnerNumberModel modeloSpinner = new SpinnerNumberModel(0, 0, 100, 1);
        getContentPane().setLayout(null);
        spinner = new JSpinner(modeloSpinner);
        spinner.setBounds(188, 233, 172, 36);
        panel.add(etiqueta6);
        panel.add(spinner);

        JLabel etiqueta7 = new JLabel("JSlider:");
        etiqueta7.setBounds(0, 279, 148, 36);
        slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        slider.setBounds(188, 280, 178, 36);
        slider.setMajorTickSpacing(20);
        slider.setMinorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        panel.add(etiqueta7);
        panel.add(slider);

        JLabel etiqueta8 = new JLabel("JComboBox:");
        etiqueta8.setBounds(0, 325, 148, 36);
        comboBox = new JComboBox<>(opciones);
        comboBox.setBounds(188, 325, 172, 36);
        panel.add(etiqueta8);
        panel.add(comboBox);

        getContentPane().add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
    	practica02_c ventana = new practica02_c();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.Bsalir) {
            String cadena = " valor de JTextField: " + this.campoTexto.getText().trim();
            cadena = cadena + "\n" + " valor de JPasswordField: " + this.campoPassword.getText().trim();
            cadena = cadena + "\n" + " valor de JTextArea: " + this.areaTexto.getText();
            cadena = cadena + "\n" + " valor de JFormattedTextField: " + this.campoFormateado.getText().trim();
            cadena = cadena + "\n" + " valor de JSpinner: " + this.spinner.getValue().toString();
            cadena = cadena + "\n" + " valor de JSlider: " + String.valueOf(this.slider.getValue());
            if (comboBox.getSelectedIndex() >= -1) {
                cadena = cadena + "\n" + " valor de JComboBox: " + this.comboBox.getSelectedItem().toString();
            }

            JOptionPane.showMessageDialog(this, cadena);
        }
    }
}
