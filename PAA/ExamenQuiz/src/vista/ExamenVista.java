package vista;

import javax.swing.*;
import java.awt.*;

public class ExamenVista extends JFrame {

    private JMenuBar menuBar;
    private JMenu menuArchivo;
    private JMenu menuOpciones;
    private JMenuItem itemAbrir;
    private JMenuItem itemSalir;
    private JRadioButtonMenuItem itemTerminoADadoB; 
    private JRadioButtonMenuItem itemTerminoBDadoA;  
    private JRadioButtonMenuItem itemMultipleChoice; 
    private ButtonGroup grupoModo;
    private JLabel headGivenLabel;
    // gridy=1 : givenLabel
    private JLabel givenLabel;
    // gridy=2 : headAnswerLabel
    private JLabel headAnswerLabel;
    // gridy=3..6 : answerLabel[0..3] -> JRadioButton
    private JRadioButton[] answerButtons;
    private ButtonGroup answerGroup;
    // gridy=7 : commentTextArea
    private JTextArea commentTextArea;
    // gridy=8 : nextButton
    private JButton nextButton;
    // gridy=9 : startButton
    private JButton startButton;

    public ExamenVista() {
        initComponents();
        buildMenus();
        buildLayout();
        configureFrame();
    }

    private void initComponents() {
      
        headGivenLabel = new JLabel(" ");
        givenLabel      = new JLabel(" ");
        headAnswerLabel = new JLabel(" ");

        answerButtons = new JRadioButton[4];
        answerGroup   = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            answerButtons[i] = new JRadioButton(" ");
            answerGroup.add(answerButtons[i]);
        }
        commentTextArea = new JTextArea("Open Exam File to Start");
        commentTextArea.setFont(new Font("Monospaced", Font.BOLD | Font.ITALIC, 14));
        commentTextArea.setForeground(Color.RED);
        commentTextArea.setBackground(new Color(255, 255, 204));
        commentTextArea.setEditable(false);
        commentTextArea.setLineWrap(true);
        commentTextArea.setWrapStyleWord(true);
        commentTextArea.setAlignmentX(CENTER_ALIGNMENT);

        nextButton  = new JButton("Next Question");
        startButton = new JButton("Start Exam");

