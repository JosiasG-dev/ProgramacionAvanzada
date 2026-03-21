package controlador;

import archivo.ArchivoJSON;
import archivo.DatosIniciales;
import archivo.ReporteExcel;
import modelo.*;
import vista.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controlador principal MVC.
 * Gestiona el flujo entre la vista y el modelo.
 * Basado en el Controlador.java original, extendido para manejar
 * la jerarquía abstracta de productos, JSON y reportes Excel.
 */
public class Controlador {

    private final Principal    principal;
    private final Catalogo     catalogo;

    private Inventario   ventaInventario;
    private Productos    ventaProductos;
    private PuntoDeVenta ventaPuntoVenta;

    private static final double IVA = 0.16;
    private Ticket ultimoTicket;

    public Controlador(Principal p) {
        this.principal = p;
        this.catalogo  = new Catalogo();

        // ── Cargar datos ─────────────────────────────────────────────────
        ArrayList<Producto> cargados = ArchivoJSON.importarProductos();
        if (cargados.isEmpty()) {
            System.out.println("Cargando datos iniciales…");
            cargados = DatosIniciales.cargar();
            for (Producto prod : cargados) catalogo.insertar(prod);
            ArchivoJSON.exportarProductos(catalogo.getLista());
        } else {
            for (Producto prod : cargados) catalogo.insertar(prod);
        }

        // ── Listeners menú ───────────────────────────────────────────────
        p.addMenuInventarioListener(    e -> abrirInventario());
        p.addMenuProductosListener(     e -> abrirProductos());
        p.addMenuPuntoDeVentaListener(  e -> abrirPuntoVenta());
        p.addMenuReporteExcelListener(  e -> generarReportes());
        p.addMenuSalirListener(         e -> salir());
    }

    // ═════════════════════════════════════════════════════════════════════
    //  ABRIR VISTAS
    // ═════════════════════════════════════════════════════════════════════

    private void abrirInventario() {
        if (ventaInventario == null || ventaInventario.isClosed()) {
            ventaInventario = new Inventario(catalogo.categorias());
            engancharInventario();
            principal.getEscritorio().add(ventaInventario);
            refrescarInventario(null, null, null, "Todos");
        }
        mostrar(ventaInventario);
    }

    private void abrirProductos() {
        if (ventaProductos == null || ventaProductos.isClosed()) {
            ventaProductos = new Productos(catalogo.categorias());
            engancharProductos();
            principal.getEscritorio().add(ventaProductos);
        }
        mostrar(ventaProductos);
    }

    private void abrirPuntoVenta() {
        if (ventaPuntoVenta == null || ventaPuntoVenta.isClosed()) {
            ventaPuntoVenta = new PuntoDeVenta();
            engancharPuntoVenta();
            cargarCombo();
            principal.getEscritorio().add(ventaPuntoVenta);
        }
        mostrar(ventaPuntoVenta);
    }

    private void mostrar(JInternalFrame v) {
        v.setVisible(true);
        try { v.setSelected(true); } catch (Exception ignored) {}
    }

    // ═════════════════════════════════════════════════════════════════════
    //  INVENTARIO
    // ═════════════════════════════════════════════════════════════════════

    private void engancharInventario() {
        ventaInventario.addBtnBuscar(    e -> invBuscar());
        ventaInventario.addBtnLimpiar(   e -> { ventaInventario.limpiarFiltros();
                                                 refrescarInventario(null,null,null,"Todos"); });
        ventaInventario.addBtnCrear(     e -> abrirProductos());
        ventaInventario.addBtnModificar( e -> invModificar());
        ventaInventario.addBtnEliminar(  e -> invEliminar());
        ventaInventario.addBtnImagen(    e -> invVerImagen());
    }

    private void invBuscar() {
        String id     = ventaInventario.getCajaBuscarId().getText().trim();
        String nombre = ventaInventario.getCajaBuscarNombre().getText().trim();
        String cat    = (String) ventaInventario.getComboCat().getSelectedItem();
        String estado = ventaInventario.getEstado();
        refrescarInventario(
            id.isEmpty()     ? null : id,
            nombre.isEmpty() ? null : nombre,
            "Todos".equals(cat) ? null : cat,
            estado);
    }

