package Librerias;

import Modelo.Academia;
import Modelo.AsignaturaAdmin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class LibreriaAdmin {

    private static final String ARCHIVO_ACADEMIAS    = "academias.csv";
    private static final String ARCHIVO_ASIGNATURAS  = "asignaturas.csv";

    public static ArrayList<Academia> leerAcademias() {
        ArrayList<Academia> lista = new ArrayList<>();
        File f = new File(ARCHIVO_ACADEMIAS);
        if (!f.exists()) return lista;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (!linea.isEmpty()) lista.add(new Academia(linea));
            }
        } catch (IOException e) {
            System.out.println("Error leyendo academias: " + e.getMessage());
        }
        return lista;
    }

    public static void guardarAcademias(ArrayList<Academia> lista) {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(ARCHIVO_ACADEMIAS), StandardCharsets.UTF_8))) {
            for (Academia a : lista) pw.println(a.getNombre());
        } catch (IOException e) {
            System.out.println("Error guardando academias: " + e.getMessage());
        }
    }

    public static ArrayList<AsignaturaAdmin> leerAsignaturas() {
        ArrayList<AsignaturaAdmin> lista = new ArrayList<>();
        File f = new File(ARCHIVO_ASIGNATURAS);
        if (!f.exists()) return lista;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",", 2);
                if (partes.length == 2)
                    lista.add(new AsignaturaAdmin(partes[0].trim(), partes[1].trim()));
            }
        } catch (IOException e) {
            System.out.println("Error leyendo asignaturas: " + e.getMessage());
        }
        return lista;
    }

    public static void guardarAsignaturas(ArrayList<AsignaturaAdmin> lista) {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(ARCHIVO_ASIGNATURAS), StandardCharsets.UTF_8))) {
            for (AsignaturaAdmin a : lista) pw.println(a.getNombre() + "," + a.getAcademia());
        } catch (IOException e) {
            System.out.println("Error guardando asignaturas: " + e.getMessage());
        }
    }
}
