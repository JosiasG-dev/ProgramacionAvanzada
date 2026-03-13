package Modelo;

import java.util.*;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;

public class ModeloTabla {

    private static final String[] COLUMNAS = {
        "D.A.", "P.EDUCATIVO", "LETRA", "PROFESOR", "MATERIA",
        "Periodo", "No.ACTA", "MATRICULA", "ALUMNO",
        "CALIFICACION", "OP.INS.", "OP.EXA.", "MES", "CICLO ESCOLAR"
    };

    public static final int COL_DA        = 0;
    public static final int COL_CARRERA   = 1;
    public static final int COL_GRUPO     = 2;
    public static final int COL_PROFESOR  = 3;
    public static final int COL_MATERIA   = 4;
    public static final int COL_PERIODO   = 5;
    public static final int COL_ACTA      = 6;
    public static final int COL_MATRICULA = 7;
    public static final int COL_ALUMNO    = 8;
    public static final int COL_CALIF     = 9;
    public static final int COL_OPINS     = 10;
    public static final int COL_OPEXA     = 11;
    public static final int COL_MES       = 12;
    public static final int COL_CICLO     = 13;

    private static final double MINIMO_APROBATORIO = 70.0;

    private final ArrayList<String[]> datos;

    public ModeloTabla(ArrayList<String[]> datos) {
        this.datos = datos;
    }

    private ArrayList<Carreras> getCarreras() {
        ListaObjetos lista = new ListaObjetos();
        for (String[] fila : datos) {
            if (fila.length <= COL_CARRERA) continue;
            Carreras c = new Carreras();
            c.setCarrera(fila[COL_CARRERA].trim());
            lista.Insertar(c);
        }
        return lista.getLista();
    }

    public DefaultListModel<Carreras> getModeloCarreras() {
        DefaultListModel<Carreras> m = new DefaultListModel<>();
        for (Object o : getCarreras()) m.addElement((Carreras) o);
        return m;
    }

    private ArrayList<Materias> getMaterias(Carreras carrera) {
        ListaObjetos lista = new ListaObjetos();
        for (String[] fila : datos) {
            if (fila.length <= COL_MATERIA) continue;
            if (!fila[COL_CARRERA].trim().equals(carrera.getCarrera())) continue;
            lista.Insertar(new Materias(fila[COL_MATERIA].trim(), fila[COL_CARRERA].trim()));
        }
        return lista.getLista();
    }

    public DefaultListModel<Materias> getModeloMaterias(Carreras carrera) {
        DefaultListModel<Materias> m = new DefaultListModel<>();
        for (Object o : getMaterias(carrera)) m.addElement((Materias) o);
        return m;
    }

    private ArrayList<Asignaturas> getAsignaturas(Materias materia) {
        ListaObjetos lista = new ListaObjetos();
        for (String[] fila : datos) {
            if (fila.length <= COL_MATERIA) continue;
            if (!fila[COL_CARRERA].trim().equals(materia.getCarrera())) continue;
            if (!fila[COL_MATERIA].trim().equals(materia.getMateria())) continue;
            lista.Insertar(new Asignaturas(
                    fila[COL_CARRERA].trim(), fila[COL_MATERIA].trim(),
                    fila[COL_PROFESOR].trim(), fila[COL_GRUPO].trim()));
        }
        return lista.getLista();
    }

    public DefaultListModel<Asignaturas> getModeloAsignaturas(Materias materia) {
        DefaultListModel<Asignaturas> m = new DefaultListModel<>();
        for (Object o : getAsignaturas(materia)) m.addElement((Asignaturas) o);
        return m;
    }

    public DefaultTableModel getTablaCarrera(Carreras carrera) {
        return filtrar(carrera.getCarrera(), COL_CARRERA, COLUMNAS);
    }

    public DefaultTableModel getTablaMateria(Materias materia) {
        DefaultTableModel modelo = new DefaultTableModel(COLUMNAS, 0);
        for (String[] fila : datos) {
            if (fila.length < COLUMNAS.length) continue;
            if (fila[COL_CARRERA].trim().equals(materia.getCarrera())
                    && fila[COL_MATERIA].trim().equals(materia.getMateria())) {
                modelo.addRow(padRow(fila));
            }
        }
        return modelo;
    }

    public DefaultTableModel getTablaAsignatura(Asignaturas asig) {
        DefaultTableModel modelo = new DefaultTableModel(COLUMNAS, 0);
        for (String[] fila : datos) {
            if (fila.length < COL_MATERIA + 1) continue;
            if (fila[COL_CARRERA].trim().equals(asig.getCarrera())
                    && fila[COL_MATERIA].trim().equals(asig.getMateria())
                    && fila[COL_GRUPO].trim().equals(asig.getGrupo().trim())
                    && fila[COL_PROFESOR].trim().equals(asig.getProfesor())) {
                modelo.addRow(padRow(fila));
            }
        }
        return modelo;
    }

    public DefaultTableModel getTablaTodos() {
        DefaultTableModel modelo = new DefaultTableModel(COLUMNAS, 0);
        for (String[] fila : datos) modelo.addRow(padRow(fila));
        return modelo;
    }

