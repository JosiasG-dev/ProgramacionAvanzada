package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Ventana interna — Gestión de Productos.
 * Formulario para crear y editar productos.
 */
public class Productos extends JInternalFrame {

    private JTextField tfCodigo;
    private JTextField tfNombre;
    private JTextField tfPrecio;
    private JTextField tfCantidad;
    private JComboBox<String> cbCategoria;
    private JTextField tfImagenUrl;
    private JTextArea  taDescripcion;

    private JButton btnGuardar;
    private JButton btnLimpiar;
    private JButton btnCancelar;

    // Panel de previsualización
    private JLabel lblImagen;
    private JLabel lblTipoAlm;

    private static final String[] CATEGORIAS = {
        "Abarrotes", "Bebidas", "Lácteos y Huevo", "Frutas y Verduras",
        "Carnes y Pescados", "Salchichonería", "Panadería y Tortillería",
        "Limpieza del Hogar", "Cuidado Personal", "Snacks y Dulcería", "Mascotas"
    };

    private static final java.util.Map<String, String> TIPO_ALM = new java.util.LinkedHashMap<>();
    static {
        TIPO_ALM.put("Abarrotes",               "No perecedero (Anaquel)");
        TIPO_ALM.put("Bebidas",                 "Líquidos / Pesado");
        TIPO_ALM.put("Lácteos y Huevo",         "Cadena de Frío");
        TIPO_ALM.put("Frutas y Verduras",       "Perecedero (Peso variable)");
        TIPO_ALM.put("Carnes y Pescados",       "Congelados / Frescos");
        TIPO_ALM.put("Salchichonería",          "Refrigerados");
        TIPO_ALM.put("Panadería y Tortillería", "Alta rotación diaria");
        TIPO_ALM.put("Limpieza del Hogar",      "Químicos (Aislados)");
        TIPO_ALM.put("Cuidado Personal",        "Higiene");
        TIPO_ALM.put("Snacks y Dulcería",       "Compra por impulso");
        TIPO_ALM.put("Mascotas",                "Volumen / Peso");
    }

    public Productos(ArrayList<String> categorias) {
        super("🛒 Gestión de Productos", true, true, true, true);
        setSize(700, 500);
        setLocation(60, 60);
        construir();
    }

    private void construir() {
        setLayout(new BorderLayout(8, 8));
        ((JComponent) getContentPane()).setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(panelFormulario(), BorderLayout.CENTER);
        add(panelLateral(),    BorderLayout.EAST);
        add(panelBotones(),    BorderLayout.SOUTH);
    }

    // ── Formulario ────────────────────────────────────────────────────────
    private JPanel panelFormulario() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("Datos del Producto"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);
        g.anchor = GridBagConstraints.WEST;

        // Fila 0: Código / Categoría
        fila(p, g, 0, "Código *",    tfCodigo    = new JTextField(12));
        fila(p, g, 1, "Nombre *",    tfNombre    = new JTextField(25));
        fila(p, g, 2, "Precio *",    tfPrecio    = new JTextField(10));
        fila(p, g, 3, "Cantidad *",  tfCantidad  = new JTextField(10));

        // Categoría con combo
        g.gridx = 0; g.gridy = 4; p.add(new JLabel("Categoría *"), g);
        g.gridx = 1; g.gridy = 4; g.fill = GridBagConstraints.HORIZONTAL;
        cbCategoria = new JComboBox<>(CATEGORIAS);
        cbCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cbCategoria.addActionListener(e -> actualizarTipoAlm());
        p.add(cbCategoria, g);
        g.fill = GridBagConstraints.NONE;

        // URL Imagen
        fila(p, g, 5, "URL Imagen",  tfImagenUrl = new JTextField(35));
        tfImagenUrl.addActionListener(e -> previewImagen());

        // Descripción
        g.gridx = 0; g.gridy = 6; p.add(new JLabel("Descripción"), g);
        g.gridx = 1; g.gridy = 6; g.fill = GridBagConstraints.BOTH;
        taDescripcion = new JTextArea(3, 35);
        taDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        taDescripcion.setLineWrap(true);
        taDescripcion.setWrapStyleWord(true);
        p.add(new JScrollPane(taDescripcion), g);
        g.fill = GridBagConstraints.NONE;

