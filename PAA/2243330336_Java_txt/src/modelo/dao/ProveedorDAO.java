
package modelo.dao;

import modelo.clases.Proveedor;
import java.util.*;

public class ProveedorDAO {
    String archivo = "datos/proveedor.txt";

    public void guardar(Proveedor obj){
        AccesoArchivo.escribir(archivo, obj.toString());
    }

    public List<String> listar(){
        return AccesoArchivo.leer(archivo);
    }
}