    private void refrescarInventario(String id, String nombre, String cat, String estado) {
        DefaultTableModel m = ventaInventario.getModelo();
        m.setRowCount(0);
        for (Producto p : catalogo.getLista()) {
            if (id     != null && !p.getCodigo().toLowerCase().contains(id.toLowerCase()))     continue;
            if (nombre != null && !p.getNombre().toLowerCase().contains(nombre.toLowerCase())) continue;
            if (cat    != null && !p.getCategoria().equalsIgnoreCase(cat))                     continue;
            if ("Stock bajo".equals(estado) && p.getCantidad() >= 10) continue;
            if ("Sin stock".equals(estado)  && p.getCantidad() > 0)   continue;
            m.addRow(new Object[]{
                p.getCodigo(), p.getNombre(),
                String.format("$%.2f", p.getPrecio()),
                p.getCantidad(), p.getCategoria(),
                p.tipoAlmacenamiento(),
                String.format("$%.2f", p.totalValor())
            });
        }
        ventaInventario.setTotal(m.getRowCount());
    }

    private void invModificar() {
        int fila = ventaInventario.getTabla().getSelectedRow();
        if (fila < 0) { error("Seleccione un producto de la tabla."); return; }
        String cod = ventaInventario.getModelo().getValueAt(fila, 0).toString();
        Producto p = catalogo.buscar(cod);
        if (p == null) return;
        abrirProductos();
        ventaProductos.llenar(p.getCodigo(), p.getNombre(),
                              p.getPrecio(), p.getCantidad(),
                              p.getCategoria(), p.getImagenUrl(), p.getDescripcion());
    }

    private void invEliminar() {
        int fila = ventaInventario.getTabla().getSelectedRow();
        if (fila < 0) { error("Seleccione un producto para eliminar."); return; }
        String cod = ventaInventario.getModelo().getValueAt(fila, 0).toString();
        int resp = JOptionPane.showConfirmDialog(ventaInventario,
            "¿Eliminar el producto " + cod + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            catalogo.eliminar(cod);
            ArchivoJSON.exportarProductos(catalogo.getLista());
            refrescarInventario(null,null,null,"Todos");
            info("Producto eliminado correctamente.");
        }
    }

    private void invVerImagen() {
        int fila = ventaInventario.getTabla().getSelectedRow();
        if (fila < 0) { error("Seleccione un producto para ver su imagen."); return; }
        String cod = ventaInventario.getModelo().getValueAt(fila, 0).toString();
        Producto p = catalogo.buscar(cod);
        if (p == null) return;
        mostrarImagen(p);
    }

