
package controlador;

import modelo.dao.DetalleVentaDAO;
import modelo.clases.DetalleVenta;
import java.util.*;

public class DetalleVentaControl {

    DetalleVentaDAO dao = new DetalleVentaDAO();

    public void guardar(int id, String nombre) {
        DetalleVenta obj = new DetalleVenta(id, nombre);
        dao.guardar(obj);
    }

    public List<String> listar() {
        return dao.listar();
    }
}
