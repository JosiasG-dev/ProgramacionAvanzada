
package controlador;

import modelo.dao.ProductoDAO;
import modelo.clases.Producto;
import java.util.*;

public class ProductoControl {

    ProductoDAO dao = new ProductoDAO();

    public void guardar(int id, String nombre) {
        Producto obj = new Producto(id, nombre);
        dao.guardar(obj);
    }

    public List<String> listar() {
        return dao.listar();
    }
}
