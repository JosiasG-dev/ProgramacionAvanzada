package com.examen.vista;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PanelListaCotejo extends JPanel {

    private static final String[] INDICADORES = {
        "El alumno domina los conceptos básicos de la asignatura.",
        "El alumno participa activamente en clase.",
        "El alumno presenta las evidencias a tiempo.",
        "El portafolio de evidencias está completo.",
        "El alumno demuestra habilidad para resolver problemas.",
        "El alumno trabaja eficientemente en equipo."
    };

    private final List<JCheckBox> checks = new ArrayList<>();
    private JButton botonMarcar;

    public PanelListaCotejo() {
        setLayout(new BorderLayout(0, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelChecks = new JPanel();
        panelChecks.setLayout(new BoxLayout(panelChecks, BoxLayout.Y_AXIS));
        panelChecks.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        for (String indicador : INDICADORES) {
            JCheckBox check = new JCheckBox(indicador);
            checks.add(check);
            panelChecks.add(check);
            panelChecks.add(Box.createVerticalStrut(4));
        }

        add(new JScrollPane(panelChecks), BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botonMarcar = new JButton("Marcar todos");
        botonMarcar.addActionListener(e -> {
            boolean todosMarcados = checks.stream().allMatch(JCheckBox::isSelected);
            checks.forEach(c -> c.setSelected(!todosMarcados));
            botonMarcar.setText(todosMarcados ? "Marcar todos" : "Desmarcar todos");
        });
        panelInferior.add(botonMarcar);
        add(panelInferior, BorderLayout.SOUTH);
    }

    public List<JCheckBox> getChecks() { return checks; }
    public String[] getIndicadores() { return INDICADORES; }

    public void limpiar() {
        checks.forEach(c -> c.setSelected(false));
        botonMarcar.setText("Marcar todos");
    }
}
