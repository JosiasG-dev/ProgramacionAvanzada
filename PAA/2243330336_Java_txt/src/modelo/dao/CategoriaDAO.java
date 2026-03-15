
package modelo.dao;

import modelo.clases.Categoria;
import java.util.*;

public class CategoriaDAO {
    String archivo = "datos/categoria.txt";

    public void guardar(Categoria obj){
        AccesoArchivo.escribir(archivo, obj.toString());
    }

    public List<String> listar(){
        return AccesoArchivo.leer(archivo);
    }
}
