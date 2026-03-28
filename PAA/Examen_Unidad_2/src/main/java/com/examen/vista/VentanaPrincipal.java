package com.examen.vista;

import com.examen.controlador.Controlador;
import com.examen.modelo.Equipo;
import com.examen.modelo.Evaluacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    private final Controlador controlador;

    private JComboBox<String> comboProfesores;
    private JComboBox<String> comboAsignaturas;
    private JComboBox<String> comboGrupo;

    private JButton botonCargar;
    private JButton botonNuevo;
    private JButton botonGuardar;
    private JButton botonEliminar;

    private JLabel etiquetaSemaforo;
    private JPanel circuloSemaforo;
    private Color colorSemaforo = Color.RED;

    private PanelProductoIntegrador panelProducto;
    private PanelRubrica panelRubrica;
    private PanelListaCotejo panelCotejo;
    private PanelBaseDatos panelBaseDatos;

    public VentanaPrincipal(Controlador controlador) {
        super("SAE-AE — Sistema de Evaluacion de Atributos de Egreso");
        this.controlador = controlador;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 680);
        setMinimumSize(new Dimension(820, 560));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(6, 6));

        construirBarraSuperior();
        construirPanelIzquierdo();
        construirPanelCentral();

        actualizarSemaforo(Color.RED);
    }

    private void construirBarraSuperior() {
        JPanel barra = new JPanel(new BorderLayout(8, 0));
        barra.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));

        JLabel titulo = new JLabel("SAE-AE");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 14f));
        titulo.setForeground(new Color(30, 60, 110));
        barra.add(titulo, BorderLayout.WEST);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        botonCargar   = new JButton("Cargar datos");
        botonNuevo    = new JButton("Nuevo");
        botonGuardar  = new JButton("Guardar / Actualizar");
        botonEliminar = new JButton("Eliminar");

        botonCargar.addActionListener(e ->
                controlador.accionCargar(getProfesor(), getAsignatura(), getGrupo()));
        botonNuevo.addActionListener(e ->
                controlador.accionNuevo());
        botonGuardar.addActionListener(e ->
                controlador.accionGuardar(getProfesor(), getAsignatura(), getGrupo()));
        botonEliminar.addActionListener(e ->
                controlador.accionEliminar(getProfesor(), getAsignatura(), getGrupo()));

        panelBotones.add(botonCargar);
        panelBotones.add(botonNuevo);
        panelBotones.add(botonGuardar);
        panelBotones.add(botonEliminar);
        barra.add(panelBotones, BorderLayout.CENTER);

        JPanel panelSemaforo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        etiquetaSemaforo = new JLabel("Sin iniciar");
        circuloSemaforo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(colorSemaforo);
                g.fillOval(2, 2, 18, 18);
                g.setColor(colorSemaforo.darker());
                g.drawOval(2, 2, 18, 18);
            }
        };
        circuloSemaforo.setPreferredSize(new Dimension(24, 24));
        circuloSemaforo.setOpaque(false);
        panelSemaforo.add(etiquetaSemaforo);
        panelSemaforo.add(circuloSemaforo);
        barra.add(panelSemaforo, BorderLayout.EAST);

        add(barra, BorderLayout.NORTH);
    }

    private void construirPanelIzquierdo() {
        JPanel izquierdo = new JPanel(new BorderLayout(0, 6));
        izquierdo.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 4));
        izquierdo.setPreferredSize(new Dimension(240, 0));

        JPanel panelProfesor = new JPanel(new BorderLayout(0, 2));
        panelProfesor.add(new JLabel("Profesor:"), BorderLayout.NORTH);
        comboProfesores = new JComboBox<>();
        comboProfesores.addActionListener(e -> {
            if (comboProfesores.getSelectedItem() != null) {
                controlador.alSeleccionarProfesor(getProfesor());
            }
        });
        panelProfesor.add(comboProfesores, BorderLayout.CENTER);

        JPanel panelAsignatura = new JPanel(new BorderLayout(0, 2));
        panelAsignatura.add(new JLabel("Asignatura:"), BorderLayout.NORTH);
        comboAsignaturas = new JComboBox<>();
        comboAsignaturas.addActionListener(e -> {
            if (comboAsignaturas.getSelectedItem() != null) {
                controlador.alSeleccionarAsignatura(getProfesor(), getAsignatura());
            }
        });
        panelAsignatura.add(comboAsignaturas, BorderLayout.CENTER);

        JPanel panelGrupo = new JPanel(new BorderLayout(0, 2));
        panelGrupo.add(new JLabel("Grupo:"), BorderLayout.NORTH);
        comboGrupo = new JComboBox<>();
        comboGrupo.addActionListener(e ->
                controlador.alSeleccionarGrupo(getProfesor(), getAsignatura(), getGrupo()));
        panelGrupo.add(comboGrupo, BorderLayout.CENTER);

        JPanel columna = new JPanel(new GridLayout(3, 1, 0, 6));
        columna.add(panelProfesor);
        columna.add(panelAsignatura);
        columna.add(panelGrupo);
        izquierdo.add(columna, BorderLayout.CENTER);

        add(izquierdo, BorderLayout.WEST);
    }

    private void construirPanelCentral() {
        panelProducto = new PanelProductoIntegrador();
        panelRubrica  = new PanelRubrica();
        panelCotejo   = new PanelListaCotejo();
        panelBaseDatos = new PanelBaseDatos();

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("1. Producto Integrador", panelProducto);
        pestanas.addTab("2. Rúbrica", panelRubrica);
        pestanas.addTab("3. Lista de Cotejo", panelCotejo);
        pestanas.addTab("4. Base de Datos", panelBaseDatos);

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBorder(BorderFactory.createEmptyBorder(8, 4, 8, 8));
        contenedor.add(pestanas, BorderLayout.CENTER);
        add(contenedor, BorderLayout.CENTER);
    }

    public void cargarProfesores(List<String> profesores) {
        comboProfesores.removeAllItems();
        profesores.forEach(comboProfesores::addItem);
        comboProfesores.setSelectedItem(null);
    }

    public void cargarAsignaturas(List<String> asignaturas) {
        comboAsignaturas.removeAllItems();
        asignaturas.forEach(comboAsignaturas::addItem);
        comboAsignaturas.setSelectedItem(null);
        comboGrupo.removeAllItems();
    }

    public void cargarGrupos(List<String> grupos) {
        comboGrupo.removeAllItems();
        grupos.forEach(comboGrupo::addItem);
    }

    public void mostrarAtributos(List<String> atributos) {
        panelProducto.mostrarAtributos(atributos);
    }

    public void cargarAlumnosEnRubrica(List<String> alumnos) {
        panelRubrica.cargarAlumnos(alumnos);
    }

    public void actualizarSemaforo(Color color) {
        colorSemaforo = color;
        if (Color.RED.equals(color))         etiquetaSemaforo.setText("Sin iniciar");
        else if (Color.YELLOW.equals(color)) etiquetaSemaforo.setText("Incompleto");
        else                                 etiquetaSemaforo.setText("Todo listo");
        circuloSemaforo.repaint();
    }

    public void limpiarFormulario() {
        panelProducto.limpiar();
        panelRubrica.limpiar();
        panelCotejo.limpiar();
    }

    public void poblarFormulario(Evaluacion ev) {
        Map<String, Object> inst = ev.getDatosInstrumento();
        if (inst != null) {
            panelProducto.setFecha(valorStr(inst.get("fecha")));
            panelProducto.setActividad(valorStr(inst.get("actividad")));
            panelProducto.setObservaciones(valorStr(inst.get("observaciones")));
            List<JCheckBox> checks = panelCotejo.getChecks();
            for (int i = 0; i < checks.size(); i++) {
                Object val = inst.get("check_" + i);
                boolean marcado = val instanceof Boolean ? (Boolean) val : "true".equals(String.valueOf(val));
                checks.get(i).setSelected(marcado);
            }
        }
        if (ev.getEquipos() != null) {
            DefaultTableModel tm = panelRubrica.getModelo();
            for (int i = 0; i < Math.min(ev.getEquipos().size(), 4); i++) {
                Equipo eq = ev.getEquipos().get(i);
                String nombres = eq.getNombres() != null ? String.join(", ", eq.getNombres()) : "";
                tm.setValueAt(nombres, i, 0);
                tm.setValueAt(String.valueOf(eq.getCalificacionRubrica()), i, 3);
            }
        }
    }

    public Evaluacion construirEvaluacion(String profesor, String asignatura, String grupo) {
        Evaluacion ev = new Evaluacion();
		ev.setAsignatura(asignatura);
		ev.setProfesor(profesor);
		ev.setGrupo(grupo);

		Map<String, Object> inst = new LinkedHashMap<>();
		inst.put("fecha", panelProducto.getFecha());
		inst.put("actividad", panelProducto.getActividad());
		inst.put("observaciones", panelProducto.getObservaciones());

		List<JCheckBox> checks = panelCotejo.getChecks();
		String[] indicadores = panelCotejo.getIndicadores();
		for (int i = 0; i < checks.size(); i++) {
			inst.put("check_" + i, checks.get(i).isSelected());
			inst.put("etiqueta_" + i, indicadores[i]);
		}
		ev.setDatosInstrumento(inst);
		ev.setEquipos(panelRubrica.obtenerEquipos());
		return ev;
	}

	public String getProfesor() {
		Object sel = comboProfesores.getSelectedItem();
		return sel != null ? sel.toString() : null;
	}

	public String getAsignatura() {
		Object sel = comboAsignaturas.getSelectedItem();
		return sel != null ? sel.toString() : null;
	}

	public String getGrupo() {
		Object sel = comboGrupo.getSelectedItem();
		return sel != null ? sel.toString() : null;
	}

	private String valorStr(Object o) {
		return o != null ? o.toString() : "";
	}

    public void cargarModelosExcel(DefaultTableModel modAsistencia, DefaultTableModel modAtributos) {
        panelBaseDatos.cargarModelos(modAsistencia, modAtributos);
    }
}
