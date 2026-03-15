
package controlador;

import modelo.dao.ClienteDAO;
import modelo.clases.Cliente;
import java.util.*;

public class ClienteControl {

    ClienteDAO dao = new ClienteDAO();

    public void guardar(int id, String nombre) {
        Cliente obj = new Cliente(id, nombre);
        dao.guardar(obj);
    }

    public List<String> listar() {
        return dao.listar();
    }
}
