package controlador;

import modelo.DataStore;
import modelo.UnidadMedida;
import javax.swing.*;
import java.util.List;

public class UnidadControlador {
    private final DataStore dataStore;

    public UnidadControlador() {
        this.dataStore = DataStore.getInstance();
    }

    public List<UnidadMedida> obtenerUnidades() {
        return dataStore.getUnidades();
    }

    public boolean guardarUnidad(UnidadMedida unidad) {
        try {
            if (unidad.getNombre() == null || unidad.getNombre().trim().isEmpty())
                throw new IllegalArgumentException("El nombre de la unidad es obligatorio.");
            if (unidad.getAbreviatura() == null || unidad.getAbreviatura().trim().isEmpty())
                throw new IllegalArgumentException("La abreviatura es obligatoria.");
            if (unidad.getId() == 0) {
                dataStore.agregarUnidad(unidad);
            } else {
                dataStore.actualizarUnidad(unidad);
            }
            return true;
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),
                "Error de validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    public void eliminarUnidad(int id) {
        dataStore.eliminarUnidad(id);
    }

    public String[] getAbreviaturas() {
        return dataStore.getUnidades().stream()
            .map(UnidadMedida::getAbreviatura)
            .toArray(String[]::new);
    }
}
