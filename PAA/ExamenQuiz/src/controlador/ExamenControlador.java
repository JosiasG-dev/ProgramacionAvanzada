package controlador;

import modelo.Examen;
import modelo.GestorArchivos;
import modelo.Pregunta;
import vista.ExamenVista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class ExamenControlador {

    private ExamenVista vista;
    private GestorArchivos gestorArchivos;
    private Examen examen;
    private Pregunta preguntaActual;
    private File archivoActual;

    public ExamenControlador(ExamenVista vista) {
        this.vista          = vista;
        this.gestorArchivos = new GestorArchivos();
        bindEvents();
    }

    // =========================================================
    //  Vinculación de eventos Vista -> Controlador
    // =========================================================

    private void bindEvents() {
        // Menú: Abrir
        vista.getItemAbrir().addActionListener(e -> abrirArchivo());

        // Menú: Salir
        vista.getItemSalir().addActionListener(e -> System.exit(0));

        // Menú Opciones: cambio de modo (solo si no hay examen en curso)
        vista.getItemTerminoADadoB().addActionListener(e -> recargarSiHayArchivo());
        vista.getItemTerminoBDadoA().addActionListener(e -> recargarSiHayArchivo());

        // Botón Start Exam
        vista.getStartButton().addActionListener(e -> iniciarExamen());

        // Botón Next Question
        vista.getNextButton().addActionListener(e -> responderYSiguiente());
    }

    // =========================================================
    //  Lógica de carga de archivo
    // =========================================================

    private void abrirArchivo() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Archivos CSV (*.csv)", "csv"));

        int resultado = chooser.showOpenDialog(vista);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            archivoActual = chooser.getSelectedFile();
            cargarArchivo();
        }
    }

    private void cargarArchivo() {
        if (archivoActual == null) return;
        try {
            boolean modoInvertido = vista.getItemTerminoBDadoA().isSelected();
            List<Pregunta> preguntas = gestorArchivos.cargarExamen(archivoActual, modoInvertido);

            // Si la carga fue exitosa, preparar la vista
            vista.setTitulo(archivoActual.getName().replace(".csv", ""));
            vista.setComentario("Archivo cargado: " + preguntas.size() + " preguntas. Presiona 'Start Exam' para comenzar.");
            vista.setComentarioColor(Color.DARK_GRAY);
            vista.habilitarStartButton(true);
            vista.habilitarBotones(false);
            vista.setAnswerButtonsEnabled(false);
            vista.bloquearMenus(false);

            // Resetear etiquetas
            vista.setHeadGiven("");
            vista.setGiven("");
            vista.setHeadAnswer("");
            vista.setOpciones(new String[]{"", "", "", ""});

            // Crear examen (sin iniciar aún)
            examen = new Examen(preguntas);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista,
                    "Error al cargar el archivo:\n" + ex.getMessage(),
                    "Error de Carga",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void recargarSiHayArchivo() {
        if (archivoActual != null) {
            cargarArchivo();
        }
    }

    // =========================================================
    //  Flujo del examen
    // =========================================================

    private void iniciarExamen() {
        if (examen == null) return;

        // Bloquear menús durante el examen
        vista.bloquearMenus(true);
        vista.habilitarStartButton(false);
        vista.habilitarBotones(true);
        vista.setAnswerButtonsEnabled(true);

        // Determinar encabezados según modo
        boolean modoInvertido = vista.getItemTerminoBDadoA().isSelected();
        if (modoInvertido) {
            vista.setHeadGiven("Capital:");
            vista.setHeadAnswer("Country:");
        } else {
            vista.setHeadGiven("Country:");
            vista.setHeadAnswer("Capital:");
        }

        mostrarSiguientePregunta();
    }

    private void mostrarSiguientePregunta() {
        preguntaActual = examen.obtenerSiguiente();

        if (preguntaActual == null) {
            finalizarExamen();
            return;
        }

        vista.setGiven(preguntaActual.getEnunciado());
        vista.setOpciones(preguntaActual.getOpciones());
        vista.limpiarSeleccion();
        vista.setComentario("Pregunta " + examen.getIndiceActual() + " de " + examen.getTotalPreguntas());
        vista.setComentarioColor(Color.DARK_GRAY);
    }

    private void responderYSiguiente() {
        // Validación: debe seleccionarse una opción
        if (!vista.hayOpcionSeleccionada()) {
            JOptionPane.showMessageDialog(vista,
                    "Por favor selecciona una respuesta antes de continuar.",
                    "Sin Selección",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String seleccionada = vista.getOpcionSeleccionada();
        boolean esCorrecta  = seleccionada.equals(preguntaActual.getRespuestaCorrecta());

        examen.registrarRespuesta(esCorrecta);

        if (esCorrecta) {
            vista.setComentario("✔ ¡Correcto!");
            vista.setComentarioColor(new Color(0, 128, 0));
        } else {
            vista.setComentario("✘ Incorrecto. La respuesta correcta era: " + preguntaActual.getRespuestaCorrecta());
            vista.setComentarioColor(Color.RED);
        }

        // Pequeña pausa visual (opcional: podría usar Timer para mejor UX)
        // Continuar a la siguiente pregunta
        mostrarSiguientePregunta();
    }

    private void finalizarExamen() {
        vista.habilitarBotones(false);
        vista.setAnswerButtonsEnabled(false);
        vista.setGiven("-- Examen Finalizado --");
        vista.setOpciones(new String[]{"", "", "", ""});

        double puntaje = examen.calcularPuntaje();
        String mensaje = String.format(
                "Examen terminado.\nAciertos: %d / %d\nPuntaje: %.1f%%",
                examen.getAciertos(), examen.getIntentos(), puntaje);

        vista.setComentario(mensaje);
        vista.setComentarioColor(puntaje >= 70 ? new Color(0, 100, 0) : Color.RED);

        // Desbloquear menús al finalizar
        vista.bloquearMenus(false);
        vista.habilitarStartButton(true);

        JOptionPane.showMessageDialog(vista, mensaje, "Resultado Final", JOptionPane.INFORMATION_MESSAGE);

        // Recargar el examen para poder jugar de nuevo
        cargarArchivo();
    }
}
