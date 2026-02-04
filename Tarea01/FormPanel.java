package Tarea01;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JPanel;

public class FormPanel extends JPanel {
	private JTextField nameField;
    private JTextField occupationField;
    private JButton okBtn;
    private FormListener formListener;

    public FormPanel() {
        Dimension dim = getPreferredSize();
        dim.width = 250;
        setPreferredSize(dim);

        nameField = new JTextField(10);
        occupationField = new JTextField(10);
        okBtn = new JButton("OK");

        okBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String occupation = occupationField.getText();
                FormEvent ev = new FormEvent(this, name, occupation);
                
                if(formListener != null) {
                    formListener.formEventOccurred(ev);
                }
            }
        });

        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.weightx = 1; gc.weighty = 0.1; gc.gridx = 0; gc.gridy = 0;
        add(new JLabel("Name: "), gc);
        gc.gridx = 1; add(nameField, gc);
        gc.gridy = 1; gc.gridx = 0; add(new JLabel("Occupation: "), gc);
        gc.gridx = 1; add(occupationField, gc);
        gc.gridy = 2; gc.gridx = 1; add(okBtn, gc);
    }

    public void setFormListener(FormListener listener) {
        this.formListener = listener;
    }
}
