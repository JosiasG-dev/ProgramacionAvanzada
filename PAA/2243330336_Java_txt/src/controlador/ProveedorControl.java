
package controlador;

import modelo.dao.ProveedorDAO;
import modelo.clases.Proveedor;
import java.util.*;

public class ProveedorControl {

    ProveedorDAO dao = new ProveedorDAO();

    public void guardar(int id, String nombre) {
        Proveedor obj = new Proveedor(id, nombre);
        dao.guardar(obj);
    }

    public List<String> listar() {
        return dao.listar();
    }
}
