import modelo.BaseDatos;
import modelo.configurador;
import libreria.ConexionBD;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class main {

    public static void main(String[] args) {

        System.out.println("=== SISTEMA DE GESTION DE MATERIAS ===");

        Map<String, String> conf = configurador.cargarConfiguracion();
        if (conf.isEmpty()) {
            System.err.println("No se pudo cargar la configuración.");
            return;
        }

        Connection con = null;

        try {
            String motor = conf.get("motor");
            System.out.println("Conectando a: " + motor.toUpperCase() + "...");

            if (motor.equalsIgnoreCase("mysql")) {
                con = ConexionBD.conectarMySQL(
                    conf.get("host"), conf.get("db"),
                    conf.get("user"), conf.get("pass")
                );
            } else {
                con = ConexionBD.conectarSQLServer(
                    conf.get("host"),
                    Integer.parseInt(conf.get("puerto")),
                    conf.get("db"),
                    conf.get("user"), conf.get("pass")
                );
            }

            System.out.println("¡Conexión exitosa!\n");
            BaseDatos bd = new BaseDatos(con);
            System.out.println("── LISTADO COMPLETO DE PRODUCTOS ──────────────");
            ArrayList<Object[]> todos = bd.consultar("producto", null, null, null);
            for (Object[] fila : todos) {
                for (Object campo : fila) System.out.print(campo + "\t| ");
                System.out.println();
            }


        } catch (Exception e) {
            System.err.println("Fallo en la ejecución: " + e.getMessage());
        } finally {
            ConexionBD.cerrar(con);
        }
    }
}
