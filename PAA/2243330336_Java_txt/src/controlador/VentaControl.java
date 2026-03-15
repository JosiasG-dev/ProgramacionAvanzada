
package controlador;

import modelo.dao.VentaDAO;
import modelo.clases.Venta;
import java.util.*;

public class VentaControl {

    VentaDAO dao = new VentaDAO();

    public void guardar(int id,String fecha,double descuento,int idCliente,String cliente,double total,String estado){
        Venta v = new Venta(id,fecha,descuento,idCliente,cliente,total,estado);
        dao.guardar(v);
    }

    public List<String> listar(){
        return dao.listar();
    }
}
