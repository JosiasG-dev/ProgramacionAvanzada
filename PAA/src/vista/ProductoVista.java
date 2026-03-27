package vista;

import controlador.ProductoControlador;
import controlador.UnidadControlador;
import modelo.Producto;
import modelo.UnidadMedida;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.util.List;

/**
 * VISTA - Gestión de Productos
 * Carga imágenes desde src/images si existen, o desde URL al descargar.
 */
public class ProductoVista extends JPanel {

    private final ProductoControlador ctrl;
    private final UnidadControlador   unidCtrl;

    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;
    private JComboBox<String> cmbCategoria;

    // Formulario
    private JLabel      lblImagen;
    private JTextField  txtNombre, txtPrecioCompra, txtGanancia, txtCantidad,
                        txtImagenUrl, txtCodigo, txtImagenLocal;
    private JTextArea   txtDescripcion;
    private JComboBox<String> cmbCategoriaProd, cmbUnidad;
    private JLabel      lblPrecioVenta;

    private List<Producto> productosVisibles;
    private int idEditando = 0;

    public ProductoVista(ProductoControlador ctrl, UnidadControlador unidCtrl) {
        this.ctrl     = ctrl;
        this.unidCtrl = unidCtrl;
        setLayout(new BorderLayout());
        construirUI();
        cargarProductos(null, null);
    }

