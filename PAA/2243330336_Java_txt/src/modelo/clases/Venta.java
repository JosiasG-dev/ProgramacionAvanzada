
package modelo.clases;

public class Venta {

    private int id;
    private String fecha;
    private double descuento;
    private int idCliente;
    private String cliente;
    private double total;
    private String estado;

    public Venta(int id, String fecha, double descuento, int idCliente, String cliente, double total, String estado) {
        this.id = id;
        this.fecha = fecha;
        this.descuento = descuento;
        this.idCliente = idCliente;
        this.cliente = cliente;
        this.total = total;
        this.estado = estado;
    }

    public String toString(){
        return id + "," + fecha + "," + descuento + "," + idCliente + "," + cliente + "," + total + "," + estado;
    }
}
