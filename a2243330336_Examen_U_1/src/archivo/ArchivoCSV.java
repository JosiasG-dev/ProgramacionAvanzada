package archivo;

import modelo.Producto;

import java.io.*;
import java.util.ArrayList;

public class ArchivoCSV {

    private static final String RUTA      = "productos.csv";
    private static final String SEPARADOR = ";";

    public static void exportarCSV(ArrayList<Producto> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA, false))) {
            bw.write("codigo;nombre;precio;cantidad;categoria");
            bw.newLine();
            for (Producto p : lista) {
                bw.write(p.getCodigo() + SEPARADOR + p.getNombre() + SEPARADOR
                        + p.getPrecio() + SEPARADOR + p.getCantidad() + SEPARADOR
                        + p.getCategoria());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al exportar CSV: " + e.getMessage());
        }
    }

    public static ArrayList<Producto> importarCSV() {
        ArrayList<Producto> lista = new ArrayList<>();
        File f = new File(RUTA);
        if (!f.exists()) {
            lista = datosIniciales();
            exportarCSV(lista);
            return lista;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            boolean primera = true;
            while ((linea = br.readLine()) != null) {
                if (primera) { primera = false; continue; }
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                String[] c = linea.split(SEPARADOR, -1);
                if (c.length < 5) continue;
                try {
                    lista.add(new Producto(c[0], c[1],
                            Double.parseDouble(c[2]),
                            Integer.parseInt(c[3]), c[4]));
                } catch (NumberFormatException ignored) {}
            }
        } catch (IOException e) {
            System.err.println("Error al importar CSV: " + e.getMessage());
        }
        return lista;
    }

    private static ArrayList<Producto> datosIniciales() {
        ArrayList<Producto> lista = new ArrayList<>();
        lista.add(new Producto("001", "Arroz 1kg",            35.0, 10, "Granos"));
        lista.add(new Producto("002", "Azucar 1kg",           25.0, 10, "Granos"));
        lista.add(new Producto("003", "Harina 1kg",           28.0, 10, "Granos"));
        lista.add(new Producto("004", "Aceite 1L",            50.0, 10, "Liquidos"));
        lista.add(new Producto("005", "Leche 1L",             35.0, 10, "Lacteos"));
        lista.add(new Producto("006", "Huevos 12 piezas",     45.0, 10, "Lacteos"));
        lista.add(new Producto("007", "Fideos 500g",          20.0, 10, "Granos"));
        lista.add(new Producto("008", "Sal 1kg",              15.0, 10, "Condimentos"));
        lista.add(new Producto("009", "Pasta de tomate 400g", 25.0, 10, "Enlatados"));
        lista.add(new Producto("010", "Atun lata 170g",       35.0, 10, "Enlatados"));
        return lista;
    }
}
