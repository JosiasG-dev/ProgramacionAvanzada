package com.examen.vista;

import com.examen.modelo.Equipo;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PanelRubrica extends JPanel {

    private JTable tabla;
    private DefaultTableModel modelo;

    public PanelRubrica() {
        setLayout(new BorderLayout(0, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnas = {"Alumno(s)", "Criterio 1", "Criterio 2", "Promedio"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int col) {
                return col != 3;
            }
        };

        modelo.addTableModelListener(e -> {
            if (e.getColumn() == 1 || e.getColumn() == 2) {
                int fila = e.getFirstRow();
                if (fila < 0) return;
                try {
                    Object v1 = modelo.getValueAt(fila, 1);
                    Object v2 = modelo.getValueAt(fila, 2);
                    double c1 = v1 == null || v1.toString().isBlank() ? 0 : Double.parseDouble(v1.toString());
                    double c2 = v2 == null || v2.toString().isBlank() ? 0 : Double.parseDouble(v2.toString());
                    modelo.setValueAt(String.format("%.1f", (c1 + c2) / 2.0), fila, 3);
                } catch (NumberFormatException ignored) {}
            }
        });

        tabla = new JTable(modelo);
        tabla.setRowHeight(24);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(220);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(80);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(80);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(80);

        tabla.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object valor,
                    boolean seleccionado, boolean enfocado, int fila, int col) {
                Component c = super.getTableCellRendererComponent(t, valor, seleccionado, enfocado, fila, col);
                if (valor != null && !valor.toString().isBlank()) {
                    try {
                        double v = Double.parseDouble(valor.toString());
                        c.setBackground(v < 70 ? new Color(255, 200, 200) :
                                (seleccionado ? t.getSelectionBackground() : t.getBackground()));
                    } catch (NumberFormatException ignored) {
                        c.setBackground(seleccionado ? t.getSelectionBackground() : t.getBackground());
                    }
                } else {
                    c.setBackground(seleccionado ? t.getSelectionBackground() : t.getBackground());
                }
                return c;
            }
        });

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        inicializarFilas();
    }

    private void inicializarFilas() {
        modelo.setRowCount(0);
        for (int i = 0; i < 4; i++) {
            modelo.addRow(new Object[]{"", "0", "0", "0"});
        }
    }

    public void cargarAlumnos(List<String> alumnos) {
        inicializarFilas();
        for (int i = 0; i < Math.min(4, alumnos.size()); i++) {
            modelo.setValueAt(alumnos.get(i), i, 0);
        }
    }

    public List<Equipo> obtenerEquipos() {
        List<Equipo> lista = new ArrayList<>();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            Object nombreObj = modelo.getValueAt(i, 0);
            if (nombreObj == null || nombreObj.toString().isBlank()) continue;
            Equipo eq = new Equipo();
            String nombreStr = nombreObj.toString();
            List<String> nombres = new ArrayList<>();
            for (String n : nombreStr.split(",")) {
                if (!n.isBlank()) nombres.add(n.trim());
            }
            eq.setNombres(nombres);
            try {
                Object promObj = modelo.getValueAt(i, 3);
                eq.setCalificacionRubrica(promObj == null ? 0 : Double.parseDouble(promObj.toString()));
            } catch (NumberFormatException e) {
                eq.setCalificacionRubrica(0);
            }
            lista.add(eq);
        }
        return lista;
    }

    public DefaultTableModel getModelo() { return modelo; }

    public void limpiar() { inicializarFilas(); }
}
