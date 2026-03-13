package Librerias;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class Libreria {

    public static boolean ExisteArchivo(String narchivo) {
        File archivo = new File(narchivo);
        return archivo.exists();
    }

    public static ArrayList<String[]> LeerDatosCSV(String narchivo) {
        ArrayList<String[]> lista = new ArrayList<>();
        try {
          FileInputStream fis = new FileInputStream(narchivo);
            byte[] bytes = fis.readAllBytes();
            fis.close();

            String contenido = new String(bytes, Charset.forName("ISO-8859-1"));
            String[] lineasRaw = contenido.split("\r?\n");

            StringBuilder acumulada = new StringBuilder();
            for (String linea : lineasRaw) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                boolean esInicioFila = linea.startsWith("(") || linea.startsWith("D.A.");

                if (esInicioFila) {
                    if (acumulada.length() > 0) {
                        procesarLinea(acumulada.toString(), lista);
                        acumulada.setLength(0);
                    }
                    acumulada.append(linea);
                } else {
                    acumulada.append(linea);
                }
            }
            if (acumulada.length() > 0) {
                procesarLinea(acumulada.toString(), lista);
            }

        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
        return lista;
    }

    private static void procesarLinea(String linea, ArrayList<String[]> lista) {
        if (linea.startsWith("D.A.")) return; 
        String[] datos = linea.split(",");
        if (datos.length >= 9) {
            lista.add(datos);
        }
    }

    public static void EscribirArchivoCSV(String narchivo, String contenido, boolean accion) {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(narchivo, accion))) {
            escritor.write(contenido);
        } catch (IOException e) {
            System.out.println("Error al escribir: " + e.getMessage());
        }
    }

    public static String lineacvs(Object[] dato) {
        StringBuilder linea = new StringBuilder();
        for (Object nodo : dato) {
            linea.append(String.valueOf(nodo)).append(",");
        }
        return linea.substring(0, linea.length() - 1);
    }
}
