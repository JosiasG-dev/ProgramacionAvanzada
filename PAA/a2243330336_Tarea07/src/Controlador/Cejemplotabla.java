package Controlador;

import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import Librerias.Libreria;
import Modelo.*;
import Vista.Vista_datos;

public class Cejemplotabla {

    private static final String ARCHIVO = "ReporteInscripcionCalificaciones_2025_1.cvs";

    private ModeloTabla modeloDatos;
    private Vista_datos vista;

    public Cejemplotabla() {

        ArrayList<String[]> datos = new ArrayList<>();
        if (Libreria.ExisteArchivo(ARCHIVO)) {
            datos = Libreria.LeerDatosCSV(ARCHIVO);
            System.out.println("Filas cargadas: " + datos.size());
        } else {
            JOptionPane.showMessageDialog(null,
                "No se encontró el archivo:\n" + ARCHIVO,
                "Archivo no encontrado", JOptionPane.WARNING_MESSAGE);
        }

        modeloDatos = new ModeloTabla(datos);

        vista = new Vista_datos();
        vista.setModeloDatos(modeloDatos);

        vista.getListacarrera().setModel(modeloDatos.getModeloCarreras());

        registrarListeners();

        if (vista.getListacarrera().getModel().getSize() > 0) {
            vista.getListacarrera().setSelectedIndex(0);
        }

        vista.setVisible(true);
    }
    private void registrarListeners() {

        vista.getListacarrera().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            Carreras carrera = vista.getListacarrera().getSelectedValue();
            if (carrera == null) return;

            DefaultListModel<Materias> mMaterias = modeloDatos.getModeloMaterias(carrera);
            vista.getListamateria().setModel(mMaterias);
            vista.getListaAsignatura().setModel(new DefaultListModel<>());

            DefaultTableModel tablaM = modeloDatos.getTablaCarrera(carrera);
            vista.setModeloTablaActual(tablaM);
            vista.actualizarTabla(tablaM, "Carrera: " + carrera.getCarrera());

            if (mMaterias.getSize() > 0) {
                vista.getListamateria().setSelectedIndex(0);
            }
        });
        vista.getListamateria().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            Materias materia = vista.getListamateria().getSelectedValue();
            if (materia == null) return;

            DefaultListModel<Asignaturas> mAsig = modeloDatos.getModeloAsignaturas(materia);
            vista.getListaAsignatura().setModel(mAsig);

            DefaultTableModel tablaM = modeloDatos.getTablaMateria(materia);
            vista.setModeloTablaActual(tablaM);
            vista.actualizarTabla(tablaM,
                "Materia: " + materia.getMateria() + "  [" + materia.getCarrera() + "]");

            if (mAsig.getSize() > 0) {
                vista.getListaAsignatura().setSelectedIndex(0);
            }
        });
        vista.getListaAsignatura().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            Asignaturas asig = vista.getListaAsignatura().getSelectedValue();
            if (asig == null) return;

            DefaultTableModel tablaM = modeloDatos.getTablaAsignatura(asig);
            vista.setModeloTablaActual(tablaM);
            vista.actualizarTabla(tablaM,
                "Grupo " + asig.getGrupo().trim() + "  –  " + asig.getProfesor());
        });
    }

    public static void main(String[] args) {
       
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new Cejemplotabla());
    }
}
