package modelo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Ticket {

    private String numero;
    private String fecha;
    private String hora;
    private String cliente;
    private String nombre;
    private ArrayList<String[]> items;
    private double subtotal;
    private double iva;
    private double total;

    public Ticket(String cliente, String nombre) {
        Date hoy = new Date();
        this.numero   = new SimpleDateFormat("yyyyMMddHHmmss").format(hoy);
        this.fecha    = new SimpleDateFormat("dd/MM/yyyy").format(hoy);
        this.hora     = new SimpleDateFormat("HH:mm:ss").format(hoy);
        this.cliente  = cliente.isEmpty()  ? "PUBLICO"  : cliente;
        this.nombre   = nombre.isEmpty()   ? "GENERAL"  : nombre;
        this.items    = new ArrayList<>();
    }

    public void agregar(String codigo, String descripcion, int cantidad, double precio) {
        items.add(new String[]{
                codigo,
                descripcion,
                String.valueOf(cantidad),
                String.format("$%.2f", precio),
                String.format("$%.2f", cantidad * precio)
        });
    }

    public void totales(double subtotal, double iva, double total) {
        this.subtotal = subtotal;
        this.iva      = iva;
        this.total    = total;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("           TICKET DE VENTA\n");
        sb.append("========================================\n");
        sb.append("Ticket  : ").append(numero).append("\n");
        sb.append("Fecha   : ").append(fecha).append("   ").append(hora).append("\n");
        sb.append("Cliente : ").append(cliente).append(" - ").append(nombre).append("\n");
        sb.append("----------------------------------------\n");
        sb.append(String.format("%-8s %-20s %4s %9s %9s\n",
                "Cod", "Descripcion", "Cant", "P.Unit", "Total"));
        sb.append("----------------------------------------\n");
        for (String[] item : items)
            sb.append(String.format("%-8s %-20s %4s %9s %9s\n",
                    item[0], item[1], item[2], item[3], item[4]));
        sb.append("----------------------------------------\n");
        sb.append(String.format("%-30s %9s\n", "Subtotal:", String.format("$%.2f", subtotal)));
        sb.append(String.format("%-30s %9s\n", "IVA (16%):", String.format("$%.2f", iva)));
        sb.append(String.format("%-30s %9s\n", "TOTAL A PAGAR:", String.format("$%.2f", total)));
        sb.append("========================================\n");
        return sb.toString();
    }

	public String getNumero() {
		return numero;
	}

	public String getFecha() {
		return fecha;
	}

	public String getHora() {
		return hora;
	}

	public String getCliente() {
		return cliente;
	}

	public String getNombre() {
		return nombre;
	}

	public ArrayList<String[]> getItems() {
		return items;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public double getIva() {
		return iva;
	}

	public double getTotal() {
		return total;
	}
}
