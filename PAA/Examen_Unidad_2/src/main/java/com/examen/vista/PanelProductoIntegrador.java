package com.examen.vista;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanelProductoIntegrador extends JPanel {

    private JTextField campoFecha;
    private JTextField campoActividad;
    private JTextArea areaAtributos;
    private JTextArea areaObservaciones;

    public PanelProductoIntegrador() {
        setLayout(new BorderLayout(0, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelSuperior = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panelSuperior.add(new JLabel("Fecha (AAAA-MM-DD):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        campoFecha = new JTextField(15);
        panelSuperior.add(campoFecha, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelSuperior.add(new JLabel("Actividad:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        campoActividad = new JTextField(30);
        panelSuperior.add(campoActividad, gbc);

        add(panelSuperior, BorderLayout.NORTH);

        JPanel panelAtributos = new JPanel(new BorderLayout(0, 4));
        panelAtributos.add(new JLabel("Atributo(s) de egreso:"), BorderLayout.NORTH);
        areaAtributos = new JTextArea(3, 40);
        areaAtributos.setEditable(false);
        areaAtributos.setLineWrap(true);
        areaAtributos.setWrapStyleWord(true);
        areaAtributos.setBackground(UIManager.getColor("Panel.background"));
        panelAtributos.add(new JScrollPane(areaAtributos), BorderLayout.CENTER);

        JPanel panelObservaciones = new JPanel(new BorderLayout(0, 4));
        panelObservaciones.add(new JLabel("Observaciones académicas:"), BorderLayout.NORTH);
        areaObservaciones = new JTextArea(5, 40);
        areaObservaciones.setLineWrap(true);
        areaObservaciones.setWrapStyleWord(true);
        panelObservaciones.add(new JScrollPane(areaObservaciones), BorderLayout.CENTER);

        JPanel panelCentro = new JPanel(new GridLayout(2, 1, 0, 8));
        panelCentro.add(panelAtributos);
        panelCentro.add(panelObservaciones);
        add(panelCentro, BorderLayout.CENTER);
    }

    public String getFecha() { return campoFecha.getText().trim(); }
    public void setFecha(String valor) { campoFecha.setText(valor); }
    public String getActividad() { return campoActividad.getText().trim(); }
    public void setActividad(String valor) { campoActividad.setText(valor); }
    public String getObservaciones() { return areaObservaciones.getText().trim(); }
    public void setObservaciones(String valor) { areaObservaciones.setText(valor); }

    public void mostrarAtributos(List<String> atributos) {
        if (atributos == null || atributos.isEmpty()) {
            areaAtributos.setText("(Sin atributos registrados)");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < atributos.size(); i++) {
            sb.append("• ").append(atributos.get(i));
            if (i < atributos.size() - 1) sb.append("\n");
        }
        areaAtributos.setText(sb.toString());
    }

    public void limpiar() {
        campoFecha.setText("");
        campoActividad.setText("");
        areaObservaciones.setText("");
    }
}
