package modelo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Ticket de venta.
 * El folio se genera automáticamente con fecha+hora+ms.
 * Se guarda como JSON con nombre = folio.
 */
public class Ticket {

    private static final double IVA = 0.16;

    private String folio;
    private String fecha;
    private String hora;
    private String cliente;
    private String nombreCliente;
    private ArrayList<String[]> items;  // {codigo, descripcion, cantidad, precioUnit, importe}
    private double subtotal;
    private double iva;
    private double total;

    public Ticket(String cliente, String nombreCliente) {
        Date ahora  = new Date();
        this.folio  = "TK" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(ahora);
        this.fecha  = new SimpleDateFormat("dd/MM/yyyy").format(ahora);
        this.hora   = new SimpleDateFormat("HH:mm:ss").format(ahora);
        this.cliente       = cliente.isEmpty()       ? "PUBLICO"  : cliente;
        this.nombreCliente = nombreCliente.isEmpty() ? "GENERAL"  : nombreCliente;
        this.items  = new ArrayList<>();
    }

    // ── Agregar artículo ─────────────────────────────────────────────────
    public void agregar(String codigo, String descripcion, double cantidad, double precioUnit) {
        double importe = Math.round(cantidad * precioUnit * 100.0) / 100.0;
        items.add(new String[]{
            codigo,
            descripcion,
            String.valueOf(cantidad),
            String.format("%.2f", precioUnit),
            String.format("%.2f", importe)
        });
    }

    // ── Calcular totales ─────────────────────────────────────────────────
    public void calcularTotales() {
        double base = 0;
        for (String[] it : items)
            base += Double.parseDouble(it[4]);
        this.subtotal = Math.round((base / (1 + IVA)) * 100.0) / 100.0;
        this.iva      = Math.round((base - subtotal) * 100.0) / 100.0;
        this.total    = Math.round(base * 100.0) / 100.0;
    }

    // ── Serialización JSON ────────────────────────────────────────────────
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"folio\":")         .append(q(folio))         .append(",\n");
        sb.append("  \"fecha\":")         .append(q(fecha))         .append(",\n");
        sb.append("  \"hora\":")          .append(q(hora))          .append(",\n");
        sb.append("  \"cliente\":")       .append(q(cliente))       .append(",\n");
        sb.append("  \"nombreCliente\":") .append(q(nombreCliente)) .append(",\n");
        sb.append("  \"items\": [\n");
        for (int i = 0; i < items.size(); i++) {
            String[] it = items.get(i);
            sb.append("    {")
              .append("\"codigo\":").append(q(it[0])).append(",")
              .append("\"descripcion\":").append(q(it[1])).append(",")
              .append("\"cantidad\":").append(it[2]).append(",")
              .append("\"precioUnit\":").append(it[3]).append(",")
              .append("\"importe\":").append(it[4])
              .append("}");
            if (i < items.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("  ],\n");
        sb.append("  \"subtotal\":").append(subtotal).append(",\n");
        sb.append("  \"iva\":")    .append(iva)     .append(",\n");
        sb.append("  \"total\":")  .append(total)   .append("\n");
        sb.append("}");
        return sb.toString();
    }

    private static String q(String s) {
        return "\"" + s.replace("\"", "\\\"") + "\"";
    }

    /** Representación imprimible del ticket (para pantalla/impresora). */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("================================================\n");
        sb.append("              TICKET DE VENTA\n");
        sb.append("================================================\n");
        sb.append(String.format("Folio   : %s%n",     folio));
        sb.append(String.format("Fecha   : %s   %s%n", fecha, hora));
        sb.append(String.format("Cliente : %s - %s%n", cliente, nombreCliente));
        sb.append("------------------------------------------------\n");
        sb.append(String.format("%-8s %-22s %5s %9s %9s%n",
                  "Cod","Descripción","Cant","P.Unit","Total"));
        sb.append("------------------------------------------------\n");
        for (String[] it : items)
            sb.append(String.format("%-8s %-22s %5s %9s %9s%n",
                      it[0], it[1].length()>22?it[1].substring(0,22):it[1],
                      it[2], "$"+it[3], "$"+it[4]));
        sb.append("------------------------------------------------\n");
        sb.append(String.format("%-34s $%8.2f%n", "Subtotal:",   subtotal));
        sb.append(String.format("%-34s $%8.2f%n", "IVA (16%):", iva));
        sb.append(String.format("%-34s $%8.2f%n", "TOTAL A PAGAR:", total));
        sb.append("================================================\n");
        return sb.toString();
    }

    // ── Getters ───────────────────────────────────────────────────────────
    public String              getFolio()         { return folio; }
    public String              getFecha()         { return fecha; }
    public String              getHora()          { return hora; }
    public String              getCliente()       { return cliente; }
    public String              getNombreCliente() { return nombreCliente; }
    public ArrayList<String[]> getItems()         { return items; }
    public double              getSubtotal()      { return subtotal; }
    public double              getIva()           { return iva; }
    public double              getTotal()         { return total; }
    public int                 getNumItems()      { return items.size(); }
}
