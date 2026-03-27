package controlador;

import modelo.DataStore;
import modelo.Proveedor;
import javax.swing.*;
import java.util.List;

public class ProveedorControlador {
    private final DataStore dataStore;

    public ProveedorControlador() {
        this.dataStore = DataStore.getInstance();
    }

    public List<Proveedor> obtenerProveedores() {
        return dataStore.getProveedores();
    }

    public boolean guardarProveedor(Proveedor proveedor) {
        try {
            validar(proveedor);
            if (proveedor.getId() == 0) {
                dataStore.agregarProveedor(proveedor);
            } else {
                dataStore.actualizarProveedor(proveedor);
            }
            return true;
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),
                "Error de validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    public void eliminarProveedor(int id) {
        dataStore.eliminarProveedor(id);
    }

    private void validar(Proveedor p) {
        if (p.getNombre() == null || p.getNombre().trim().isEmpty())
            throw new IllegalArgumentException("El nombre del proveedor es obligatorio.");
        if (p.getTelefono() == null || p.getTelefono().trim().isEmpty())
            throw new IllegalArgumentException("El teléfono es obligatorio.");
    }
}