        nextButton.setEnabled(false);
        startButton.setEnabled(false);
    }

    private void buildMenus() {
        menuBar = new JMenuBar();
        menuArchivo = new JMenu("File");

        itemAbrir = new JMenuItem("Open");
        itemSalir = new JMenuItem("Exit");

        menuArchivo.add(itemAbrir);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);

        menuOpciones = new JMenu("Options");

        grupoModo = new ButtonGroup();

        itemTerminoADadoB  = new JRadioButtonMenuItem("Country, Given Capital");
        itemTerminoBDadoA  = new JRadioButtonMenuItem("Capital, Given Country", true); // default
        itemMultipleChoice = new JRadioButtonMenuItem("Multiple Choice Answers", true);

        grupoModo.add(itemTerminoADadoB);
        grupoModo.add(itemTerminoBDadoA);

        menuOpciones.add(itemTerminoADadoB);
        menuOpciones.add(itemTerminoBDadoA);
        menuOpciones.addSeparator();
        menuOpciones.add(itemMultipleChoice);

        menuBar.add(menuArchivo);
        menuBar.add(menuOpciones);
        setJMenuBar(menuBar);
    }

    private void buildLayout() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx    = 0;
        gbc.fill     = GridBagConstraints.HORIZONTAL;
        gbc.weightx  = 1.0;
        gbc.insets   = new Insets(2, 8, 2, 8);

        gbc.gridy = 0;
        styleLabel(headGivenLabel, true);
        panel.add(headGivenLabel, gbc);

        gbc.gridy = 1;
        styleLabel(givenLabel, false);
        panel.add(givenLabel, gbc);

        gbc.gridy = 2;
        styleLabel(headAnswerLabel, true);
        panel.add(headAnswerLabel, gbc);

        for (int i = 0; i < 4; i++) {
            gbc.gridy = 3 + i;
            styleRadio(answerButtons[i]);
            panel.add(answerButtons[i], gbc);
        }

        gbc.gridy   = 7;
        gbc.fill    = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        JScrollPane scrollPane = new JScrollPane(commentTextArea);
        scrollPane.setPreferredSize(new Dimension(0, 80));
        panel.add(scrollPane, gbc);

        gbc.gridy   = 8;
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;
        panel.add(nextButton, gbc);

        gbc.gridy = 9;
        panel.add(startButton, gbc);

        add(panel);
    }
    private void styleLabel(JLabel label, boolean isHeader) {
        label.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        label.setPreferredSize(new Dimension(0, 28));
        if (isHeader) {
            label.setFont(label.getFont().deriveFont(Font.BOLD));
            label.setBackground(new Color(230, 230, 230));
            label.setOpaque(true);
        }
    }

    private void styleRadio(JRadioButton rb) {
        rb.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        rb.setBackground(Color.WHITE);
        rb.setOpaque(true);
        rb.setPreferredSize(new Dimension(0, 28));
    }

    private void configureFrame() {
        setTitle("Multiple Choice Exam - No File");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 500);
        setMinimumSize(new Dimension(380, 450));
        setLocationRelativeTo(null);
    }
    public void setTitulo(String titulo) {
        setTitle("Multiple Choice Exam - " + titulo);
    }

    public void setHeadGiven(String texto) {
        headGivenLabel.setText("  " + texto);
    }

    public void setGiven(String texto) {
        givenLabel.setText("  " + texto);
    }

    public void setHeadAnswer(String texto) {
        headAnswerLabel.setText("  " + texto);
    }

    public void setOpciones(String[] opciones) {
        answerGroup.clearSelection();
        for (int i = 0; i < 4; i++) {
            answerButtons[i].setText(opciones[i]);
            answerButtons[i].setSelected(false);
        }
    }

    public void setComentario(String texto) {
        commentTextArea.setForeground(Color.BLACK);
        commentTextArea.setText(texto);
    }

    public void setComentarioColor(Color color) {
        commentTextArea.setForeground(color);
    }

    public void setComentarioInicial() {
        commentTextArea.setForeground(Color.RED);
        commentTextArea.setFont(new Font("Monospaced", Font.BOLD | Font.ITALIC, 14));
        commentTextArea.setText("Open Exam File to Start");
    }

    public String getOpcionSeleccionada() {
        for (JRadioButton rb : answerButtons) {
            if (rb.isSelected()) return rb.getText();
        }
        return null;
    }

    public boolean hayOpcionSeleccionada() {
        for (JRadioButton rb : answerButtons) {
            if (rb.isSelected()) return true;
        }
        return false;
    }

    public void limpiarSeleccion() {
        answerGroup.clearSelection();
    }

    public void habilitarBotones(boolean habilitar) {
        nextButton.setEnabled(habilitar);
    }

    public void habilitarStartButton(boolean habilitar) {
        startButton.setEnabled(habilitar);
    }

    public void bloquearMenus(boolean bloquear) {
        menuOpciones.setEnabled(!bloquear);
        itemAbrir.setEnabled(!bloquear);
    }

    public void setAnswerButtonsEnabled(boolean enabled) {
        for (JRadioButton rb : answerButtons) {
            rb.setEnabled(enabled);
        }
    }
    public JMenuItem getItemAbrir()          { return itemAbrir; }
    public JMenuItem getItemSalir()          { return itemSalir; }
    public JRadioButtonMenuItem getItemTerminoADadoB() { return itemTerminoADadoB; }
    public JRadioButtonMenuItem getItemTerminoBDadoA() { return itemTerminoBDadoA; }
    public JButton getNextButton()           { return nextButton; }
    public JButton getStartButton()          { return startButton; }
}
