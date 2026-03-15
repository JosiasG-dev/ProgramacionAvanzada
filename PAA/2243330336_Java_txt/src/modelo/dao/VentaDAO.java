
package modelo.dao;

import modelo.clases.Venta;
import java.util.*;

public class VentaDAO {

    String archivo = "datos/ventas.txt";

    public void guardar(Venta v){
        AccesoArchivo.escribir(archivo, v.toString());
    }

    public List<String> listar(){
        return AccesoArchivo.leer(archivo);
    }
}