    public DefaultTableModel calcularPromedios() {
        String[] cols = {
            "Profesor", "Asignatura", "Grupo",
            "Promedio Calificaciones", "Num. Alumnos", "Num. Reprobados",
            "% Aprobaron", "% Reprobaron", "Promedio Alumnos Acreditaron"
        };
        DefaultTableModel modelo = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        LinkedHashMap<String, ArrayList<Double>> grupos = new LinkedHashMap<>();
        LinkedHashMap<String, String[]> metadatos = new LinkedHashMap<>();

        for (String[] fila : datos) {
            if (fila.length <= COL_CALIF) continue;
            String profesor = fila[COL_PROFESOR].trim();
            String materia  = fila[COL_MATERIA].trim();
            String grupo    = fila[COL_GRUPO].trim();
            String califStr = fila[COL_CALIF].trim();

            double calif;
            try { calif = Double.parseDouble(califStr); }
            catch (NumberFormatException e) { continue; }

            String clave = profesor + "|" + materia + "|" + grupo;
            grupos.computeIfAbsent(clave, k -> new ArrayList<>()).add(calif);
            metadatos.putIfAbsent(clave, new String[]{profesor, materia, grupo});
        }

        for (Map.Entry<String, ArrayList<Double>> entry : grupos.entrySet()) {
            String[] meta  = metadatos.get(entry.getKey());
            ArrayList<Double> califs = entry.getValue();
            int total = califs.size();
            double suma = 0, sumaAcred = 0;
            int reprobados = 0, acreditaron = 0;

            for (double c : califs) {
                suma += c;
                if (c < MINIMO_APROBATORIO) reprobados++;
                else { sumaAcred += c; acreditaron++; }
            }

            double promedio      = total > 0 ? suma / total : 0;
            double pctAprobaron  = total > 0 ? (double)(total - reprobados) / total * 100 : 0;
            double pctReprobaron = total > 0 ? (double) reprobados / total * 100 : 0;
            double promedioAcred = acreditaron > 0 ? sumaAcred / acreditaron : 0;

            modelo.addRow(new Object[]{
                meta[0], meta[1], meta[2],
                String.format("%.2f", promedio),
                total, reprobados,
                String.format("%.1f%%", pctAprobaron),
                String.format("%.1f%%", pctReprobaron),
                String.format("%.2f", promedioAcred)
            });
        }
        return modelo;
    }

    public DefaultTableModel calcularCedula332() {
        String[] cols = {
            "Academia", "Asignatura", "Número de Grupos",
            "Promedio", "% Mayor al Promedio",
            "% Reprobación", "Profesores"
        };
        DefaultTableModel modelo = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        LinkedHashMap<String, LinkedHashMap<String, ArrayList<Double>>> estructura = new LinkedHashMap<>();
        LinkedHashMap<String, LinkedHashSet<String>> profesoresPorAsig = new LinkedHashMap<>();

        for (String[] fila : datos) {
            if (fila.length <= COL_CALIF) continue;
            String academia = fila[COL_CARRERA].trim();
            String materia  = fila[COL_MATERIA].trim();
            String grupo    = fila[COL_GRUPO].trim();
            String profesor = fila[COL_PROFESOR].trim();
            String califStr = fila[COL_CALIF].trim();

            double calif;
            try { calif = Double.parseDouble(califStr); }
            catch (NumberFormatException e) { continue; }

            String claveAsig  = academia + "|" + materia;
            String claveGrupo = grupo + "|" + profesor;

            estructura
                .computeIfAbsent(claveAsig, k -> new LinkedHashMap<>())
                .computeIfAbsent(claveGrupo, k -> new ArrayList<>())
                .add(calif);

            profesoresPorAsig
                .computeIfAbsent(claveAsig, k -> new LinkedHashSet<>())
                .add(profesor);
        }

        for (Map.Entry<String, LinkedHashMap<String, ArrayList<Double>>> entryAsig : estructura.entrySet()) {
            String[] partes = entryAsig.getKey().split("\\|", 2);
            String academia = partes[0];
            String materia  = partes.length > 1 ? partes[1] : "";
            LinkedHashMap<String, ArrayList<Double>> gruposMap = entryAsig.getValue();

            int numGrupos = gruposMap.size();
            double sumaTotal = 0;
            int countTotal = 0, reprobTotal = 0;
            ArrayList<Double> promediosPorGrupo = new ArrayList<>();

            for (ArrayList<Double> califs : gruposMap.values()) {
                double sg = 0; int cg = 0;
                for (double c : califs) {
                    sg += c; cg++; sumaTotal += c; countTotal++;
                    if (c < MINIMO_APROBATORIO) reprobTotal++;
                }
                promediosPorGrupo.add(cg > 0 ? sg / cg : 0.0);
            }

            double promedioGeneral = countTotal > 0 ? sumaTotal / countTotal : 0;
            long gruposMayores = 0;
            for (double p : promediosPorGrupo) if (p > promedioGeneral) gruposMayores++;
            double pctMayor = numGrupos > 0 ? (double) gruposMayores / numGrupos * 100 : 0;
            double pctRepro = countTotal > 0 ? (double) reprobTotal / countTotal * 100 : 0;

            String profesores = String.join(", ",
                profesoresPorAsig.getOrDefault(entryAsig.getKey(), new LinkedHashSet<>()));

            modelo.addRow(new Object[]{
                academia, materia, numGrupos,
                String.format("%.2f", promedioGeneral),
                String.format("%.1f%%", pctMayor),
                String.format("%.1f%%", pctRepro),
                profesores
            });
        }
        return modelo;
    }

    private DefaultTableModel filtrar(String valor, int columna, String[] cols) {
        DefaultTableModel modelo = new DefaultTableModel(cols, 0);
        for (String[] fila : datos) {
            if (fila.length <= columna) continue;
            if (fila[columna].trim().equals(valor)) modelo.addRow(padRow(fila));
        }
        return modelo;
    }

    private Object[] padRow(String[] fila) {
        Object[] row = new Object[COLUMNAS.length];
        for (int i = 0; i < COLUMNAS.length; i++)
            row[i] = (i < fila.length) ? fila[i].trim() : "";
        return row;
    }

    public String[] getColumnas()         { return COLUMNAS; }
    public ArrayList<String[]> getDatos() { return datos; }
}
