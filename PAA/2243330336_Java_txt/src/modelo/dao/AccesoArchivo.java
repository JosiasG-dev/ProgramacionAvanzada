
package modelo.dao;
import java.io.*;
import java.util.*;

public class AccesoArchivo {

    public static void escribir(String archivo, String dato) {
        try {
            FileWriter fw = new FileWriter(archivo, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(dato);
            bw.newLine();
            bw.close();
        } catch (Exception e) {
            System.out.println("Error escribiendo archivo");
        }
    }

    public static List<String> leer(String archivo) {
        List<String> lista = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;
            while((linea = br.readLine()) != null){
                lista.add(linea);
            }
            br.close();
        } catch(Exception e){
            System.out.println("Error leyendo archivo");
        }
        return lista;
    }
}
