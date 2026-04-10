package modelo;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

public class BaseDatos {

    private Connection conexion;

    public BaseDatos(Connection con) {
        this.conexion = con;
    }

    public ArrayList<Object[]> consultar(String tabla,
                                         String condicion,
                                         String orden,
                                         Object[] parametros) {
        ArrayList<Object[]> resultados = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(tabla);
        if (condicion != null && !condicion.isBlank()) {
            sql.append(" WHERE ").append(condicion);
        }
        if (orden != null && !orden.isBlank()) {
            sql.append(" ORDER BY ").append(orden);
        }

        try (PreparedStatement ps = conexion.prepareStatement(sql.toString())) {

            if (parametros != null) {
                for (int i = 0; i < parametros.length; i++) {
                    ps.setObject(i + 1, parametros[i]);
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int columnas = meta.getColumnCount();

                while (rs.next()) {
                    Object[] fila = new Object[columnas];
                    for (int i = 0; i < columnas; i++) {
                        fila[i] = rs.getObject(i + 1);
                    }
                    resultados.add(fila);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error en consultar(): " + e.getMessage());
        }

        return resultados;
    }

    public ArrayList<Object[]> consultar(String tabla) {
        return consultar(tabla, null, null, null);
    }

    public <T> ArrayList<T> consultarAObjeto(String sql,
                                              Class<T> clase,
                                              Object[] parametros) {
        ArrayList<T> resultados = new ArrayList<>();

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            if (parametros != null) {
                for (int i = 0; i < parametros.length; i++) {
                    ps.setObject(i + 1, parametros[i]);
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int columnas = meta.getColumnCount();

                while (rs.next()) {
                    T obj = crearInstancia(clase);
                    if (obj == null) break;

                    for (int i = 1; i <= columnas; i++) {
                        String nombreColumna = meta.getColumnLabel(i);
                        Object valor = rs.getObject(i);
                        asignarCampo(obj, nombreColumna, valor);
                    }

                    resultados.add(obj);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error en consultarAObjeto(): " + e.getMessage());
        }

        return resultados;
    }

    public <T> ArrayList<T> consultarAObjeto(String sql, Class<T> clase) {
        return consultarAObjeto(sql, clase, null);
    }

    private <T> T crearInstancia(Class<T> clase) {
        try {
            var constructor = clase.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            System.err.println("No se pudo instanciar " + clase.getSimpleName()
                    + ": asegúrate de tener un constructor sin argumentos. "
                    + e.getMessage());
            return null;
        }
    }

    private <T> void asignarCampo(T obj, String nombreColumna, Object valor) {
        Class<?> clase = obj.getClass();

        while (clase != null && clase != Object.class) {
            for (Field campo : clase.getDeclaredFields()) {
                if (campo.getName().equalsIgnoreCase(nombreColumna)) {
                    try {
                        campo.setAccessible(true);
                        campo.set(obj, convertirTipo(valor, campo.getType()));
                    } catch (IllegalAccessException e) {
                        System.err.println("No se pudo asignar campo '"
                                + campo.getName() + "': " + e.getMessage());
                    }
                    return; 
                }
            }
            clase = clase.getSuperclass();
        }
    }
    private Object convertirTipo(Object valor, Class<?> tipo) {
        if (valor == null) return null;
        if (tipo.isInstance(valor)) return valor;

        String s = valor.toString();
        if (tipo == int.class     || tipo == Integer.class)   return Integer.parseInt(s);
        if (tipo == long.class    || tipo == Long.class)      return Long.parseLong(s);
        if (tipo == double.class  || tipo == Double.class)    return Double.parseDouble(s);
        if (tipo == float.class   || tipo == Float.class)     return Float.parseFloat(s);
        if (tipo == boolean.class || tipo == Boolean.class)   return Boolean.parseBoolean(s);
        if (tipo == String.class)                             return s;

        return valor;
    }
    public int modificar(String tabla,
                         Map<String, Object> valores,
                         String condicion,
                         Object[] parametrosCondicion) {

        if (valores == null || valores.isEmpty()) {
            System.err.println("modificar(): el mapa de valores está vacío.");
            return -1;
        }

        StringBuilder sql = new StringBuilder("UPDATE ").append(tabla).append(" SET ");
        boolean primero = true;
        for (String columna : valores.keySet()) {
            if (!primero) sql.append(", ");
            sql.append(columna).append(" = ?");
            primero = false;
        }

        if (condicion != null && !condicion.isBlank()) {
            sql.append(" WHERE ").append(condicion);
        }

        try (PreparedStatement ps = conexion.prepareStatement(sql.toString())) {

            int idx = 1;
            for (Object val : valores.values()) {
                ps.setObject(idx++, val);
            }

            if (parametrosCondicion != null) {
                for (Object param : parametrosCondicion) {
                    ps.setObject(idx++, param);
                }
            }

            return ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error en modificar(): " + e.getMessage());
            return -1;
        }
    }
    public int modificar(String tabla, Map<String, Object> valores) {
        return modificar(tabla, valores, null, null);
    }
    public boolean insertar(String sql, Object[] params) {
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error en insertar(): " + e.getMessage());
            return false;
        }
    }

    public int eliminar(String tabla, String condicion, Object[] parametros) {
        StringBuilder sql = new StringBuilder("DELETE FROM ").append(tabla);
        if (condicion != null && !condicion.isBlank()) {
            sql.append(" WHERE ").append(condicion);
        }

        try (PreparedStatement ps = conexion.prepareStatement(sql.toString())) {
            if (parametros != null) {
                for (int i = 0; i < parametros.length; i++) {
                    ps.setObject(i + 1, parametros[i]);
                }
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error en eliminar(): " + e.getMessage());
            return -1;
        }
    }
}
