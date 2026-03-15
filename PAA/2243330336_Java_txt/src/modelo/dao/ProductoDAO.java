
package modelo.dao;

import modelo.clases.Producto;
import java.util.*;

public class ProductoDAO {
    String archivo = "datos/producto.txt";

    public void guardar(Producto obj){
        AccesoArchivo.escribir(archivo, obj.toString());
    }

    public List<String> listar(){
        return AccesoArchivo.leer(archivo);
    }
}
