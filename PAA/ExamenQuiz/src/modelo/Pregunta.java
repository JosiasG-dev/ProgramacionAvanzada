package modelo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * Modelo de Negocio: Representa una pregunta del examen.
 * Contiene el enunciado, la respuesta correcta y un arreglo de 4 opciones.
 */
public class Pregunta {

    private String enunciado;
    private String respuestaCorrecta;
    private String[] opciones; // 4 opciones mezcladas (incluye la correcta)

    /**
     * Constructor que recibe enunciado, respuesta correcta y 3 distractores.
     * Las opciones se mezclan aleatoriamente.
     */
    public Pregunta(String enunciado, String respuestaCorrecta, String[] distractores) {
        this.enunciado = enunciado;
        this.respuestaCorrecta = respuestaCorrecta;
        this.opciones = new String[4];

        // Colocar respuesta correcta en posición 0, luego copiar distractores
        this.opciones[0] = respuestaCorrecta;
        System.arraycopy(distractores, 0, opciones, 1, 3);

        // Mezclar opciones para que la correcta no siempre sea la primera
        List<String> lista = new ArrayList<>(Arrays.asList(opciones));
        Collections.shuffle(lista);
        this.opciones = lista.toArray(new String[0]);
    }

    // Getters
    public String getEnunciado() {
        return enunciado;
    }

    public String getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public String[] getOpciones() {
        return opciones;
    }
}
