package com.examen.modelo;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Map;

public class GeneradorExcel {

    public static File obtenerArchivo(Evaluacion evaluacion, String carpetaDestino) {
        String nombreArchivo = evaluacion.getAsignatura() + "_"
                + evaluacion.getProfesor() + "_"
                + evaluacion.getGrupo() + ".xlsx";
        nombreArchivo = nombreArchivo.replaceAll("[\\\\/:*?\"<>|]", "_");
        if (carpetaDestino != null && !carpetaDestino.isEmpty()) {
            return new File(carpetaDestino, nombreArchivo);
        }
        return new File(nombreArchivo);
    }

    public static void generarOActualizar(Evaluacion evaluacion, File archivo) throws IOException {
        Workbook libro;
        if (archivo.exists()) {
            try (FileInputStream fis = new FileInputStream(archivo)) {
                libro = new XSSFWorkbook(fis);
            }
        } else {
            libro = new XSSFWorkbook();
        }

        llenarHojaDashboard(libro, evaluacion);
        llenarHojaRubrica(libro, evaluacion);
        llenarHojaListaCotejo(libro, evaluacion);

        try (FileOutputStream fos = new FileOutputStream(archivo)) {
            libro.write(fos);
        }
        libro.close();
    }

    private static void llenarHojaDashboard(Workbook libro, Evaluacion evaluacion) {
        Sheet hoja = obtenerOCrearHoja(libro, "Dashboard");
        escribirCelda(hoja, 0, 0, "Asignatura");
        escribirCelda(hoja, 0, 1, "Profesor");
        escribirCelda(hoja, 0, 2, "Grupo");
        escribirCelda(hoja, 1, 0, evaluacion.getAsignatura());
        escribirCelda(hoja, 1, 1, evaluacion.getProfesor());
        escribirCelda(hoja, 1, 2, evaluacion.getGrupo());

        escribirCelda(hoja, 3, 0, "Fecha");
        escribirCelda(hoja, 3, 1, "Actividad");
        escribirCelda(hoja, 3, 2, "Observaciones");

        if (evaluacion.getDatosInstrumento() != null) {
            Map<String, Object> inst = evaluacion.getDatosInstrumento();
            escribirCelda(hoja, 4, 0, valorStr(inst.get("fecha")));
            escribirCelda(hoja, 4, 1, valorStr(inst.get("actividad")));
            escribirCelda(hoja, 4, 2, valorStr(inst.get("observaciones")));
        }
    }

    private static void llenarHojaRubrica(Workbook libro, Evaluacion evaluacion) {
        Sheet hoja = obtenerOCrearHoja(libro, "Rubrica");
        escribirCelda(hoja, 0, 0, "Alumno(s)");
        escribirCelda(hoja, 0, 1, "Criterio 1");
        escribirCelda(hoja, 0, 2, "Criterio 2");
        escribirCelda(hoja, 0, 3, "Promedio");

        if (evaluacion.getEquipos() != null) {
            int fila = 1;
            double sumaTotal = 0;
            int contador = 0;
            for (Equipo eq : evaluacion.getEquipos()) {
                String nombres = eq.getNombres() != null ? String.join(", ", eq.getNombres()) : "";
                escribirCelda(hoja, fila, 0, nombres);
                escribirCeldaNumerica(hoja, fila, 3, eq.getCalificacionRubrica());
                sumaTotal += eq.getCalificacionRubrica();
                contador++;
                fila++;
            }
            if (contador > 0) {
                escribirCelda(hoja, fila, 2, "Promedio general:");
                escribirCeldaNumerica(hoja, fila, 3, sumaTotal / contador);
            }
        }
    }

    private static void llenarHojaListaCotejo(Workbook libro, Evaluacion evaluacion) {
        Sheet hoja = obtenerOCrearHoja(libro, "Lista de Cotejo");
        escribirCelda(hoja, 0, 0, "Indicador");
        escribirCelda(hoja, 0, 1, "Cumple");

        if (evaluacion.getDatosInstrumento() != null) {
            Map<String, Object> inst = evaluacion.getDatosInstrumento();
            int fila = 1;
            int i = 0;
            while (inst.containsKey("check_" + i)) {
                String etiqueta = valorStr(inst.get("etiqueta_" + i));
                if (etiqueta.isEmpty()) etiqueta = "Indicador " + (i + 1);
                Object val = inst.get("check_" + i);
                boolean marcado = val instanceof Boolean ? (Boolean) val : "true".equals(String.valueOf(val));
                escribirCelda(hoja, fila, 0, etiqueta);
                escribirCelda(hoja, fila, 1, marcado ? "Si" : "No");
                fila++;
                i++;
            }
        }
    }

    private static Sheet obtenerOCrearHoja(Workbook libro, String nombre) {
        Sheet hoja = libro.getSheet(nombre);
        return hoja != null ? hoja : libro.createSheet(nombre);
    }

    private static Row obtenerOCrearFila(Sheet hoja, int numFila) {
        Row fila = hoja.getRow(numFila);
        return fila != null ? fila : hoja.createRow(numFila);
    }

    private static Cell obtenerOCrearCelda(Row fila, int numColumna) {
        Cell celda = fila.getCell(numColumna);
        return celda != null ? celda : fila.createCell(numColumna);
    }

    private static void escribirCelda(Sheet hoja, int numFila, int numCol, String valor) {
        obtenerOCrearCelda(obtenerOCrearFila(hoja, numFila), numCol).setCellValue(valor != null ? valor : "");
    }

    private static void escribirCeldaNumerica(Sheet hoja, int numFila, int numCol, double valor) {
        obtenerOCrearCelda(obtenerOCrearFila(hoja, numFila), numCol).setCellValue(valor);
    }

    private static String valorStr(Object o) {
        return o != null ? o.toString() : "";
    }
}
