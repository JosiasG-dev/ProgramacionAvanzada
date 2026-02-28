package Controlador;
import Libreria.Archivotxt;
import Modelo.Categoria;
import Modelo.Insumo;
import Modelo.ListaCategorias;
import Modelo.ListaInsumos;
import Vista.ProductoVistaGrid;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductoControladorGrid implements ActionListener, ListSelectionListener {

    private ProductoVistaGrid vista;
    private ListaInsumos modeloInsumos;
    private ListaCategorias modeloCategorias;
    private Archivotxt archivoInsumos;

    public ProductoControladorGrid(ProductoVistaGrid vista, ListaInsumos modeloInsumos,
                                   ListaCategorias modeloCategorias, Archivotxt archivoInsumos) {
        this.vista = vista;
        this.modeloInsumos = modeloInsumos;
        this.modeloCategorias = modeloCategorias;
        this.archivoInsumos = archivoInsumos;

        // Registrar listeners
        this.vista.addAgregarListener(this);
        this.vista.addEliminarListener(this);
        this.vista.addSalirListener(this);
        this.vista.addTablaSelectionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        if (comando.equals("Agregar") || comando.equals("Salvar")) {
            manejarAgregar();
        } else if (comando.equals("Eliminar")) {
            manejarEliminar();
        } else if (comando.equals("Salir") || comando.equals("Cancelar")) {
            manejarSalir();
        }
    }

    private void manejarAgregar() {
        if (vista.getTextoBotonAgregar().equals("Agregar")) {
            // Entrar en modo edición
            if (modeloCategorias.generarModeloCategorias().getSize() > 0) {
                // Seleccionar primera categoría (la vista ya lo hace en limpiarCampos, pero podemos forzar)
                // Por ahora, no es necesario.
            }
            vista.cambiarTextoBotonAgregar("Salvar");
            vista.cambiarTextoBotonSalir("Cancelar");
            vista.habilitarBotonEliminar(false);
            vista.habilitarEdicion(true);
            vista.limpiarCampos();
        } else {
            // Modo salvar
            if (datosCompletos()) {
                String id = vista.getIdText();
                String insumo = vista.getInsumoText();
                Categoria cat = vista.getCategoriaSeleccionada();
                if (cat == null) {
                    vista.mostrarMensaje("Debe seleccionar una categoría");
                    return;
                }
                String idCategoria = cat.getIdcategoria();
                Insumo nuevo = new Insumo(id, insumo, idCategoria);
                if (modeloInsumos.agregarInsumo(nuevo)) {
                    archivoInsumos.guardar(modeloInsumos.toArchivo());
                    vista.actualizarTabla();
                    volverAlInicio();
                } else {
                    vista.mostrarMensaje("El ID " + id + " ya existe. Tiene asignado: " +
                            modeloInsumos.buscarInsumo(id));
                }
            } else {
                vista.mostrarMensaje("Debe completar todos los campos");
            }
        }
    }

    private void manejarEliminar() {
        String[] ids = modeloInsumos.idinsumos();
        if (ids.length == 0) {
            vista.mostrarMensaje("No hay insumos para eliminar");
            return;
        }
        String seleccion = (String) JOptionPane.showInputDialog(
                vista.getFrame(),
                "Seleccione el ID a eliminar:",
                "Eliminar Insumo",
                JOptionPane.PLAIN_MESSAGE,
                null,
                ids,
                ids[0]);
        if (seleccion != null && !seleccion.isEmpty()) {
            if (modeloInsumos.eliminarInsumoPorId(seleccion)) {
                archivoInsumos.guardar(modeloInsumos.toArchivo());
                vista.actualizarTabla();
                vista.mostrarMensaje("Insumo eliminado");
            } else {
                vista.mostrarMensaje("No existe ese ID");
            }
        }
    }

    private void manejarSalir() {
        if (vista.getTextoBotonSalir().equals("Cancelar")) {
            volverAlInicio();
        } else {
            vista.dispose();
            System.exit(0);
        }
    }

    private void volverAlInicio() {
        vista.cambiarTextoBotonAgregar("Agregar");
        vista.cambiarTextoBotonSalir("Salir");
        vista.habilitarBotonEliminar(true);
        vista.habilitarEdicion(false);
        vista.limpiarCampos();
    }

    private boolean datosCompletos() {
        return !vista.getIdText().isEmpty() &&
               !vista.getInsumoText().isEmpty() &&
               vista.getCategoriaSeleccionada() != null;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            // Aquí se podría actualizar una imagen si la hubiera
            // Por ahora, no hacemos nada
        }
    }
}