    /** Muestra la imagen del producto en un diálogo (carga desde URL). */
    private void mostrarImagen(Producto p) {
        JDialog dlg = new JDialog(principal, "Imagen: " + p.getNombre(), true);
        dlg.setLayout(new BorderLayout(10, 10));

        JLabel lblInfo = new JLabel("<html><b>" + p.getNombre() + "</b><br>"
            + p.getCategoria() + " — " + p.tipoAlmacenamiento() + "<br>"
            + p.getDescripcion() + "</html>", SwingConstants.CENTER);
        lblInfo.setBorder(BorderFactory.createEmptyBorder(10,10,5,10));

        JLabel lblImg = new JLabel("Cargando imagen…", SwingConstants.CENTER);
        lblImg.setPreferredSize(new Dimension(220, 220));
        lblImg.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));

        JLabel lblUrl = new JLabel("<html><i><font size='2'>" + p.getImagenUrl() + "</font></i></html>",
            SwingConstants.CENTER);
        lblUrl.setBorder(BorderFactory.createEmptyBorder(5,10,10,10));

        dlg.add(lblInfo, BorderLayout.NORTH);
        dlg.add(lblImg,  BorderLayout.CENTER);
        dlg.add(lblUrl,  BorderLayout.SOUTH);

        // Cargar imagen en hilo separado
        new Thread(() -> {
            try {
                java.net.URL url = new java.net.URL(p.getImagenUrl());
                ImageIcon icon = new ImageIcon(url);
                Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                SwingUtilities.invokeLater(() -> lblImg.setIcon(new ImageIcon(img)));
                SwingUtilities.invokeLater(() -> lblImg.setText(""));
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> lblImg.setText("(No se pudo cargar la imagen)"));
            }
        }).start();

        dlg.pack();
        dlg.setLocationRelativeTo(principal);
        dlg.setVisible(true);
    }

    // ═════════════════════════════════════════════════════════════════════
    //  GESTIÓN DE PRODUCTOS (CRUD)
    // ═════════════════════════════════════════════════════════════════════

    private void engancharProductos() {
        ventaProductos.addBtnGuardar(  e -> prodGuardar());
        ventaProductos.addBtnLimpiar(  e -> ventaProductos.limpiar());
        ventaProductos.addBtnCancelar( e -> ventaProductos.limpiar());
    }

    private void prodGuardar() {
        String cod    = ventaProductos.getCodigo().trim();
        String nom    = ventaProductos.getNombreP().trim();
        String preStr = ventaProductos.getPrecio().trim();
        String canStr = ventaProductos.getCantidad().trim();
        String cat    = ventaProductos.getCategoria().trim();
        String url    = ventaProductos.getImagenUrl().trim();
        String desc   = ventaProductos.getDescripcion().trim();

        if (cod.isEmpty() || nom.isEmpty() || preStr.isEmpty() || canStr.isEmpty() || cat.isEmpty()) {
            error("Código, Nombre, Precio, Cantidad y Categoría son obligatorios."); return;
        }
        double precio; int cantidad;
        try { precio   = Double.parseDouble(preStr); }
        catch (NumberFormatException ex) { error("Precio inválido."); return; }
        try { cantidad = Integer.parseInt(canStr); }
        catch (NumberFormatException ex) { error("Cantidad inválida."); return; }

        if (catalogo.existe(cod)) {
            catalogo.actualizar(cod, nom, precio, cantidad, cat);
            Producto p = catalogo.buscar(cod);
            if (p != null) { p.setImagenUrl(url); p.setDescripcion(desc); }
            info("Producto actualizado correctamente.");
        } else {
            Producto nuevo = crearProducto(cod, nom, precio, cantidad, cat, url, desc);
            if (nuevo == null) { error("Categoría no reconocida: " + cat); return; }
            catalogo.insertar(nuevo);
            info("Producto guardado correctamente.");
        }
        ArchivoJSON.exportarProductos(catalogo.getLista());
        ventaProductos.limpiar();
        if (ventaInventario != null && !ventaInventario.isClosed())
            refrescarInventario(null,null,null,"Todos");
    }

    /** Crea la subclase correcta según la categoría seleccionada. */
    private Producto crearProducto(String cod, String nom, double precio, int cant,
                                   String cat, String url, String desc) {
        switch (cat) {
            case "Abarrotes":               return new ProductoAbarrotes(cod,nom,precio,cant,1.0,url,desc);
            case "Bebidas":                 return new ProductoBebidas(cod,nom,precio,cant,500,false,url,desc);
            case "Lácteos y Huevo":         return new ProductoLacteos(cod,nom,precio,cant,7,url,desc);
            case "Frutas y Verduras":       return new ProductoFrutasVerduras(cod,nom,precio,cant,"kg","Todo el año",url,desc);
            case "Carnes y Pescados":       return new ProductoCarnesPescados(cod,nom,precio,cant,"Entero",false,url,desc);
            case "Salchichonería":          return new ProductoSalchichoneria(cod,nom,precio,cant,false,url,desc);
            case "Panadería y Tortillería": return new ProductoPanaderia(cod,nom,precio,cant,1,url,desc);
            case "Limpieza del Hogar":      return new ProductoLimpieza(cod,nom,precio,cant,true,url,desc);
            case "Cuidado Personal":        return new ProductoCuidadoPersonal(cod,nom,precio,cant,"Todo tipo",url,desc);
            case "Snacks y Dulcería":       return new ProductoSnacks(cod,nom,precio,cant,"Original",url,desc);
            case "Mascotas":                return new ProductoMascotas(cod,nom,precio,cant,"Perro",1.0,url,desc);
            default: return null;
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    //  PUNTO DE VENTA
    // ═════════════════════════════════════════════════════════════════════

    private Ticket ticketActual = null;

    private void engancharPuntoVenta() {
        ventaPuntoVenta.addBtnAgregar(  e -> pvAgregar());
        ventaPuntoVenta.addBtnQuitar(   e -> pvQuitar());
        ventaPuntoVenta.addBtnCobrar(   e -> pvCobrar());
        ventaPuntoVenta.addBtnNuevo(    e -> pvNuevo());
        ventaPuntoVenta.addBtnImagen(   e -> pvVerImagen());

        ticketActual = null;
        ventaPuntoVenta.limpiarTicket();
    }

    private void cargarCombo() {
        ventaPuntoVenta.cargarProductos(catalogo.getLista());
    }

    private void pvAgregar() {
        Producto p = ventaPuntoVenta.getProductoSeleccionado();
        if (p == null) { error("Seleccione un producto."); return; }
        int cant;
        try { cant = Integer.parseInt(ventaPuntoVenta.getCantidadStr().trim()); }
        catch (NumberFormatException ex) { error("Cantidad inválida."); return; }
        if (cant <= 0) { error("La cantidad debe ser mayor a 0."); return; }
        if (cant > p.getCantidad()) { error("Stock insuficiente (" + p.getCantidad() + " disponibles)."); return; }

        if (ticketActual == null)
            ticketActual = new Ticket(
                ventaPuntoVenta.getCliente(),
                ventaPuntoVenta.getNombreCliente());

        ticketActual.agregar(p.getCodigo(), p.getNombre(), cant, p.getPrecio());
        ticketActual.calcularTotales();
        ventaPuntoVenta.agregarFilaTicket(
            p.getCodigo(), p.getNombre(), cant,
            p.getPrecio(), cant * p.getPrecio());
        ventaPuntoVenta.actualizarTotales(
            ticketActual.getSubtotal(), ticketActual.getIva(), ticketActual.getTotal());
    }

    private void pvQuitar() {
        int fila = ventaPuntoVenta.getTablaTicket().getSelectedRow();
        if (fila < 0) { error("Seleccione un artículo para quitar."); return; }
        ventaPuntoVenta.quitarFila(fila);
        // Reconstruir ticket
        ticketActual = new Ticket(
            ventaPuntoVenta.getCliente(),
            ventaPuntoVenta.getNombreCliente());
        DefaultTableModel m = ventaPuntoVenta.getModeloTicket();
        for (int r = 0; r < m.getRowCount(); r++) {
            String  cod  = m.getValueAt(r,0).toString();
            String  nom  = m.getValueAt(r,1).toString();
            double  cant = Double.parseDouble(m.getValueAt(r,2).toString());
            double  pu   = Double.parseDouble(m.getValueAt(r,3).toString());
            ticketActual.agregar(cod, nom, (int)cant, pu);
        }
        ticketActual.calcularTotales();
        ventaPuntoVenta.actualizarTotales(
            ticketActual.getSubtotal(), ticketActual.getIva(), ticketActual.getTotal());
    }

    private void pvCobrar() {
        if (ticketActual == null || ticketActual.getItems().isEmpty()) {
            error("El ticket está vacío."); return;
        }
        ticketActual.calcularTotales();
        String ruta = ArchivoJSON.guardarTicket(ticketActual);
        ultimoTicket = ticketActual;

        // Actualizar stocks
        for (String[] it : ticketActual.getItems()) {
            Producto p = catalogo.buscar(it[0]);
            if (p != null) p.setCantidad(p.getCantidad() - (int)Double.parseDouble(it[2]));
        }
        ArchivoJSON.exportarProductos(catalogo.getLista());

        // Mostrar ticket
        JOptionPane.showMessageDialog(ventaPuntoVenta,
            ticketActual.toString() + "\n\n✔ Ticket guardado: " + ruta,
            "Ticket " + ticketActual.getFolio(),
            JOptionPane.INFORMATION_MESSAGE);

        pvNuevo();
        if (ventaInventario != null && !ventaInventario.isClosed())
            refrescarInventario(null,null,null,"Todos");
    }

    private void pvNuevo() {
        ticketActual = null;
        ventaPuntoVenta.limpiarTicket();
    }

    private void pvVerImagen() {
        Producto p = ventaPuntoVenta.getProductoSeleccionado();
        if (p == null) { error("Seleccione un producto para ver su imagen."); return; }
        mostrarImagen(p);
    }

    // ═════════════════════════════════════════════════════════════════════
    //  REPORTES EXCEL
    // ═════════════════════════════════════════════════════════════════════

    private void generarReportes() {
        try {
            String r1 = ReporteExcel.reporteListadoProductos(catalogo.getLista());
            String r2 = ReporteExcel.reportePorCategoria(catalogo.getLista());
            List<String[]> tickets = ArchivoJSON.cargarControlTickets();
            String r3 = ReporteExcel.reporteControlTickets(tickets);
            info("Reportes generados exitosamente:\n\n• " + r1 + "\n• " + r2 + "\n• " + r3);
        } catch (IOException ex) {
            error("Error al generar reportes: " + ex.getMessage());
        }
    }

    // ═════════════════════════════════════════════════════════════════════
    //  Helpers
    // ═════════════════════════════════════════════════════════════════════

    private void salir() {
        ArchivoJSON.exportarProductos(catalogo.getLista());
        System.exit(0);
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(principal, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void info(String msg) {
        JOptionPane.showMessageDialog(principal, msg, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}
