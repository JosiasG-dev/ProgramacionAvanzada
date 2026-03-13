package Vista;

import Modelo.*;
import Librerias.LibreriaAdmin;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;

public class Vista_datos extends JFrame {

    private static final Font F_TITULO  = new Font("Dialog", Font.BOLD,  12);
    private static final Font F_NORMAL  = new Font("Dialog", Font.PLAIN, 12);
    private static final Font F_PEQUEÑA = new Font("Dialog", Font.PLAIN, 11);

    private JList<Carreras>    listaCarrera;
    private JList<Materias>    listaMateria;
    private JList<Asignaturas> listaAsignatura;

    private JTable            tabla;
    private DefaultTableModel modeloTabla;

    private JComboBox<String> comboFiltro;
    private JTextField        txtBuscar;
    private JLabel            lblContador;
    private JLabel            lblTitulo;

    private JButton btnTodos;
    private JButton btnExportar;

    private JTable  tablaPromedios;
    private JButton btnCalcular;

    private JTable  tablaCedula;
    private JButton btnCedula;
  
    private DefaultListModel<Academia> modeloListaAcademias;
    private JList<Academia>   listaAcademias;
    private JTextField        txtNombreAcademia;
    private JButton           btnAgregarAcademia;
    private JButton           btnEditarAcademia;
    private JButton           btnEliminarAcademia;

    private DefaultListModel<AsignaturaAdmin> modeloListaAsigAdmin;
    private JList<AsignaturaAdmin> listaAsigAdmin;
    private JTextField             txtNombreAsig;
    private JComboBox<Academia>    comboAcademiaAsig;
    private JButton                btnAgregarAsig;
    private JButton                btnEditarAsig;
    private JButton                btnEliminarAsig;

    private ModeloTabla modeloDatos;

