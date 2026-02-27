package modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Modelo de Datos: Encapsula toda la lógica de lectura del archivo CSV.
 * Única clase responsable de convertir líneas de texto CSV en objetos Pregunta.
 */
public class GestorArchivos {

    /**
     * Carga un examen desde un archivo CSV.
     * Formato esperado:
     *   Línea 1: Título del examen
     *   Línea 2: Encabezados (TerminoA,TerminoB)
     *   Líneas siguientes: terminoA,terminoB
     *
     * @param archivo El archivo CSV a leer
     * @param modoInvertido Si true, pregunta TerminoB dado TerminoA; si false, al revés
     * @return Lista de objetos Pregunta
     * @throws Exception Si el archivo tiene formato incorrecto o menos de 5 registros
     */
    public List<Pregunta> cargarExamen(File archivo, boolean modoInvertido) throws Exception {
        List<Pregunta> preguntas = new ArrayList<>();
        List<String> terminosA = new ArrayList<>();
        List<String> terminosB = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(archivo));

        String titulo = br.readLine(); // Línea 1: Título
        if (titulo == null || titulo.trim().isEmpty()) {
            br.close();
            throw new Exception("El archivo CSV no tiene título en la primera línea.");
        }

        String encabezados = br.readLine(); // Línea 2: Encabezados
        if (encabezados == null || !encabezados.contains(",")) {
            br.close();
            throw new Exception("El archivo CSV no tiene encabezados válidos en la segunda línea.");
        }

        String linea;
        while ((linea = br.readLine()) != null) {
            String[] partes = linea.split(",");
            if (partes.length >= 2) {
                terminosA.add(partes[0].trim());
                terminosB.add(partes[1].trim());
            }
        }
        br.close();

        // Validación: Mínimo 5 registros
        if (terminosA.size() < 5) {
            throw new Exception("El archivo debe tener al menos 5 registros. Se encontraron: " + terminosA.size());
        }

        // Crear objetos Pregunta según el modo
        for (int i = 0; i < terminosA.size(); i++) {
            String enunciado, correcta;
            List<String> fuenteDistr;

            if (modoInvertido) {
                // Pregunta: dado TerminoB, responde TerminoA
                enunciado = terminosB.get(i);
                correcta = terminosA.get(i);
                fuenteDistr = terminosA;
            } else {
                // Pregunta: dado TerminoA, responde TerminoB
                enunciado = terminosA.get(i);
                correcta = terminosB.get(i);
                fuenteDistr = terminosB;
            }

            String[] distractores = obtenerDistractores(fuenteDistr, i);
            preguntas.add(new Pregunta(enunciado, correcta, distractores));
        }

        return preguntas;
    }

    /**
     * Obtiene 3 distractores aleatorios de la lista, excluyendo el índice correcto.
     */
    private String[] obtenerDistractores(List<String> lista, int indexCorrecto) {
        List<String> copia = new ArrayList<>(lista);
        copia.remove(indexCorrecto); // Evitar duplicar la correcta
        Collections.shuffle(copia);
        return new String[]{copia.get(0), copia.get(1), copia.get(2)};
    }
}
