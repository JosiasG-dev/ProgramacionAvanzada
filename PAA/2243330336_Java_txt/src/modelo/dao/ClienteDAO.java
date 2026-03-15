
package modelo.dao;

import modelo.clases.Cliente;
import java.util.*;

public class ClienteDAO {
    String archivo = "datos/cliente.txt";

    public void guardar(Cliente obj){
        AccesoArchivo.escribir(archivo, obj.toString());
    }

    public List<String> listar(){
        return AccesoArchivo.leer(archivo);
    }
}
