package com.examen.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelBaseDatos extends JPanel {

    private JTable tablaAsistencia;
    private JTable tablaAtributos;

    public PanelBaseDatos() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        tablaAsistencia = new JTable();
        tablaAtributos = new JTable();

        JTabbedPane pestanasDato = new JTabbedPane();
        pestanasDato.addTab("Listas de Asistencia", new JScrollPane(tablaAsistencia));
        pestanasDato.addTab("Asignatura - Atributo", new JScrollPane(tablaAtributos));

        add(pestanasDato, BorderLayout.CENTER);
    }

    public void cargarModelos(DefaultTableModel modeloAsistencia, DefaultTableModel modeloAtributos) {
        tablaAsistencia.setModel(modeloAsistencia);
        tablaAtributos.setModel(modeloAtributos);
    }
}