    // ===================== UI =====================
    private void construirUI() {
        JLabel titulo = new JLabel("  Gestión de Productos");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 15));
        titulo.setBorder(BorderFactory.createEmptyBorder(6, 4, 6, 4));
        add(titulo, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            crearPanelLista(), crearPanelFormulario());
        split.setDividerLocation(600);
        split.setResizeWeight(0.56);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);
    }

    private JPanel crearPanelLista() {
        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 4));

        // Filtros
        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        txtBuscar = new JTextField(14);
        List<String> cats = ctrl.obtenerCategorias();
        cmbCategoria = new JComboBox<>();
        cmbCategoria.addItem("— Todas —");
        cats.forEach(cmbCategoria::addItem);
        JButton btnFiltrar = new JButton("Filtrar");
        JButton btnLimpiar = new JButton("Limpiar");
        btnFiltrar.addActionListener(e -> aplicarFiltro());
        btnLimpiar.addActionListener(e -> { txtBuscar.setText(""); cmbCategoria.setSelectedIndex(0); cargarProductos(null, null); });
        filtros.add(new JLabel("Buscar:"));
        filtros.add(txtBuscar);
        filtros.add(cmbCategoria);
        filtros.add(btnFiltrar);
        filtros.add(btnLimpiar);

        // Tabla
        String[] cols = {"ID", "Nombre", "Categoría", "Compra", "Gan%", "Venta", "Stock", "Unidad"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(22);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getColumnModel().getColumn(0).setMaxWidth(40);
        tabla.getColumnModel().getColumn(4).setMaxWidth(55);
        tabla.getColumnModel().getColumn(6).setMaxWidth(50);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarEnFormulario();
        });

        // Botones CRUD
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        JButton btnNuevo    = new JButton("Nuevo");
        JButton btnEliminar = new JButton("Eliminar");
        btnNuevo.addActionListener(e    -> limpiarFormulario());
        btnEliminar.addActionListener(e -> eliminarSeleccionado());
        btns.add(btnNuevo);
        btns.add(btnEliminar);

        panel.add(filtros, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        panel.add(btns, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        JLabel tituloForm = new JLabel("Detalle / Editar Producto");
        tituloForm.setFont(new Font("SansSerif", Font.BOLD, 13));
        panel.add(tituloForm, BorderLayout.NORTH);

        // Imagen
        lblImagen = new JLabel("Sin imagen", SwingConstants.CENTER);
        lblImagen.setPreferredSize(new Dimension(170, 130));
        lblImagen.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        lblImagen.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblImagen.setForeground(Color.GRAY);
        JPanel panelImg = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelImg.add(lblImagen);

        // Formulario
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 4, 4, 4);

        txtNombre       = new JTextField();
        txtPrecioCompra = new JTextField();
        txtGanancia     = new JTextField();
        txtCantidad     = new JTextField();
        txtImagenUrl    = new JTextField();
        txtImagenLocal  = new JTextField();
        txtCodigo       = new JTextField();
        lblPrecioVenta  = new JLabel("$ —");
        lblPrecioVenta.setFont(new Font("SansSerif", Font.BOLD, 16));
        txtDescripcion  = new JTextArea(2, 18);
        txtDescripcion.setLineWrap(true);

        String[] catArr = {
            "Despensa Básica","Lácteos y Huevo","Bebidas y Líquidos",
            "Botanas y Dulces","Frutas y Verduras","Carnes y Salchichonería",
            "Cuidado del Hogar","Higiene y Cuidado Personal","Alimentos Preparados"
        };
        cmbCategoriaProd = new JComboBox<>(catArr);

        List<UnidadMedida> unidades = unidCtrl.obtenerUnidades();
        String[] unidArr = unidades.stream().map(UnidadMedida::getAbreviatura).toArray(String[]::new);
        cmbUnidad = new JComboBox<>(unidArr);

        // Recalcular precio en tiempo real
        java.awt.event.FocusAdapter recalc = new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent e) { recalcularPrecio(); }
        };
        txtPrecioCompra.addFocusListener(recalc);
        txtGanancia.addFocusListener(recalc);

        int r = 0;
        addRow(form, gbc, r++, "Nombre:",              txtNombre);
        addRow(form, gbc, r++, "Categoría:",           cmbCategoriaProd);
        addRow(form, gbc, r++, "Código barras:",       txtCodigo);
        addRow(form, gbc, r++, "Precio compra $:",     txtPrecioCompra);
        addRow(form, gbc, r++, "% Ganancia:",          txtGanancia);
        addRow(form, gbc, r++, "Precio venta:",        lblPrecioVenta);
        addRow(form, gbc, r++, "Stock:",               txtCantidad);
        addRow(form, gbc, r++, "Unidad:",              cmbUnidad);
        addRow(form, gbc, r++, "URL Imagen:",          txtImagenUrl);
        addRow(form, gbc, r++, "Imagen local (src/images/):", txtImagenLocal);

        // Botón descargar imagen desde URL
        JButton btnDescImg = new JButton("Descargar imagen de URL");
        gbc.gridx = 0; gbc.gridy = r; gbc.gridwidth = 2;
        form.add(btnDescImg, gbc); r++;
        gbc.gridwidth = 1;

        // Botón cargar imagen local
        JButton btnCargarLocal = new JButton("Cargar imagen local");
        gbc.gridx = 0; gbc.gridy = r; gbc.gridwidth = 2;
        form.add(btnCargarLocal, gbc); r++;
        gbc.gridwidth = 1;

        addRow(form, gbc, r++, "Descripción:", new JScrollPane(txtDescripcion));

        btnDescImg.addActionListener(e -> descargarImagenURL());
        btnCargarLocal.addActionListener(e -> cargarImagenLocal());

        // Botones guardar/limpiar
        JButton btnGuardar = new JButton("Guardar");
        JButton btnLimpiar = new JButton("Limpiar");
        btnGuardar.addActionListener(e -> guardar());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        btns.add(btnLimpiar);
        btns.add(btnGuardar);

        JPanel centro = new JPanel(new BorderLayout());
        centro.add(panelImg, BorderLayout.NORTH);
        centro.add(new JScrollPane(form), BorderLayout.CENTER);
        centro.add(btns, BorderLayout.SOUTH);

        panel.add(centro, BorderLayout.CENTER);
        return panel;
    }

    private void addRow(JPanel form, GridBagConstraints gbc, int fila, String label, JComponent campo) {
        gbc.gridx = 0; gbc.gridy = fila; gbc.gridwidth = 1; gbc.weightx = 0.35;
        form.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 0.65;
        form.add(campo, gbc);
    }

    // ===================== LÓGICA =====================
    private void cargarProductos(String texto, String categoria) {
        productosVisibles = ctrl.obtenerTodosLosProductos();
        if (texto != null && !texto.isBlank()) {
            final String t = texto.toLowerCase();
            productosVisibles = productosVisibles.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(t))
                .collect(java.util.stream.Collectors.toList());
        }
        if (categoria != null && !categoria.startsWith("—")) {
            productosVisibles = productosVisibles.stream()
                .filter(p -> p.getCategoria().equals(categoria))
                .collect(java.util.stream.Collectors.toList());
        }
        modeloTabla.setRowCount(0);
        for (Producto p : productosVisibles) {
            modeloTabla.addRow(new Object[]{
                p.getId(), p.getNombre(), p.getCategoria(),
                "$" + p.getPrecioCompra(), p.getPorcentajeGanancia() + "%",
                "$" + p.getPrecioVenta(), p.getCantidadAlmacen(), p.getUnidad()
            });
        }
    }

    private void aplicarFiltro() {
        cargarProductos(txtBuscar.getText(), (String) cmbCategoria.getSelectedItem());
    }

    private void cargarEnFormulario() {
        int fila = tabla.getSelectedRow();
        if (fila < 0 || fila >= productosVisibles.size()) return;
        Producto p = productosVisibles.get(fila);
        idEditando = p.getId();
        txtNombre.setText(p.getNombre());
        txtPrecioCompra.setText(p.getPrecioCompra().toPlainString());
        txtGanancia.setText(String.valueOf(p.getPorcentajeGanancia()));
        txtCantidad.setText(String.valueOf(p.getCantidadAlmacen()));
        txtImagenUrl.setText(p.getImagenUrl() != null ? p.getImagenUrl() : "");
        txtImagenLocal.setText(p.getImagenLocal() != null ? p.getImagenLocal() : "");
        txtCodigo.setText(p.getCodigoBarras() != null ? p.getCodigoBarras() : "");
        txtDescripcion.setText(p.getDescripcion() != null ? p.getDescripcion() : "");
        lblPrecioVenta.setText("$ " + p.getPrecioVenta().toPlainString());

        for (int i = 0; i < cmbCategoriaProd.getItemCount(); i++) {
            if (cmbCategoriaProd.getItemAt(i).equals(p.getCategoria())) {
                cmbCategoriaProd.setSelectedIndex(i); break;
            }
        }
        for (int i = 0; i < cmbUnidad.getItemCount(); i++) {
            if (cmbUnidad.getItemAt(i).equals(p.getUnidad())) {
                cmbUnidad.setSelectedIndex(i); break;
            }
        }

        // Mostrar imagen: primero intenta local, luego sin imagen
        mostrarImagen(p);
    }

    /** Intenta mostrar imagen del producto: local primero, luego placeholder */
    private void mostrarImagen(Producto p) {
        lblImagen.setIcon(null);
        lblImagen.setText("Sin imagen");

        // 1. ¿Hay ruta local guardada en el modelo?
        if (p.getImagenLocal() != null && !p.getImagenLocal().isBlank()) {
            File f = new File(p.getImagenLocal());
            if (!f.isAbsolute()) f = new File("src/images/" + p.getImagenLocal());
            if (mostrarArchivoImagen(f)) return;
        }

        // 2. ¿Hay archivo en src/images con el nombre del producto?
        String nombre = p.getNombre().replaceAll("\\s+", "_").replaceAll("[^a-zA-Z0-9_]", "");
        File f2 = new File("src/images/" + nombre + ".jpg");
        if (mostrarArchivoImagen(f2)) return;

        // 3. Sin imagen disponible
        lblImagen.setText("Sin imagen");
    }

    private boolean mostrarArchivoImagen(File f) {
        if (f.exists() && f.isFile()) {
            try {
                BufferedImage img = ImageIO.read(f);
                if (img != null) {
                    ImageIcon icon = new ImageIcon(img.getScaledInstance(160, 120, Image.SCALE_SMOOTH));
                    lblImagen.setIcon(icon);
                    lblImagen.setText("");
                    return true;
                }
            } catch (Exception ignored) {}
        }
        return false;
    }

    private void limpiarFormulario() {
        idEditando = 0;
        txtNombre.setText(""); txtPrecioCompra.setText(""); txtGanancia.setText("");
        txtCantidad.setText(""); txtImagenUrl.setText(""); txtImagenLocal.setText("");
        txtCodigo.setText(""); txtDescripcion.setText("");
        lblPrecioVenta.setText("$ —");
        lblImagen.setIcon(null); lblImagen.setText("Sin imagen");
        tabla.clearSelection();
    }

    private void recalcularPrecio() {
        try {
            BigDecimal compra = new BigDecimal(txtPrecioCompra.getText().trim());
            double ganancia = Double.parseDouble(txtGanancia.getText().trim());
            Producto temp = new Producto();
            temp.setPrecioCompra(compra);
            temp.setPorcentajeGanancia(ganancia);
            temp.calcularPrecioVenta();
            lblPrecioVenta.setText("$ " + temp.getPrecioVenta().toPlainString());
        } catch (Exception ignored) {
            lblPrecioVenta.setText("$ —");
        }
    }

    private void guardar() {
        Producto p = new Producto();
        p.setId(idEditando);
        p.setNombre(txtNombre.getText().trim());
        p.setCategoria((String) cmbCategoriaProd.getSelectedItem());
        p.setUnidad((String) cmbUnidad.getSelectedItem());
        p.setImagenUrl(txtImagenUrl.getText().trim());
        p.setImagenLocal(txtImagenLocal.getText().trim());
        p.setCodigoBarras(txtCodigo.getText().trim());
        p.setDescripcion(txtDescripcion.getText().trim());
        try {
            p.setPrecioCompra(new BigDecimal(txtPrecioCompra.getText().trim()));
            p.setPorcentajeGanancia(Double.parseDouble(txtGanancia.getText().trim()));
            p.setCantidadAlmacen(Integer.parseInt(txtCantidad.getText().trim()));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio, ganancia y stock deben ser números.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (ctrl.guardarProducto(p)) {
            cargarProductos(null, null);
            limpiarFormulario();
            JOptionPane.showMessageDialog(this, "Producto guardado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione un producto."); return; }
        Producto p = productosVisibles.get(fila);
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar \"" + p.getNombre() + "\"?",
                "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            ctrl.eliminarProducto(p.getId());
            cargarProductos(null, null);
            limpiarFormulario();
        }
    }

    /** Descarga imagen desde URL y la guarda en src/images/ */
    private void descargarImagenURL() {
        String url = txtImagenUrl.getText().trim();
        if (url.isBlank()) {
            JOptionPane.showMessageDialog(this, "Ingrese la URL de la imagen.", "URL vacía", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nombre = txtNombre.getText().trim().replaceAll("\\s+", "_").replaceAll("[^a-zA-Z0-9_]", "");
        if (nombre.isBlank()) nombre = "producto_" + System.currentTimeMillis();
        lblImagen.setText("Descargando...");
        ctrl.descargarImagen(url, nombre, new ProductoControlador.ImageDescargadaCallback() {
            @Override
            public void onDescargada(String ruta, BufferedImage img) {
                ImageIcon icon = new ImageIcon(img.getScaledInstance(160, 120, Image.SCALE_SMOOTH));
                lblImagen.setIcon(icon);
                lblImagen.setText("");
                txtImagenLocal.setText(ruta);
                JOptionPane.showMessageDialog(ProductoVista.this,
                    "Imagen guardada en: " + ruta, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
            @Override
            public void onError(String msg) {
                lblImagen.setText("Error");
                JOptionPane.showMessageDialog(ProductoVista.this,
                    "No se pudo descargar:\n" + msg, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /** Permite seleccionar una imagen del sistema de archivos y la copia a src/images/ */
    private void cargarImagenLocal() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Seleccionar imagen");
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Imágenes (jpg, png, gif)", "jpg", "jpeg", "png", "gif"));
        // Sugiere empezar en src/images
        File imgDir = new File("src/images");
        if (imgDir.exists()) fc.setCurrentDirectory(imgDir);

        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File seleccionado = fc.getSelectedFile();
            try {
                // Copiar a src/images con nombre normalizado
                String nombre = txtNombre.getText().trim().replaceAll("\\s+", "_")
                    .replaceAll("[^a-zA-Z0-9_]", "");
                if (nombre.isBlank()) nombre = "producto_" + System.currentTimeMillis();
                String ext = seleccionado.getName().contains(".")
                    ? seleccionado.getName().substring(seleccionado.getName().lastIndexOf('.'))
                    : ".jpg";
                File destino = new File("src/images/" + nombre + ext);
                destino.getParentFile().mkdirs();
                java.nio.file.Files.copy(seleccionado.toPath(), destino.toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                BufferedImage img = ImageIO.read(destino);
                if (img != null) {
                    lblImagen.setIcon(new ImageIcon(img.getScaledInstance(160, 120, Image.SCALE_SMOOTH)));
                    lblImagen.setText("");
                }
                txtImagenLocal.setText(destino.getPath());
                JOptionPane.showMessageDialog(this, "Imagen guardada en: " + destino.getPath(),
                    "Imagen cargada", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar imagen: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
