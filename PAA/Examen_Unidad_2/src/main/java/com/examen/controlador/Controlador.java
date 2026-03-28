package com.examen.controlador;

import com.examen.modelo.*;
import com.examen.vista.VentanaPrincipal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

public class Controlador {

    private VentanaPrincipal vista;
    private final RepositorioJson repositorio;
    private final LectorDatos datos;

    public Controlador() {
        repositorio = new RepositorioJson();
        datos = new LectorDatos();
        vista = new VentanaPrincipal(this);
    }

    public void iniciar() {
        vista.cargarProfesores(datos.getProfesores());
        vista.setVisible(true);
    }

    public void alSeleccionarProfesor(String profesor) {
        if (profesor == null || profesor.isBlank()) return;
        vista.cargarAsignaturas(datos.getAsignaturas(profesor));
        vista.cargarGrupos(Collections.emptyList());
        vista.actualizarSemaforo(Color.RED);
    }

    public void alSeleccionarAsignatura(String profesor, String asignatura) {
        if (profesor == null || asignatura == null) return;
        vista.cargarGrupos(datos.getGrupos(profesor, asignatura));
        vista.mostrarAtributos(datos.getAtributos(asignatura));
        vista.actualizarSemaforo(Color.RED);
    }

    public void alSeleccionarGrupo(String profesor, String asignatura, String grupo) {
        if (profesor == null || asignatura == null || grupo == null) return;
        List<String> alumnos = datos.getAlumnos(profesor, asignatura, grupo);
        vista.cargarAlumnosEnRubrica(alumnos);
    }

    public void accionCargar(String profesor, String asignatura, String grupo) {
        if (!validarSeleccion(profesor, asignatura, grupo)) return;
        String id = generarId(profesor, asignatura, grupo);
        Evaluacion ev = repositorio.buscarPorId(id);
        if (ev != null) {
            vista.poblarFormulario(ev);
            vista.actualizarSemaforo(Color.GREEN);
            JOptionPane.showMessageDialog(vista, "Registro cargado correctamente.",
                    "Consulta exitosa", JOptionPane.INFORMATION_MESSAGE);
        } else {
            vista.limpiarFormulario();
            vista.actualizarSemaforo(Color.RED);
            JOptionPane.showMessageDialog(vista, "No existe registro para esta combinación.",
                    "Sin datos", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void accionNuevo() {
        vista.limpiarFormulario();
        vista.actualizarSemaforo(Color.RED);
    }

    public void accionGuardar(String profesor, String asignatura, String grupo) {
        if (!validarSeleccion(profesor, asignatura, grupo)) return;

        Evaluacion evaluacion = vista.construirEvaluacion(profesor, asignatura, grupo);
        evaluacion.setId(generarId(profesor, asignatura, grupo));

        repositorio.insertarOActualizar(evaluacion);

        boolean completo = esCompleto(evaluacion);
        vista.actualizarSemaforo(completo ? Color.GREEN : Color.YELLOW);

        String carpetaDestino = "";
        int respuesta = JOptionPane.showConfirmDialog(vista,
                "¿Deseas elegir la carpeta donde guardar el archivo Excel?",
                "Guardar Excel", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            JFileChooser selector = new JFileChooser();
            selector.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (selector.showSaveDialog(vista) == JFileChooser.APPROVE_OPTION) {
                carpetaDestino = selector.getSelectedFile().getAbsolutePath();
            }
        }

        try {
            File archivoExcel = GeneradorExcel.obtenerArchivo(evaluacion, carpetaDestino);
            GeneradorExcel.generarOActualizar(evaluacion, archivoExcel);
            JOptionPane.showMessageDialog(vista,
                    "Guardado correctamente.\nExcel: " + archivoExcel.getName(),
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista,
                    "Error al generar Excel: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void accionEliminar(String profesor, String asignatura, String grupo) {
        if (!validarSeleccion(profesor, asignatura, grupo)) return;
        String id = generarId(profesor, asignatura, grupo);
        Evaluacion ev = repositorio.buscarPorId(id);
        if (ev == null) {
            JOptionPane.showMessageDialog(vista, "No existe registro para eliminar.",
                    "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirmar = JOptionPane.showConfirmDialog(vista,
                "¿Confirmar eliminación del registro?", "Eliminar", JOptionPane.YES_NO_OPTION);
        if (confirmar != JOptionPane.YES_OPTION) return;

        repositorio.eliminar(id);

        int borrarExcel = JOptionPane.showConfirmDialog(vista,
                "¿Eliminar también el archivo Excel generado?",
                "Eliminar Excel", JOptionPane.YES_NO_OPTION);
        if (borrarExcel == JOptionPane.YES_OPTION) {
            File archivoExcel = GeneradorExcel.obtenerArchivo(ev, "");
            if (archivoExcel.exists()) archivoExcel.delete();
        }

        vista.limpiarFormulario();
        vista.actualizarSemaforo(Color.RED);
        JOptionPane.showMessageDialog(vista, "Registro eliminado.", "Listo", JOptionPane.INFORMATION_MESSAGE);
    }

    public String generarId(String profesor, String asignatura, String grupo) {
        return (asignatura + "_" + profesor + "_" + grupo).replaceAll("\\s+", "");
    }

    private boolean validarSeleccion(String profesor, String asignatura, String grupo) {
        if (profesor == null || profesor.isBlank()
                || asignatura == null || asignatura.isBlank()
                || grupo == null || grupo.isBlank()) {
            JOptionPane.showMessageDialog(vista,
                    "Selecciona Profesor, Asignatura y Grupo antes de continuar.",
                    "Selección incompleta", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean esCompleto(Evaluacion ev) {
        if (ev.getDatosInstrumento() == null) return false;
        Map<String, Object> inst = ev.getDatosInstrumento();
        String fecha = inst.getOrDefault("fecha", "").toString();
        String obs   = inst.getOrDefault("observaciones", "").toString();
        boolean tieneEquipos = ev.getEquipos() != null && !ev.getEquipos().isEmpty();
        return !fecha.isBlank() && !obs.isBlank() && tieneEquipos;
    }
}