    public Vista_datos() {
        setTitle("Control Escolar MVC");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 750);
        setLocationRelativeTo(null);
        construirUI();
    }

    private void construirUI() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(F_NORMAL);
        tabs.addTab("Datos",           crearTabDatos());
        tabs.addTab("Calcular Promedios", crearTabPromedios());
        tabs.addTab("Cédula 3.3.2",    crearTabCedula());
        tabs.addTab("Administración",  crearTabAdmin());
        setContentPane(tabs);
    }

    private JPanel crearTabDatos() {
        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        raiz.add(crearBarraHerramientas(), BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                crearPanelIzq(), crearPanelCentro());
        split.setDividerLocation(190);
        split.setDividerSize(4);
        split.setBorder(null);
        raiz.add(split, BorderLayout.CENTER);
        return raiz;
    }

    private JPanel crearBarraHerramientas() {
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));

        lblTitulo = new JLabel("Seleccione un programa educativo");
        lblTitulo.setFont(F_NORMAL);

        btnTodos    = new JButton("Ver todos");
        btnTodos.setFont(F_PEQUEÑA);
        btnExportar = new JButton("Exportar CSV");
        btnExportar.setFont(F_PEQUEÑA);

        JLabel lblBuscar = new JLabel("Buscar en:");
        lblBuscar.setFont(F_PEQUEÑA);

        comboFiltro = new JComboBox<>(new String[]{
            "ALUMNO","MATRICULA","CALIFICACION","PROFESOR","MATERIA","P.EDUCATIVO"
        });
        comboFiltro.setFont(F_PEQUEÑA);
        comboFiltro.setPreferredSize(new Dimension(140, 24));

        txtBuscar = new JTextField(14);
        txtBuscar.setFont(F_PEQUEÑA);

        lblContador = new JLabel("0 registros");
        lblContador.setFont(F_PEQUEÑA);

        barra.add(lblTitulo);
        barra.add(Box.createHorizontalStrut(10));
        barra.add(btnTodos);
        barra.add(btnExportar);
        barra.add(Box.createHorizontalStrut(10));
        barra.add(lblBuscar);
        barra.add(comboFiltro);
        barra.add(txtBuscar);
        barra.add(Box.createHorizontalStrut(10));
        barra.add(lblContador);

        barra.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY),
            BorderFactory.createEmptyBorder(2, 0, 2, 0)
        ));

        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { aplicarFiltroTexto(); }
            public void removeUpdate(DocumentEvent e)  { aplicarFiltroTexto(); }
            public void changedUpdate(DocumentEvent e) { aplicarFiltroTexto(); }
        });

        btnTodos.addActionListener(e -> mostrarTodos());
        btnExportar.addActionListener(e -> exportarTabla());
        return barra;
    }

    private JPanel crearPanelIzq() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 4));
        panel.add(crearSeccionLista("Alumnos",    crearScrollLista(getListacarrera())));
        panel.add(crearSeccionLista("Materias",   crearScrollLista(getListamateria())));
        panel.add(crearSeccionLista("Profesores", crearScrollLista(getListaAsignatura())));
        return panel;
    }

    private JPanel crearSeccionLista(String etiqueta, JScrollPane scroll) {
        JPanel sec = new JPanel(new BorderLayout(0, 2));
        sec.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(F_TITULO);
        sec.add(lbl,    BorderLayout.NORTH);
        sec.add(scroll, BorderLayout.CENTER);
        return sec;
    }

    private <T> JScrollPane crearScrollLista(JList<T> lista) {
        lista.setFont(F_PEQUEÑA);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane sp = new JScrollPane(lista);
        sp.setPreferredSize(new Dimension(175, 120));
        return sp;
    }

    private JPanel crearPanelCentro() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 0, 0));
        tabla = new JTable();
        tabla.setFont(F_PEQUEÑA);
        tabla.setRowHeight(20);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabla.setFillsViewportHeight(true);
        JTableHeader header = tabla.getTableHeader();
        header.setFont(F_TITULO);
        header.setReorderingAllowed(false);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearTabPromedios() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel norte = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        btnCalcular = new JButton("Calcular Promedios");
        btnCalcular.setFont(F_NORMAL);
        norte.add(btnCalcular);
        norte.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

        tablaPromedios = new JTable();
        tablaPromedios.setFont(F_PEQUEÑA);
        tablaPromedios.setRowHeight(20);
        tablaPromedios.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tablaPromedios.setFillsViewportHeight(true);
        tablaPromedios.getTableHeader().setFont(F_TITULO);
        tablaPromedios.getTableHeader().setReorderingAllowed(false);

        panel.add(norte, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaPromedios), BorderLayout.CENTER);

        btnCalcular.addActionListener(e -> {
            if (modeloDatos == null) return;
            DefaultTableModel m = modeloDatos.calcularPromedios();
            tablaPromedios.setModel(m);
            ajustarColumnasTabla(tablaPromedios,
                new int[]{200, 260, 60, 160, 100, 110, 90, 90, 180});
        });

        return panel;
    }

    private JPanel crearTabCedula() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel norte = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        btnCedula = new JButton("Generar Datos Cédula 3.3.2");
        btnCedula.setFont(F_NORMAL);
        norte.add(btnCedula);
        norte.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

        tablaCedula = new JTable();
        tablaCedula.setFont(F_PEQUEÑA);
        tablaCedula.setRowHeight(20);
        tablaCedula.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tablaCedula.setFillsViewportHeight(true);
        tablaCedula.getTableHeader().setFont(F_TITULO);
        tablaCedula.getTableHeader().setReorderingAllowed(false);

        panel.add(norte, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaCedula), BorderLayout.CENTER);

        btnCedula.addActionListener(e -> {
            if (modeloDatos == null) return;
            DefaultTableModel m = modeloDatos.calcularCedula332();
            tablaCedula.setModel(m);
            ajustarColumnasTabla(tablaCedula,
                new int[]{280, 280, 110, 90, 130, 110, 300});
        });

        return panel;
    }

    private JPanel crearTabAdmin() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        panel.add(crearPanelAcademias());
        panel.add(crearPanelAsignaturasAdmin());
        registrarListenersAdmin(); 
        return panel;
    }

    private JPanel crearPanelAcademias() {
        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Academias", TitledBorder.LEFT,
            TitledBorder.TOP, F_TITULO));

        modeloListaAcademias = new DefaultListModel<>();
        listaAcademias = new JList<>(modeloListaAcademias);
        listaAcademias.setFont(F_PEQUEÑA);
        listaAcademias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel formPanel = new JPanel(new BorderLayout(4, 4));
        formPanel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));

        JLabel lbl = new JLabel("Nombre:");
        lbl.setFont(F_PEQUEÑA);
        txtNombreAcademia = new JTextField();
        txtNombreAcademia.setFont(F_PEQUEÑA);

        JPanel fieldRow = new JPanel(new BorderLayout(4, 0));
        fieldRow.add(lbl, BorderLayout.WEST);
        fieldRow.add(txtNombreAcademia, BorderLayout.CENTER);

        btnAgregarAcademia  = new JButton("Agregar");
        btnEditarAcademia   = new JButton("Actualizar");
        btnEliminarAcademia = new JButton("Eliminar");
        for (JButton b : new JButton[]{btnAgregarAcademia, btnEditarAcademia, btnEliminarAcademia})
            b.setFont(F_PEQUEÑA);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        btnRow.add(btnAgregarAcademia);
        btnRow.add(btnEditarAcademia);
        btnRow.add(btnEliminarAcademia);

        formPanel.add(fieldRow, BorderLayout.NORTH);
        formPanel.add(btnRow,   BorderLayout.CENTER);

        panel.add(new JScrollPane(listaAcademias), BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.SOUTH);

        cargarAcademias();
        return panel;
    }

    private JPanel crearPanelAsignaturasAdmin() {
        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Asignaturas", TitledBorder.LEFT,
            TitledBorder.TOP, F_TITULO));

        modeloListaAsigAdmin = new DefaultListModel<>();
        listaAsigAdmin = new JList<>(modeloListaAsigAdmin);
        listaAsigAdmin.setFont(F_PEQUEÑA);
        listaAsigAdmin.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 4, 4));
        formPanel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));

        JLabel lblNom = new JLabel("Nombre:");
        lblNom.setFont(F_PEQUEÑA);
        txtNombreAsig = new JTextField();
        txtNombreAsig.setFont(F_PEQUEÑA);

        JLabel lblAcad = new JLabel("Academia:");
        lblAcad.setFont(F_PEQUEÑA);
        comboAcademiaAsig = new JComboBox<>();
        comboAcademiaAsig.setFont(F_PEQUEÑA);
        for (int i = 0; i < modeloListaAcademias.getSize(); i++)
            comboAcademiaAsig.addItem(modeloListaAcademias.getElementAt(i));

        btnAgregarAsig  = new JButton("Agregar");
        btnEditarAsig   = new JButton("Actualizar");
        btnEliminarAsig = new JButton("Eliminar");
        for (JButton b : new JButton[]{btnAgregarAsig, btnEditarAsig, btnEliminarAsig})
            b.setFont(F_PEQUEÑA);

        formPanel.add(lblNom);   formPanel.add(txtNombreAsig);
        formPanel.add(lblAcad);  formPanel.add(comboAcademiaAsig);
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        btnRow.add(btnAgregarAsig); btnRow.add(btnEditarAsig); btnRow.add(btnEliminarAsig);
        formPanel.add(new JLabel());
        formPanel.add(btnRow);

        cargarAsignaturasAdmin();

        panel.add(new JScrollPane(listaAsigAdmin), BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void registrarListenersAdmin() {
        listaAcademias.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            Academia sel = listaAcademias.getSelectedValue();
            if (sel != null) txtNombreAcademia.setText(sel.getNombre());
        });

        listaAsigAdmin.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            AsignaturaAdmin sel = listaAsigAdmin.getSelectedValue();
            if (sel != null) {
                txtNombreAsig.setText(sel.getNombre());
                for (int i = 0; i < comboAcademiaAsig.getItemCount(); i++) {
                    if (comboAcademiaAsig.getItemAt(i).getNombre().equals(sel.getAcademia())) {
                        comboAcademiaAsig.setSelectedIndex(i); break;
                    }
                }
            }
        });

        btnAgregarAcademia.addActionListener(e -> {
            String nombre = txtNombreAcademia.getText().trim();
            if (nombre.isEmpty()) { mostrarError("Ingrese un nombre."); return; }
            Academia nueva = new Academia(nombre);
            if (modeloListaAcademias.contains(nueva)) { mostrarError("La academia ya existe."); return; }
            modeloListaAcademias.addElement(nueva);
            comboAcademiaAsig.addItem(nueva);
            guardarAcademias();
            txtNombreAcademia.setText("");
        });

        btnEditarAcademia.addActionListener(e -> {
            int idx = listaAcademias.getSelectedIndex();
            if (idx < 0) { mostrarError("Seleccione una academia."); return; }
            String nombre = txtNombreAcademia.getText().trim();
            if (nombre.isEmpty()) { mostrarError("Ingrese un nombre."); return; }
            Academia vieja = modeloListaAcademias.getElementAt(idx);
            Academia nueva = new Academia(nombre);
            modeloListaAcademias.setElementAt(nueva, idx);
         
            for (int i = 0; i < comboAcademiaAsig.getItemCount(); i++)
                if (comboAcademiaAsig.getItemAt(i).equals(vieja)) {
                    comboAcademiaAsig.removeItemAt(i);
                    comboAcademiaAsig.insertItemAt(nueva, i);
                    break;
                }
            guardarAcademias();
        });

        btnEliminarAcademia.addActionListener(e -> {
            int idx = listaAcademias.getSelectedIndex();
            if (idx < 0) { mostrarError("Seleccione una academia."); return; }
            Academia a = modeloListaAcademias.getElementAt(idx);
            modeloListaAcademias.removeElementAt(idx);
            comboAcademiaAsig.removeItem(a);
            guardarAcademias();
            txtNombreAcademia.setText("");
        });

        btnAgregarAsig.addActionListener(e -> {
            String nombre = txtNombreAsig.getText().trim();
            Academia acad = (Academia) comboAcademiaAsig.getSelectedItem();
            if (nombre.isEmpty()) { mostrarError("Ingrese un nombre."); return; }
            if (acad == null) { mostrarError("Seleccione una academia."); return; }
            AsignaturaAdmin nueva = new AsignaturaAdmin(nombre, acad.getNombre());
            if (modeloListaAsigAdmin.contains(nueva)) { mostrarError("La asignatura ya existe."); return; }
            modeloListaAsigAdmin.addElement(nueva);
            guardarAsignaturasAdmin();
            txtNombreAsig.setText("");
        });

        btnEditarAsig.addActionListener(e -> {
            int idx = listaAsigAdmin.getSelectedIndex();
            if (idx < 0) { mostrarError("Seleccione una asignatura."); return; }
            String nombre = txtNombreAsig.getText().trim();
            Academia acad = (Academia) comboAcademiaAsig.getSelectedItem();
            if (nombre.isEmpty()) { mostrarError("Ingrese un nombre."); return; }
            if (acad == null) { mostrarError("Seleccione una academia."); return; }
            modeloListaAsigAdmin.setElementAt(
                new AsignaturaAdmin(nombre, acad.getNombre()), idx);
            guardarAsignaturasAdmin();
        });

        btnEliminarAsig.addActionListener(e -> {
            int idx = listaAsigAdmin.getSelectedIndex();
            if (idx < 0) { mostrarError("Seleccione una asignatura."); return; }
            modeloListaAsigAdmin.removeElementAt(idx);
            guardarAsignaturasAdmin();
            txtNombreAsig.setText("");
        });
    }

    private void cargarAcademias() {
        modeloListaAcademias.clear();
        for (Academia a : LibreriaAdmin.leerAcademias())
            modeloListaAcademias.addElement(a);
    }

    private void cargarAsignaturasAdmin() {
        modeloListaAsigAdmin.clear();
        for (AsignaturaAdmin a : LibreriaAdmin.leerAsignaturas())
            modeloListaAsigAdmin.addElement(a);
    }

    private void guardarAcademias() {
        ArrayList<Academia> lista = new ArrayList<>();
        for (int i = 0; i < modeloListaAcademias.getSize(); i++)
            lista.add(modeloListaAcademias.getElementAt(i));
        LibreriaAdmin.guardarAcademias(lista);
    }

    private void guardarAsignaturasAdmin() {
        ArrayList<AsignaturaAdmin> lista = new ArrayList<>();
        for (int i = 0; i < modeloListaAsigAdmin.getSize(); i++)
            lista.add(modeloListaAsigAdmin.getElementAt(i));
        LibreriaAdmin.guardarAsignaturas(lista);
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    public void actualizarTabla(DefaultTableModel modelo, String titulo) {
        tabla.setModel(modelo);
        ajustarColumnas();
        txtBuscar.setText("");
        lblTitulo.setText(titulo);
        actualizarContador();
    }

    private void ajustarColumnas() {
        TableColumnModel tcm = tabla.getColumnModel();
        int[] anchos = {120, 230, 55, 180, 200, 55, 60, 100, 230, 80, 55, 55, 60, 110};
        for (int i = 0; i < tcm.getColumnCount() && i < anchos.length; i++)
            tcm.getColumn(i).setPreferredWidth(anchos[i]);
    }

    private void ajustarColumnasTabla(JTable t, int[] anchos) {
        TableColumnModel tcm = t.getColumnModel();
        for (int i = 0; i < tcm.getColumnCount() && i < anchos.length; i++)
            tcm.getColumn(i).setPreferredWidth(anchos[i]);
    }

    private void aplicarFiltroTexto() {
        if (modeloTabla == null || tabla.getModel() == null) return;
        String texto = txtBuscar.getText().trim().toLowerCase();
        String col   = (String) comboFiltro.getSelectedItem();

        DefaultTableModel base = modeloTabla;
        if (texto.isEmpty()) { tabla.setModel(base); actualizarContador(); return; }

        int idxCol = -1;
        for (int i = 0; i < base.getColumnCount(); i++)
            if (base.getColumnName(i).equalsIgnoreCase(col)) { idxCol = i; break; }
        if (idxCol == -1) return;

        DefaultTableModel filtrado = new DefaultTableModel(obtenerNombresColumnas(base), 0);
        for (int r = 0; r < base.getRowCount(); r++) {
            String val = String.valueOf(base.getValueAt(r, idxCol)).toLowerCase();
            if (val.contains(texto)) {
                Object[] fila = new Object[base.getColumnCount()];
                for (int c = 0; c < fila.length; c++) fila[c] = base.getValueAt(r, c);
                filtrado.addRow(fila);
            }
        }
        tabla.setModel(filtrado);
        actualizarContador();
    }

    private String[] obtenerNombresColumnas(DefaultTableModel m) {
        String[] cols = new String[m.getColumnCount()];
        for (int i = 0; i < cols.length; i++) cols[i] = m.getColumnName(i);
        return cols;
    }

    private void actualizarContador() {
        int filas = tabla.getModel().getRowCount();
        lblContador.setText(filas + " registro" + (filas != 1 ? "s" : ""));
    }

    private void mostrarTodos() {
        if (modeloDatos == null) return;
        modeloTabla = modeloDatos.getTablaTodos();
        actualizarTabla(modeloTabla, "Todos los registros");
        listaCarrera.clearSelection();
        listaMateria.clearSelection();
        listaAsignatura.clearSelection();
    }

    private void exportarTabla() {
        DefaultTableModel m = modeloTabla;
        if (m == null || m.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No hay datos para exportar.");
            return;
        }
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new java.io.File("exportacion.csv"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (java.io.PrintWriter pw = new java.io.PrintWriter(fc.getSelectedFile(), "UTF-8")) {
                StringBuilder sb = new StringBuilder();
                for (int c = 0; c < m.getColumnCount(); c++) {
                    if (c > 0) sb.append(",");
                    sb.append(m.getColumnName(c));
                }
                pw.println(sb);
                for (int r = 0; r < m.getRowCount(); r++) {
                    sb.setLength(0);
                    for (int c = 0; c < m.getColumnCount(); c++) {
                        if (c > 0) sb.append(",");
                        sb.append(m.getValueAt(r, c));
                    }
                    pw.println(sb);
                }
                JOptionPane.showMessageDialog(this, "Archivo exportado correctamente.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage());
            }
        }
    }

	public JList<Carreras> getListacarrera() {
		if (listaCarrera == null)
			listaCarrera = new JList<>();
		return listaCarrera;
	}

	public JList<Materias> getListamateria() {
		if (listaMateria == null)
			listaMateria = new JList<>();
		return listaMateria;
	}

	public JList<Asignaturas> getListaAsignatura() {
		if (listaAsignatura == null)
			listaAsignatura = new JList<>();
		return listaAsignatura;
	}

	public void setModeloDatos(ModeloTabla m) {
		this.modeloDatos = m;
	}

	public void setModeloTablaActual(DefaultTableModel m) {
		this.modeloTabla = m;
	}
}
