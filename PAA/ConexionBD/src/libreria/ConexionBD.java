package libreria;

import java.sql.*;

public class ConexionBD {

    private static final String URL_MYSQL     = "jdbc:mysql://";
    private static final String URL_POSTGRES  = "jdbc:postgresql://";
    private static final String URL_MARIADB   = "jdbc:mariadb://";
    private static final String URL_ORACLE    = "jdbc:oracle:thin:@";
    private static final String URL_ACCESS    = "jdbc:ucanaccess://";
    private static final String URL_SQLSERVER = "jdbc:sqlserver://";

    public static Connection conectarMySQL(String host, String bd, String user, String pass) throws SQLException {
        String url = URL_MYSQL + host + "/" + bd;
        return DriverManager.getConnection(url, user, pass);
    }

    public static Connection conectarPostgreSQL(String host, String puerto, String bd, String user, String pass) throws SQLException {
        String url = URL_POSTGRES + host + ":" + puerto + "/" + bd;
        return DriverManager.getConnection(url, user, pass);
    }

    public static Connection conectarOracle(String host, String puerto, String sid, String user, String pass) throws SQLException {
        String url = URL_ORACLE + host + ":" + puerto + ":" + sid;
        return DriverManager.getConnection(url, user, pass);
    }

    public static Connection conectarSQLServer(String host, int puerto, String bd, String user, String pass) throws SQLException {
        String url = URL_SQLSERVER + host + ":" + puerto +
                     ";databaseName=" + bd +
                     ";encrypt=false;trustServerCertificate=true;";
        return DriverManager.getConnection(url, user, pass);
    }

    public static Connection conectarUCanAccess(String rutaArchivoAccdb) throws SQLException {
        String url = URL_ACCESS + rutaArchivoAccdb;
        return DriverManager.getConnection(url);
    }

    public static void cerrar(Connection con) {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                System.out.println("Conexión cerrada exitosamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al intentar cerrar la conexión: " + e.getMessage());
        }
    }
}
