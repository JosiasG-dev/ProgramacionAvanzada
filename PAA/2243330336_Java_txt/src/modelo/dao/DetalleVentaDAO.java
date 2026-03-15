
package modelo.dao;

import modelo.clases.DetalleVenta;
import java.util.*;

public class DetalleVentaDAO {
    String archivo = "datos/detalleventa.txt";

    public void guardar(DetalleVenta obj){
        AccesoArchivo.escribir(archivo, obj.toString());
    }

    public List<String> listar(){
        return AccesoArchivo.leer(archivo);
    }
}