        return p;
    }

    private void fila(JPanel p, GridBagConstraints g, int row, String label, JTextField tf) {
        g.gridx = 0; g.gridy = row; p.add(new JLabel(label), g);
        g.gridx = 1; g.gridy = row; g.fill = GridBagConstraints.HORIZONTAL;
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        p.add(tf, g);
        g.fill = GridBagConstraints.NONE;
    }

    // ── Panel lateral (previsualización) ──────────────────────────────────
    private JPanel panelLateral() {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBorder(BorderFactory.createTitledBorder("Vista previa"));
        p.setPreferredSize(new Dimension(180, 0));

        lblImagen = new JLabel("Sin imagen", SwingConstants.CENTER);
        lblImagen.setPreferredSize(new Dimension(160, 160));
        lblImagen.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        lblImagen.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblImagen.setForeground(Color.GRAY);

        lblTipoAlm = new JLabel("<html><center>Tipo:<br><b>"
                + TIPO_ALM.get(CATEGORIAS[0]) + "</b></center></html>",
                SwingConstants.CENTER);
        lblTipoAlm.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTipoAlm.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JButton btnCargarImg = new JButton("🔄 Cargar imagen");
        btnCargarImg.addActionListener(e -> previewImagen());

        p.add(lblImagen,    BorderLayout.CENTER);
        p.add(lblTipoAlm,   BorderLayout.NORTH);
        p.add(btnCargarImg, BorderLayout.SOUTH);
        return p;
    }

    // ── Botones ───────────────────────────────────────────────────────────
    private JPanel panelBotones() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 6));
        btnGuardar  = btn("💾 Guardar",   new Color(39, 174, 96));
        btnLimpiar  = btn("🔄 Limpiar",   new Color(100, 100, 100));
        btnCancelar = btn("✖ Cancelar",  new Color(192, 57, 43));
        p.add(btnGuardar);
        p.add(btnLimpiar);
        p.add(btnCancelar);
        return p;
    }

    private JButton btn(String texto, Color bg) {
        JButton b = new JButton(texto);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setPreferredSize(new Dimension(140, 34));
        return b;
    }

    // ── Lógica interna ────────────────────────────────────────────────────
    private void actualizarTipoAlm() {
        String cat = (String) cbCategoria.getSelectedItem();
        String tipo = TIPO_ALM.getOrDefault(cat, "");
        lblTipoAlm.setText("<html><center>Tipo:<br><b>" + tipo + "</b></center></html>");
    }

    private void previewImagen() {
        String url = tfImagenUrl.getText().trim();
        if (url.isEmpty()) return;
        lblImagen.setText("Cargando…");
        lblImagen.setIcon(null);
        new Thread(() -> {
            try {
                java.net.URL u = new java.net.URL(url);
                ImageIcon icon = new ImageIcon(u);
                java.awt.Image img = icon.getImage()
                        .getScaledInstance(155, 155, java.awt.Image.SCALE_SMOOTH);
                SwingUtilities.invokeLater(() -> {
                    lblImagen.setIcon(new ImageIcon(img));
                    lblImagen.setText("");
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    lblImagen.setText("No se pudo cargar");
                    lblImagen.setIcon(null);
                });
            }
        }).start();
    }

    // ── API pública ───────────────────────────────────────────────────────
    public void limpiar() {
        tfCodigo.setText(""); tfCodigo.setEditable(true);
        tfNombre.setText("");
        tfPrecio.setText("");
        tfCantidad.setText("");
        cbCategoria.setSelectedIndex(0);
        tfImagenUrl.setText("");
        taDescripcion.setText("");
        lblImagen.setIcon(null);
        lblImagen.setText("Sin imagen");
        actualizarTipoAlm();
    }

    public void llenar(String cod, String nom, double precio, int cant,
                       String cat, String url, String desc) {
        tfCodigo.setText(cod);    tfCodigo.setEditable(false);
        tfNombre.setText(nom);
        tfPrecio.setText(String.valueOf(precio));
        tfCantidad.setText(String.valueOf(cant));
        cbCategoria.setSelectedItem(cat);
        tfImagenUrl.setText(url);
        taDescripcion.setText(desc);
        actualizarTipoAlm();
        if (!url.isEmpty()) previewImagen();
    }

    public String getCodigo()      { return tfCodigo.getText(); }
    public String getNombreP()     { return tfNombre.getText(); }
    public String getPrecio()      { return tfPrecio.getText(); }
    public String getCantidad()    { return tfCantidad.getText(); }
    public String getCategoria()   { return (String) cbCategoria.getSelectedItem(); }
    public String getImagenUrl()   { return tfImagenUrl.getText(); }
    public String getDescripcion() { return taDescripcion.getText(); }

    public void addBtnGuardar(ActionListener al)  { btnGuardar.addActionListener(al); }
    public void addBtnLimpiar(ActionListener al)  { btnLimpiar.addActionListener(al); }
    public void addBtnCancelar(ActionListener al) { btnCancelar.addActionListener(al); }
}
