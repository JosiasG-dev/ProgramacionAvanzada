package modelo;

import java.util.Collections;
import java.util.List;

/**
 * Modelo de Lista: Gestiona la lista de preguntas del examen.
 * Controla el índice actual, aciertos e intentos.
 */
public class Examen {

    private List<Pregunta> listaPreguntas;
    private int indiceActual;
    private int aciertos;
    private int intentos;

    public Examen(List<Pregunta> preguntas) {
        this.listaPreguntas = preguntas;
        this.indiceActual = 0;
        this.aciertos = 0;
        this.intentos = 0;
        Collections.shuffle(this.listaPreguntas); // Aleatoriedad
    }

    /**
     * Retorna la siguiente pregunta o null si se acabaron.
     */
    public Pregunta obtenerSiguiente() {
        if (indiceActual < listaPreguntas.size()) {
            return listaPreguntas.get(indiceActual++);
        }
        return null;
    }

    /**
     * Retorna la pregunta actual sin avanzar el índice.
     */
    public Pregunta obtenerActual() {
        if (indiceActual > 0 && indiceActual <= listaPreguntas.size()) {
            return listaPreguntas.get(indiceActual - 1);
        }
        return null;
    }

    public void registrarRespuesta(boolean esCorrecta) {
        this.intentos++;
        if (esCorrecta) this.aciertos++;
    }

    public double calcularPuntaje() {
        if (intentos == 0) return 0;
        return (double) aciertos / intentos * 100;
    }

    public boolean hayMasPreguntas() {
        return indiceActual < listaPreguntas.size();
    }

    public int getIndiceActual() {
        return indiceActual;
    }

    public int getTotalPreguntas() {
        return listaPreguntas.size();
    }

    public int getAciertos() {
        return aciertos;
    }

    public int getIntentos() {
        return intentos;
    }
}
