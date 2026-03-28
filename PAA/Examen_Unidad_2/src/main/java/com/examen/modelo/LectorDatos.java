package com.examen.modelo;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class LectorDatos {

    private static final String RUTA_EXCEL = "Datosbase.xlsx";

    private Map<String, Map<String, List<String>>> profesorAsignaturaGrupos = new LinkedHashMap<>();
    private Map<String, List<String>> alumnosPorClave = new HashMap<>();
    private Map<String, List<String>> atributosPorAsignatura = new LinkedHashMap<>();

    public LectorDatos() {
        cargarExcel();
    }

    private void cargarExcel() {
        File archivo = new File(RUTA_EXCEL);
        if (!archivo.exists()) {
            System.err.println("No se encontro: " + archivo.getAbsolutePath());
            return;
        }
        try (FileInputStream fis = new FileInputStream(archivo);
             Workbook libro = new XSSFWorkbook(fis)) {

            cargarListasAsistencia(libro.getSheet("ListasAsistencia"));
            cargarAtributos(libro.getSheet("AsignaturaAtributo"));

        } catch (Exception e) {
            System.err.println("Error leyendo Excel: " + e.getMessage());
        }
    }

    private void cargarListasAsistencia(Sheet hoja) {
        if (hoja == null) return;
        boolean primera = true;
        for (Row fila : hoja) {
            if (primera) { primera = false; continue; }
            String grupo     = celdaStr(fila, 0).trim();
            String profesor  = celdaStr(fila, 1).trim();
            String materia   = celdaStr(fila, 2).trim();
            String matricula = celdaStr(fila, 3).trim();
            String alumno    = celdaStr(fila, 4).trim();
            if (profesor.isEmpty() || materia.isEmpty()) continue;

            profesorAsignaturaGrupos
                .computeIfAbsent(profesor, k -> new LinkedHashMap<>())
                .computeIfAbsent(materia, k -> new ArrayList<>());

            List<String> gruposList = profesorAsignaturaGrupos.get(profesor).get(materia);
            if (!gruposList.contains(grupo)) gruposList.add(grupo);

            String clave = profesor + "|" + materia + "|" + grupo;
            alumnosPorClave.computeIfAbsent(clave, k -> new ArrayList<>());
            if (!alumno.isEmpty()) {
                String identificadorCompleto = matricula.isEmpty() ? alumno : "[" + matricula + "] - " + alumno;
                alumnosPorClave.get(clave).add(identificadorCompleto);
            }
        }
    }

    private void cargarAtributos(Sheet hoja) {
        if (hoja == null) return;
        for (Row fila : hoja) {
            String carrera    = celdaStr(fila, 0).trim();
            String asignatura = celdaStr(fila, 1).trim();
            String atributo   = celdaStr(fila, 2).trim();
            if (asignatura.isEmpty() || atributo.isEmpty()) continue;
            atributosPorAsignatura
                .computeIfAbsent(asignatura, k -> new ArrayList<>());
            List<String> lista = atributosPorAsignatura.get(asignatura);
            
            String valorCompetencia = carrera.isEmpty() ? atributo : "[" + carrera + "] " + atributo;
            if (!lista.contains(valorCompetencia)) lista.add(valorCompetencia);
        }
    }

    private String celdaStr(Row fila, int col) {
        if (fila == null) return "";
        Cell celda = fila.getCell(col);
        if (celda == null) return "";
        if (celda.getCellType() == CellType.NUMERIC)
            return String.valueOf((long) celda.getNumericCellValue());
        return celda.toString();
    }

    public List<String> getProfesores() {
        List<String> lista = new ArrayList<>(profesorAsignaturaGrupos.keySet());
        Collections.sort(lista);
        return lista;
    }

    public List<String> getAsignaturas(String profesor) {
        Map<String, List<String>> mapa = profesorAsignaturaGrupos.get(profesor);
        if (mapa == null) return Collections.emptyList();
        List<String> lista = new ArrayList<>(mapa.keySet());
        Collections.sort(lista);
        return lista;
    }

    public List<String> getGrupos(String profesor, String asignatura) {
        Map<String, List<String>> mapa = profesorAsignaturaGrupos.get(profesor);
        if (mapa == null) return Collections.emptyList();
        List<String> grupos = mapa.getOrDefault(asignatura, Collections.emptyList());
        List<String> resultado = new ArrayList<>(grupos);
        Collections.sort(resultado);
        return resultado;
    }

    public List<String> getAlumnos(String profesor, String asignatura, String grupo) {
        String clave = profesor + "|" + asignatura + "|" + grupo;
        return alumnosPorClave.getOrDefault(clave, Collections.emptyList());
    }

    public List<String> getAtributos(String asignatura) {
        return atributosPorAsignatura.getOrDefault(asignatura, Collections.emptyList());
    }
}
