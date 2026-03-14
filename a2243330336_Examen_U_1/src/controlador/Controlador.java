package controlador;

import archivo.ArchivoCSV;
import modelo.Catalogo;
import modelo.Producto;
import modelo.Ticket;
import vista.Inventario;
import vista.Principal;
import vista.Productos;
import vista.PuntoDeVenta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Controlador {

    private final Principal principal;
    private final Catalogo  catalogo;

    private Inventario   ventaInventario;
    private Productos    ventaProductos;
    private PuntoDeVenta ventaPuntoVenta;

    private static final double IVA = 0.16;
    private Ticket ultimoTicket;

    public Controlador(Principal p) {
        this.principal = p;
        this.catalogo  = new Catalogo();
        catalogo.setLista(ArchivoCSV.importarCSV());

        p.addMenuInventarioListener(  e -> abrirInventario());
        p.addMenuProductosListener(   e -> abrirProductos());
        p.addMenuPuntoDeVentaListener(e -> abrirPuntoVenta());
        p.addMenuSalirListener(       e -> salir());
    }

    private void abrirInventario() {
        if (ventaInventario == null || ventaInventario.isClosed()) {
            ventaInventario = new Inventario();
            engancharInventario();
            principal.getEscritorio().add(ventaInventario);
            refrescarInventario(null, null, null, "Todos");
        }
        mostrar(ventaInventario);
    }

    private void abrirProductos() {
        if (ventaProductos == null || ventaProductos.isClosed()) {
            ventaProductos = new Productos();
            engancharProductos();
            principal.getEscritorio().add(ventaProductos);
            refrescarProductos();
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
        try { v.setSelected(true); } catch (Exception e) {}
    }

    private void engancharInventario() {
        ventaInventario.addBtnBuscar(   e -> invBuscar());
        ventaInventario.addBtnLimpiar(  e -> { ventaInventario.limpiarFiltros(); refrescarInventario(null,null,null,"Todos"); });
        ventaInventario.addBtnCrear(    e -> abrirProductos());
        ventaInventario.addBtnModificar(e -> invModificar());
        ventaInventario.addBtnEliminar( e -> invEliminar());
    }

    private void invBuscar() {
        String id     = ventaInventario.getCajaBuscarId().getText().trim();
        String nombre = ventaInventario.getCajaBuscarNombre().getText().trim();
        String tipo   = (String) ventaInventario.getListaTipo().getSelectedItem();
        String estado = ventaInventario.getEstado();
        refrescarInventario(
                id.isEmpty()     ? null : id,
                nombre.isEmpty() ? null : nombre,
                "Todos".equals(tipo) ? null : tipo,
                estado);
    }

    private void invModificar() {
        int fila = ventaInventario.getTabla().getSelectedRow();
        if (fila < 0) { error("Seleccione un producto de la tabla."); return; }
        String cod = ventaInventario.getModeloTabla().getValueAt(fila, 0).toString();
        Producto p = catalogo.buscar(cod);
        if (p == null) return;
        abrirProductos();
        ventaProductos.llenar(p.getCodigo(), p.getNombre(),
                String.valueOf(p.getPrecio()), String.valueOf(p.getCantidad()), p.getCategoria());
    }

    private void invEliminar() {
        int fila = ventaInventario.getTabla().getSelectedRow();
        if (fila < 0) { error("Seleccione un producto para eliminar."); return; }
        String cod = ventaInventario.getModeloTabla().getValueAt(fila, 0).toString();
        Producto p = catalogo.buscar(cod);
        if (p == null) return;
        int r = JOptionPane.showConfirmDialog(principal,
                "Eliminar: " + p.getNombre() + "?\nEsta accion no se puede deshacer.",
                "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (r != JOptionPane.YES_OPTION) return;
        catalogo.eliminar(cod);
        guardarTodo();
        aviso("Producto eliminado.");
    }

    private void refrescarInventario(String fId, String fNom, String fTipo, String fEst) {
        if (ventaInventario == null) return;
        DefaultTableModel m = ventaInventario.getModeloTabla();
        m.setRowCount(0);
        for (Producto p : catalogo.getLista()) {
            if (fId   != null && !p.getCodigo().contains(fId)) continue;
            if (fNom  != null && !p.getNombre().toLowerCase().contains(fNom.toLowerCase())) continue;
            if (fTipo != null && !p.getCategoria().equalsIgnoreCase(fTipo)) continue;
            String est = p.getCantidad() > 0 ? "Disponible" : "Agotado";
            if (!"Todos".equals(fEst) && !est.equals(fEst)) continue;
            m.addRow(new Object[]{ p.getCodigo(), p.getNombre(), p.getCategoria(),
                    p.getCantidad(), String.format("$%.2f", p.getPrecio()), est });
        }
    }

    private void engancharProductos() {
        ventaProductos.addBtnGuardar(  e -> prodGuardar());
        ventaProductos.addBtnConsultar(e -> prodConsultar());
        ventaProductos.addBtnEliminar( e -> prodEliminar());
        ventaProductos.addBtnLimpiar(  e -> ventaProductos.limpiar());
        ventaProductos.addBtnBuscar(   e -> prodBuscar());
        ventaProductos.addBtnTodos(    e -> refrescarProductos());
        ventaProductos.addBtnExportar( e -> { ArchivoCSV.exportarCSV(catalogo.getLista()); aviso("Exportado a productos.csv"); });
        ventaProductos.getTabla().getSelectionModel().addListSelectionListener(ev -> {
            if (!ev.getValueIsAdjusting()) prodCargarFila();
        });
    }

    private void prodGuardar() {
        String cod    = ventaProductos.getCajaCodigo().getText().trim();
        String nombre = ventaProductos.getCajaNombre().getText().trim();
        String spv    = ventaProductos.getCajaPrecioVenta().getText().trim();
        String sst    = ventaProductos.getCajaStock().getText().trim();
        String cat    = (String) ventaProductos.getListaCategoria().getSelectedItem();

        if (nombre.isEmpty() || spv.isEmpty() || sst.isEmpty()) {
            error("Nombre, Precio y Stock son obligatorios."); return;
        }
        double precio; int stock;
        try {
            precio = Double.parseDouble(spv);
            stock  = Integer.parseInt(sst);
        } catch (NumberFormatException ex) {
            error("Precio y Stock deben ser numericos."); return;
        }
        if (precio < 0 || stock < 0) { error("No se admiten valores negativos."); return; }

        if (cod.isEmpty()) {
            String nuevo = generarCodigo();
            catalogo.insertar(new Producto(nuevo, nombre, precio, stock, cat));
            aviso("Producto guardado con codigo: " + nuevo);
        } else {
            if (!catalogo.existe(cod)) { error("El codigo no existe. Use Limpiar para insertar uno nuevo."); return; }
            catalogo.actualizar(cod, nombre, precio, stock, cat);
            aviso("Producto actualizado.");
        }
        guardarTodo();
        ventaProductos.limpiar();
    }

    private void prodConsultar() {
        String cod = ventaProductos.getCajaCodigo().getText().trim();
        if (cod.isEmpty()) { error("Escriba un codigo en el campo ID."); return; }
        Producto p = catalogo.buscar(cod);
        if (p == null) { error("No existe el codigo: " + cod); return; }
        ventaProductos.llenar(p.getCodigo(), p.getNombre(),
                String.valueOf(p.getPrecio()), String.valueOf(p.getCantidad()), p.getCategoria());
        marcarFila(ventaProductos.getTabla(), ventaProductos.getModeloTabla(), cod, 1);
        aviso("Encontrado: " + p.getNombre());
    }

    private void prodEliminar() {
        String cod = ventaProductos.getCajaCodigo().getText().trim();
        if (cod.isEmpty()) { error("Consulte primero el producto a eliminar."); return; }
        Producto p = catalogo.buscar(cod);
        if (p == null) { error("No existe el codigo: " + cod); return; }
        int r = JOptionPane.showConfirmDialog(principal,
                "Eliminar: " + p.getNombre() + "?\nEsta accion no se puede deshacer.",
                "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (r != JOptionPane.YES_OPTION) return;
        catalogo.eliminar(cod);
        guardarTodo();
        ventaProductos.limpiar();
        aviso("Producto eliminado.");
    }

    private void prodBuscar() {
        String por    = (String) ventaProductos.getListaBuscarPor().getSelectedItem();
        String texto  = ventaProductos.getCajaBusqueda().getText().trim();
        if (texto.isEmpty()) { refrescarProductos(); return; }
        DefaultTableModel m = ventaProductos.getModeloTabla();
        m.setRowCount(0);
        int n = 1;
        for (Producto p : catalogo.getLista()) {
            boolean ok = false;
            if ("Nombre".equals(por))    ok = p.getNombre().toLowerCase().contains(texto.toLowerCase());
            if ("Categoria".equals(por)) ok = p.getCategoria().equalsIgnoreCase(texto);
            if ("Estado".equals(por))    ok = true;
            if (ok) m.addRow(new Object[]{ n++, p.getCodigo(), p.getNombre(),
                    p.getCategoria(), p.getCantidad(), String.format("$%.2f", p.getPrecio()), "Activo" });
        }
    }

    private void prodCargarFila() {
        int fila = ventaProductos.getTabla().getSelectedRow();
        if (fila < 0) return;
        DefaultTableModel m = ventaProductos.getModeloTabla();
        String cod = m.getValueAt(fila, 1).toString();
        Producto p = catalogo.buscar(cod);
        if (p == null) return;
        ventaProductos.llenar(p.getCodigo(), p.getNombre(),
                String.valueOf(p.getPrecio()), String.valueOf(p.getCantidad()), p.getCategoria());
    }

    private void refrescarProductos() {
        if (ventaProductos == null) return;
        DefaultTableModel m = ventaProductos.getModeloTabla();
        m.setRowCount(0);
        int n = 1;
        for (Producto p : catalogo.getLista())
            m.addRow(new Object[]{ n++, p.getCodigo(), p.getNombre(),
                    p.getCategoria(), p.getCantidad(), String.format("$%.2f", p.getPrecio()), "Activo" });
    }

    private void engancharPuntoVenta() {
        ventaPuntoVenta.addBtnAgregar(   e -> pvAgregar());
        ventaPuntoVenta.addBtnModificar( e -> pvModificar());
        ventaPuntoVenta.addBtnQuitar(    e -> pvQuitar());
        ventaPuntoVenta.addBtnVaciar(    e -> ventaPuntoVenta.vaciar());
        ventaPuntoVenta.addBtnCobrar(    e -> pvCobrar());
        ventaPuntoVenta.addBtnVerTicket( e -> pvVerTicket());
    }

    private void cargarCombo() {
        if (ventaPuntoVenta == null) return;
        JComboBox<String> combo = ventaPuntoVenta.getListaProductos();
        combo.removeAllItems();
        for (Producto p : catalogo.getLista())
            combo.addItem(p.getCodigo() + "  " + p.getNombre()
                    + "  ($" + String.format("%.2f", p.getPrecio()) + ")");
    }

    private void pvAgregar() {
        int idx = ventaPuntoVenta.getListaProductos().getSelectedIndex();
        if (idx < 0) { error("Seleccione un producto."); return; }
        String sc = ventaPuntoVenta.getCajaCantidad().getText().trim();
        if (sc.isEmpty()) { error("Ingrese la cantidad."); return; }
        int cant;
        try { cant = Integer.parseInt(sc); }
        catch (NumberFormatException ex) { error("La cantidad debe ser un numero entero."); return; }
        if (cant <= 0) { error("La cantidad debe ser mayor a cero."); return; }

        Producto p = catalogo.getLista().get(idx);
        if (p.getCantidad() < cant) { error("Stock insuficiente. Disponible: " + p.getCantidad()); return; }

        DefaultTableModel m = ventaPuntoVenta.getModeloTabla();
        for (int i = 0; i < m.getRowCount(); i++) {
            if (m.getValueAt(i, 0).toString().equals(p.getCodigo())) {
                int nueva = Integer.parseInt(m.getValueAt(i, 2).toString()) + cant;
                m.setValueAt(nueva, i, 2);
                m.setValueAt(String.format("$%.2f", p.getPrecio() * nueva), i, 4);
                calcularTotales();
                ventaPuntoVenta.getCajaCantidad().setText("");
                return;
            }
        }
        m.addRow(new Object[]{ p.getCodigo(), p.getNombre(), cant,
                String.format("$%.2f", p.getPrecio()),
                String.format("$%.2f", p.getPrecio() * cant) });
        calcularTotales();
        ventaPuntoVenta.getCajaCantidad().setText("");
    }

    private void pvModificar() {
        int fila = ventaPuntoVenta.getTabla().getSelectedRow();
        if (fila < 0) { error("Seleccione un item del carrito."); return; }
        DefaultTableModel m = ventaPuntoVenta.getModeloTabla();
        String sc = JOptionPane.showInputDialog(principal,
                "Nueva cantidad para: " + m.getValueAt(fila, 1));
        if (sc == null || sc.trim().isEmpty()) return;
        int cant;
        try { cant = Integer.parseInt(sc.trim()); }
        catch (NumberFormatException ex) { error("Cantidad invalida."); return; }
        Producto p = catalogo.buscar(m.getValueAt(fila, 0).toString());
        if (p == null) return;
        m.setValueAt(cant, fila, 2);
        m.setValueAt(String.format("$%.2f", p.getPrecio() * cant), fila, 4);
        calcularTotales();
    }

    private void pvQuitar() {
        int fila = ventaPuntoVenta.getTabla().getSelectedRow();
        if (fila < 0) { error("Seleccione un item para quitar."); return; }
        ventaPuntoVenta.getModeloTabla().removeRow(fila);
        calcularTotales();
    }

    private void calcularTotales() {
        DefaultTableModel m = ventaPuntoVenta.getModeloTabla();
        double sub = 0;
        for (int i = 0; i < m.getRowCount(); i++) {
            try { sub += Double.parseDouble(m.getValueAt(i, 4).toString().replace("$", "")); }
            catch (NumberFormatException e) {}
        }
        double iva   = sub * IVA;
        ventaPuntoVenta.setSubtotal(String.format("$%.2f", sub));
        ventaPuntoVenta.setIVA(     String.format("$%.2f", iva));
        ventaPuntoVenta.setTotal(   String.format("$%.2f", sub + iva));
    }

    private void pvCobrar() {
        DefaultTableModel m = ventaPuntoVenta.getModeloTabla();
        if (m.getRowCount() == 0) { error("El carrito esta vacio."); return; }

        String cliente = ventaPuntoVenta.getCajaCliente().getText().trim();
        String nombre  = ventaPuntoVenta.getCajaNombreCliente().getText().trim();
        ultimoTicket   = new Ticket(cliente, nombre);

        double sub = 0;
        for (int i = 0; i < m.getRowCount(); i++) {
            String cod  = m.getValueAt(i, 0).toString();
            int    cant = Integer.parseInt(m.getValueAt(i, 2).toString());
            Producto p  = catalogo.buscar(cod);
            if (p != null) {
                ultimoTicket.agregar(p.getCodigo(), p.getNombre(), cant, p.getPrecio());
                sub += p.getPrecio() * cant;
                catalogo.actualizar(cod, p.getNombre(), p.getPrecio(),
                        Math.max(0, p.getCantidad() - cant), p.getCategoria());
            }
        }
        double iva = sub * IVA;
        ultimoTicket.totales(sub, iva, sub + iva);

        ArchivoCSV.exportarCSV(catalogo.getLista());
        refrescarInventario(null, null, null, "Todos");
        refrescarProductos();
        cargarCombo();
        ventaPuntoVenta.vaciar();

        JOptionPane.showMessageDialog(principal,
                ultimoTicket.toString(),
                "Ticket de Venta - " + ultimoTicket.getNumero(),
                JOptionPane.PLAIN_MESSAGE);
    }

    private void pvVerTicket() {
        if (ultimoTicket == null) { error("Aun no se ha realizado ninguna venta."); return; }
        JOptionPane.showMessageDialog(principal,
                ultimoTicket.toString(),
                "Ticket - " + ultimoTicket.getNumero(),
                JOptionPane.PLAIN_MESSAGE);
    }

    private void guardarTodo() {
        ArchivoCSV.exportarCSV(catalogo.getLista());
        refrescarInventario(null, null, null, "Todos");
        refrescarProductos();
        cargarCombo();
    }

    private String generarCodigo() {
        int max = 0;
        for (Producto p : catalogo.getLista()) {
            try { int n = Integer.parseInt(p.getCodigo()); if (n > max) max = n; }
            catch (NumberFormatException e) {}
        }
        return String.format("%03d", max + 1);
    }

    private void marcarFila(JTable t, DefaultTableModel m, String val, int col) {
        for (int i = 0; i < m.getRowCount(); i++) {
            if (m.getValueAt(i, col).toString().equalsIgnoreCase(val)) {
                t.setRowSelectionInterval(i, i);
                t.scrollRectToVisible(t.getCellRect(i, 0, true));
                break;
            }
        }
    }

    private void salir() {
        int r = JOptionPane.showConfirmDialog(principal,
                "Desea salir del sistema?\nLos datos estan guardados.",
                "Salir", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (r == JOptionPane.YES_OPTION) System.exit(0);
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(principal, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void aviso(String msg) {
        JOptionPane.showMessageDialog(principal, msg, "Listo", JOptionPane.INFORMATION_MESSAGE);
    }
}
