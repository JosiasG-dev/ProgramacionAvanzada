package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Ventana interna — Inventario.
 * Muestra la tabla de productos con filtros y acciones CRUD.
 */
public class Inventario extends JInternalFrame {

    // ── Filtros ──────────────────────────────────────────────────────────
    private JTextField cajaBuscarId;
    private JTextField cajaBuscarNombre;
    private JComboBox<String> comboCat;
    private JComboBox<String> comboEstado;

    // ── Tabla ────────────────────────────────────────────────────────────
    private JTable tabla;
    private DefaultTableModel modelo;

    // ── Botones ──────────────────────────────────────────────────────────
    private JButton btnBuscar;
    private JButton btnLimpiar;
    private JButton btnCrear;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnImagen;

    // ── Pie ──────────────────────────────────────────────────────────────
    private JLabel lblTotal;

    public Inventario(ArrayList<String> categorias) {
        super("📦 Inventario de Productos", true, true, true, true);
        setSize(980, 560);
        setLocation(10, 10);
        construir(categorias);
    }

    private void construir(ArrayList<String> categorias) {
        setLayout(new BorderLayout(8, 8));
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createEmptyBorder(8, 8, 8, 8));

        add(panelFiltros(categorias), BorderLayout.NORTH);
        add(panelTabla(),             BorderLayout.CENTER);
        add(panelBotones(),           BorderLayout.EAST);
        add(panelPie(),               BorderLayout.SOUTH);
    }

    // ── Panel filtros ─────────────────────────────────────────────────────
    private JPanel panelFiltros(ArrayList<String> categorias) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        p.setBorder(BorderFactory.createTitledBorder("🔍 Filtros"));

        cajaBuscarId     = campo(p, "Código:",    120);
        cajaBuscarNombre = campo(p, "Nombre:",    180);

        p.add(new JLabel("Categoría:"));
        String[] cats = new String[categorias.size() + 1];
        cats[0] = "Todos";
        for (int i = 0; i < categorias.size(); i++) cats[i+1] = categorias.get(i);
        comboCat = new JComboBox<>(cats);
        comboCat.setPreferredSize(new Dimension(180, 26));
        p.add(comboCat);

        p.add(new JLabel("Estado:"));
        comboEstado = new JComboBox<>(new String[]{"Todos","Stock bajo","Sin stock"});
        comboEstado.setPreferredSize(new Dimension(120, 26));
        p.add(comboEstado);

        btnBuscar  = boton("🔍 Buscar",  new Color(31, 78, 121));
        btnLimpiar = boton("🔄 Limpiar", new Color(100, 100, 100));
        p.add(btnBuscar);
        p.add(btnLimpiar);
        return p;
    }

    // ── Panel tabla ───────────────────────────────────────────────────────
    private JPanel panelTabla() {
        String[] cols = {"Código","Nombre","Precio","Cantidad","Categoría","Almacenamiento","Valor Total"};
        modelo = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setRowHeight(22);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setBackground(new Color(31, 78, 121));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setGridColor(new Color(220, 220, 220));

        // Anchos de columna
        int[] anchos = {80, 230, 80, 80, 160, 180, 90};
        for (int i = 0; i < anchos.length; i++)
            tabla.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);

        JPanel p = new JPanel(new BorderLayout());
        p.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return p;
    }

    // ── Panel botones laterales ───────────────────────────────────────────
    private JPanel panelBotones() {
        JPanel p = new JPanel(new GridLayout(6, 1, 0, 8));
        p.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
        btnCrear     = boton("➕ Nuevo",    new Color(39, 174, 96));
        btnModificar = boton("✏ Editar",   new Color(41, 128, 185));
        btnEliminar  = boton("🗑 Eliminar", new Color(192, 57, 43));
        btnImagen    = boton("🖼 Imagen",   new Color(142, 68, 173));
        p.add(btnCrear);
        p.add(btnModificar);
        p.add(btnEliminar);
        p.add(btnImagen);
        p.add(new JLabel()); // espacio
        p.add(new JLabel());
        return p;
    }

    // ── Panel pie ─────────────────────────────────────────────────────────
    private JPanel panelPie() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotal = new JLabel("Total: 0 registros");
        lblTotal.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        p.add(lblTotal);
        return p;
    }

    // ── Helpers ───────────────────────────────────────────────────────────
    private JTextField campo(JPanel p, String label, int ancho) {
        p.add(new JLabel(label));
        JTextField tf = new JTextField();
        tf.setPreferredSize(new Dimension(ancho, 26));
        p.add(tf);
        return tf;
    }

    private JButton boton(String texto, Color bg) {
        JButton b = new JButton(texto);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setPreferredSize(new Dimension(130, 32));
        return b;
    }

    // ── API pública ───────────────────────────────────────────────────────
    public void limpiarFiltros() {
        cajaBuscarId.setText("");
        cajaBuscarNombre.setText("");
        comboCat.setSelectedIndex(0);
        comboEstado.setSelectedIndex(0);
    }

    public void setTotal(int n) { lblTotal.setText("Total: " + n + " registros"); }

    public String getEstado() { return (String) comboEstado.getSelectedItem(); }

    public JTextField      getCajaBuscarId()     { return cajaBuscarId; }
    public JTextField      getCajaBuscarNombre() { return cajaBuscarNombre; }
    public JComboBox<String> getComboCat()       { return comboCat; }
    public JTable          getTabla()            { return tabla; }
    public DefaultTableModel getModelo()         { return modelo; }

    public void addBtnBuscar(ActionListener al)    { btnBuscar.addActionListener(al); }
    public void addBtnLimpiar(ActionListener al)   { btnLimpiar.addActionListener(al); }
    public void addBtnCrear(ActionListener al)     { btnCrear.addActionListener(al); }
    public void addBtnModificar(ActionListener al) { btnModificar.addActionListener(al); }
    public void addBtnEliminar(ActionListener al)  { btnEliminar.addActionListener(al); }
    public void addBtnImagen(ActionListener al)    { btnImagen.addActionListener(al); }
}
