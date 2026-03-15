
package controlador;

import modelo.dao.CategoriaDAO;
import modelo.clases.Categoria;
import java.util.*;

public class CategoriaControl {

    CategoriaDAO dao = new CategoriaDAO();

    public void guardar(int id, String nombre) {
        Categoria obj = new Categoria(id, nombre);
        dao.guardar(obj);
    }

    public List<String> listar() {
        return dao.listar();
    }
}
